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
import com.google.gson.JsonObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class StoryCommentViewModel @Inject constructor(app: Application, private val repo: CalisRespository,private val networkHelper: NetworkHelper) : AndroidViewModel(app) {

    private val addCommentData: MutableStateFlow<Resource<AddEventResponse>> = MutableStateFlow(Resource.Empty())
    val _addCommentData: StateFlow<Resource<AddEventResponse>> = addCommentData

//  Add Comment and Reply of a comment Api

    fun addStoryCommentApi(token: String,typeOfPost :String,
                      storyId :String,reply :String) = viewModelScope.launch {
        addCommentData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.addStoryCommentApi(token, typeOfPost, storyId, reply)
                .catch { e ->
                    addCommentData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    addCommentData.value = addCommentResponseHandle(data)
                }
        }else{
            addCommentData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun addCommentResponseHandle(response: Response<AddEventResponse>): Resource<AddEventResponse> {
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