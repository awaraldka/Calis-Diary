package com.callisdairy.Interface

import android.widget.ImageView
import android.widget.LinearLayout
import com.callisdairy.api.response.MediaUrls


interface HomeComments {
    fun commentSection(userName: String, _id: String)
    fun ownCommentDeleteClick(userName: String, comment: String, _id: String, position: Int)
    fun ownCommentEditClick(userName: String, comment: String, _id: String, position: Int)
    fun ownCommentRepliedDeleteClick(userName: String, comment: String, _id: String, position: Int, s: String)
    fun ownCommentRepliedEditClick(userName: String, comment: String, _id: String, position: Int, s: String)
}

interface ViewReply{
    fun showAllComments(_id: String, position: Int)
    fun likeComment(
        _id: String,
        likedValue: Boolean,
        unlikePost: ImageView,
        likePost: ImageView,
        position: Int,
        commentId:String
    )
}



interface HomeLikePost{
    fun likePost(
        _id: String,
        likedValue: Boolean,
        unlikePost: ImageView,
        likePost: ImageView,
        position: Int
    )

    fun likePostText(
        _id: String,
        likedValue: Boolean,
        unlikePost: ImageView,
        likePost: ImageView,
        position: Int
    )



    fun commentView(position:Int,id:String)
}




interface MorOptionsClick{
    fun openPopUpMore(_id: String, position: Int, _id1: String,imageUrl:String)
    fun sharePost(imageUrl:String)
    fun viewImages(media: ArrayList<MediaUrls>)
}



interface SuggestionListClick{
    fun follow(
        _id: String,
        position: Int,
        follow: Boolean,
        requested: Boolean,
        followButton: LinearLayout,
        unFollowButton: LinearLayout,
        privacyType: String
    )
    fun unFollow(
        _id: String,
        position: Int,
        follow: Boolean,
        requested: Boolean,
        followButton: LinearLayout,
        unFollowButton: LinearLayout,
        privacyType: String,
        requestedButton: LinearLayout
    )

    fun requested(
        _id: String,
        position: Int,
        follow: Boolean,
        requested: Boolean,
        followButton: LinearLayout,
        unFollowButton: LinearLayout,
        privacyType: String,
        requestedButton: LinearLayout
    )
}



interface CategoryClick{
    fun getCategoryValue(_id: String)
}

interface ServiceClick{
    fun getServiceValue(_id: String)
}


interface SubCategoryClick{
    fun getSubCategoryValue(_id: String, subCategoryName: String)
}

interface InterestedClick{
    fun productInterest(_id: String, position: Int)
    fun petInterest(_id: String, position: Int)
    fun serviceInterest(_id: String, position: Int)
}

interface LocationClick{
    fun getLocation(locationName: String)
}

interface LocationClickNearBy{
    fun getLocationNearBy(locationName: String)
}



interface HomeStoryClick{
    fun addStory()
}



interface GetFilterData{
    fun getFilterData(Name: String)
}




