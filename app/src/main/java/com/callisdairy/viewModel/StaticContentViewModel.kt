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
class StaticContentViewModel @Inject constructor(app: Application, private val repo: CalisRespository,private val networkHelper: NetworkHelper) : AndroidViewModel(app) {

    private val staticContentData: MutableStateFlow<Resource<StaticContentResponse>> = MutableStateFlow(Resource.Empty())
    val _staticContentData: StateFlow<Resource<StaticContentResponse>> = staticContentData


    private val exploreData: MutableStateFlow<Resource<ExploreListResponse>> = MutableStateFlow(Resource.Empty())
    val _ExploreData: StateFlow<Resource<ExploreListResponse>> = exploreData


    private val contactUsData: MutableStateFlow<Resource<AddPostResponse>> = MutableStateFlow(Resource.Empty())
    val _contactUsData: StateFlow<Resource<AddPostResponse>> = contactUsData





   
//    staticApi

    fun staticApi(type:String) = viewModelScope.launch {
        staticContentData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.staticContentApi(type)
                .catch { e ->
                    staticContentData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    staticContentData.value = staticResponseHandle(data)
                }
        }else{
            staticContentData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun staticResponseHandle(response: Response<StaticContentResponse>): Resource<StaticContentResponse> {
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

//   Explore


    fun exploreApi(search:String,page:Int, limit:Int) = viewModelScope.launch {
        exploreData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.listExploreApi(search,page, limit)
                .catch { e ->
                    exploreData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    exploreData.value = exploreApiResponseHandle(data)
                }
        }else{
            exploreData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun exploreApiResponseHandle(response: Response<ExploreListResponse>): Resource<ExploreListResponse> {
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


//   Contact Us


    fun contactUsApi(token: String,name: String,email: String,mobileNumber: String,reason: String) = viewModelScope.launch {
        contactUsData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.createContactUsApi(token=token,name=name,email=email,mobileNumber=mobileNumber,reason=reason)
                .catch { e ->
                    contactUsData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    contactUsData.value = createContactUsApiResponseHandle(data)
                }
        }else{
            contactUsData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun createContactUsApiResponseHandle(response: Response<AddPostResponse>): Resource<AddPostResponse> {
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