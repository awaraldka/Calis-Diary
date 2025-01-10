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
import com.callisdairy.api.response.CheckLimitPlanResponse
import com.callisdairy.api.response.CountryResponse
import com.callisdairy.api.response.MissingPetResponse
import com.google.gson.GsonBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class MissingPetViewModel @Inject constructor(app: Application, private val repo: CalisRespository,private val networkHelper: NetworkHelper) : AndroidViewModel(app) {

    private val missingPetData: MutableStateFlow<Resource<MissingPetResponse>> = MutableStateFlow(Resource.Empty())
    val _missingPetData: StateFlow<Resource<MissingPetResponse>> = missingPetData

    private val myMmissingPetData: MutableStateFlow<Resource<MissingPetResponse>> = MutableStateFlow(Resource.Empty())
    val _myMmissingPetData: StateFlow<Resource<MissingPetResponse>> = myMmissingPetData


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



    //    Missing Pet Api


    fun missingPetApi(token:String,search: String,type:String,page:Int,limit:Int,radius : String,country : String,
                      state : String,city : String, lat: Double, lng: Double,petBreedId:String) = viewModelScope.launch {
        missingPetData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.missingPetListApi(token, search, type, page, limit,radius,country,state,city,lat, lng,petBreedId)
                .catch { e ->
                    missingPetData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    missingPetData.value = listEventResponseHandle(data)
                }
        }else{
            missingPetData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun listEventResponseHandle(response: Response<MissingPetResponse>): Resource<MissingPetResponse> {
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




   //   My Missing Pet Api


    fun myMissingPetApi(token:String,search: String,type:String,page:Int,limit:Int,radius : String,country : String,state : String,city : String, lat: Double, lng: Double) = viewModelScope.launch {
        myMmissingPetData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.missingPetListApi(token, search, type, page, limit,radius,country,state,city,lat, lng, "")
                .catch { e ->
                    myMmissingPetData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    myMmissingPetData.value = myMissingPetResponseHandle(data)
                }
        }else{
            myMmissingPetData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun myMissingPetResponseHandle(response: Response<MissingPetResponse>): Resource<MissingPetResponse> {
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


    fun deleteEventApi(token: String,id:String) = viewModelScope.launch {
        myListDeleteData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.deleteMissingPetApi(token, id)
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