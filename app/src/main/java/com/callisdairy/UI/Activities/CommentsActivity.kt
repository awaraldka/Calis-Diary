package com.callisdairy.UI.Activities

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.common.util.Util
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.callisdairy.Adapter.CommentsAdapter
import com.callisdairy.Adapter.ImageHomeAdapter
import com.callisdairy.CalisApp
import com.callisdairy.Interface.DeleteClick
import com.callisdairy.Interface.HomeComments
import com.callisdairy.Interface.MorOptionsClick
import com.callisdairy.Interface.ViewReply
import com.callisdairy.ModalClass.CommentsModelClass
import com.callisdairy.R
import com.callisdairy.R.id
import com.callisdairy.R.layout
import com.callisdairy.R.style
import com.callisdairy.Socket.SocketManager
import com.callisdairy.UI.Fragments.autoPlayVideo.PlayerViewAdapter
import com.callisdairy.UI.dialogs.ImageShowDialog
import com.callisdairy.Utils.Home
import com.callisdairy.Utils.Home.showKeyboard
import com.callisdairy.Utils.Progresss
import com.callisdairy.Utils.Resource
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.api.response.CommentListDocs
import com.callisdairy.api.response.MediaUrls
import com.callisdairy.api.response.RepliesListDocs
import com.callisdairy.databinding.ActivityCommentsBinding
import com.callisdairy.extension.androidExtension
import com.callisdairy.extension.setSafeOnClickListener
import com.callisdairy.viewModel.CommentsViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CommentsActivity : AppCompatActivity(), HomeComments, ViewReply, DeleteClick,
    MorOptionsClick {

    private lateinit var binding: ActivityCommentsBinding

    var postId = ""
    var name = ""
    var flag = false
    var data : ArrayList<CommentsModelClass> = ArrayList()
    lateinit var Adapter : CommentsAdapter
    var addCommentId = ""
    var imageList = ArrayList<MediaUrls>()
    var deleteItemPosition = 0
    var commentId = ""
    var commentItem = ""

    var deleteRepliedItemPosition = 0
    var commentRepliedId = ""
    var commentRepliedItem = ""
    var commentFlag = ""
    var position = 0
    lateinit var imageAdaptor: ImageHomeAdapter

    lateinit var dialog: BottomSheetDialog
    lateinit var dialogs: BottomSheetDialog

    var token = ""
    var pages = 0
    var page = 1
    var limit = 10

    var pages1 = 0
    var page1 = 1
    var limit1 = 10

    var positionValue = 0
    var dataLoadFlag =  false
    var loaderFlag = true
    var dataLoadFlag1 =  false
    var loaderFlag1 = true

    private lateinit var mediaDataSourceFactory: DataSource.Factory


    var liked = false
    var postPosition = 0
    lateinit var unlikePostView:ImageView
    lateinit var likePostView:ImageView
    lateinit var changeComment:LinearLayout
    lateinit var editComment:EditText

    private var exoPlayer : ExoPlayer? = null


    var commentData = ArrayList<CommentListDocs>()
    var commentReplyData = ArrayList<RepliesListDocs>()

    private val viewModel: CommentsViewModel by viewModels()
    var paginationFlag = false
    lateinit var socketInstance: SocketManager

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommentsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.attributes.windowAnimations = style.Fade
        exoPlayer = ExoPlayer.Builder(this).build()
        intent.getStringExtra("_id")?.let {
            postId = it
        }
        intent.getIntExtra("position",0).let {
            position = it
        }

        socketInstance = SocketManager.getInstance(this)
        val userId = SavedPrefManager.getStringPreferences(this, SavedPrefManager.userId).toString()
        socketInstance.onlineUser(userId)

        token = SavedPrefManager.getStringPreferences(this, SavedPrefManager.Token).toString()

        page = 1
        limit = 10
        dataLoadFlag =  false
        viewModel.commentListApi(token,postId,page,limit)



        binding.BackClick.setSafeOnClickListener{
            finishAfterTransition()
        }



        binding.images.setSafeOnClickListener{
            ImageShowDialog(imageList).show(supportFragmentManager, "ShowImage")
        }



        val callback: ViewPager2.OnPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                PlayerViewAdapter.playIndexThenPausePreviousPlayer(position)

            }
        }
        binding.storeViewpager.registerOnPageChangeCallback(callback)



        binding.previousCommentsTv.setSafeOnClickListener {
            dataLoadFlag = true
            paginationFlag = true
            binding.ProgressBarScroll.visibility = View.VISIBLE
            page++

            if (page > pages) {
                binding.previousCommentsTv.isVisible = false
                binding.ProgressBarScroll.visibility = View.GONE
            } else {

                viewModel.commentListApi(token,postId,page,limit)
            }
        }




        binding.etCommentSection.addTextChangedListener(textWatcher)


        binding.PostComment.setSafeOnClickListener {
            if (binding.etCommentSection.text.isEmpty()){
                Toast.makeText(this, getString(R.string.cant_post_comment_empty), Toast.LENGTH_SHORT).show()
            }else{

                paginationFlag = false
                binding.PostComment.isEnabled = false
                if (name.isNotEmpty()){
                    viewModel.addCommentApi(token,"COMMENT",postId,"",addCommentId,binding.etCommentSection.text.toString())
                }else{
                    viewModel.addCommentApi(token,"POST",postId,binding.etCommentSection.text.toString(),"","")

                }
            }

            
        }


        observeResponseCommentList()
        observeResponseAddComment()
        observeResponseCommentRepliedList()
        observeResponseAddLike()
        observeResponseCommentDeleted()
        observeResponseUpdateComment()
    }


    private fun observeResponseCommentList() {

        lifecycleScope.launch {
            viewModel._commentsListData.collectLatest { response ->

                when (response) {

                    is Resource.Success -> {

                        Progresss.stop()

                        if(response.data?.responseCode == 200) {
                            try {
                                binding.ProgressBarScroll.visibility = View.GONE
                                loaderFlag = false
                                if (!dataLoadFlag) {
                                    commentData.clear()
                                }

                                commentData.addAll(response.data.result.docs)
                                pages = response.data.result.totalPages
                                page = response.data.result.page
                                commentAdapter()
                                binding.previousCommentsTv.isVisible = pages > 1

                                    val petInfo = response.data.result.mediaUrls
                                    if (petInfo[0].type.lowercase() == "image"){
                                        binding.imagesVideoPlay.isVisible = true
                                        binding.images.isVisible = true
                                        binding.mediaContainer.isVisible = false

                                        imageList = response.data.result.mediaUrls

                                        setImageAdaptor(response.data.result.mediaUrls)
                                        binding.indicator.isVisible = response.data.result.mediaUrls.size >1

                                    }


                                    if (petInfo.getOrNull(0)?.type?.lowercase() == "video"){
                                        binding.imagesVideoPlay.isVisible = true
                                        binding.images.isVisible = false
                                        binding.mediaContainer.isVisible = true


                                        playVideo(petInfo[0].media.mediaUrlMobile)
                                    }






                            }catch (e:Exception){
                                e.printStackTrace()
                            }
                        }
                    }

                    is Resource.Error -> {
                        Progresss.stop()
                        response.message?.let { message ->
                            androidExtension.alertBox(message,this@CommentsActivity)
                        }

                    }

                    is Resource.Loading -> {
                        if (loaderFlag){
                            Progresss.start(this@CommentsActivity)
                        }
                    }

                    is Resource.Empty -> {
                        Progresss.stop()
                    }

                }

            }
        }
    }

    @OptIn(UnstableApi::class)
    private fun playVideo(url: String) {
        // Set up the PlayerView
        binding.itemVideoExoplayer.player = exoPlayer
        binding.itemVideoExoplayer.keepScreenOn = true
        binding.itemVideoExoplayer.controllerAutoShow = false
        binding.itemVideoExoplayer.controllerHideOnTouch = true
        binding.itemVideoExoplayer.useController = false

        // Set repeat mode
        exoPlayer?.repeatMode = Player.REPEAT_MODE_ALL

        // Create a data source factory
        val upstreamDataSourceFactory = DefaultHttpDataSource.Factory()
            .setUserAgent(Util.getUserAgent(applicationContext, getString(R.string.app_name)))

        val mediaDataSourceFactory = CalisApp.simpleCache?.let {
            CacheDataSource.Factory()
                .setCache(it) // Set the cache
                .setUpstreamDataSourceFactory(
                    upstreamDataSourceFactory
                )
        }

        // Create a media source
        val mediaSource: MediaSource = ProgressiveMediaSource.Factory(mediaDataSourceFactory!!)
            .createMediaSource(MediaItem.fromUri(Uri.parse(url)))

        // Prepare the player with the media source
        exoPlayer?.setMediaSource(mediaSource)
        exoPlayer?.prepare()
        exoPlayer?.playWhenReady = true // Auto play video

        // Add a listener to the player
        exoPlayer?.addListener(object : Player.Listener {
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                super.onPlayerStateChanged(playWhenReady, playbackState)

                when (playbackState) {
                    Player.STATE_BUFFERING -> {
                        binding.thumbnail.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    Player.STATE_READY -> {
                        binding.progressBar.visibility = View.GONE
                        binding.thumbnail.visibility = View.GONE
                    }
                }
            }
        })
    }



    private fun commentAdapter(){
        binding.commentsRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        Adapter = CommentsAdapter(this,commentData,commentReplyData,this,this)
        binding.commentsRecycler.adapter = Adapter
        if(!paginationFlag) {
            binding.commentScrollView.post {
                binding.commentScrollView.fullScroll(View.FOCUS_DOWN)
            }
        }




    }

    @SuppressLint("SetTextI18n")
    override fun commentSection(userName: String, _id: String) {
        binding.etCommentSection.setText("@$userName ")
        addCommentId = _id
        name= userName
        binding.etCommentSection.requestFocus()
        binding.etCommentSection.isFocusableInTouchMode = true
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.etCommentSection, InputMethodManager.SHOW_FORCED)
        binding.etCommentSection.setSelection(binding.etCommentSection.text.length)
    }

    override fun ownCommentDeleteClick(userName: String, comment: String, _id: String, position: Int) {
        deleteItemPosition = position
        commentId = _id
        commentItem = comment
        androidExtension.alertBoxDelete(getString(R.string.do_you_want_to_delete_the_comment),this,this)
    }

    override fun ownCommentEditClick(userName: String, comment: String, _id: String, position: Int) {
        deleteItemPosition = position
        commentId = _id
        commentItem = comment

        commentEditPopUp()
    }

    override fun ownCommentRepliedDeleteClick(
        userName: String,
        comment: String,
        _id: String,
        position: Int,
        s: String) {
        deleteRepliedItemPosition = position
        commentRepliedId = _id
        commentRepliedItem = comment
        commentFlag = s
        androidExtension.alertBoxDelete(getString(R.string.do_you_want_to_delete_the_comment),this,this)

    }

    override fun ownCommentRepliedEditClick(
        userName: String,
        comment: String,
        _id: String,
        position: Int,
        s: String
    ) {
        deleteRepliedItemPosition = position
        commentRepliedId = _id
        commentRepliedItem = comment
        commentFlag = s
        commentEditPopUp()
    }


    private fun observeResponseAddComment() {

        lifecycleScope.launchWhenCreated {
            viewModel._addCommentData.collectLatest { response ->

                when (response) {
                    is Resource.Success -> {
                        Progresss.stop()
                        if(response.data?.responseCode == 200) {
                            try {

                                Home.data[position].commentCount = Home.data[position].commentCount + 1

                                dataLoadFlag = false
                                viewModel.commentListApi(token,postId,1,10)
                                binding.etCommentSection.setText("")
                                binding.PostComment.isEnabled = true
                            }catch (e:Exception){
                                e.printStackTrace()
                            }
                        }
                    }

                    is Resource.Error -> {
                        binding.PostComment.isEnabled = true
                        Progresss.stop()
                        response.message?.let { message ->
//                            androidExtension.alertBox(message,this@CommentsActivity)
                        }

                    }

                    is Resource.Loading -> {
                        Progresss.start(this@CommentsActivity)
                    }

                    is Resource.Empty -> {
                    }

                }

            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeResponseUpdateComment() {

        lifecycleScope.launchWhenCreated {
            viewModel.update_commentData.collectLatest { response ->

                when (response) {
                    is Resource.Success -> {
                        if(response.data?.responseCode == 200) {
                            try {
                                changeComment.isEnabled = true
                                if (commentFlag == "Replied"){
                                    commentReplyData[deleteRepliedItemPosition].reply = editComment.text.toString()
                                }else{
                                    commentData[deleteItemPosition].comment = editComment.text.toString()
                                }


                                Adapter.notifyItemRangeChanged(deleteItemPosition, commentData.size)
                                Adapter.notifyDataSetChanged()
                                binding.etCommentSection.setText("")
                                binding.commentsRecycler.scrollToPosition(Adapter.itemCount -1)
                                commentFlag = ""
                                name = ""
                                commentRepliedItem = ""
                                dialogs.dismiss()
                                dialog.dismiss()


                            }catch (e:Exception){
                                e.printStackTrace()
                            }
                        }
                    }

                    is Resource.Error -> {
                        changeComment.isEnabled = true
                        response.message?.let { message ->
//                            androidExtension.alertBox(message,this@CommentsActivity)
                        }

                    }

                    is Resource.Loading -> {
                    }

                    is Resource.Empty -> {
                    }

                }

            }
        }
    }

    val textWatcher = object : TextWatcher {
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

        }

        override fun afterTextChanged(s: Editable) {
            if (s.toString() == "") {
                name = ""
            }
        }

    }

    override fun showAllComments(_id: String, position: Int) {
        positionValue = position
        viewModel.repliedCommentApi(token,_id,page1,limit1)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeResponseCommentRepliedList() {

        lifecycleScope.launch {
            viewModel._viewRepliedCommentData.collectLatest { response ->

                when (response) {

                    is Resource.Success -> {

                        if(response.data?.responseCode == 200) {
                            try {

                                loaderFlag1 = false
                                if (!dataLoadFlag1) {
                                    commentReplyData.clear()
                                }

                                commentReplyData.addAll(response.data.result.docs)
                                pages1 = response.data.result.totalPages
                                page1 = response.data.result.page
                                Adapter.notifyItemChanged(positionValue)
                                Adapter.notifyDataSetChanged()

                            }catch (e:Exception){
                                e.printStackTrace()
                            }
                        }
                    }

                    is Resource.Error -> {
                        response.message?.let { message ->
                            androidExtension.alertBox(message,this@CommentsActivity)
                        }

                    }

                    is Resource.Loading -> {
                    }

                    is Resource.Empty -> {

                    }

                }

            }
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    override fun likeComment(
        _id: String,
        likedValue: Boolean,
        unlikePost: ImageView,
        likePost: ImageView,
        position: Int,
        commentId:String
    ) {

        liked = likedValue
        unlikePostView = unlikePost
        likePostView = likePost
        postPosition = position

        if (unlikePostView.isVisible) {
            commentData[postPosition].isLiked = true
            commentData[postPosition].likeCount = commentData[postPosition].likeCount + 1
            Adapter.notifyItemChanged(position)
            Adapter.notifyItemRangeChanged(position, commentData.size)
            Adapter.notifyDataSetChanged()

        } else {
            commentData[postPosition].isLiked = false
            commentData[postPosition].likeCount -= 1
            Adapter.notifyItemChanged(position)
            Adapter.notifyItemRangeChanged(position, commentData.size)
            Adapter.notifyDataSetChanged()

        }

        viewModel.addLikeApi(token,"COMMENT","",commentId)



    }


    private fun observeResponseAddLike() {


        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._addLikeData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {
                            if (response.data?.responseCode == 200) {
                                try {

                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
                            response.message?.let { message ->
                            }

                        }

                        is Resource.Loading -> {
                        }

                        is Resource.Empty -> {
                        }

                    }

                }
            }
        }


    }

//   edit and delete own comment from post.

    @SuppressLint("InflateParams")
    private fun commentEditDeletePopUp() {
        dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(layout.comment_edit_delete, null)!!
        val metrics = DisplayMetrics()
        this.windowManager?.defaultDisplay?.getMetrics(metrics)
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        dialog.behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        dialog.behavior.peekHeight = metrics.heightPixels




        val editClick:LinearLayout = view.findViewById(id.EditClick)
        val deleteClick:LinearLayout = view.findViewById(id.DeleteClick)





        deleteClick.setSafeOnClickListener {
            androidExtension.alertBoxDelete(getString(R.string.are_you_sure_you_want_to_delete_this_comment),this,this)
            dialog.dismiss()
        }


        editClick.setSafeOnClickListener {
//            commentEditPopUp()
            dialog.dismiss()
        }







        dialog.setContentView(view)
        dialog.show()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun deleteItem() {

        if (commentFlag == "Replied"){
            commentData[positionValue].repliesCount -= 1
            commentReplyData.removeAt(deleteRepliedItemPosition)
            Adapter.notifyDataSetChanged()
            viewModel.deleteCommentApi(token,"POST",postId,"",commentRepliedId,"")
        }else{
//            commentData[deleteItemPosition].repliesCount -= 1
            commentData.removeAt(deleteItemPosition)
            commentReplyData.removeAll(commentReplyData.toSet())
            Adapter.notifyItemRemoved(deleteItemPosition)
            Adapter.notifyItemRangeChanged(deleteItemPosition, commentData.size)
            Adapter.notifyDataSetChanged()
            viewModel.deleteCommentApi(token,"POST",postId,"",commentId,"")
        }


    }


    private fun observeResponseCommentDeleted() {

        lifecycleScope.launch {
            viewModel.deleteComment_data.collectLatest { response ->

                when (response) {

                    is Resource.Success -> {

                        if(response.data?.responseCode == 200) {
                            try {

                            }catch (e:Exception){
                                e.printStackTrace()
                            }
                        }
                    }

                    is Resource.Error -> {
                        response.message?.let { message ->
//                            androidExtension.alertBox(message,this@CommentsActivity)
                        }

                    }

                    is Resource.Loading -> {
                    }

                    is Resource.Empty -> {

                    }

                }

            }
        }
    }



//    Edit Comment

    @SuppressLint("InflateParams")
    private fun commentEditPopUp() {
        dialogs = BottomSheetDialog(this)
        val view = layoutInflater.inflate(layout.edit_comment_layout, null)!!
        val metrics = DisplayMetrics()
        this.windowManager?.defaultDisplay?.getMetrics(metrics)
        dialogs.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        dialogs.behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        dialogs.behavior.peekHeight = metrics.heightPixels




        editComment = view.findViewById(id.etCommentSection)
        val cancelButton:LinearLayout = view.findViewById(id.CancelButton)
        changeComment = view.findViewById(id.ChangeComment)
        val comment:TextView = view.findViewById(id.comment)


        if (commentFlag == "Replied"){
            editComment.setText(commentRepliedItem)
            editComment.requestFocus()
            editComment.isFocusableInTouchMode = true
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(editComment, InputMethodManager.SHOW_FORCED)
            editComment.setSelection(editComment.text.length)
            showKeyboard(this)
        }else{
            editComment.setText(commentItem)
            editComment.requestFocus()
            editComment.isFocusableInTouchMode = true
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(editComment, InputMethodManager.SHOW_FORCED)
            editComment.setSelection(editComment.text.length)
            showKeyboard(this)
        }





        editComment.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                if (s.toString() == "") {
                    changeComment.isEnabled = false
                    changeComment.setBackgroundResource(R.drawable.disable_background)
                }else{
                    changeComment.isEnabled = true
                    changeComment.setBackgroundResource(R.drawable.button_background)
                }
            }

        })


        changeComment.setOnClickListener {
            changeComment.isEnabled = false
            if (commentFlag == "Replied"){
                viewModel.updateCommentApi(token,"REPLY",postId,"",commentRepliedId,editComment.text.toString())

            }else{
                viewModel.updateCommentApi(token,"POST",postId,editComment.text.toString(),commentId,"")

            }
        }


        cancelButton.setOnClickListener {
            dialogs.dismiss()
        }


        dialogs.setContentView(view)
        dialogs.show()
    }



    override fun onStop() {
        super.onStop()
        exoPlayer?.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayer?.release()
    }

    override fun onPause() {
        super.onPause()
        exoPlayer?.stop()
    }

    override fun onResume() {
        super.onResume()
        exoPlayer?.playWhenReady = true

    }


    private fun setImageAdaptor(petImage: ArrayList<MediaUrls>) {
        imageAdaptor = ImageHomeAdapter(this,petImage,this)
        binding.storeViewpager.adapter = imageAdaptor
        binding.indicator.setViewPager(binding.storeViewpager)

    }

    override fun openPopUpMore(_id: String, position: Int, _id1: String, imageUrl: String) {

    }

    override fun sharePost(imageUrl: String) {

    }

    override fun viewImages(media: ArrayList<MediaUrls>) {
        ImageShowDialog(media).show(supportFragmentManager, "ShowImage")
    }


}