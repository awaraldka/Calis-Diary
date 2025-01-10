package com.callisdairy.UI.Activities.storyui

import android.animation.Animator
import android.animation.ValueAnimator
import android.net.Uri
import android.os.Bundle
import android.util.SparseIntArray
import androidx.appcompat.app.AppCompatActivity
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.media3.common.util.Util
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DataSpec
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.SimpleCache
import com.bumptech.glide.Glide
import com.callisdairy.CalisApp
import com.callisdairy.ModalClass.storymodel.StoryUser
import com.callisdairy.R
import com.callisdairy.Utils.story.CubeOutTransformer
import com.callisdairy.databinding.ActivityStoryBinding
import com.callisdairy.storycustomview.StoryPagerAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
        val scope = CoroutineScope(Dispatchers.IO) // Use a CoroutineScope for background tasks
        videoList.forEach { data ->
            scope.launch {
                val dataUri = Uri.parse(data)
                val dataSpec = DataSpec(dataUri, 0, 500 * 1024, null)

                val cache: SimpleCache? = CalisApp.simpleCache // Assuming this is your cache instance

                // Create a DefaultDataSource with caching
                val upstreamDataSourceFactory = DefaultHttpDataSource.Factory()
                    .setUserAgent(Util.getUserAgent(applicationContext, getString(R.string.app_name)))


                // Create the CacheDataSource with the upstream factory
                val cacheDataSourceFactory = CacheDataSource.Factory()
                    .setCache(cache!!) // Use your cache instance
                    .setUpstreamDataSourceFactory(upstreamDataSourceFactory)

                val dataSource: DataSource = cacheDataSourceFactory.createDataSource()

                // Instead of a CacheUtil listener, you can manually log the cache progress if needed
                // However, the CacheDataSource itself does not provide a built-in progress listener
                // You can implement your logic to monitor download progress if necessary.

                try {
                    // Instead of CacheUtil.cache(), you can read from the data source to cache
                    val cacheResult = dataSource.open(dataSpec) // Open the dataSpec to start caching
                    // Process the cache result as needed, e.g., read the input stream.
//                    cacheResult.close() // Close when done
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
