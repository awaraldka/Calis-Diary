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
import com.callisdairy.api.Constants.NO_INTERNET
import com.callisdairy.api.request.AddEventRequest
import com.callisdairy.api.request.EditEventRequest
import com.callisdairy.api.response.*
import com.google.gson.GsonBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class AddEventViewModel @Inject constructor(app: Application, private val repo: CalisRespository,private val networkHelper: NetworkHelper) : AndroidViewModel(app) {

    private val addEventData: MutableStateFlow<Resource<AddEventResponse>> = MutableStateFlow(Resource.Empty())
    val _addEventData: StateFlow<Resource<AddEventResponse>> = addEventData

    private val updateData: MutableStateFlow<Resource<AddEventResponse>> = MutableStateFlow(Resource.Empty())
    val _updateData: StateFlow<Resource<AddEventResponse>> = updateData


    private val countryStateFlow: MutableStateFlow<Resource<CountryResponse>> = MutableStateFlow(Resource.Empty())
    val _countryStateFlow: StateFlow<Resource<CountryResponse>> = countryStateFlow


    private val stateData: MutableStateFlow<Resource<CountryResponse>> = MutableStateFlow(Resource.Empty())
    val _stateData: StateFlow<Resource<CountryResponse>> = stateData


    private val citydata: MutableStateFlow<Resource<CountryResponse>> = MutableStateFlow(Resource.Empty())
    val _citydata: StateFlow<Resource<CountryResponse>> = citydata


    private val viewEventData: MutableStateFlow<Resource<ViewEventResponse>> = MutableStateFlow(Resource.Empty())
    val _viewEventData: StateFlow<Resource<ViewEventResponse>> = viewEventData



    private val uploadImagesData: MutableStateFlow<Resource<ImageUploadResponse>> = MutableStateFlow(Resource.Empty())
    val _uploadImagesData: StateFlow<Resource<ImageUploadResponse>> = uploadImagesData



    private val uploadImagesDataAudio: MutableStateFlow<Resource<ImageUploadResponse>> = MutableStateFlow(Resource.Empty())
    val _uploadImagesDataAudio: StateFlow<Resource<ImageUploadResponse>> = uploadImagesDataAudio






    //    Add Event Api


    fun addEventApi(token: String,request: AddEventRequest) = viewModelScope.launch {
        addEventData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.addEventApi(token, request)
                .catch { e ->
                    addEventData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    addEventData.value = addEventResponseHandle(data)
                }
        }else{
            addEventData.value = Resource.Error(NO_INTERNET)
        }

    }
    private fun addEventResponseHandle(response: Response<AddEventResponse>): Resource<AddEventResponse> {
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



   //    Update Event Api


    fun updateEventApi(token: String,request: EditEventRequest) = viewModelScope.launch {
        updateData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.updateEventApi(token, request)
                .catch { e ->
                    updateData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    updateData.value = updateEventResponseHandle(data)
                }
        }else{
            updateData.value = Resource.Error(NO_INTERNET)
        }

    }
    private fun updateEventResponseHandle(response: Response<AddEventResponse>): Resource<AddEventResponse> {
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




//    View Event

    fun viewEventApi(eventId: String) = viewModelScope.launch {
        viewEventData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.viewEvent(eventId)
                .catch { e ->
                    viewEventData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    viewEventData.value = viewEventResponseHandle(data)
                }
        }else{
            viewEventData.value = Resource.Error(NO_INTERNET)
        }

    }
    private fun viewEventResponseHandle(response: Response<ViewEventResponse>): Resource<ViewEventResponse> {
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


    //     Get Country List

    fun getCountryApi() = viewModelScope.launch {
        countryStateFlow.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.getAllCountryApi()
                .catch { e ->
                    countryStateFlow.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    countryStateFlow.value = countryResponseHandle(data)
                }
        }else{
            countryStateFlow.value = Resource.Error(NO_INTERNET)
        }

    }
    private fun countryResponseHandle(response: Response<CountryResponse>): Resource<CountryResponse> {
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



//     Get State List

    fun getStateListApi(countryCode:String) = viewModelScope.launch {
        stateData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.getAllStateApi(countryCode)
                .catch { e ->
                    stateData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    stateData.value = stateResponseHandle(data)
                }
        }else{
            stateData.value = Resource.Error(NO_INTERNET)
        }

    }
    private fun stateResponseHandle(response: Response<CountryResponse>): Resource<CountryResponse> {
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



//     Get City List

    fun getCityListApi(countryCode:String, stateCode:String) = viewModelScope.launch {
        citydata.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.getAllCityApi(countryCode,stateCode)
                .catch { e ->
                    citydata.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    citydata.value = cityResponseHandle(data)
                }
        }else{
            citydata.value = Resource.Error(NO_INTERNET)
        }

    }
    private fun cityResponseHandle(response: Response<CountryResponse>): Resource<CountryResponse> {
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




//     UploadMultipleImages Api


    fun uploadMultipleImagesApi(file : ArrayList<MultipartBody.Part>) = viewModelScope.launch {
        uploadImagesData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.uploadMultipleFile(file)
                .catch { e ->
                    uploadImagesData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    uploadImagesData.value = uploadMultipleImagesHandle(data)
                }
        }else{
            uploadImagesData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun uploadMultipleImagesHandle(response: Response<ImageUploadResponse>): Resource<ImageUploadResponse> {
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





//     Audio upload Api


    fun uploadAudioApi(file : ArrayList<MultipartBody.Part>) = viewModelScope.launch {
        uploadImagesDataAudio.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.uploadMultipleFile(file)
                .catch { e ->
                    uploadImagesDataAudio.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    uploadImagesDataAudio.value = uploadAudioHandle(data)
                }
        }else{
            uploadImagesDataAudio.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun uploadAudioHandle(response: Response<ImageUploadResponse>): Resource<ImageUploadResponse> {
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