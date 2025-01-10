package com.callisdairy.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.callisdairy.ModalClass.PojoClass
import com.callisdairy.Repositry.CalisRespository
import com.callisdairy.Utils.NetworkHelper
import com.callisdairy.Utils.Resource
import com.callisdairy.api.Constants.NO_INTERNET
import com.callisdairy.api.response.SignUpResponse
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
class ChangePasswordViewModel @Inject constructor(app: Application, private val repo: CalisRespository,private val networkHelper: NetworkHelper) : AndroidViewModel(app) {

    private val changePasswordData: MutableStateFlow<Resource<SignUpResponse>> = MutableStateFlow(Resource.Empty())
    val _changePasswordData : StateFlow<Resource<SignUpResponse>> = changePasswordData


   //    changePassword Api


    fun changePasswordApi(token:String,jsonObject: JsonObject) = viewModelScope.launch {
        changePasswordData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.changePassword(token,jsonObject)
                .catch { e ->
                    changePasswordData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    changePasswordData.value = changePasswordResponseHandle(data)
                }
        }else{
            changePasswordData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun changePasswordResponseHandle(response: Response<SignUpResponse>): Resource<SignUpResponse> {
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