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
import com.callisdairy.api.request.EditProfileRequest
import com.callisdairy.api.request.LoginRequest
import com.callisdairy.api.request.SignUpRequest
import com.callisdairy.api.response.*
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class othersProfileViewModel @Inject constructor(app: Application, private val repo: CalisRespository,private val networkHelper: NetworkHelper) : AndroidViewModel(app) {
    

    private val profileViewData: MutableStateFlow<Resource<OtherUserResponse>> = MutableStateFlow(Resource.Empty())
    val _profileViewData: StateFlow<Resource<OtherUserResponse>> = profileViewData

    private val postData: MutableStateFlow<Resource<MyPostResponse>> = MutableStateFlow(Resource.Empty())
    val _postData: StateFlow<Resource<MyPostResponse>> = postData

    private val petData: MutableStateFlow<Resource<MyPetListResponse>> = MutableStateFlow(Resource.Empty())
    val _petData: StateFlow<Resource<MyPetListResponse>> = petData

    private val followUnfollowData: MutableStateFlow<Resource<AddToIntrestedResponse>> = MutableStateFlow(Resource.Empty())
    val _followUnfollowData : StateFlow<Resource<AddToIntrestedResponse>> = followUnfollowData



//    get Profile Api

    fun viewOthersProfileApi(token:String,id:String) = viewModelScope.launch {
        profileViewData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.othersProfile(token,id)
                .catch { e ->
                    profileViewData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    profileViewData.value = viewProfileResponseHandle(data)
                }
        }else{
            profileViewData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun viewProfileResponseHandle(response: Response<OtherUserResponse>): Resource<OtherUserResponse> {
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

    
    
//      post list api

    fun othersPostApi(token:String,userId:String,page:Int, limit:Int) = viewModelScope.launch {
        postData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.otherUserPostListApi(token,userId,page, limit)
                .catch { e ->
                    postData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    postData.value = postResponseHandle(data)
                }
        }else{
            postData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun postResponseHandle(response: Response<MyPostResponse>): Resource<MyPostResponse> {
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



//     PET list api

    fun othersPetApi(token:String,id:String,page:Int, limit:Int) = viewModelScope.launch {
        petData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.otherUserPetListApi(token,id,page, limit)
                .catch { e ->
                    petData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    petData.value = petResponseHandle(data)
                }
        }else{
            petData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun petResponseHandle(response: Response<MyPetListResponse>): Resource<MyPetListResponse> {
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


//     Follow Unfollow Api


    fun followUnfollowAPi(token:String,userId:String) = viewModelScope.launch {
        followUnfollowData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.followUnfollowUserApi(token,userId)
                .catch { e ->
                    followUnfollowData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    followUnfollowData.value = followUnfollowResponseHandle(data)
                }
        }else{
            followUnfollowData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun followUnfollowResponseHandle(response: Response<AddToIntrestedResponse>): Resource<AddToIntrestedResponse> {
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