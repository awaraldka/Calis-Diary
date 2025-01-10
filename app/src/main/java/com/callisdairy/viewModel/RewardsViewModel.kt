package com.callisdairy.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.callisdairy.ModalClass.PojoClass
import com.callisdairy.Repositry.CalisRespository
import com.callisdairy.Utils.NetworkHelper
import com.callisdairy.Utils.Resource
import com.callisdairy.api.Constants.NO_INTERNET
import com.callisdairy.api.response.AddEventResponse
import com.callisdairy.api.response.EditProfileResponse
import com.callisdairy.api.response.RewardsPointResponse
import com.callisdairy.api.response.RewardsResponseVendor
import com.google.gson.GsonBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class RewardsViewModel @Inject constructor(app: Application, private val repo: CalisRespository,private val networkHelper: NetworkHelper) : AndroidViewModel(app) {

    private val rewardsData: MutableStateFlow<Resource<RewardsPointResponse>> = MutableStateFlow(Resource.Empty())
    val _rewardsData: StateFlow<Resource<RewardsPointResponse>> = rewardsData


    private val profileViewData: MutableStateFlow<Resource<EditProfileResponse>> = MutableStateFlow(Resource.Empty())
    val _profileViewData: StateFlow<Resource<EditProfileResponse>> = profileViewData


    private val privacyTypeData: MutableStateFlow<Resource<AddEventResponse>> = MutableStateFlow(Resource.Empty())
    val _privacyTypeData: StateFlow<Resource<AddEventResponse>> = privacyTypeData


    private val vendorRewardsData: MutableStateFlow<Resource<RewardsResponseVendor>> = MutableStateFlow(Resource.Empty())
    val _vendorRewardsData: StateFlow<Resource<RewardsResponseVendor>> = vendorRewardsData

   
//    RewardsApi

    fun rewardsApi(token:String) = viewModelScope.launch {
        rewardsData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.viewRewardsPointsApi(token)
                .catch { e ->
                    rewardsData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    rewardsData.value = RewardsResponseHandle(data)
                }
        }else{
            rewardsData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun RewardsResponseHandle(response: Response<RewardsPointResponse>): Resource<RewardsPointResponse> {
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



//    RewardsApi Vendor

    fun rewardsApiVendor(token: String,search: String,fromDate: String,toDate: String,page: Int,limit: Int) = viewModelScope.launch {
        vendorRewardsData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.redeemPointsListApi(token,search, fromDate, toDate, page, limit)
                .catch { e ->
                    vendorRewardsData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    vendorRewardsData.value = RewardsVendorResponseHandle(data)
                }
        }else{
            vendorRewardsData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun RewardsVendorResponseHandle(response: Response<RewardsResponseVendor>): Resource<RewardsResponseVendor> {
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



//    get Profile Api

    fun viewProfileApi(token:String) = viewModelScope.launch {
        profileViewData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.getProfileApi(token)
                .catch { e ->
                    profileViewData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    profileViewData.value = ViewProfileResponseHandle(data)
                }
        }else{
            profileViewData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun ViewProfileResponseHandle(response: Response<EditProfileResponse>): Resource<EditProfileResponse> {
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


//   Private Public Account Api


    fun makePrivatePublicAccountApi(token:String) = viewModelScope.launch {
        privacyTypeData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.privateOrPublicAccountApi(token)
                .catch { e ->
                    privacyTypeData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    privacyTypeData.value = makePrivatePublicAccountResponseHandle(data)
                }
        }else{
            privacyTypeData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun makePrivatePublicAccountResponseHandle(response: Response<AddEventResponse>): Resource<AddEventResponse> {
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