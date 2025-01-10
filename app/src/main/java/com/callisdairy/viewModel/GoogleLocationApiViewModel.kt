package com.callisdairy.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.callisdairy.ModalClass.PojoClass
import com.callisdairy.Utils.NetworkHelper
import com.callisdairy.Utils.Resource
import com.callisdairy.api.Constants
import com.callisdairy.api.OtherApi.CallisRepository
import com.callisdairy.api.OtherApi.NearByLocationResponse
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.kanabix.api.LocationLatLngResponse
import com.kanabix.api.LocationResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class GoogleLocationApiViewModel @Inject constructor(app: Application, private val respository: CallisRepository,private val networkHelper: NetworkHelper): AndroidViewModel(app) {


    private val LocationStateFlow: MutableStateFlow<Resource<LocationResponse>> = MutableStateFlow(Resource.Empty())
    val _LocationStateFlow = LocationStateFlow.asStateFlow()

    private val latLngStateFLow: MutableStateFlow<Resource<LocationLatLngResponse>> = MutableStateFlow(Resource.Empty())
    val _latLngStateFLow = latLngStateFLow.asStateFlow()

    private val latLngStatePaymentFLow: MutableStateFlow<Resource<JsonObject>> = MutableStateFlow(Resource.Empty())
    val _latLngStatePaymentFLow = latLngStatePaymentFLow.asStateFlow()

    private val getNearByLocationFLow: MutableStateFlow<Resource<NearByLocationResponse>> = MutableStateFlow(Resource.Empty())
    val _getNearByLocationFLow = getNearByLocationFLow.asStateFlow()


//     GetLocationApi

    fun getLocationApi(text:String,key:String) = viewModelScope.launch{

        LocationStateFlow.value=Resource.Loading()
        if (networkHelper.hasInternetConnection()){
            respository.getLocationApi(text, key)
                .catch { e->
                    LocationStateFlow.value=Resource.Error(e.message.toString())
                }.collect { data ->
                    LocationStateFlow.value = LocationHandle(data)
                }
        }else{
            LocationStateFlow.value = Resource.Error(Constants.NO_INTERNET)
        }
    }

    fun LocationHandle(response: Response<LocationResponse>): Resource<LocationResponse> {
        if (response.isSuccessful) {
            response.body()?.let { data ->
                Log.e("Mtlb","hsih")
                return Resource.Success(data)
            }
        }
        val gson = GsonBuilder().create()
        var pojo = PojoClass()

        try {
            pojo = gson.fromJson(response.errorBody()!!.string(), pojo::class.java)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Resource.Error(pojo.responseMessage)
    }


//    get lat Long Api



    fun getLatLng(text:String,key:String) = viewModelScope.launch{

        latLngStateFLow.value=Resource.Loading()
        if (networkHelper.hasInternetConnection()){
            respository.getLatLng(text, key)
                .catch { e->
                    latLngStateFLow.value=Resource.Error(e.message.toString())
                }.collect { data ->
                    latLngStateFLow.value = latLngHandle(data)
                }
        }else{
            latLngStateFLow.value = Resource.Error(Constants.NO_INTERNET)
        }
    }
    fun latLngHandle(response: Response<LocationLatLngResponse>): Resource<LocationLatLngResponse> {
        if (response.isSuccessful) {
            response.body()?.let { data ->
                Log.e("Mtlb","hsih")
                return Resource.Success(data)
            }
        }
        val gson = GsonBuilder().create()
        var pojo = PojoClass()

        try {
            pojo = gson.fromJson(response.errorBody()!!.string(), pojo::class.java)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Resource.Error(pojo.responseMessage)
    }



    fun getLatLngPayment(text:String,key:String) = viewModelScope.launch{

        latLngStatePaymentFLow.value=Resource.Loading()
        if (networkHelper.hasInternetConnection()){
            respository.getLatLngForPayment(text, key)
                .catch { e->
                    latLngStatePaymentFLow.value=Resource.Error(e.message.toString())
                }.collect { data ->
                    latLngStatePaymentFLow.value = getLatLngPaymentHandle(data)
                }
        }else{
            latLngStatePaymentFLow.value = Resource.Error(Constants.NO_INTERNET)
        }
    }
    fun getLatLngPaymentHandle(response: Response<JsonObject>): Resource<JsonObject> {
        if (response.isSuccessful) {
            response.body()?.let { data ->
                Log.e("Mtlb","hsih")
                return Resource.Success(data)
            }
        }
        val gson = GsonBuilder().create()
        var pojo = PojoClass()

        try {
            pojo = gson.fromJson(response.errorBody()!!.string(), pojo::class.java)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Resource.Error(pojo.responseMessage)
    }




//    Get near By Locations


    fun getNearByLocationApi(keyword :String,location:String,
                             radius:Int,type:String,key:String) = viewModelScope.launch{

        getNearByLocationFLow.value=Resource.Loading()
        if (networkHelper.hasInternetConnection()){
            respository.nearBySearchApi(keyword, location, radius, type, key)
                .catch { e->
                    getNearByLocationFLow.value=Resource.Error(e.message.toString())
                }.collect { data ->
                    getNearByLocationFLow.value = getNearByLocationHandle(data)
                }
        }else{
            getNearByLocationFLow.value = Resource.Error(Constants.NO_INTERNET)
        }
    }
    private fun getNearByLocationHandle(response: Response<NearByLocationResponse>): Resource<NearByLocationResponse> {
        if (response.isSuccessful) {
            response.body()?.let { data ->
                Log.e("Mtlb","hsih")
                return Resource.Success(data)
            }
        }
        val gson = GsonBuilder().create()
        var pojo = PojoClass()

        try {
            pojo = gson.fromJson(response.errorBody()!!.string(), pojo::class.java)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Resource.Error(pojo.responseMessage)
    }





//    Get near By Locations


    fun nearBySearchCollegesApi(query:String,key:String) = viewModelScope.launch{

        getNearByLocationFLow.value=Resource.Loading()
        if (networkHelper.hasInternetConnection()){
            respository.nearBySearchCollegesApi(query, key)
                .catch { e->
                    getNearByLocationFLow.value=Resource.Error(e.message.toString())
                }.collect { data ->
                    getNearByLocationFLow.value = getNearByLocationHandle(data)
                }
        }else{
            getNearByLocationFLow.value = Resource.Error(Constants.NO_INTERNET)
        }
    }



}