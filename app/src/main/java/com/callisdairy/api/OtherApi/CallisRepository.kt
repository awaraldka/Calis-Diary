package com.callisdairy.api.OtherApi

import com.google.gson.JsonObject
import com.kanabix.api.LocationLatLngResponse
import com.kanabix.api.LocationResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import javax.inject.Inject

class CallisRepository @Inject constructor(private val apiServiceImpl: location_Interface_implement) {

    fun getLocationApi(text:String,key:String): Flow<Response<LocationResponse>> = flow {
        emit(apiServiceImpl.getLocationApi(text, key))
    }.flowOn(Dispatchers.IO)


    fun getLatLng(text:String,key:String): Flow<Response<LocationLatLngResponse>> = flow {
        emit(apiServiceImpl.getLatLng(text, key))
    }.flowOn(Dispatchers.IO)

    fun getLatLngForPayment(text:String,key:String): Flow<Response<JsonObject>> = flow {
        emit(apiServiceImpl.getLatLngForPayment(text, key))
    }.flowOn(Dispatchers.IO)


    fun nearBySearchApi(keyword :String,location:String,
                  radius :Int,type:String,key:String): Flow<Response<NearByLocationResponse>> = flow {
        emit(apiServiceImpl.nearBySearchApi(keyword, location, radius, type, key))
    }.flowOn(Dispatchers.IO)


    fun nearBySearchCollegesApi(query:String,key:String): Flow<Response<NearByLocationResponse>> = flow {
        emit(apiServiceImpl.nearBySearchCollegesApi(query, key))
    }.flowOn(Dispatchers.IO)


}