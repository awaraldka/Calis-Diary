package com.callisdairy.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.callisdairy.ModalClass.PojoClass
import com.callisdairy.Repositry.CalisRespository
import com.callisdairy.Utils.NetworkHelper
import com.callisdairy.Utils.Resource
import com.callisdairy.api.Constants
import com.callisdairy.api.request.AddAppointmentRequest
import com.callisdairy.api.response.AddPostResponse
import com.callisdairy.api.response.AppointmentDateResponse
import com.callisdairy.api.response.ProductsResponse
import com.google.gson.GsonBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MakeAppointmentViewModel @Inject constructor(app:Application,private val repo:CalisRespository,private val networkHelper: NetworkHelper):AndroidViewModel(app){

    private val getAllSlots: MutableStateFlow<Resource<AppointmentDateResponse>> = MutableStateFlow(Resource.Empty())
    val _getAllSlots: StateFlow<Resource<AppointmentDateResponse>> = getAllSlots

    private val addAppointmentData: MutableStateFlow<Resource<AddPostResponse>> = MutableStateFlow(Resource.Empty())
    val _addAppointmentData: StateFlow<Resource<AddPostResponse>> = addAppointmentData




    fun getAllAppointmentListApi(token: String,date:String,doctorVetId:String) = viewModelScope.launch {
        getAllSlots.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.availableSlotApi(token,date, doctorVetId)
                .catch { e ->
                    getAllSlots.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    getAllSlots.value = listResponseHandle(data)
                }
        }else{
            getAllSlots.value = Resource.Error(Constants.NO_INTERNET)
        }

    }

    private fun listResponseHandle(response: Response<AppointmentDateResponse>): Resource<AppointmentDateResponse> {
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



//   Book an Appointment


    fun bookAppointmentApi(token: String,request: AddAppointmentRequest) = viewModelScope.launch {
        addAppointmentData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.addAppointmentApi(token,request)
                .catch { e ->
                    addAppointmentData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    addAppointmentData.value = bookAppointmentResponseHandle(data)
                }
        }else{
            addAppointmentData.value = Resource.Error(Constants.NO_INTERNET)
        }

    }

    private fun bookAppointmentResponseHandle(response: Response<AddPostResponse>): Resource<AddPostResponse> {
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