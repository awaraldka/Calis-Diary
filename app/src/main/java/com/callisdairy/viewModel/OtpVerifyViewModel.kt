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
class OtpVerifyViewModel @Inject constructor(app: Application, private val repo: CalisRespository,private val networkHelper: NetworkHelper) : AndroidViewModel(app) {

    private val otpVerifyData: MutableStateFlow<Resource<VerifyOtpResponse>> = MutableStateFlow(Resource.Empty())
    val _otpVerifyData: StateFlow<Resource<VerifyOtpResponse>> = otpVerifyData

    private val resendOtpData: MutableStateFlow<Resource<VerifyOtpResponse>> = MutableStateFlow(Resource.Empty())
    val _resendOtpData: StateFlow<Resource<VerifyOtpResponse>> = resendOtpData


   //    OTP Verification  Api


    fun verifyOtpApi(jsonObject: JsonObject) = viewModelScope.launch {
        otpVerifyData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.verifyOTPApi(jsonObject)
                .catch { e ->
                    otpVerifyData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    otpVerifyData.value = otpResponseHandle(data)
                }
        }else{
            otpVerifyData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun otpResponseHandle(response: Response<VerifyOtpResponse>): Resource<VerifyOtpResponse> {
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


//    Resend Otp APi


    fun resendOtpApi(jsonObject: JsonObject) = viewModelScope.launch {
        resendOtpData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.resendOTPApi(jsonObject)
                .catch { e ->
                    resendOtpData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    resendOtpData.value = otpResendResponseHandle(data)
                }
        }else{
            resendOtpData.value = Resource.Error(NO_INTERNET)
        }

    }


    private fun otpResendResponseHandle(response: Response<VerifyOtpResponse>): Resource<VerifyOtpResponse> {
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