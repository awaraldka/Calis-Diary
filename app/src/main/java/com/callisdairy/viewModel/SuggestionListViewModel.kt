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
import com.callisdairy.api.response.AddEventResponse
import com.callisdairy.api.response.AddToIntrestedResponse
import com.callisdairy.api.response.suggestionListResponse
import com.google.gson.GsonBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class SuggestionListViewModel @Inject constructor(app: Application, private val repo: CalisRespository,private val networkHelper: NetworkHelper) : AndroidViewModel(app) {

    private val suggestionListData: MutableStateFlow<Resource<suggestionListResponse>> = MutableStateFlow(Resource.Empty())
    val _suggestionListData : StateFlow<Resource<suggestionListResponse>> = suggestionListData


    private val followUnfollowData: MutableStateFlow<Resource<AddToIntrestedResponse>> = MutableStateFlow(Resource.Empty())
    val _followUnfollowData : StateFlow<Resource<AddToIntrestedResponse>> = followUnfollowData


    private val updateStatusData: MutableStateFlow<Resource<AddEventResponse>> = MutableStateFlow(Resource.Empty())
    val _updateStatusData : StateFlow<Resource<AddEventResponse>> = updateStatusData





   //    Suggestion Api


    fun suggestionListAPi(token:String,search:String,page:Int,limit:Int) = viewModelScope.launch {
        suggestionListData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.listSuggestedUserApi(token,search,page,limit,"true")
                .catch { e ->
                    suggestionListData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    suggestionListData.value = suggestionListResponseHandle(data)
                }
        }else{
            suggestionListData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun suggestionListResponseHandle(response: Response<suggestionListResponse>): Resource<suggestionListResponse> {
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



//    Update Status Suggestion

    fun updateStatusAPi(token:String) = viewModelScope.launch {
        updateStatusData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.updateStatusApi(token)
                .catch { e ->
                    updateStatusData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    updateStatusData.value = updateStatusResponseHandle(data)
                }
        }else{
            updateStatusData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun updateStatusResponseHandle(response: Response<AddEventResponse>): Resource<AddEventResponse> {
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