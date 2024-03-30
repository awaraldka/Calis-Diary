package com.callisdairy.viewModel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.callisdairy.CalisApp
import com.callisdairy.ModalClass.PojoClass
import com.callisdairy.Repositry.CalisRespository
import com.callisdairy.Utils.NetworkHelper
import com.callisdairy.Utils.Resource
import com.callisdairy.api.Constants
import com.callisdairy.api.Constants.NO_INTERNET
import com.callisdairy.api.response.*
import com.google.gson.GsonBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class ViewPetViewModel @Inject constructor(app: Application, private val repo: CalisRespository,private val networkHelper: NetworkHelper) : AndroidViewModel(app) {

    private val petDescriptionData: MutableStateFlow<Resource<ViewPetResponse>> = MutableStateFlow(Resource.Empty())
    val _petDescriptionData: StateFlow<Resource<ViewPetResponse>> = petDescriptionData


    private val addToInterestedData: MutableStateFlow<Resource<AddToIntrestedResponse>> = MutableStateFlow(Resource.Empty())
    val _addToInterestedData: StateFlow<Resource<AddToIntrestedResponse>> = addToInterestedData


    private val addToMarketData: MutableStateFlow<Resource<AddEventResponse>> = MutableStateFlow(Resource.Empty())
    val _addToMarketData: StateFlow<Resource<AddEventResponse>> = addToMarketData


    private val removeToMarketData: MutableStateFlow<Resource<AddEventResponse>> = MutableStateFlow(Resource.Empty())
    val _removeToMarketData: StateFlow<Resource<AddEventResponse>> = removeToMarketData


    private val removePetData: MutableStateFlow<Resource<AddEventResponse>> = MutableStateFlow(Resource.Empty())
    val _removePetData : StateFlow<Resource<AddEventResponse>> = removePetData



//    petDescriptionApi

    fun petDescriptionApi(token:String,id:String) = viewModelScope.launch {
        petDescriptionData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.viewPetApi(token,id)
                .catch { e ->
                    petDescriptionData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    petDescriptionData.value = viewPetResponseHandle(data)
                }
        }else{
            petDescriptionData.value = Resource.Error(NO_INTERNET)
        }

    }
    private fun viewPetResponseHandle(response: Response<ViewPetResponse>): Resource<ViewPetResponse> {
        if (response.isSuccessful) {
            response.body()?.let { data ->
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



//    Add To Intrested APi

    fun addToInterestedApi(token:String,type:String,petId:String,productId:String,serviceId:String) = viewModelScope.launch {
        addToInterestedData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.addToInterestedApi(token, type, petId, productId, serviceId)
                .catch { e ->
                    addToInterestedData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    addToInterestedData.value = addToIntrestedResponseHandle(data)
                }
        }else{
            addToInterestedData.value = Resource.Error(Constants.NO_INTERNET)
        }

    }
    private fun addToIntrestedResponseHandle(response: Response<AddToIntrestedResponse>): Resource<AddToIntrestedResponse> {
        if (response.isSuccessful) {
            response.body()?.let { data ->
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


//    Add To Market API


    fun addToMarketApi(token:String,id:String,price:String,currency:String) = viewModelScope.launch {
        addToMarketData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.addPetToNFTMarketPlaceApi(token,id,price,currency)
                .catch { e ->
                    addToMarketData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    addToMarketData.value = addToMarketResponseHandle(data)
                }
        }else{
            addToMarketData.value = Resource.Error(NO_INTERNET)
        }

    }
    private fun addToMarketResponseHandle(response: Response<AddEventResponse>): Resource<AddEventResponse> {
        if (response.isSuccessful) {
            response.body()?.let { data ->
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



//    remove Pet To Market API


    fun removeToMarketApi(token:String,id:String) = viewModelScope.launch {
        removeToMarketData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.removePetToMarketPlaceApi(token,id)
                .catch { e ->
                    removeToMarketData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    removeToMarketData.value = removeToMarketResponseHandle(data)
                }
        }else{
            removeToMarketData.value = Resource.Error(Constants.NO_INTERNET)
        }

    }
    private fun removeToMarketResponseHandle(response: Response<AddEventResponse>): Resource<AddEventResponse> {
        if (response.isSuccessful) {
            response.body()?.let { data ->
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


//    remove Pet API


    fun removePetApi(token:String,id:String) = viewModelScope.launch {
        removePetData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.removePetApi(token,id)
                .catch { e ->
                    removePetData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    removePetData.value = removePetResponseHandle(data)
                }
        }else{
            removePetData.value = Resource.Error(Constants.NO_INTERNET)
        }

    }
    private fun removePetResponseHandle(response: Response<AddEventResponse>): Resource<AddEventResponse> {
        if (response.isSuccessful) {
            response.body()?.let { data ->
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








   
   

}