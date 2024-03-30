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
class EventListViewModel @Inject constructor(app: Application, private val repo: CalisRespository,private val networkHelper: NetworkHelper) : AndroidViewModel(app) {

    private val listEventData: MutableStateFlow<Resource<ListEventResponse>> = MutableStateFlow(Resource.Empty())
    val _listEventData: StateFlow<Resource<ListEventResponse>> = listEventData

    private val myListEventData: MutableStateFlow<Resource<ListEventResponse>> = MutableStateFlow(Resource.Empty())
    val _myListEventData: StateFlow<Resource<ListEventResponse>> = myListEventData

    private val myListDeleteData: MutableStateFlow<Resource<AddEventResponse>> = MutableStateFlow(Resource.Empty())
    val _myListDeleteData: StateFlow<Resource<AddEventResponse>> = myListDeleteData


    private val countryStateFlow: MutableStateFlow<Resource<CountryResponse>> = MutableStateFlow(Resource.Empty())
    val _countryStateFlow: StateFlow<Resource<CountryResponse>> = countryStateFlow


    private val stateData: MutableStateFlow<Resource<CountryResponse>> = MutableStateFlow(Resource.Empty())
    val _stateData: StateFlow<Resource<CountryResponse>> = stateData


    private val citydata: MutableStateFlow<Resource<CountryResponse>> = MutableStateFlow(Resource.Empty())
    val _citydata: StateFlow<Resource<CountryResponse>> = citydata


    private val petBreeddata: MutableStateFlow<Resource<CountryResponse>> = MutableStateFlow(Resource.Empty())
    val _petBreeddata: StateFlow<Resource<CountryResponse>> = petBreeddata


    private val checkPlanData: MutableStateFlow<Resource<CheckLimitPlanResponse>> = MutableStateFlow(Resource.Empty())
    val _checkPlanData: StateFlow<Resource<CheckLimitPlanResponse>> = checkPlanData




    //    Lit Event Api


    fun listEventApi(token:String,search:String,page: Int,limit:Int,radius : String,
                     country : String,
                     state : String,
                     city : String, lat: Double, lng: Double,petBreedId:String) = viewModelScope.launch {
        listEventData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.listEventApi(token,search,page,limit,radius, country, state, city,lat, lng,petBreedId)
                .catch { e ->
                    listEventData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    listEventData.value = listEventResponseHandle(data)
                }
        }else{
            listEventData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun listEventResponseHandle(response: Response<ListEventResponse>): Resource<ListEventResponse> {
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






   //    My List Event Api


    fun myListEventApi(token:String,search:String,page: Int,limit:Int) = viewModelScope.launch {
        myListEventData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.myListEventApi(token,search,page,limit)
                .catch { e ->
                    myListEventData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    myListEventData.value = myListEventResponseHandle(data)
                }
        }else{
            myListEventData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun myListEventResponseHandle(response: Response<ListEventResponse>): Resource<ListEventResponse> {
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



//      Delete Event APi


    fun deleteEventApi(token: String,eventId:String) = viewModelScope.launch {
        myListDeleteData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.deleteEventApi(token, eventId)
                .catch { e ->
                    myListDeleteData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    myListDeleteData.value = deleteEventResponseHandle(data)
                }
        }else{
            myListDeleteData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun deleteEventResponseHandle(response: Response<AddEventResponse>): Resource<AddEventResponse> {
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




//     Pet Breed List Api


    fun petBreedListApi(petCategoryId : String) = viewModelScope.launch {
        petBreeddata.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.petBreedListApi(petCategoryId =petCategoryId  )
                .catch { e ->
                    petBreeddata.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    petBreeddata.value = petBreeddataHandle(data)
                }
        }else{
            petBreeddata.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun petBreeddataHandle(response: Response<CountryResponse>): Resource<CountryResponse> {
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



//     Check plans

    fun checkPlanApi(token:String) = viewModelScope.launch {
        checkPlanData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.checkPlanApi(token= token)
                .catch { e ->
                    checkPlanData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    checkPlanData.value = checkPlanResponseHandle(data)
                }
        }else{
            checkPlanData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun checkPlanResponseHandle(response: Response<CheckLimitPlanResponse>): Resource<CheckLimitPlanResponse> {
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