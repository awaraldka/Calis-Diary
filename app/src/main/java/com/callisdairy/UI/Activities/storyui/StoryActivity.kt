package com.callisdairy.UI.Activities.storyui

import android.animation.Animator
import android.animation.ValueAnimator
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.SparseIntArray
import androidx.appcompat.app.AppCompatActivity
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.bumptech.glide.Glide
import com.callisdairy.CalisApp
import com.callisdairy.ModalClass.storymodel.StoryUser
import com.callisdairy.R
import com.callisdairy.Utils.story.CubeOutTransformer
import com.callisdairy.databinding.ActivityStoryBinding
import com.callisdairy.storycustomview.StoryPagerAdapter
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DataSpec
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.CacheUtil
import com.google.android.exoplayer2.util.Util
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

@AndroidEntryPoint
class StoryActivity : AppCompatActivity(),
    PageViewOperator  {
    private lateinit var binding: ActivityStoryBinding
    private lateinit var pagerAdapter: StoryPagerAdapter
    private var currentPage: Int = 0
    var storyData = ArrayList<StoryUser>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.attributes.windowAnimations = R.style.Fade
        storyData = intent.getParcelableArrayListExtra("storyData")!!
        currentPage = intent.getIntExtra("currentPosition",0)
        setUpPager()
    }

    override fun backPageView() {
        if (binding.viewPager.currentItem > 0) {
            try {
                fakeDrag(false)
            } catch (e: Exception) {
                //NO OP
            }
        }
    }

    override fun nextPageView() {
        if (binding.viewPager.currentItem + 1 < binding.viewPager.adapter?.count ?: 0) {
            try {
                fakeDrag(true)
            } catch (e: Exception) {
                //NO OP
            }
        } else {
            //there is no next story
            finishAfterTransition()
//            Toast.makeText(this, "All stories displayed.", Toast.LENGTH_LONG).show()
        }
    }

    private fun setUpPager() {
//        val storyUserList = StoryGenerator.generateStories()
        preLoadStories(storyData)

        pagerAdapter =
            StoryPagerAdapter(
                supportFragmentManager,
                storyData
            )

        binding.viewPager.adapter = pagerAdapter
        binding.viewPager.currentItem = currentPage
        binding.viewPager.setPageTransformer(
            true,
            CubeOutTransformer()
        )
        binding.viewPager.addOnPageChangeListener(object : PageChangeListener() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                currentPage = position
            }

            override fun onPageScrollCanceled() {
                currentFragment()?.resumeCurrentStory()
            }
        })
    }

    private fun preLoadStories(storyUserList: ArrayList<StoryUser>) {
        val imageList = mutableListOf<String>()
        val videoList = mutableListOf<String>()

        storyUserList.forEach { storyUser ->
            storyUser.stories.forEach { story ->
                if (story.isVideo()) {
                    videoList.add(story.url)
                } else {
                    imageList.add(story.url)
                }
            }
        }
        preLoadVideos(videoList)
        preLoadImages(imageList)
    }

    private fun preLoadVideos(videoList: MutableList<String>) {
        videoList.map { data ->
            GlobalScope.async {
                val dataUri = Uri.parse(data)
                val dataSpec = DataSpec(dataUri, 0, 500 * 1024, null)
                val dataSource: DataSource =
                    DefaultDataSourceFactory(
                        applicationContext,
                        Util.getUserAgent(applicationContext, getString(R.string.app_name))
                    ).createDataSource()



                val listener = CacheUtil.ProgressListener { requestLength: Long, bytesCached: Long, _: Long ->
                        val downloadPercentage = (bytesCached * 100.0
                                / requestLength)
                        Log.d("preLoadVideos", "downloadPercentage: $downloadPercentage")
                    }

                try {
                    CacheUtil.cache(
                        dataSpec,
                        CalisApp.simpleCache,
                        CacheUtil.DEFAULT_CACHE_KEY_FACTORY,
                        dataSource,
                        listener,
                        null
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }




    private fun preLoadImages(imageList: MutableList<String>) {
        imageList.forEach { imageStory ->
            Glide.with(this).load(imageStory).preload()
        }
    }

    private fun currentFragment(): StoryDisplayFragment? {
        return pagerAdapter.findFragmentByPosition(binding.viewPager, currentPage) as StoryDisplayFragment
    }

    private var prevDragPosition = 0

    private fun fakeDrag(forward: Boolean) {
        if (prevDragPosition == 0 && binding.viewPager.beginFakeDrag()) {
            ValueAnimator.ofInt(0, binding.viewPager.width).apply {
                duration = 400L
                interpolator = FastOutSlowInInterpolator()
                addListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator) {

                    }

                    override fun onAnimationEnd(animation: Animator) {
                        removeAllUpdateListeners()
                        if (binding.viewPager.isFakeDragging) {
                            binding.viewPager.endFakeDrag()
                        }
                        prevDragPosition = 0
                    }

                    override fun onAnimationCancel(animation: Animator) {
                        removeAllUpdateListeners()
                        if (binding.viewPager.isFakeDragging) {
                            binding.viewPager.endFakeDrag()
                        }
                        prevDragPosition = 0
                    }

                    override fun onAnimationRepeat(animation: Animator) {

                    }
                })
                addUpdateListener {
                    if (!binding.viewPager.isFakeDragging) return@addUpdateListener
                    val dragPosition: Int = it.animatedValue as Int
                    val dragOffset: Float =
                        ((dragPosition - prevDragPosition) * if (forward) -1 else 1).toFloat()
                    prevDragPosition = dragPosition
                    binding.viewPager.fakeDragBy(dragOffset)
                }
            }.start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        progressState.clear()
    }

    companion object {
        val progressState = SparseIntArray()
    }

//    override fun openStory() {
//        TODO("Not yet implemented")
//    }
//
//    override fun closeStory() {
//        TODO("Not yet implemented")
//    }

    }
