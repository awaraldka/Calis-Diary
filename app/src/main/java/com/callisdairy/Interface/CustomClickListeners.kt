package com.callisdairy.Interface

import android.view.View
import android.widget.ImageView
import com.callisdairy.api.response.MediaUrls

interface CustomClickListeners {
}

interface FollowDialogListener {
    fun restrictListener()
    fun followListener()
    fun muteListener()
}

interface RestrictDialogListener {
    fun restrictAccountListener()
}

interface PetListener {
    fun sellListener()
    fun removeListener()
    fun deleteListener()
}


interface UserProfileClick{

    fun userProfileListener(_id: String, userName: String)
}

interface ViewPost{
    fun viewPost(_id: String)
}

interface VendorFilter{
    fun vendorFilter(startDate:String,endDate:String,type:String)
}

interface AddPostListener{
    fun post()
    fun story()
    fun pet()
    fun live()
}



interface AddListenerVendor{
    fun pet()
    fun product()
    fun service()
}




interface MoreOptions{
    fun share(imageUrl:String)
    fun hidePost(_id: String, position: Int)
    fun report(_id: String, position: Int)
    fun deletePost(_id: String, position: Int)
}



interface PopupItemClickListener {
    fun getData(data: String, flag: String, code:String)
}

interface PetNameClickListener {
    fun selectedData(
        petName: String?,
        mediaUrls: ArrayList<MediaUrls>,
        gender: String?,
        breed: String?,
        name: String,
        address: String,
        email: String,
        mobileNumber: String,
        petIdBreed:String,
        petId:String
    )
}

interface PopupItemClickListenerProfile {
    fun getDataForProfile(data: String, flag: String, code:String)
}

interface ViewPetFromProfile{
    fun viewPetDetails(_id: String)
}

interface HomeUserProfileView{
    fun viewProfile(_id: String, userName: String)
}


interface AllProductsView{
    fun viewAll()
}



interface StoryView{
    fun viewStories(image: ImageView, skip: View, reverse: View)
}

interface LocalizationClick{
    fun getLanguage(code: String)
}

interface CommonDialogInterface{
    fun commonWork()
}

interface CancelAppointmentInterface{
    fun commonWork(date:String)
}

interface AddPetInterface{
    fun petPrice(petPriceValue: String)
}


interface StoryViewListeners{
    fun openStory()
    fun closeStory()
}

interface BannerSliderListener {
    fun slider(s: String, videoUrl: String, duration: Long)
}


interface AudioListeners {
    fun playAudioClick(
        url: String,
        ownUserPlay: ImageView,
        ownUserPause: ImageView,
        otherUserPlay: ImageView,
        otherUserPause: ImageView,
        s: String,
        index: Int,
        finishAudioListener: FinishAudioListener
    )
    fun pauseAudioClick()
}

interface FinishAudioListener {
    fun finishAudio(flag : String)
}


interface Logout{
    fun logoutUser()
}

interface GetLocation {
    fun getLocation(name: String)
}


interface ViewImages {
    fun viewImage(message: String)
}

interface ViewUserList {
    fun viewUserList(_id: String?, from: String)
}

interface SpecializationClick {
    fun clickValue(name:String)
}


interface CommentsViewClick {

}