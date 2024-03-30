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
import com.callisdairy.api.Constants
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
class ListedPetViewModel @Inject constructor(app: Application, private val repo: CalisRespository,private val networkHelper: NetworkHelper) : AndroidViewModel(app) {



    private val latestListedPetData: MutableStateFlow<Resource<PetListResponse>> = MutableStateFlow(Resource.Empty())
    val _latestListedPetData: StateFlow<Resource<PetListResponse>> = latestListedPetData


    private val likePet: MutableStateFlow<Resource<LikeUnlikeProductsResponse>> = MutableStateFlow(Resource.Empty())
    val _likePet: StateFlow<Resource<LikeUnlikeProductsResponse>> = likePet

    private val addToInterestedData: MutableStateFlow<Resource<AddToIntrestedResponse>> = MutableStateFlow(Resource.Empty())
    val _addToInterestedData: StateFlow<Resource<AddToIntrestedResponse>> = addToInterestedData


    private val countryStateFlow: MutableStateFlow<Resource<CountryResponse>> = MutableStateFlow(Resource.Empty())
    val _countryStateFlow: StateFlow<Resource<CountryResponse>> = countryStateFlow


    private val stateData: MutableStateFlow<Resource<CountryResponse>> = MutableStateFlow(Resource.Empty())
    val _stateData: StateFlow<Resource<CountryResponse>> = stateData


    private val citydata: MutableStateFlow<Resource<CountryResponse>> = MutableStateFlow(Resource.Empty())
    val _citydata: StateFlow<Resource<CountryResponse>> = citydata







//    latest Listed Pet Api

    fun listPetApi(token:String,page:Int,limit:Int,lat:Double,lng:Double,maxDistance:Int,
                   radius : String,
                   country : String,
                   state : String,
                   city : String,search: String) = viewModelScope.launch {
        latestListedPetData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.listPetApi(token, page, limit, lat, lng, maxDistance,radius, country, state, city,search)
                .catch { e ->
                    latestListedPetData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    latestListedPetData.value = latestListedPetResponseHandle(data)
                }
        }else{
            latestListedPetData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun latestListedPetResponseHandle(response: Response<PetListResponse>): Resource<PetListResponse> {
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




//    Pet like APi

    fun likeUnlikePetApi(token:String,id:String) = viewModelScope.launch {
        likePet.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.favUnfavPetApi(token, id)
                .catch { e ->
                    likePet.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    likePet.value = likePetResponseHandle(data)
                }
        }else{
            likePet.value = Resource.Error(Constants.NO_INTERNET)
        }

    }
    private fun likePetResponseHandle(response: Response<LikeUnlikeProductsResponse>): Resource<LikeUnlikeProductsResponse> {
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




//    Add To Intrested APi

    fun addToInterestedApi(token:String,type:String,petId:String,productId:String,serviceId:String) = viewModelScope.launch {
        addToInterestedData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.addToInterestedApi(token, type, petId, productId, serviceId)
                .catch { e ->
                    addToInterestedData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    addToInterestedData.value = addToIntrestedResponseHandle(data)
                }
        }else{
            addToInterestedData.value = Resource.Error(Constants.NO_INTERNET)
        }

    }
    private fun addToIntrestedResponseHandle(response: Response<AddToIntrestedResponse>): Resource<AddToIntrestedResponse> {
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








   

}