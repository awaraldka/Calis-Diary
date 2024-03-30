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
import com.callisdairy.api.response.AddToIntrestedResponse
import com.callisdairy.api.response.FollowersListResponse
import com.callisdairy.api.response.FollowingUserListResponse
import com.google.gson.GsonBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class FollowingListViewModel @Inject constructor(app:Application, private  val repo:CalisRespository,private val networkHelper: NetworkHelper):AndroidViewModel(app) {


    private val followingListData: MutableStateFlow<Resource<FollowingUserListResponse>> =  MutableStateFlow(Resource.Empty())
    val _followingListData: StateFlow<Resource<FollowingUserListResponse>> = followingListData


    private val othersFollowingListData: MutableStateFlow<Resource<FollowingUserListResponse>> =  MutableStateFlow(Resource.Empty())
    val _othersFollowingListData: StateFlow<Resource<FollowingUserListResponse>> = othersFollowingListData


    private val othersFollowersListData: MutableStateFlow<Resource<FollowersListResponse>> =  MutableStateFlow(Resource.Empty())
    val _othersFollowersListData: StateFlow<Resource<FollowersListResponse>> = othersFollowersListData


    private val followersListData: MutableStateFlow<Resource<FollowersListResponse>> =  MutableStateFlow(Resource.Empty())
    val _followersListData: StateFlow<Resource<FollowersListResponse>> = followersListData


    private val followUnfollowData: MutableStateFlow<Resource<AddToIntrestedResponse>> = MutableStateFlow(Resource.Empty())
    val _followUnfollowData : StateFlow<Resource<AddToIntrestedResponse>> = followUnfollowData






//    Following List API

    fun followingListApi(token:String,search: String, page: Int, limit: Int) = viewModelScope.launch {
        followingListData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.followingUserListApi(token,search, page, limit)
                .catch { e ->
                    followingListData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    followingListData.value = followingListResponseHandle(data)
                }
        }else{
            followingListData.value = Resource.Error(Constants.NO_INTERNET)
        }

    }
    private fun followingListResponseHandle(response: Response<FollowingUserListResponse>): Resource<FollowingUserListResponse> {
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


//    Followers List Api


    fun followersListApi(token:String,search: String, page: Int, limit: Int) = viewModelScope.launch {
        followersListData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.followerUserListApi(token,search, page, limit)
                .catch { e ->
                    followersListData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    followersListData.value = followersListResponseHandle(data)
                }
        }else{
            followersListData.value = Resource.Error(Constants.NO_INTERNET)
        }

    }
    private fun followersListResponseHandle(response: Response<FollowersListResponse>): Resource<FollowersListResponse> {
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




//    Others Following List API

    fun othersFollowingListApi(token:String,id:String, page: Int, limit: Int,search: String) = viewModelScope.launch {
        othersFollowingListData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.otherfollowingUserListApi(token,id,page, limit, search)
                .catch { e ->
                    othersFollowingListData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    othersFollowingListData.value = otherFollowingListResponseHandle(data)
                }
        }else{
            othersFollowingListData.value = Resource.Error(Constants.NO_INTERNET)
        }

    }
    private fun otherFollowingListResponseHandle(response: Response<FollowingUserListResponse>): Resource<FollowingUserListResponse> {
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


//     others Followers List Api


    fun othersFollowersListApi(token:String,id:String,search: String, page: Int, limit: Int) = viewModelScope.launch {
        othersFollowersListData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.otherfollowerUserListApi(token,id,search, page, limit)
                .catch { e ->
                    othersFollowersListData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    othersFollowersListData.value = otherFollowersListResponseHandle(data)
                }
        }else{
            othersFollowersListData.value = Resource.Error(Constants.NO_INTERNET)
        }

    }
    private fun otherFollowersListResponseHandle(response: Response<FollowersListResponse>): Resource<FollowersListResponse> {
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