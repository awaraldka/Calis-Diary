package com.callisdairy.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.callisdairy.Interface.HomeLikePost
import com.callisdairy.Interface.HomeUserProfileView
import com.callisdairy.Interface.MorOptionsClick
import com.callisdairy.Interface.SuggestionListClick
import com.callisdairy.ModalClass.descriptionImage
import com.callisdairy.R
import com.callisdairy.UI.Activities.SuggestionListActivity
import com.callisdairy.UI.Activities.WatchActivity
import com.callisdairy.UI.Fragments.autoPlayVideo.PlayerStateCallback
import com.callisdairy.UI.Fragments.autoPlayVideo.PlayerViewAdapter
import com.callisdairy.UI.Fragments.autoPlayVideo.PlayerViewAdapter.Companion.loadVideo
import com.callisdairy.UI.Fragments.autoPlayVideo.PlayerViewAdapter.Companion.releaseRecycledPlayers
import com.callisdairy.Utils.DateFormat
import com.callisdairy.Utils.Home
import com.callisdairy.api.response.HomePageDocs
import com.callisdairy.api.response.suggestionListDocs
import com.callisdairy.databinding.HomePostModellayoutBinding
import com.callisdairy.extension.setSafeOnClickListener
import com.google.android.exoplayer2.Player


