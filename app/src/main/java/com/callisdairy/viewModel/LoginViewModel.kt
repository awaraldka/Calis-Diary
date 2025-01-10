package com.callisdairy.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.callisdairy.ModalClass.PojoClass
import com.callisdairy.Repositry.CalisRespository
import com.callisdairy.Utils.NetworkHelper
import com.callisdairy.Utils.Resource
import com.callisdairy.api.Constants.NO_INTERNET
import com.callisdairy.api.request.LoginRequest
import com.callisdairy.api.response.AddEventResponse
import com.callisdairy.api.response.AppConfigResponse
import com.callisdairy.api.response.GetSubscriptionDetailsResponse
import com.callisdairy.api.response.LogInResponse
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
class LoginViewModel @Inject constructor(app: Application, private val repo: CalisRespository,private val networkHelper: NetworkHelper) : AndroidViewModel(app) {

    private val loginData: MutableStateFlow<Resource<LogInResponse>> = MutableStateFlow(Resource.Empty())
    val _loginData: StateFlow<Resource<LogInResponse>> = loginData

    private val logoutData : MutableStateFlow<Resource<AddEventResponse>> = MutableStateFlow(Resource.Empty())
    val _logoutData : MutableStateFlow<Resource<AddEventResponse>> = logoutData

    private val socialLoginData: MutableStateFlow<Resource<LogInResponse>> = MutableStateFlow(Resource.Empty())
    val _socialLoginData: StateFlow<Resource<LogInResponse>> = socialLoginData


    private val deleteAccountData: MutableStateFlow<Resource<AddEventResponse>> = MutableStateFlow(Resource.Empty())
    val _deleteAccountData: StateFlow<Resource<AddEventResponse>> = deleteAccountData

    private val deactivateAccountData: MutableStateFlow<Resource<AddEventResponse>> = MutableStateFlow(Resource.Empty())
    val _deactivateAccountData: StateFlow<Resource<AddEventResponse>> = deactivateAccountData

    private val appConfigData: MutableStateFlow<Resource<AppConfigResponse>> = MutableStateFlow(Resource.Empty())
    val _appConfigData: StateFlow<Resource<AppConfigResponse>> = appConfigData

    private val getSubscriptionData: MutableStateFlow<Resource<GetSubscriptionDetailsResponse>> = MutableStateFlow(Resource.Empty())
    val _getSubscriptionData: StateFlow<Resource<GetSubscriptionDetailsResponse>> = getSubscriptionData


    //    Login Api


    fun socialLoginApi(request: JsonObject) = viewModelScope.launch {
        socialLoginData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.socialLogin(request)
                .catch { e ->
                    socialLoginData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    socialLoginData.value = socialLoginResponseHandle(data)
                }
        }else{
            socialLoginData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun socialLoginResponseHandle(response: Response<LogInResponse>): Resource<LogInResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
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



    //    Login Api


    fun loginApi(request: LoginRequest) = viewModelScope.launch {
        loginData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.login(request)
                .catch { e ->
                    loginData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    loginData.value = loginResponseHandle(data)
                }
        }else{
            loginData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun loginResponseHandle(response: Response<LogInResponse>): Resource<LogInResponse> {
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


//   Check Status


    fun userLogout(token:String,fireToken:String) = viewModelScope.launch {
        logoutData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.userLogout(token,fireToken)
                .catch { e ->
                    logoutData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    logoutData.value = logoutResponseHandle(data)
                }
        }else{
            logoutData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun logoutResponseHandle(response: Response<AddEventResponse>): Resource<AddEventResponse> {
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





//    Delete Account


    fun deleteAccountApi(token:String) = viewModelScope.launch {
        deleteAccountData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.deleteAccount(token)
                .catch { e ->
                    deleteAccountData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    deleteAccountData.value = deleteAccountResponseHandle(data)
                }
        }else{
            deleteAccountData.value = Resource.Error(NO_INTERNET)
        }

    }
    private fun deleteAccountResponseHandle(response: Response<AddEventResponse>): Resource<AddEventResponse> {
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



//    deactivate Account


    fun deactivateAccountApi(token:String) = viewModelScope.launch {
        deactivateAccountData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.deleteAccount(token)
                .catch { e ->
                    deactivateAccountData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    deactivateAccountData.value = deactivateAccountResponseHandle(data)
                }
        }else{
            deactivateAccountData.value = Resource.Error(NO_INTERNET)
        }

    }
    private fun deactivateAccountResponseHandle(response: Response<AddEventResponse>): Resource<AddEventResponse> {
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


//    Get All Api Keys


    fun appConfig() = viewModelScope.launch {
        appConfigData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.appConfig()
                .catch { e ->
                    appConfigData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    appConfigData.value = appConfigHandle(data)
                }
        }else{
            appConfigData.value = Resource.Error(NO_INTERNET)
        }

    }
    private fun appConfigHandle(response: Response<AppConfigResponse>): Resource<AppConfigResponse> {
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



//    Get subscription Api


    fun getSubscriptionApi(token:String) = viewModelScope.launch {
        getSubscriptionData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.getSubscriptionApi(token = token)
                .catch { e ->
                    getSubscriptionData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    getSubscriptionData.value = getSubscriptionApiHandle(data)
                }
        }else{
            getSubscriptionData.value = Resource.Error(NO_INTERNET)
        }

    }
    private fun getSubscriptionApiHandle(response: Response<GetSubscriptionDetailsResponse>): Resource<GetSubscriptionDetailsResponse> {
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