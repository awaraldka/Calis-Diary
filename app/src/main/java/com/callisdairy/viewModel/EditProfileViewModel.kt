package com.callisdairy.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.callisdairy.ModalClass.PojoClass
import com.callisdairy.Repositry.CalisRespository
import com.callisdairy.Utils.NetworkHelper
import com.callisdairy.Utils.Resource
import com.callisdairy.api.Constants.NO_INTERNET
import com.callisdairy.api.request.EditProfileRequest
import com.callisdairy.api.request.EditProfileVendorRequest
import com.callisdairy.api.response.CountryResponse
import com.callisdairy.api.response.EditProfileResponse
import com.google.gson.GsonBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class EditProfileViewModel @Inject constructor(app: Application, private val repo: CalisRespository,private val networkHelper: NetworkHelper) : AndroidViewModel(app) {

    private val editProfileData: MutableStateFlow<Resource<EditProfileResponse>> = MutableStateFlow(Resource.Empty())
    val _editProfileData: StateFlow<Resource<EditProfileResponse>> = editProfileData



    private val countryStateFlow: MutableStateFlow<Resource<CountryResponse>> = MutableStateFlow(Resource.Empty())
    val _countryStateFlow: StateFlow<Resource<CountryResponse>> = countryStateFlow


    private val stateData: MutableStateFlow<Resource<CountryResponse>> = MutableStateFlow(Resource.Empty())
    val _stateData: StateFlow<Resource<CountryResponse>> = stateData


    private val citydata: MutableStateFlow<Resource<CountryResponse>> = MutableStateFlow(Resource.Empty())
    val _citydata: StateFlow<Resource<CountryResponse>> = citydata


    private val editProfileViewData: MutableStateFlow<Resource<EditProfileResponse>> = MutableStateFlow(Resource.Empty())
    val _editProfileViewData : StateFlow<Resource<EditProfileResponse>> = editProfileViewData

        private val editProfileVendorData: MutableStateFlow<Resource<EditProfileResponse>> = MutableStateFlow(Resource.Empty())
    val _editProfileVendorData: StateFlow<Resource<EditProfileResponse>> = editProfileVendorData


//    get Profile Api

    fun editViewProfileApi(token:String) = viewModelScope.launch {
        editProfileViewData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.getProfileApi(token)
                .catch { e ->
                    editProfileViewData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    editProfileViewData.value = editViewProfileResponseHandle(data)
                }
        }else{
            editProfileViewData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun editViewProfileResponseHandle(response: Response<EditProfileResponse>): Resource<EditProfileResponse> {
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


    //    Edit Profile Api


    fun editProfileApi(token:String,request: EditProfileRequest) = viewModelScope.launch {
        editProfileData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.editProfileApi(token,request)
                .catch { e ->
                    editProfileData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    editProfileData.value = editProfileResponseHandle(data)
                }
        }else{
            editProfileData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun editProfileResponseHandle(response: Response<EditProfileResponse>): Resource<EditProfileResponse> {
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




    //    Edit Profile Api


    fun editProfileVendorApi(token:String,request: EditProfileVendorRequest) = viewModelScope.launch {
        editProfileVendorData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.editProfileVendorApi(token,request)
                .catch { e ->
                    editProfileVendorData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    editProfileVendorData.value = editProfileVendorResponseHandle(data)
                }
        }else{
            editProfileVendorData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun editProfileVendorResponseHandle(response: Response<EditProfileResponse>): Resource<EditProfileResponse> {
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



    //     Get Country List

    fun getCountryApi() = viewModelScope.launch {
        countryStateFlow.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.getAllCountryApi()
                .catch { e ->
                    countryStateFlow.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    countryStateFlow.value = countryResponseHandle(data)
                }
        }else{
            countryStateFlow.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun countryResponseHandle(response: Response<CountryResponse>): Resource<CountryResponse> {
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



//     Get State List

    fun getStateListApi(countryCode:String) = viewModelScope.launch {
        stateData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.getAllStateApi(countryCode)
                .catch { e ->
                    stateData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    stateData.value = stateResponseHandle(data)
                }
        }else{
            stateData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun stateResponseHandle(response: Response<CountryResponse>): Resource<CountryResponse> {
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



//     Get City List

    fun getCityListApi(countryCode:String, stateCode:String) = viewModelScope.launch {
        citydata.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.getAllCityApi(countryCode,stateCode)
                .catch { e ->
                    citydata.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    citydata.value = cityResponseHandle(data)
                }
        }else{
            citydata.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun cityResponseHandle(response: Response<CountryResponse>): Resource<CountryResponse> {
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