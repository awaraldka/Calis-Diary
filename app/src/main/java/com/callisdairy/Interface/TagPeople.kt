package com.callisdairy.Interface

interface TagPeople {
    fun selectedPeople(id:String,name:String)
}

interface RemoveTaggedPeople{
    fun remove(id: String, adapterPosition: Int)
}


interface RemoveImage{
    fun deleteImage(adapterPosition: Int)
}

interface PetProfileClick{
    fun petProfileUpdate(petId: String, text: String)
    fun checkPlan()
}