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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class ResetPasswordViewModel @Inject constructor(app: Application, private val repo: CalisRespository,private val networkHelper: NetworkHelper) : AndroidViewModel(app) {

    private val resetPasswordData: MutableStateFlow<Resource<SignUpResponse>> = MutableStateFlow(Resource.Empty())
    val _resetPasswordData: StateFlow<Resource<SignUpResponse>> = resetPasswordData


   //    Signup Api


    fun resetPasswordApi(token:String, password:String,confirmPassword:String) = viewModelScope.launch {
        resetPasswordData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.resetPasswordApi(token, password, confirmPassword)
                .catch { e ->
                    resetPasswordData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    resetPasswordData.value = resetPasswordResponseHandle(data)
                }
        }else{
            resetPasswordData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun resetPasswordResponseHandle(response: Response<SignUpResponse>): Resource<SignUpResponse> {
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