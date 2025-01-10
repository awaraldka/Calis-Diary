package com.callisdairy.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.callisdairy.ModalClass.PojoClass
import com.callisdairy.Repositry.CalisRespository
import com.callisdairy.Utils.NetworkHelper
import com.callisdairy.Utils.Resource
import com.callisdairy.api.Constants
import com.callisdairy.api.Constants.NO_INTERNET
import com.callisdairy.api.response.CheckLimitPlanResponse
import com.callisdairy.api.response.EditProfileResponse
import com.callisdairy.api.response.MyPetListResponse
import com.callisdairy.api.response.MyPostResponse
import com.google.gson.GsonBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(app: Application, private val repo: CalisRespository,private val networkHelper: NetworkHelper) : AndroidViewModel(app) {
    

    private val profileViewData: MutableStateFlow<Resource<EditProfileResponse>> = MutableStateFlow(Resource.Empty())
    val _profileViewData: StateFlow<Resource<EditProfileResponse>> = profileViewData

    private val myPostData: MutableStateFlow<Resource<MyPostResponse>> = MutableStateFlow(Resource.Empty())
    val _myPostData: StateFlow<Resource<MyPostResponse>> = myPostData

    private val myPetData: MutableStateFlow<Resource<MyPetListResponse>> = MutableStateFlow(Resource.Empty())
    val _myPetData: StateFlow<Resource<MyPetListResponse>> = myPetData


    private val checkPlanData: MutableStateFlow<Resource<CheckLimitPlanResponse>> = MutableStateFlow(Resource.Empty())
    val _checkPlanData: StateFlow<Resource<CheckLimitPlanResponse>> = checkPlanData





//    get Profile Api

    fun viewProfileApi(token:String) = viewModelScope.launch {
        profileViewData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.getProfileApi(token)
                .catch { e ->
                    profileViewData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    profileViewData.value = editViewProfileResponseHandle(data)
                }
        }else{
            profileViewData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun editViewProfileResponseHandle(response: Response<EditProfileResponse>): Resource<EditProfileResponse> {
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




//     MY post list api

    fun myPostApi(token:String,page:Int, limit:Int) = viewModelScope.launch {
        myPostData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.myPostListApi(token,page, limit)
                .catch { e ->
                    myPostData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    myPostData.value = myPostResponseHandle(data)
                }
        }else{
            myPostData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun myPostResponseHandle(response: Response<MyPostResponse>): Resource<MyPostResponse> {
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



//     MY PET list api

    fun myPetApi(token:String,search: String,page:Int, limit:Int,fromDate: String,toDate: String,publishStatus:String) = viewModelScope.launch {
        myPetData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.myPetListApi(token,search,page, limit,fromDate, toDate,publishStatus)
                .catch { e ->
                    myPetData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    myPetData.value = myPetResponseHandle(data)
                }
        }else{
            myPetData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun myPetResponseHandle(response: Response<MyPetListResponse>): Resource<MyPetListResponse> {
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



//     Check plans

    fun checkPlanApi(token:String) = viewModelScope.launch {
        checkPlanData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.checkPlanApi(token= token)
                .catch { e ->
                    checkPlanData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    checkPlanData.value = checkPlanResponseHandle(data)
                }
        }else{
            checkPlanData.value = Resource.Error(Constants.NO_INTERNET)
        }

    }

    private fun checkPlanResponseHandle(response: Response<CheckLimitPlanResponse>): Resource<CheckLimitPlanResponse> {
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