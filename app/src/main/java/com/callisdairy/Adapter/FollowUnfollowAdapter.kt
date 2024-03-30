package com.callisdairy.Adapter


import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.callisdairy.Interface.ProfileFollowUnfollowMain
import com.callisdairy.Interface.UserProfileClick
import com.callisdairy.UI.Fragments.profile.FollowerListFragment
import com.callisdairy.UI.Fragments.profile.FollowingListFragment



class FollowUnfollowAdapter(
    var context: Context,
    fm: FragmentManager,
    var totalTabs: Int,
    val userProfileClick: UserProfileClick,
    val followUnfollow: ProfileFollowUnfollowMain,
    val userType: String,
    val userId: String


    ) : FragmentPagerAdapter(fm) {

    // this is for fragment tabs
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                FollowerListFragment(userProfileClick,userType,userId,followUnfollow)
            }
            1 -> {
                FollowingListFragment(userProfileClick,followUnfollow,userType,userId)
            }
            else -> null!!

        }
    }

    // this counts total number of tabs
    override fun getCount(): Int {
        return totalTabs
    }
}