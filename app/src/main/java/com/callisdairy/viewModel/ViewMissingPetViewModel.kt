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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class ViewMissingPetViewModel @Inject constructor(app: Application, private val repo: CalisRespository,private val networkHelper: NetworkHelper) : AndroidViewModel(app) {

    private val viewMissingPetData: MutableStateFlow<Resource<ViewMissingPetResponse>> = MutableStateFlow(Resource.Empty())
    val _viewMissingPetData: StateFlow<Resource<ViewMissingPetResponse>> = viewMissingPetData

  


   //    Missing Pet Api


    fun viewMissingPetApi(token:String,petId: String) = viewModelScope.launch {
        viewMissingPetData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.viewMissingPetApi(token, petId)
                .catch { e ->
                    viewMissingPetData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    viewMissingPetData.value = viewMissingPetResponseHandle(data)
                }
        }else{
            viewMissingPetData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun viewMissingPetResponseHandle(response: Response<ViewMissingPetResponse>): Resource<ViewMissingPetResponse> {
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