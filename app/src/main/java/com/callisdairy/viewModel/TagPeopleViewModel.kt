package com.callisdairy.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.callisdairy.ModalClass.PojoClass
import com.callisdairy.Repositry.CalisRespository
import com.callisdairy.Utils.NetworkHelper
import com.callisdairy.Utils.Resource
import com.callisdairy.api.Constants.NO_INTERNET
import com.callisdairy.api.response.AddPostResponse
import com.callisdairy.api.response.TagPeopleResponse
import com.google.gson.GsonBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class TagPeopleViewModel @Inject constructor(app: Application, private val repo: CalisRespository,private val networkHelper: NetworkHelper) : AndroidViewModel(app) {

    private val tagPeopleData: MutableStateFlow<Resource<TagPeopleResponse>> = MutableStateFlow(Resource.Empty())
    val _tagPeopleData: StateFlow<Resource<TagPeopleResponse>> = tagPeopleData


    private val addPostData: MutableStateFlow<Resource<AddPostResponse>> = MutableStateFlow(Resource.Empty())
    val _addPostData: StateFlow<Resource<AddPostResponse>> = addPostData



   //    Tag people Api


    fun tagPeopleApi(token:String, search:String) = viewModelScope.launch {
        tagPeopleData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.tagPeopleListApi(token,search)
                .catch { e ->
                    tagPeopleData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    tagPeopleData.value = tagPeopleResponseHandle(data)
                }
        }else{
            tagPeopleData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun tagPeopleResponseHandle(response: Response<TagPeopleResponse>): Resource<TagPeopleResponse> {
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


//    Add Post API

    fun addPostApi(token:String, file: ArrayList<MultipartBody.Part>,caption:String,requestMetaWords:ArrayList<String>,
                   requestTagPeople:ArrayList<String>,latitude:Double,longitude:Double,LocationName:String) = viewModelScope.launch {
        addPostData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.addPostApi(token,file,caption,requestMetaWords,requestTagPeople,latitude,longitude,LocationName)
                .catch { e ->
                    addPostData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    addPostData.value = addPostResponseHandle(data)
                }
        }else{
            addPostData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun addPostResponseHandle(response: Response<AddPostResponse>): Resource<AddPostResponse> {
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