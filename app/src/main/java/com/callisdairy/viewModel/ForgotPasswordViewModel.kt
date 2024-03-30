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
import com.callisdairy.api.request.SignUpRequest
import com.callisdairy.api.response.CountryResponse
import com.callisdairy.api.response.SignUpResponse
import com.callisdairy.api.response.VerifyOtpResponse
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(app: Application, private val repo: CalisRespository,private val networkHelper: NetworkHelper) : AndroidViewModel(app) {

    private val forgotPasswordData: MutableStateFlow<Resource<SignUpResponse>> = MutableStateFlow(Resource.Empty())
    val _forgotPasswordData: StateFlow<Resource<SignUpResponse>> = forgotPasswordData


   //    Signup Api


    fun forgotPasswordApi(jsonObject: JsonObject) = viewModelScope.launch {
        forgotPasswordData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.forgotPasswordApi(jsonObject)
                .catch { e ->
                    forgotPasswordData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    forgotPasswordData.value = forgotPasswordResponseHandle(data)
                }
        }else{
            forgotPasswordData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun forgotPasswordResponseHandle(response: Response<SignUpResponse>): Resource<SignUpResponse> {
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