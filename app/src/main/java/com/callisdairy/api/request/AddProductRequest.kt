package com.callisdairy.api.request

import com.google.gson.annotations.SerializedName

class AddProductRequest {
    @SerializedName("categoryId") var categoryId: String = ""
    @SerializedName("description") var description: String = ""
    @SerializedName("lat") var lat: Double = 0.0
    @SerializedName("long") var long: Double = 0.0
    @SerializedName("petCategoryId") var petCategoryId: ArrayList<String> = arrayListOf()
    @SerializedName("productImage") var productImage: ArrayList<String> = arrayListOf()
    @SerializedName("price") var price: String = ""
    @SerializedName("productName") var productName: String = ""
    @SerializedName("subCategoryId") var subCategoryId: String = ""
    @SerializedName("weight") var weight: String = ""
    @SerializedName("currency") var currency: String = ""
}
class UpdateProductRequest {
    @SerializedName("productId") var productId: String = ""
    @SerializedName("categoryId") var categoryId: String = ""
    @SerializedName("description") var description: String = ""
    @SerializedName("productImage") var productImage: ArrayList<String> = arrayListOf()
    @SerializedName("price") var price: String = ""
    @SerializedName("currency") var currency: String = ""
    @SerializedName("productName") var productName: String = ""
    @SerializedName("subCategoryId") var subCategoryId: String = ""
    @SerializedName("weight") var weight: String = ""
}

class AddServiceRequest {
    @SerializedName("categoryId") var categoryId: String = ""
    @SerializedName("description") var description: String = ""
    @SerializedName("lat") var lat: Double = 0.0
    @SerializedName("long") var long: Double = 0.0
    @SerializedName("petCategoryId") var petCategoryId: ArrayList<String> = arrayListOf()
    @SerializedName("serviceImage") var productImage: ArrayList<String> = arrayListOf()
    @SerializedName("price") var price: String = ""
    @SerializedName("serviceName") var serviceName: String = ""
    @SerializedName("subCategoryId") var subCategoryId: String = ""
    @SerializedName("experience") var experience: String = ""
    @SerializedName("experience_month") var experience_month: String = ""
    @SerializedName("currency") var currency: String = ""
}

class UpdateServiceRequest {
    @SerializedName("serviceId") var serviceId: String = ""
    @SerializedName("serviceName") var serviceName: String = ""
    @SerializedName("description") var description: String = ""
    @SerializedName("categoryId") var categoryId: String = ""
    @SerializedName("subCategoryId") var subCategoryId: String = ""
    @SerializedName("price") var price: String = ""
    @SerializedName("currency") var currency: String = ""
    @SerializedName("experience") var experience: String = ""
    @SerializedName("experience_month") var experience_month: String = ""
    @SerializedName("serviceImage") var serviceImage: ArrayList<String> = arrayListOf()

}