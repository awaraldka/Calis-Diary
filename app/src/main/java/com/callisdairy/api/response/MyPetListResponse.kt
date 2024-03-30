package com.callisdairy.api.response

import com.google.gson.annotations.SerializedName

class MyPetListResponse(
    @SerializedName("responseMessage") val responseMessage : String?= null,
    @SerializedName("result") val result : MyPetListResult,
    @SerializedName("responseCode") val responseCode : Int
)


class MyPetListResult(
    @SerializedName("docs") val docs : ArrayList<MyPetListDocs>,
    @SerializedName("total") val total : Int?= null,
    @SerializedName("limit") val limit : Int?= null,
    @SerializedName("page") val page : Int?= null,
    @SerializedName("pages") val pages : Int?= null
)

class MyPetListDocs(
    @SerializedName("petDescription") val petDescription : PetDescriptionView,
//    @SerializedName("favUser") val favUser : ArrayList<String>,
//    @SerializedName("petImage") val petImage : ArrayList<String>,
    @SerializedName("lat") val lat : Number,
    @SerializedName("long") val long : Number,
    @SerializedName("mediaUrls") val mediaUrls : ArrayList<MediaUrls>,
    @SerializedName("status") val status : String?= null,
    @SerializedName("_id") val _id : String?= null,
//    @SerializedName("categoryId") val categoryId : CategoryId,
//    @SerializedName("subCategoryId") val subCategoryId : SubCategoryId,
    @SerializedName("petName") val petName : String?= null,
    @SerializedName("isMarketPlace") val isMarketPlace : Boolean,
    @SerializedName("dob") val dob : String?= null,
    @SerializedName("gender") val gender : String?= null,
    @SerializedName("breed") val breed : String?= null,
    @SerializedName("petBreedId") val petBreedId : petBreedIdResult,
    @SerializedName("language") val language : String?= null,
    @SerializedName("purchesStore") val purchesStore : String?= null,
    @SerializedName("description") val description : String?= null,
    @SerializedName("price") val price : Number,
    @SerializedName("dietFreq") val dietFreq : String?= null,
    @SerializedName("travel") val travel : String?= null,
    @SerializedName("movie") val movie : String?= null,
    @SerializedName("song") val song : String?= null,
    @SerializedName("celebrate") val celebrate : String?= null,
    @SerializedName("veterinary") val veterinary : String?= null,
    @SerializedName("veterinaryType") val veterinaryType : String?= null,
    @SerializedName("doctorAppoint") val doctorAppoint : String?= null,
    @SerializedName("petType") val petType : String?= null,
    @SerializedName("favColour") val favColour : String?= null,
    @SerializedName("habit") val habit : String?= null,
    @SerializedName("favFood") val favFood : String?= null,
    @SerializedName("favPlace") val favPlace : String?= null,
    @SerializedName("userId") val userId : UserId,
    @SerializedName("createdAt") val createdAt : String?= null,
    @SerializedName("updatedAt") val updatedAt : String?= null,
    @SerializedName("__v") val __v : Int?= null,
    @SerializedName("experience") val experience: String?= null,
    @SerializedName("percentage") val percentage: Int?= null,
    @SerializedName("animalShelter") val animalShelter: String?= null,
    @SerializedName("medicalReport") val medicalReport: String?= null,
    @SerializedName("habits") val habits: String?= null,
    @SerializedName("awards") val awards: String?= null,
    @SerializedName("favoriteClimate") val favoriteClimate: String?= null,
    @SerializedName("placeOfBirth") val placeOfBirth: String?= null,
    @SerializedName("commonDiseases") val commonDiseases: String?= null,
    @SerializedName("insurance") val insurance: String?= null,
    @SerializedName("toy") val toy: String?= null,
    @SerializedName("bestFriend") val bestFriend: String?= null,
    @SerializedName("origin") val origin: String
)


class PetDescriptionView(
        @SerializedName("size") val size : String? = null,
    @SerializedName("lastVaccinate") val lastVaccinate : String? = null
)

class petBreedIdResult(
    @SerializedName("_id") var _id:String = "",
)