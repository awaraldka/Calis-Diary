package com.callisdairy.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.callisdairy.ModalClass.PojoClass
import com.callisdairy.Repositry.CalisRespository
import com.callisdairy.Utils.NetworkHelper
import com.callisdairy.Utils.Resource
import com.callisdairy.api.Constants
import com.callisdairy.api.response.AddEventResponse
import com.callisdairy.api.response.AddLikeResponse
import com.callisdairy.api.response.ViewPostResponse
import com.google.gson.GsonBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class ViewPostViewModel @Inject constructor(app:Application, private val repo:CalisRespository,private val networkHelper: NetworkHelper):AndroidViewModel(app) {
    
    
    private val viewPostData: MutableStateFlow<Resource<ViewPostResponse>> =  MutableStateFlow(Resource.Empty())
    val _viewPostData: StateFlow<Resource<ViewPostResponse>> = viewPostData

    private val addLikeData: MutableStateFlow<Resource<AddLikeResponse>> = MutableStateFlow(Resource.Empty())
    val _addLikeData: StateFlow<Resource<AddLikeResponse>> = addLikeData


    private val deletePostData: MutableStateFlow<Resource<AddEventResponse>> = MutableStateFlow(Resource.Empty())
    val _deletePostData: StateFlow<Resource<AddEventResponse>> = deletePostData

    //    View Post Api

    fun viewPostApi(token:String,id:String) = viewModelScope.launch {
        viewPostData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.viewPostApi(token,id)
                .catch { e ->
                    viewPostData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    viewPostData.value = viewPostResponseHandle(data)
                }
        }else{
            viewPostData.value = Resource.Error(Constants.NO_INTERNET)
        }

    }

    private fun viewPostResponseHandle(response: Response<ViewPostResponse>): Resource<ViewPostResponse> {
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



//     AddLike Api

    fun addLikeApi(token:String,typeOfPost :String,postId :String, commentId:String) = viewModelScope.launch {
        addLikeData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.addLikeApi(token, typeOfPost, postId, commentId)
                .catch { e ->
                    addLikeData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    addLikeData.value = addLikeResponseHandle(data)
                }
        }else{
            addLikeData.value = Resource.Error(Constants.NO_INTERNET)
        }

    }


    private fun addLikeResponseHandle(response: Response<AddLikeResponse>): Resource<AddLikeResponse> {
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




//     Delete post Api


    fun deletePostApi(token: String,id:String) = viewModelScope.launch {
        deletePostData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()) {

            repo.removePostApi(token, id)
                .catch { e ->
                    deletePostData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    deletePostData.value = deletePostResponseHandle(data)
                }
        } else {
            deletePostData.value = Resource.Error(Constants.NO_INTERNET)
        }

    }
    private fun deletePostResponseHandle(response: Response<AddEventResponse>): Resource<AddEventResponse> {
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