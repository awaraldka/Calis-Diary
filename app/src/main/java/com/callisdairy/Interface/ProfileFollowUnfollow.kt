package com.callisdairy.Interface

import com.callisdairy.Adapter.FollowerAdapter
import com.callisdairy.Adapter.FollowingAdapter
import com.callisdairy.api.response.FollowersListDocs
import com.callisdairy.api.response.FollowingUserListFollowing

interface ProfileFollowUnfollow {
    fun  followUnfollow(
        _id: String,
        position: Int,
        followingData: ArrayList<FollowingUserListFollowing>,

        )
    fun  followUnfollowTwo(
        _id: String,
        position: Int,
        followingData: ArrayList<FollowersListDocs>,

        )

}

interface ProfileFollowUnfollowMain {

    fun  followUnfollowMain(
        _id: String,
        position: Int,
        followingData: ArrayList<FollowingUserListFollowing>,
        followingAdapter: FollowingAdapter,

        )
    fun  followUnfollowMainTwo(
        _id: String,
        position: Int,
        followingData: ArrayList<FollowersListDocs>,
        followingAdapter: FollowerAdapter,

        )
}