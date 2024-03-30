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
import com.callisdairy.api.response.AddPostResponse
import com.callisdairy.api.response.AddToIntrestedResponse
import com.callisdairy.api.response.PetProfileResponse
import com.google.gson.GsonBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class PetProfileViewModel @Inject constructor(app: Application, private val repo: CalisRespository,private val networkHelper: NetworkHelper) : AndroidViewModel(app) {


    private val profileViewData: MutableStateFlow<Resource<PetProfileResponse>> = MutableStateFlow(Resource.Empty())
    val _profileViewData: StateFlow<Resource<PetProfileResponse>> = profileViewData

    private val switchProfileData: MutableStateFlow<Resource<AddToIntrestedResponse>> = MutableStateFlow(Resource.Empty())
    val _switchProfileData: StateFlow<Resource<AddToIntrestedResponse>> = switchProfileData

    private val changeLanguageData: MutableStateFlow<Resource<AddPostResponse>> = MutableStateFlow(Resource.Empty())
    val _changeLanguageData: StateFlow<Resource<AddPostResponse>> = changeLanguageData



    //    get Profile Api

    fun viewProfileApi(token:String) = viewModelScope.launch {
        profileViewData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.userProfileListApi(token)
                .catch { e ->
                    profileViewData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    profileViewData.value = viewProfileResponseHandle(data)
                }
        }else{
            profileViewData.value = Resource.Error(Constants.NO_INTERNET)
        }

    }

    private fun viewProfileResponseHandle(response: Response<PetProfileResponse>): Resource<PetProfileResponse> {
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


//    Switch Profile Api

    fun switchProfileApi(token:String,petCategoryId :String) = viewModelScope.launch {
        switchProfileData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.setDefaultUserProfileApi(token,petCategoryId)
                .catch { e ->
                    switchProfileData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    switchProfileData.value = switchProfileResponseHandle(data)
                }
        }else{
            switchProfileData.value = Resource.Error(Constants.NO_INTERNET)
        }

    }

    private fun switchProfileResponseHandle(response: Response<AddToIntrestedResponse>): Resource<AddToIntrestedResponse> {
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







//    Switch Language Api

    fun switchLanguageApi(token: String,languageCode :String) = viewModelScope.launch {
        changeLanguageData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.setDefaultUserLanguageApi(token,languageCode)
                .catch { e ->
                    changeLanguageData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    changeLanguageData.value = switchLanguageResponseHandle(data)
                }
        }else{
            changeLanguageData.value = Resource.Error(Constants.NO_INTERNET)
        }

    }

    private fun switchLanguageResponseHandle(response: Response<AddPostResponse>): Resource<AddPostResponse> {
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