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
import com.callisdairy.api.response.RequestedListResponse
import com.google.gson.GsonBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class RequestedListViewModel @Inject constructor(app: Application, private val repo: CalisRespository,private val networkHelper: NetworkHelper) : AndroidViewModel(app) {

    private val RequestedListData: MutableStateFlow<Resource<RequestedListResponse>> = MutableStateFlow(Resource.Empty())
    val _RequestedListData: StateFlow<Resource<RequestedListResponse>> = RequestedListData


    private val acceptOrRejectData: MutableStateFlow<Resource<AddEventResponse>> = MutableStateFlow(Resource.Empty())
    val _acceptOrRejectData: StateFlow<Resource<AddEventResponse>> = acceptOrRejectData




//    Request List Api


    fun requestedListApi(token:String,page:Int,limit:Int) = viewModelScope.launch {
        RequestedListData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.requestListApi(token,page, limit)
                .catch { e ->
                    RequestedListData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    RequestedListData.value = requestListResponseHandle(data)
                }
        }else{
            RequestedListData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun requestListResponseHandle(response: Response<RequestedListResponse>): Resource<RequestedListResponse> {
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


//    Accept Or reject Api


    fun acceptRejectApi(token:String,type:String,requestId:String) = viewModelScope.launch {
        acceptOrRejectData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.acceptOrDenyRequestApi(token,type, requestId)
                .catch { e ->
                    acceptOrRejectData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    acceptOrRejectData.value = acceptRejectResponseHandle(data)
                }
        }else{
            acceptOrRejectData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun acceptRejectResponseHandle(response: Response<AddEventResponse>): Resource<AddEventResponse> {
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