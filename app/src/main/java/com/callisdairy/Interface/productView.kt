package com.callisdairy.Interface

import android.widget.ImageView

interface productView {
    fun view(_id: String, interested: Boolean, position: Int)
}

interface serviceView{
    fun viewDes(_id: String, interested: Boolean)
}

interface viewPests{
    fun viewPet(_id: String, interested: Boolean)
}

interface MissingPest{
    fun viewMissingPet(_id: String)
    fun trackPet(trackingId: String)
    fun deleteMissingPet(_id: String,position: Int)
}

interface viewChat{
    fun viewChatClick()
}

interface ViewProfile{
    fun view()
}


interface CategoryView{
    fun petCategoryView(text: String)
}


interface LikeUnlikeProduct{
    fun addLikeUnlike(_id: String, position: Int, heart: ImageView, heartfill: ImageView)
}


interface LikeUnlikePet{
    fun addLikeUnlikePet(_id: String, position: Int, heart: ImageView, heartfill: ImageView)
}


interface LikeUnlikeService{
    fun addLikeUnlikeService(_id: String, position: Int, heart: ImageView, heartfill: ImageView)
}



interface DeleteMyEvent{
    fun delete(id:String,position: Int)
    fun editEvent(id: String, position: Int)
    fun share(imageUrl:String)
}

interface DeleteClick{
    fun deleteItem()
}

interface Finish{
    fun finishActivity()
}


interface FinishBack{
    fun finishBackActivity()
}


interface petProfile {
    fun addProfile()
    fun notAddingNow()
}


interface ReportPost{
    fun reportPost(etCaption:String,position:Int,id:String)
}


interface UnIntrestedPetProductService {
    fun petUnIntrested(id:String,position:Int)
    fun productUnIntrested(id:String,position:Int)
    fun serviceUnIntrested(id:String,position:Int)

}