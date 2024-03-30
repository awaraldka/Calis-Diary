package com.callisdairy.api.request

import com.google.gson.annotations.SerializedName

class AddPetRequest {
    @SerializedName("petName") var petName:String = ""
    @SerializedName("mediaUrls") var upload_file: ArrayList<mediaUrls> = ArrayList()
    @SerializedName("dob") var dob:String = ""
    @SerializedName("origin") var origin:String = ""
    @SerializedName("gender") var gender:String = ""
    @SerializedName("size") var size:String = ""
    @SerializedName("lastVaccinate") var lastVaccinate:String = ""
    @SerializedName("breed") var breed:String = ""
    @SerializedName("description") var description:String = ""
    @SerializedName("purchesStore") var purchesStore:String = ""
    @SerializedName("petType") var petType:String = ""
    @SerializedName("celebrate") var celebrate:String = ""
    @SerializedName("movie") var movie:String = ""
    @SerializedName("song") var song:String = ""
    @SerializedName("favColour") var favColour:String = ""
    @SerializedName("favFood") var favFood:String = ""
    @SerializedName("favPlace") var favPlace:String = ""
    @SerializedName("veterinary") var veterinary:String = ""
    @SerializedName("doctorAppoint") var doctorAppoint:String = ""
    @SerializedName("petBreedId") var petBreedId:String = ""
    @SerializedName("travel") var travel:String = ""
    @SerializedName("language") var language:String = ""
    @SerializedName("dietFreq") var dietFreq:String = ""
    @SerializedName("animalShelter") var animalShelter:String = ""
    @SerializedName("medicalReport") var medicalReport:String = ""
    @SerializedName("habits") var habits:String = ""
    @SerializedName("awards") var awards:String = ""
    @SerializedName("favoriteClimate") var favoriteClimate:String = ""
    @SerializedName("placeOfBirth") var placeOfBirth:String = ""
    @SerializedName("commonDiseases") var commonDiseases:String = ""
    @SerializedName("insurance") var insurance:String = ""
    @SerializedName("bestFriend") var bestFriend:String = ""
    @SerializedName("placeOfTravel") var placeOfTravel:String = ""
    @SerializedName("toy") var toy:String = ""
    @SerializedName("petAddress") var petAddress:String = ""
    @SerializedName("petCategoryId") var petCategoryId:String = ""
    @SerializedName("lat") var lat: Double = 0.0
    @SerializedName("long") var long: Double = 0.0
}
   

class UpdatePetRequest {
    @SerializedName("petName") var petName:String = ""
    @SerializedName("petCategoryId") var petCategoryId:String = ""
    @SerializedName("mediaUrls") var upload_file: ArrayList<mediaUrls> = ArrayList()
    @SerializedName("dob") var dob:String = ""
    @SerializedName("origin") var origin:String = ""
    @SerializedName("address") var petAddress:String = ""
    @SerializedName("description") var description:String = ""
    @SerializedName("gender") var gender:String = ""
    @SerializedName("size") var size:String = ""
    @SerializedName("lastVaccinate") var lastVaccinate:String = ""
    @SerializedName("breed") var breed:String = ""
    @SerializedName("petBreedId") var petBreedId:String = ""
    @SerializedName("purchesStore") var purchesStore:String = ""
    @SerializedName("petType") var petType:String = ""
    @SerializedName("celebrate") var celebrate:String = ""
    @SerializedName("movie") var movie:String = ""
    @SerializedName("song") var song:String = ""
    @SerializedName("favColour") var favColour:String = ""
    @SerializedName("favFood") var favFood:String = ""
    @SerializedName("favPlace") var favPlace:String = ""
    @SerializedName("veterinary") var veterinary:String = ""
    @SerializedName("doctorAppoint") var doctorAppoint:String = ""
    @SerializedName("travel") var travel:String = ""
    @SerializedName("language") var language:String = ""
    @SerializedName("dietFreq") var dietFreq:String = ""
    @SerializedName("animalShelter") var animalShelter:String = ""
    @SerializedName("medicalReport") var medicalReport:String = ""
    @SerializedName("habits") var habits:String = ""
    @SerializedName("awards") var awards:String = ""
    @SerializedName("favoriteClimate") var favoriteClimate:String = ""
    @SerializedName("placeOfBirth") var placeOfBirth:String = ""
    @SerializedName("commonDiseases") var commonDiseases:String = ""
    @SerializedName("insurance") var insurance:String = ""
    @SerializedName("bestFriend") var bestFriend:String = ""
    @SerializedName("placeOfTravel") var placeOfTravel:String = ""
    @SerializedName("toy") var toy:String = ""
    @SerializedName("lat") var lat: Double = 0.0
    @SerializedName("long") var long: Double = 0.0


}

//@SerializedName("categoryId") var categoryId:String = ""
//@SerializedName("subCategoryId") var subCategoryId:String = ""








class mediaUrls (
@SerializedName("media")var media: MediaRequestHome,
@SerializedName("type")var type: String = ""
)


class MediaRequestHome {
    @SerializedName("thumbnail")
    var thumbnail : String = ""
    @SerializedName("mediaUrlMobile")
    var mediaUrlMobile : String= ""
    @SerializedName("mediaUrlWeb")
    var mediaUrlWeb : String= ""
}






class EditPetRequest{
    @SerializedName("name") var name:String = ""
    @SerializedName("countryCode") var countryCode:String = ""
    @SerializedName("mobileNumber") var mobileNumber:String = ""
    @SerializedName("email") var email:String = ""
    @SerializedName("address") var address:String = ""
    @SerializedName("city") var city:String = ""
    @SerializedName("state") var state:String = ""
    @SerializedName("country") var country:String = ""
    @SerializedName("userTypes") var userTypes:String = ""
    @SerializedName("dateOfBirth") var dateOfBirth:String = ""
    @SerializedName("gender") var gender:String = ""
    @SerializedName("zipCode") var zipCode:String = ""
    @SerializedName("profilePic") var profilePic:String = ""
    @SerializedName("coverPic") var coverPic:String = ""

}
    


