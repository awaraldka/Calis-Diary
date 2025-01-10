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
import com.callisdairy.api.response.AgoraCallingResponse
import com.google.gson.GsonBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class AgoraVideoCallingViewModel @Inject constructor(app:Application, private val repo:CalisRespository,private val networkHelper: NetworkHelper): AndroidViewModel(app) {

    private val getTokenData: MutableStateFlow<Resource<AgoraCallingResponse>> = MutableStateFlow(Resource.Empty())
    val getTokenOrIdData : StateFlow<Resource<AgoraCallingResponse>> = getTokenData

    private val callUserData: MutableStateFlow<Resource<AddToIntrestedResponse>> = MutableStateFlow(Resource.Empty())
    val callUsersData : StateFlow<Resource<AddToIntrestedResponse>> = callUserData

    
    
//   Get Token and id form agora Api

    fun getTokenOrIdApi(token:String) = viewModelScope.launch {
        getTokenData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.agoraTokenGeneratorApi(token)
                .catch { e ->
                    getTokenData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    getTokenData.value = getTokenResponseHandle(data)
                }
        }else{
            getTokenData.value = Resource.Error(Constants.NO_INTERNET)
        }

    }

    private fun getTokenResponseHandle(response: Response<AgoraCallingResponse>): Resource<AgoraCallingResponse> {
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




//   Notify Api

    fun notifyUserApi(token: String, receiverId: String, type: String) = viewModelScope.launch {
        callUserData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.callUserApi(token, receiverId, type)
                .catch { e ->
                    callUserData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    callUserData.value = notifyUserResponseHandle(data)
                }
        }else{
            callUserData.value = Resource.Error(Constants.NO_INTERNET)
        }

    }

    private fun notifyUserResponseHandle(response: Response<AddToIntrestedResponse>): Resource<AddToIntrestedResponse> {
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