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
import com.callisdairy.api.response.NotificationListResponse
import com.google.gson.GsonBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class NotificationViewModel @Inject constructor(app: Application, private val repo: CalisRespository,private val networkHelper: NetworkHelper) : AndroidViewModel(app) {

    private val notificationData: MutableStateFlow<Resource<NotificationListResponse>> = MutableStateFlow(Resource.Empty())
    val _notificationData: StateFlow<Resource<NotificationListResponse>> = notificationData

    private val removeData: MutableStateFlow<Resource<AddEventResponse>> = MutableStateFlow(Resource.Empty())
    val _removeData: StateFlow<Resource<AddEventResponse>> = removeData

    private val deleteAccountData: MutableStateFlow<Resource<AddEventResponse>> = MutableStateFlow(Resource.Empty())
    val _deleteAccountData: StateFlow<Resource<AddEventResponse>> = deleteAccountData

    private val deactivateAccountData: MutableStateFlow<Resource<AddEventResponse>> = MutableStateFlow(Resource.Empty())
    val _deactivateAccountData: StateFlow<Resource<AddEventResponse>> = deactivateAccountData


   //    Notification List Api

    fun notificationApi(token:String, page: Int,limit: Int) = viewModelScope.launch {
        notificationData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.notificationListApi(token,page, limit)
                .catch { e ->
                    notificationData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    notificationData.value = notificationResponseHandle(data)
                }
        }else{
            notificationData.value = Resource.Error(NO_INTERNET)
        }

    }
    private fun notificationResponseHandle(response: Response<NotificationListResponse>): Resource<NotificationListResponse> {
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


//    Delete Notification


    fun deleteNotificationApi(token:String,notificationId:String) = viewModelScope.launch {
        removeData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.clearNotificationApi(token,notificationId)
                .catch { e ->
                    removeData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    removeData.value = deleteNotificationResponseHandle(data)
                }
        }else{
            removeData.value = Resource.Error(NO_INTERNET)
        }

    }
    private fun deleteNotificationResponseHandle(response: Response<AddEventResponse>): Resource<AddEventResponse> {
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











}