package com.callisdairy.api.OtherApi

import com.google.gson.JsonObject
import com.kanabix.api.LocationLatLngResponse
import com.kanabix.api.LocationResponse
import retrofit2.Response
import javax.inject.Inject

class location_Interface_implement @Inject constructor(private val Location_Interface:  Location_Interface) {
    suspend fun getLocationApi(text :String,key:String): Response<LocationResponse> = Location_Interface.getLocationApi(text, key)

    suspend fun getLatLng(text :String,key:String): Response<LocationLatLngResponse> = Location_Interface.getLatLng(text, key)


    suspend fun getLatLngForPayment(text :String,key:String): Response<JsonObject> = Location_Interface.getLatLngForPayment(text, key)


    suspend fun nearBySearchApi(keyword :String,location:String,
                                radius :Int,type:String,key:String): Response<NearByLocationResponse> = Location_Interface.nearBySearchApi(keyword, location, radius, type, key)


    suspend fun nearBySearchCollegesApi(query:String,key:String): Response<NearByLocationResponse> = Location_Interface.nearBySearchCollegesApi(query, key)



}