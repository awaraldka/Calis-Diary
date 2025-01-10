package com.callisdairy.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.callisdairy.ModalClass.PojoClass
import com.callisdairy.Repositry.CalisRespository
import com.callisdairy.Utils.NetworkHelper
import com.callisdairy.Utils.Resource
import com.callisdairy.api.Constants
import com.callisdairy.api.response.AddToIntrestedResponse
import com.callisdairy.api.response.GlobalSearchResponse
import com.google.gson.GsonBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class GlobalSearchViewModel @Inject constructor(app: Application, private val repo: CalisRespository,private val networkHelper: NetworkHelper) : AndroidViewModel(app) {

    private var searchJob: Job? = null


    private val globalSearchData: MutableStateFlow<Resource<GlobalSearchResponse>> = MutableStateFlow(Resource.Empty())
    val _globalSearchData: StateFlow<Resource<GlobalSearchResponse>> = globalSearchData


    private val followUnfollowData: MutableStateFlow<Resource<AddToIntrestedResponse>> = MutableStateFlow(Resource.Empty())
    val _followUnfollowData : StateFlow<Resource<AddToIntrestedResponse>> = followUnfollowData


    //    Global Search  Api


    fun globalSearchApi(token: String, search: String) = viewModelScope.launch {
        searchJob?.cancel() // Cancel the previous search job if it exists
        globalSearchData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()) {
            searchJob = launch {
                repo.globalSearchApi(token, search)
                    .catch { e ->
                        globalSearchData.value = Resource.Error(e.message.toString())
                    }
                    .collectLatest { response ->
                        globalSearchData.value = globalSearchResponseHandle(response)
                    }
            }
        } else {
            globalSearchData.value = Resource.Error(Constants.NO_INTERNET)
        }
    }
    private fun globalSearchResponseHandle(response: Response<GlobalSearchResponse>): Resource<GlobalSearchResponse> {
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
            followUnfollowData.value = Resource.Error(Constants.NO_INTERNET)
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