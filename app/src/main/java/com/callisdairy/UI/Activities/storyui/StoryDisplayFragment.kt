package com.callisdairy.UI.Activities.storyui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
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
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.callisdairy.CalisApp
import com.callisdairy.ModalClass.storymodel.Story
import com.callisdairy.ModalClass.storymodel.StoryUser
import com.callisdairy.R
import com.callisdairy.Utils.Resource
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.Utils.story.OnSwipeTouchListener
import com.callisdairy.Utils.story.hide
import com.callisdairy.Utils.story.show
import com.callisdairy.databinding.FragmentStoryDisplayBinding
import com.callisdairy.extension.androidExtension
import com.callisdairy.storycustomview.StoriesProgressView
import com.callisdairy.viewModel.StoryCommentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import java.util.Calendar
import java.util.Locale


@AndroidEntryPoint
class StoryDisplayFragment : Fragment(), StoriesProgressView.StoriesListener {
    private var _binding: FragmentStoryDisplayBinding? = null
    private val binding get() = _binding!!
    var storyId = ""
    private lateinit var mediaDataSourceFactory: DataSource.Factory

    private val position: Int by
    lazy { arguments?.getInt(EXTRA_POSITION) ?: 0 }

    private val storyUser: StoryUser by
    lazy {
        (arguments?.getParcelable<StoryUser>(
            EXTRA_STORY_USER
        ) as StoryUser)
    }

    private val stories: ArrayList<Story> by
    lazy { storyUser.stories }


    private var simpleExoPlayer: ExoPlayer? = null

    //    private lateinit var mediaDataSourceFactory: DataSource.Factory
    private var pageViewOperator: PageViewOperator? = null
    private var counter = 0
    private var pressTime = 0L
    private var limit = 500L
    private var onResumeCalled = false
    private var onVideoPrepared = false
    private val viewModel: StoryCommentViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("RestrictedApi")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentStoryDisplayBinding.inflate(layoutInflater, container, false)


//        _binding!!.activityRootView.setSafeOnClickListener{
//            _binding!!.etCommentSection.focusable= View.NOT_FOCUSABLE
//            hideKeyboard()
//        }
        _binding!!.etCommentSection.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                simpleExoPlayer?.playWhenReady = false
                binding.storiesProgressView?.abandon()
            }
        }

        _binding!!.PostComment.setOnClickListener {
            if (_binding!!.etCommentSection.text.toString().isNotEmpty()) {
                println("========binding.etCommentSection.text======== $counter")
                hideKeyboard()
                _binding!!.etCommentSection.clearFocus()
                onResumeCalled = true
                if (stories[counter].isVideo() && !onVideoPrepared) {
                    simpleExoPlayer?.playWhenReady = false
                }

                simpleExoPlayer?.seekTo(5)
                simpleExoPlayer?.playWhenReady = true

                if (counter == 0) {
                    binding.storiesProgressView?.startStories()
                } else {
                    // restart animation
                    counter = StoryActivity.progressState.get(arguments?.getInt(EXTRA_POSITION) ?: 0)
                    binding.storiesProgressView?.startStories(counter)
                }

                val comment = _binding!!.etCommentSection.text.toString()
                viewModel.addStoryCommentApi(
                    SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.Token).toString(),
                    "STORY",
                    storyId,
                    comment
                )

                _binding!!.etCommentSection.text.clear()
            } else {
                Toast.makeText(requireContext(), "Please write any comment", Toast.LENGTH_SHORT).show()
            }
        }

        observeResponseAddComment()
        return binding.root
    }

    @SuppressLint("RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.storyDisplayVideo.useController = false
        updateStory()
        setUpUi()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.pageViewOperator = context as PageViewOperator
    }

    override fun onStart() {
        super.onStart()
        println("======onStart========== $counter")
        counter = restorePosition()
    }

    override fun onResume() {
        super.onResume()
        onResumeCalled = true
        println("======onResume========== $counter")
        if (stories[counter].isVideo() && !onVideoPrepared) {
            simpleExoPlayer?.playWhenReady = false
            return
        }

        simpleExoPlayer?.seekTo(5)
        simpleExoPlayer?.playWhenReady = true
        if (counter == 0) {
            binding.storiesProgressView?.startStories()
        } else {
            // restart animation
            counter = StoryActivity.progressState.get(arguments?.getInt(EXTRA_POSITION) ?: 0)
            binding.storiesProgressView?.startStories(counter)
        }
    }

    override fun onPause() {
        super.onPause()

        simpleExoPlayer?.playWhenReady = false
        binding.storiesProgressView?.abandon()
    }

    override fun onComplete() {
        simpleExoPlayer?.release()
        pageViewOperator?.nextPageView()
    }

    override fun onPrev() {
        println("======onPrev========== $counter")
        if (counter - 1 < 0) return
        --counter
        savePosition(counter)
        updateStory()
    }

    override fun onNext() {
        println("======onNext========== $counter")
        if (stories.size <= counter + 1) {
            return
        }
        ++counter
        savePosition(counter)
        updateStory()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        simpleExoPlayer?.release()
    }

    private fun updateStory() {
        println("======updateStory========== $counter")
        if (arguments?.getInt(EXTRA_POSITION) == 0) {
            _binding!!.commentPost.visibility = View.GONE
        } else {
            _binding!!.commentPost.visibility = View.VISIBLE
        }
        simpleExoPlayer?.stop()
        if (stories[counter].isVideo()) {
            binding.storyDisplayVideo.show()
            binding.storyDisplayImage.hide()
            binding.storyDisplayVideoProgress.show()
            initializePlayer()
        } else {
            binding.storyDisplayVideo.hide()
            binding.storyDisplayVideoProgress.hide()
            binding.storyDisplayImage.show()

            Glide.with(this)
                .load(stories[counter].url).thumbnail(0.25f)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        pauseCurrentStory()
                        binding.storyDisplayVideoProgress.show()
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: com.bumptech.glide.load.DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        resumeCurrentStory()
                        binding.storyDisplayVideoProgress.hide()

                        // Start the story progress view after the image is loaded
                        if (counter == 0) {
                            binding.storiesProgressView?.startStories()
                        } else {
                            counter = StoryActivity.progressState.get(arguments?.getInt(EXTRA_POSITION) ?: 0)
                            binding.storiesProgressView?.startStories(counter)
                        }

                        return false
                    }
                })
                .into(binding.storyDisplayImage)



