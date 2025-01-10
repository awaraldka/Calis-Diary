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
import com.callisdairy.api.response.AddToIntrestedResponse
import com.callisdairy.api.response.CountryResponse
import com.callisdairy.api.response.EditProfileResponse
import com.callisdairy.api.response.VetOrDoctorResponse
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
class DoctorViewModel @Inject constructor(app:Application, private val repo:CalisRespository, private val networkHelper: NetworkHelper):AndroidViewModel(app){

    private val listAllDoc: MutableStateFlow<Resource<VetOrDoctorResponse>> = MutableStateFlow(Resource.Empty())
    val _listAllDocData: StateFlow<Resource<VetOrDoctorResponse>> = listAllDoc


    private val viewDetailsData: MutableStateFlow<Resource<EditProfileResponse>> = MutableStateFlow(Resource.Empty())
    val _viewDetailsData: StateFlow<Resource<EditProfileResponse>> = viewDetailsData


    private val blockUserData: MutableStateFlow<Resource<AddPostResponse>> = MutableStateFlow(Resource.Empty())
    val _blockUserData: StateFlow<Resource<AddPostResponse>> = blockUserData


    private val followUnfollowData: MutableStateFlow<Resource<AddToIntrestedResponse>> = MutableStateFlow(Resource.Empty())
    val _followUnfollowData : StateFlow<Resource<AddToIntrestedResponse>> = followUnfollowData


    private val countryStateFlow: MutableStateFlow<Resource<CountryResponse>> = MutableStateFlow(Resource.Empty())
    val _countryStateFlow: StateFlow<Resource<CountryResponse>> = countryStateFlow


    private val stateData: MutableStateFlow<Resource<CountryResponse>> = MutableStateFlow(Resource.Empty())
    val _stateData: StateFlow<Resource<CountryResponse>> = stateData


    private val citydata: MutableStateFlow<Resource<CountryResponse>> = MutableStateFlow(Resource.Empty())
    val _citydata: StateFlow<Resource<CountryResponse>> = citydata




//     Get All Doctor List

    fun getAllAppointmentListApi(token: String,userTypes:String,search:String,page: Int,limit: Int,
                                 radius : String,
                                 country : String,
                                 state : String,
                                 city : String, lat: Double, lng: Double) = viewModelScope.launch {
        listAllDoc.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.listVendorApi(token,userTypes, search,page, limit,radius, country, state, city, lat, lng)
                .catch { e ->
                    listAllDoc.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    listAllDoc.value = listResponseHandle(data)
                }
        }else{
            listAllDoc.value = Resource.Error(Constants.NO_INTERNET)
        }

    }

    private fun listResponseHandle(response: Response<VetOrDoctorResponse>): Resource<VetOrDoctorResponse> {
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


//    View Details Api


    fun viewDetailsApi(token: String,id:String) = viewModelScope.launch {
        viewDetailsData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.viewUser(token,id)
                .catch { e ->
                    viewDetailsData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    viewDetailsData.value = viewDetailsResponseHandle(data)
                }
        }else{
            viewDetailsData.value = Resource.Error(Constants.NO_INTERNET)
        }

    }

    private fun viewDetailsResponseHandle(response: Response<EditProfileResponse>): Resource<EditProfileResponse> {
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

    //     Follow Unfollow Api


    fun followUnfollowAPi(token:String,userId:String) = viewModelScope.launch {
        followUnfollowData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.followUnfollowUserApi(token,userId)
                .catch { e ->
                    followUnfollowData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    followUnfollowData.value = followUnfollowResponseHandle(data)
                }
        }else{
            followUnfollowData.value = Resource.Error(Constants.NO_INTERNET)
        }

    }

    private fun followUnfollowResponseHandle(response: Response<AddToIntrestedResponse>): Resource<AddToIntrestedResponse> {
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
            stateData.value = Resource.Error(Constants.NO_INTERNET)
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
            citydata.value = Resource.Error(Constants.NO_INTERNET)
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
            countryStateFlow.value = Resource.Error(Constants.NO_INTERNET)
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




}