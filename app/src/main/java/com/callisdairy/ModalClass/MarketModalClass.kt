package com.callisdairy.ModalClass

class MarketModalClass (
    var Image:Int,
    var Name:String
    )
class productCategoryModalClass (
    var Image:Int,
    var Name:String
    )

class serviceCategoryModalClass (
    var Image:Int,
    var Name:String
    )


class PetListDataModelClass(
    var petName:String,
    var Image:Int,
    var location:String,
    var ownerName:String,
    var price:String,
    var isLiske:Boolean
)


class productListModelClass(
    var image:Int,
    var productNAme:String,
    var productPrice:String,
    var productQuantity:String
)

class descriptionImage(
    var image:Int
)

class language(
    var image : Int,
    var language: String,
    var code: String,
    var selectedLanguage: String,
)

class FAQSModelClass(
    var heading : String,
    var content: String,
    var expand : Boolean = false
)

class storyHome(
    var ProfileImage:Int,
    var StoryView:Int,
    var name:String
)


class DialogData(
    var name:String
)


class BlockUserData(
    var name:String,
    var petName:String,
    var petType:String,
    var imagePet:Int,
    var imageUser:Int,
    var isBlocked:Boolean
    )
class AppointmentData(
    var name:String,
    var petName:String,
    var petType:String,
    var imagePet:Int,
    var imageUser:Int,
    var status:String,
    var date:String,

    )




class BlockDoctorData(
    var name:String,
    var specialization:String,
    var exp:String,
    var imageUser:Int,
    var isBlocked:Boolean
)