class HomePostAdapter(
    var context: Context,
    var dataList: ArrayList<HomePageDocs>,
    val click: HomeUserProfileView,
    var likeClick: HomeLikePost,
    var moreClick: MorOptionsClick,
    private val clickSuggestedUser: SuggestionListClick,
    var suggestedUserData: ArrayList<suggestionListDocs>
) :
    RecyclerView.Adapter<HomePostAdapter.ViewHolder>() , PlayerStateCallback {
    var data: ArrayList<descriptionImage> = ArrayList()
    lateinit var commentAdapter: HomePostCommentAdapter
    lateinit var imageAdaptor: ImageHomeAdapter
    lateinit var adapterSuggestedUser: SuggestedHomeAdapter




    var check = false
    var indexValue = 0



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = HomePostModellayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)



        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty() && payloads[0] == "liked") {
            val dataItem = dataList[position]
            with(holder.binding) {
                likeCountText.text = dataItem.likeCount.toString()
                val isLiked = dataItem.isLiked
                likePost.isVisible = isLiked
                likePostText.isVisible = isLiked
                UnlikePost.isVisible = !isLiked
                UnlikePostText.isVisible = !isLiked
            }
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }


    override fun onViewRecycled(holder: ViewHolder) {
            super.onViewRecycled(holder)
            val position = holder.adapterPosition
            releaseRecycledPlayers(position)
            super.onViewRecycled(holder)
        }



    override fun getItemCount(): Int {
        return if(dataList.isEmpty() && Home.suggestedUserData.isEmpty()){
            dataList.size
        }else{
            dataList.size + 1
        }

    }



    inner class ViewHolder(val binding: HomePostModellayoutBinding) :
        RecyclerView.ViewHolder(binding.root)



    private fun formatUserTag(listData: HomePageDocs): CharSequence {
        val userName = "<font color=\"black\">${listData.userId.userName}</font>"
        val address = if (listData.address.isNotBlank()) {
            "in <font color=\"black\">${listData.address}</font>"
        } else {
            ""
        }
        val tagCount = listData.tagPeople.size

        val secondName = if (tagCount > 0) {
            if (tagCount == 1) {
                "tagged <font color=\"black\">${listData.tagPeople[0].name}</font>"
            } else {
                val names = StringBuilder()
                names.append("tagged <font color=\"black\">${listData.tagPeople[0].name}</font>")
                names.append(",")
                names.append("<font color=\"black\">${listData.tagPeople[1].name}</font>")
                names.toString()
            }
        } else {
            ""
        }

        val others = if (tagCount > 2) {
            "<font color=\"black\">${tagCount - 2} others</font>"
        } else {
            ""
        }

        return Html.fromHtml(when {
            tagCount > 2 && address.isNotBlank() -> "$userName $secondName and $others $address"
            tagCount == 2 && address.isNotBlank() -> "$userName $secondName $address"
            tagCount > 2 && address.isBlank() -> "$userName $secondName and $others"
            tagCount == 2 && address.isBlank() -> "$userName $secondName"
            address.isNotBlank() -> "$userName $address"
            else -> userName
        })
    }




    override fun onVideoDurationRetrieved(duration: Long, player: Player) {}

    override fun onVideoBuffering(player: Player) {

    }

    override fun onStartedPlaying(player: Player) {
    }


    override fun onFinishedPlaying(player: Player) {

    }


    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            if (dataList.isEmpty() && Home.suggestedUserData.isNotEmpty()) {

                holder.binding.suggestionUserList.isVisible = true
                holder.binding.mainHomeLayout.isVisible = false

                holder.binding.suggestedUserRecycler.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                adapterSuggestedUser = SuggestedHomeAdapter(context, Home.suggestedUserData, clickSuggestedUser)
                holder.binding.suggestedUserRecycler.adapter = adapterSuggestedUser


            }

            holder.binding.seeAll.setOnClickListener {
                val intent = Intent(context, SuggestionListActivity::class.java)
                intent.putExtra("from","Home")
                context.startActivity(intent)
            }
            dataList[position].also {


                            if (dataList.size > 4 && Home.suggestedUserData.isNotEmpty()){
                if (position == 5){

                    holder.binding.suggestionUserList.isVisible = true
                    holder.binding.mainHomeLayout.isVisible = false

                    holder.binding.suggestedUserRecycler.layoutManager = LinearLayoutManager(context,RecyclerView.HORIZONTAL,false)
                    adapterSuggestedUser = SuggestedHomeAdapter(context, Home.suggestedUserData,clickSuggestedUser)
                    holder.binding.suggestedUserRecycler.adapter = adapterSuggestedUser

                }else {
                    mainPageData(it,holder,position)
                }

            }else{
                if (position ==2 && dataList.size > 1 && Home.suggestedUserData.isNotEmpty()){

                    holder.binding.suggestionUserList.isVisible = true
                    holder.binding.mainHomeLayout.isVisible = false

                    holder.binding.suggestedUserRecycler.layoutManager = LinearLayoutManager(context,RecyclerView.HORIZONTAL,false)
                    adapterSuggestedUser = SuggestedHomeAdapter(context, Home.suggestedUserData,clickSuggestedUser)
                    holder.binding.suggestedUserRecycler.adapter = adapterSuggestedUser

                }
                else{
                mainPageData(it,holder,position)
            }
                }
            }


        }catch (e:Exception){
            e.printStackTrace()
        }

    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun mainPageData(listData: HomePageDocs, holder: ViewHolder,position:Int) {

        holder.binding.suggestionUserList.isVisible = false
        holder.binding.mainHomeLayout.isVisible = true


        with(holder.binding) {
            userName.text = formatUserTag(listData)
            Glide.with(context).load(listData.userId.petPic).placeholder(R.drawable.placeholder_pet).into(petImage)
            Glide.with(context).load(listData.userId.profilePic).placeholder(R.drawable.placeholder).into(userImage)
            totalDaysBack.text = DateFormat.covertTimeOtherFormat(listData.createdAt)
            CommentsCount.text = listData.commentCount.toString()
            commentCountText.text = listData.commentCount.toString()
            commentCountText.isVisible = listData.commentCount > 0
            LikesCount.text = listData.likeCount.toString()
            likeCountText.text = listData.likeCount.toString()
            likeCountText.isVisible = listData.likeCount > 0
            likes.text = context.getString(if (listData.likeCount > 1) R.string.likes else R.string.like)
            comments.text = context.getString(if (listData.commentCount > 1) R.string.comments else R.string.comment)
        }



        Glide.with(context).load(listData.mediaUrls.getOrNull(0)?.media?.thumbnail).into(holder.binding.thumbnail)
        holder.binding.itemVideoExoplayer.loadVideo(listData.mediaUrls.getOrNull(0)?.media?.mediaUrlMobile!!,this@HomePostAdapter,holder.binding.progressBar,holder.binding.thumbnail,position,false)


        val mediaUrl = listData.mediaUrls.getOrNull(0)?.media?.mediaUrlMobile
        if (mediaUrl!!.isEmpty()) {
            holder.binding.textViewShow.isVisible = true
            holder.binding.imagesView.isVisible = false
            holder.binding.videoView.isVisible = false
            holder.binding.thumbnail.isVisible = false
        }else{
            if (listData.mediaUrls.getOrNull(0)?.type.equals("video", ignoreCase = true) ) { // listData.mediaUrls.size == 1
                holder.binding.itemVideoExoplayer.keepScreenOn = true
                holder.binding.videoView.isVisible = true
                holder.binding.imagesView.isVisible = false
                holder.binding.textViewShow.isVisible = true
                holder.binding.thumbnail.isVisible = true
                holder.binding.progressBar.isVisible = true
            } else {
                holder.binding.videoView.isVisible = false
                holder.binding.imagesView.isVisible = true
                holder.binding.textViewShow.isVisible = true
                holder.binding.thumbnail.isVisible = false
                holder.binding.progressBar.isVisible = false
            }
        }



        if (listData.isLiked) {
            holder.binding.likePost.isVisible = true
            holder.binding.likePostText.isVisible = true
            holder.binding.UnlikePost.isVisible = false
            holder.binding.UnlikePostText.isVisible = false
        } else {
            holder.binding.likePost.isVisible = false
            holder.binding.likePostText.isVisible = false
            holder.binding.UnlikePost.isVisible = true
            holder.binding.UnlikePostText.isVisible = true
        }

//            Description Clicks

//            val title = listData.title.replace("\"", "")
        val title = listData.title

        if (title.length > 120) {
            val text = "<font color=\"blue\">Read More</font>"

            holder.binding.txtDescription.text = Html.fromHtml(
                "${title.substring(1, 110)}...$text",
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )


            holder.binding.textAllDescription.setSafeOnClickListener {
                holder.binding.textAllDescription.isVisible = false
                holder.binding.txtDescription.isVisible = true
            }

            holder.binding.txtDescription.setSafeOnClickListener {
                holder.binding.textAllDescription.isVisible = true
                holder.binding.txtDescription.isVisible = false
            }

        } else {
            holder.binding.txtDescription.text = title.replace("\"","")
        }

        val text_more = "<font color=\"blue\">Read Less</font>"
        holder.binding.textAllDescription.text =
            Html.fromHtml(
                "${title}...$text_more",
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )


//            Image list Set


        holder.binding.indicator.isVisible = listData.mediaUrls.size > 1

        imageAdaptor = ImageHomeAdapter(context, listData.mediaUrls,moreClick)
        holder.binding.storeViewpager.adapter = imageAdaptor
        holder.binding.indicator.setViewPager(holder.binding.storeViewpager)

//            Comments Adapter Set


        holder.binding.commentView.isVisible = listData.commentCount > 0

        holder.binding.commentsRecycler.layoutManager = LinearLayoutManager(context)
        commentAdapter = HomePostCommentAdapter(context, listData.topComments)
        holder.binding.commentsRecycler.adapter = commentAdapter




        holder.binding.userProfile.setSafeOnClickListener {
            click.viewProfile(listData.userId._id, listData.userId.userName)
        }


//            CommentClick

        holder.binding.CommentClick.setSafeOnClickListener {
            likeClick.commentView(position,listData._id)


        }

        holder.binding.commentsClickText.setSafeOnClickListener {
            likeClick.commentView(position,listData._id)
        }

        holder.binding.commentView.setSafeOnClickListener {
            likeClick.commentView(position,listData._id)

        }






//            Like Post Click


        holder.binding.likeClick.setSafeOnClickListener {
            likeClick.likePost(
                listData._id,
                listData.isLiked,
                holder.binding.UnlikePost,
                holder.binding.likePost,
                position
            )
        }

        holder.binding.likeClickText.setSafeOnClickListener {
            likeClick.likePostText(
                listData._id,
                listData.isLiked,
                holder.binding.UnlikePostText,
                holder.binding.likePostText,
                position
            )
        }


        holder.binding.videoView.setOnClickListener{
            val intent =  Intent(context, WatchActivity::class.java)
            intent.putExtra("url",listData.mediaUrls[0].media.mediaUrlMobile)
            context.startActivity(intent)

        }




//            More options Click

        var image = ""
        if (listData.mediaUrls.size > 0) {
            image = listData.mediaUrls[0].media.mediaUrlMobile
        }

        holder.binding.moreOptions.setSafeOnClickListener {
            moreClick.openPopUpMore(listData._id, position, listData.userId._id, image)
        }

        holder.binding.sharePost.setSafeOnClickListener {
            moreClick.sharePost(image)
        }

        val callback: ViewPager2.OnPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                if (position != indexValue) {
                    releaseRecycledPlayers(indexValue)
                }
                indexValue = position
                PlayerViewAdapter.playIndexThenPausePreviousPlayer(position)

            }
        }
        holder.binding.storeViewpager.registerOnPageChangeCallback(callback)


    }


    @SuppressLint("NotifyDataSetChanged")
    fun suggestionUsersAdapterNotify(position:Int){
        adapterSuggestedUser.notifyItemChanged(position)
        adapterSuggestedUser.notifyItemChanged(position, Home.suggestedUserData)
        adapterSuggestedUser.notifyDataSetChanged()
    }



    }

