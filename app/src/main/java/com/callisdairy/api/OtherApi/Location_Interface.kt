package com.callisdairy.api.OtherApi

import com.google.gson.JsonObject
import com.kanabix.api.LocationLatLngResponse
import com.kanabix.api.LocationResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Location_Interface {

    @GET("place/queryautocomplete/json")
    suspend fun getLocationApi(@Query("input") text:String, @Query("key") key:String) : Response<LocationResponse>

    @GET("geocode/json")
    suspend fun getLatLng(@Query("address") text:String, @Query("key") key:String) : Response<LocationLatLngResponse>

    @GET("place/nearbysearch/json")
    suspend fun nearBySearchApi(@Query("keyword") keyword:String, @Query("location") location:String
                                ,@Query("radius") radius:Int,@Query("type") type:String,@Query("key") key:String) : Response<NearByLocationResponse>

    @GET("place/nearbysearch/json")
    suspend fun nearBySearchCollegesApi(@Query("query") query:String,@Query("key") key:String) : Response<NearByLocationResponse>



    @GET("geocode/json")
    suspend fun getLatLngForPayment(@Query("latlng") text:String, @Query("key") key:String) : Response<JsonObject>

}