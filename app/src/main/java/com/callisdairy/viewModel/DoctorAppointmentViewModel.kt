package com.callisdairy.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.callisdairy.ModalClass.PojoClass
import com.callisdairy.Repositry.CalisRespository
import com.callisdairy.Utils.NetworkHelper
import com.callisdairy.Utils.Resource
import com.callisdairy.api.Constants
import com.callisdairy.api.response.AddPostResponse
import com.callisdairy.api.response.AppointmentListResponse
import com.callisdairy.api.response.BlockedUserResponse
import com.callisdairy.api.response.UserFeedBackListResponse
import com.callisdairy.api.response.ViewAppointmentResponse
import com.google.gson.GsonBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class DoctorAppointmentViewModel @Inject constructor(app: Application, private val repo: CalisRespository, private val networkHelper: NetworkHelper) : AndroidViewModel(app) {


    private val appointmentListData: MutableStateFlow<Resource<AppointmentListResponse>> = MutableStateFlow(Resource.Empty())
    val _appointmentListData: StateFlow<Resource<AppointmentListResponse>> = appointmentListData

    private val appointmentListVirtualData: MutableStateFlow<Resource<AppointmentListResponse>> = MutableStateFlow(Resource.Empty())
    val _appointmentListVirtualData: StateFlow<Resource<AppointmentListResponse>> = appointmentListVirtualData

    private val appointmentDetailsData: MutableStateFlow<Resource<ViewAppointmentResponse>> = MutableStateFlow(Resource.Empty())
    val _appointmentDetailsData: StateFlow<Resource<ViewAppointmentResponse>> = appointmentDetailsData

    private val blockUserData: MutableStateFlow<Resource<AddPostResponse>> = MutableStateFlow(Resource.Empty())
    val _blockUserData: StateFlow<Resource<AddPostResponse>> = blockUserData

    private val markAsDoneData: MutableStateFlow<Resource<AddPostResponse>> = MutableStateFlow(Resource.Empty())
    val _markAsDoneData: StateFlow<Resource<AddPostResponse>> = markAsDoneData

    private val getAllBlockedUserData: MutableStateFlow<Resource<BlockedUserResponse>> = MutableStateFlow(Resource.Empty())
    val _getAllBlockedUserData: StateFlow<Resource<BlockedUserResponse>> = getAllBlockedUserData

    private val addFeedBackData: MutableStateFlow<Resource<AddPostResponse>> = MutableStateFlow(Resource.Empty())
    val _addFeedBackData: StateFlow<Resource<AddPostResponse>> = addFeedBackData

    private val listFeedBackData: MutableStateFlow<Resource<UserFeedBackListResponse>> = MutableStateFlow(Resource.Empty())
    val _listFeedBackData: StateFlow<Resource<UserFeedBackListResponse>> = listFeedBackData

    private val cancelAppointmentData: MutableStateFlow<Resource<AddPostResponse>> = MutableStateFlow(Resource.Empty())
    val _cancelAppointmentData: StateFlow<Resource<AddPostResponse>> = cancelAppointmentData

    private val cancelAllAppointmentData: MutableStateFlow<Resource<AddPostResponse>> = MutableStateFlow(Resource.Empty())
    val _cancelAllAppointmentData: StateFlow<Resource<AddPostResponse>> = cancelAllAppointmentData



//  Appointment list Api For InPerson
    fun appointmentListApi(token: String,search:String,page:Int,limit:Int,consultingType:String,appointmentStatus:String) = viewModelScope.launch {
        appointmentListData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.listUserAppointmentApi(token, search, page, limit,consultingType,appointmentStatus)
                .catch { e ->
                    appointmentListData.value = Resource.Error(e.message.toString())
                }.collectLatest { data ->
                    appointmentListData.value = appointmentResponseHandle(data)
                }
        }else{
            appointmentListData.value = Resource.Error(Constants.NO_INTERNET)
        }

    }

    private fun appointmentResponseHandle(response: Response<AppointmentListResponse>): Resource<AppointmentListResponse> {
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

//    Appointment list Api For Virtual

    fun appointmentListVirtualApi(token: String,search:String,page:Int,limit:Int,consultingType:String,appointmentStatus:String) = viewModelScope.launch {
        appointmentListVirtualData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.listUserAppointmentApi(token, search, page, limit,consultingType,appointmentStatus)
                .catch { e ->
                    appointmentListVirtualData.value = Resource.Error(e.message.toString())
                }.collectLatest { data ->
                    appointmentListVirtualData.value = appointmentVirtualResponseHandle(data)
                }
        }else{
            appointmentListVirtualData.value = Resource.Error(Constants.NO_INTERNET)
        }

    }

    private fun appointmentVirtualResponseHandle(response: Response<AppointmentListResponse>): Resource<AppointmentListResponse> {
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



// View Appointment Details


    fun appointmentDetailsApi(token: String,id:String) = viewModelScope.launch {
        appointmentDetailsData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.viewAppointmentApi(token,id)
                .catch { e ->
                    appointmentDetailsData.value = Resource.Error(e.message.toString())
                }.collectLatest { data ->
                    appointmentDetailsData.value = appointmentDetailsResponseHandle(data)
                }
        }else{
            appointmentDetailsData.value = Resource.Error(Constants.NO_INTERNET)
        }

    }

    private fun appointmentDetailsResponseHandle(response: Response<ViewAppointmentResponse>): Resource<ViewAppointmentResponse> {
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


//    Block Unblock Api


    fun blockUserApi(token: String,id:String) = viewModelScope.launch {
        blockUserData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.addBlockDoctorUserAPi(token,id)
                .catch { e ->
                    blockUserData.value = Resource.Error(e.message.toString())
                }.collectLatest { data ->
                    blockUserData.value = blockUserResponseHandle(data)
                }
        }else{
            blockUserData.value = Resource.Error(Constants.NO_INTERNET)
        }

    }

    private fun blockUserResponseHandle(response: Response<AddPostResponse>): Resource<AddPostResponse> {
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


//    Mark As Done Api




    fun markAsDoneApi(token: String,id:String) = viewModelScope.launch {
        markAsDoneData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.markAsDoneAppointmentApi(token,id)
                .catch { e ->
                    markAsDoneData.value = Resource.Error(e.message.toString())
                }.collectLatest { data ->
                    markAsDoneData.value = markAsDoneResponseHandle(data)
                }
        }else{
            markAsDoneData.value = Resource.Error(Constants.NO_INTERNET)
        }

    }

    private fun markAsDoneResponseHandle(response: Response<AddPostResponse>): Resource<AddPostResponse> {
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


//     Get All Blocked Users List

    fun getAllBlockedUserApi(token: String,search:String,page: Int,limit: Int) = viewModelScope.launch {
        getAllBlockedUserData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.listBlockDoctorUserApi(token,search, page, limit)
                .catch { e ->
                    getAllBlockedUserData.value = Resource.Error(e.message.toString())
                }.collectLatest { data ->
                    getAllBlockedUserData.value = getAllBlockedUserResponseHandle(data)
                }
        }else{
            getAllBlockedUserData.value = Resource.Error(Constants.NO_INTERNET)
        }

    }

    private fun getAllBlockedUserResponseHandle(response: Response<BlockedUserResponse>): Resource<BlockedUserResponse> {
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


//     Add Feed Back  List

    fun addFeedBackApi(token: String,appointmentId:String,doctorId: String,title: String,rating: Double,message: String) = viewModelScope.launch {
        addFeedBackData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.addFeedbacknratingApi(token,appointmentId, doctorId, title, rating, message)
                .catch { e ->
                    addFeedBackData.value = Resource.Error(e.message.toString())
                }.collectLatest { data ->
                    addFeedBackData.value = addFeedBackResponseHandle(data)
                }
        }else{
            addFeedBackData.value = Resource.Error(Constants.NO_INTERNET)
        }

    }

    private fun addFeedBackResponseHandle(response: Response<AddPostResponse>): Resource<AddPostResponse> {
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


//     Add Feed Back  List

    fun listFeedBackApi(token: String,search:String,page: Int,limit: Int) = viewModelScope.launch {
        listFeedBackData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.listfeedbacknratingApi(token,search, page, limit)
                .catch { e ->
                    listFeedBackData.value = Resource.Error(e.message.toString())
                }.collectLatest { data ->
                    listFeedBackData.value = listFeedBackDataResponseHandle(data)
                }
        }else{
            listFeedBackData.value = Resource.Error(Constants.NO_INTERNET)
        }

    }

    private fun listFeedBackDataResponseHandle(response: Response<UserFeedBackListResponse>): Resource<UserFeedBackListResponse> {
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


//     Cancel Appointment

    fun cancelAppointmentApi(token:String,id: String) = viewModelScope.launch {
        cancelAppointmentData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.cancelAppointmentApi(token =token,id = id)
                .catch { e ->
                    cancelAppointmentData.value = Resource.Error(e.message.toString())
                }.collectLatest { data ->
                    cancelAppointmentData.value = cancelAppointmentDataResponseHandle(data)
                }
        }else{
            cancelAppointmentData.value = Resource.Error(Constants.NO_INTERNET)
        }

    }

    private fun cancelAppointmentDataResponseHandle(response: Response<AddPostResponse>): Resource<AddPostResponse> {
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


//     Cancel All Appointments

    fun cancelAllAppointmentApi(token:String,date: String) = viewModelScope.launch {
        cancelAllAppointmentData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.canceleAppointmentByday(token =token,date = date)
                .catch { e ->
                    cancelAllAppointmentData.value = Resource.Error(e.message.toString())
                }.collectLatest { data ->
                    cancelAllAppointmentData.value = cancelAllAppointmentDataResponseHandle(data)
                }
        }else{
            cancelAllAppointmentData.value = Resource.Error(Constants.NO_INTERNET)
        }

    }

    private fun cancelAllAppointmentDataResponseHandle(response: Response<AddPostResponse>): Resource<AddPostResponse> {
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