//            Glide.with(this).load(stories[counter].url).into(binding.storyDisplayImage)
        }

        val cal: Calendar = Calendar.getInstance(Locale.ENGLISH).apply {
            timeInMillis = stories[counter].storyDate
        }
        binding.storyDisplayTime.text = DateFormat.format("HH:mm aa", cal).toString()
        binding.storyDisplayCaption.text = stories[counter].caption.replace("\"", "")
        storyId = stories[counter]._id
    }

    @OptIn(UnstableApi::class) @SuppressLint("Range")
    private fun initializePlayer() {
        println("======initializePlayer========== $counter")
        if (simpleExoPlayer == null) {
            simpleExoPlayer = ExoPlayer.Builder(requireContext()).build()
        } else {
            simpleExoPlayer?.release()
            simpleExoPlayer = null
            simpleExoPlayer = ExoPlayer.Builder(requireContext()).build()
        }
        binding.storyDisplayVideo.keepScreenOn = true

        val upstreamDataSourceFactory = DefaultHttpDataSource.Factory()
            .setUserAgent(Util.getUserAgent(requireContext(), getString(R.string.app_name)))

        mediaDataSourceFactory = CalisApp.simpleCache?.let {
            CacheDataSource.Factory()
                .setCache(it) // Set the cache
                .setUpstreamDataSourceFactory(
                    upstreamDataSourceFactory
                )
        }!!

        // Create a media source
        val mediaSource: MediaSource = ProgressiveMediaSource.Factory(mediaDataSourceFactory!!)
            .createMediaSource(MediaItem.fromUri(Uri.parse(stories[counter].url)))


        simpleExoPlayer?.prepare(mediaSource, false, false)
        if (onResumeCalled) {
            simpleExoPlayer?.playWhenReady = true
        }

        binding.storyDisplayVideo.setShutterBackgroundColor(Color.BLACK)
        binding.storyDisplayVideo.player = simpleExoPlayer

        simpleExoPlayer?.addListener(object : Player.Listener {
//            override fun onPlayerError(error: ExoPlaybackException?) {
//                super.onPlayerError(error)
//                binding.storyDisplayVideoProgress.hide()
//                if (counter == stories.size.minus(1)) {
//                    pageViewOperator?.nextPageView()
//                } else {
//                    binding.storiesProgressView?.skip()
//                }
//            }

            override fun onLoadingChanged(isLoading: Boolean) {
                super.onLoadingChanged(isLoading)
                if (isLoading) {
                    binding.storyDisplayVideoProgress.show()
                    pressTime = System.currentTimeMillis()
                    pauseCurrentStory()
                } else {
                    binding.storyDisplayVideoProgress.hide()
                    binding.storiesProgressView?.getProgressWithIndex(counter)
                        ?.setDuration(simpleExoPlayer?.duration ?: 8000L)
                    onVideoPrepared = true
                    resumeCurrentStory()
                }
            }
        })



    }

    private fun setUpUi() {
        println("======setUpUi========== $counter")

        val touchListener = object : OnSwipeTouchListener(requireActivity()) {
            override fun onSwipeTop() {
//                Toast.makeText(activity, "onSwipeTop", Toast.LENGTH_LONG).show()
            }

            override fun onSwipeBottom() {
                activity?.finishAfterTransition()
//                Toast.makeText(activity, "onSwipeBottom", Toast.LENGTH_LONG).show()
            }

            override fun onClick(view: View) {
                when (view) {
                    binding.next -> {
                        if (counter == stories.size - 1) {
                            pageViewOperator?.nextPageView()
                        } else {
                            StoryActivity.progressState.clear()
                            binding.storiesProgressView?.skip()
                        }
                    }
                    binding.previous -> {
                        if (counter == 0) {
                            pageViewOperator?.backPageView()
                        } else {
                            binding.storiesProgressView?.reverse()
                        }
                    }
                }
            }

            override fun onLongClick() {
                hideStoryOverlay()
            }

            override fun onTouchView(view: View, event: MotionEvent): Boolean {
                super.onTouchView(view, event)
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        pressTime = System.currentTimeMillis()
                        pauseCurrentStory()
                        return false
                    }
                    MotionEvent.ACTION_UP -> {
                        showStoryOverlay()
                        resumeCurrentStory()
                        return limit < System.currentTimeMillis() - pressTime
                    }
                }
                return false
            }
        }
        binding.previous.setOnTouchListener(touchListener)
        binding.next.setOnTouchListener(touchListener)

        binding.storiesProgressView?.setStoriesCountDebug(
            stories.size, position = arguments?.getInt(EXTRA_POSITION) ?: -1
        )
        binding.storiesProgressView?.setAllStoryDuration(4000L)
        binding.storiesProgressView?.setStoriesListener(this)

        Glide.with(this).load(storyUser.profilePicUrl).circleCrop()
            .into(binding.storyDisplayProfilePicture)
        binding.storyDisplayNick.text = storyUser.username
    }

    private fun showStoryOverlay() {
        if (binding.storyOverlay == null || binding.storyOverlay.alpha != 0F) return

        binding.storyOverlay.animate()
            .setDuration(100)
            .alpha(1F)
            .start()
    }

    private fun hideStoryOverlay() {
        if (binding.storyOverlay == null || binding.storyOverlay.alpha != 1F) return

        binding.storyOverlay.animate()
            .setDuration(200)
            .alpha(0F)
            .start()
    }

    private fun savePosition(pos: Int) {
        StoryActivity.progressState.put(position, pos)
    }

    private fun restorePosition(): Int {
        return StoryActivity.progressState.get(position)
    }

    fun pauseCurrentStory() {
        simpleExoPlayer?.playWhenReady = false
        binding.storiesProgressView.pause()
    }

    fun resumeCurrentStory() {
        if (onResumeCalled) {
            simpleExoPlayer?.playWhenReady = true
            showStoryOverlay()
            binding.storiesProgressView.resume()
        }
    }

    companion object {
        private const val EXTRA_POSITION = "EXTRA_POSITION"
        private const val EXTRA_STORY_USER = "EXTRA_STORY_USER"
        fun newInstance(position: Int, story: StoryUser): StoryDisplayFragment {
            return StoryDisplayFragment().apply {
                arguments = Bundle().apply {
                    putInt(EXTRA_POSITION, position)
                    putParcelable(EXTRA_STORY_USER, story)
                }
            }
        }
    }


    fun hideKeyboard() {
        try {
            val `in`: InputMethodManager =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            val view = requireActivity().findViewById<View>(android.R.id.content)
            assert(`in` != null)
            `in`.hideSoftInputFromWindow(
                view.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        } catch (ignored: Throwable) {
        }
    }

    private fun observeResponseAddComment() {
        lifecycleScope.launchWhenCreated {
            viewModel._addCommentData.collectLatest { response ->

                when (response) {
                    is Resource.Success -> {

                        if (response.data?.responseCode == 200) {
                            try {
                                Toast.makeText(
                                    requireContext(),
                                    response.data?.responseMessage,
                                    Toast.LENGTH_SHORT
                                ).show()
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }

                    is Resource.Error -> {
                        response.message?.let { message ->
                            androidExtension.alertBox(message, requireContext())
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