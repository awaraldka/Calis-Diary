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
import com.callisdairy.api.response.AddLikeResponse
import com.callisdairy.api.response.CountryResponse
import com.callisdairy.api.response.HomePageListResponse
import com.callisdairy.api.response.UserStoriesResponse
import com.callisdairy.api.response.VendorDashboardResponse
import com.google.gson.GsonBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(app: Application, private val repo: CalisRespository,private val networkHelper: NetworkHelper) :
    AndroidViewModel(app) {

    private val homeListData: MutableStateFlow<Resource<HomePageListResponse>> =
        MutableStateFlow(Resource.Empty())
    val _homeListData: StateFlow<Resource<HomePageListResponse>> = homeListData

    private val storyListData: MutableStateFlow<Resource<UserStoriesResponse>> =
        MutableStateFlow(Resource.Empty())
    val _storyListData: StateFlow<Resource<UserStoriesResponse>> = storyListData


    private val addLikeData: MutableStateFlow<Resource<AddLikeResponse>> = MutableStateFlow(Resource.Empty())
    val _addLikeData: StateFlow<Resource<AddLikeResponse>> = addLikeData


    private val hidePostData: MutableStateFlow<Resource<AddEventResponse>> = MutableStateFlow(Resource.Empty())
    val _hidePostData: StateFlow<Resource<AddEventResponse>> = hidePostData


    private val deletePostData: MutableStateFlow<Resource<AddEventResponse>> = MutableStateFlow(Resource.Empty())
    val _deletePostData: StateFlow<Resource<AddEventResponse>> = deletePostData


    private val reportPostData: MutableStateFlow<Resource<AddEventResponse>> = MutableStateFlow(Resource.Empty())
    val _reportPostData: StateFlow<Resource<AddEventResponse>> = reportPostData

    private val countryStateFlow: MutableStateFlow<Resource<CountryResponse>> = MutableStateFlow(Resource.Empty())
    val _countryStateFlow: StateFlow<Resource<CountryResponse>> = countryStateFlow


    private val stateData: MutableStateFlow<Resource<CountryResponse>> = MutableStateFlow(Resource.Empty())
    val _stateData: StateFlow<Resource<CountryResponse>> = stateData


    private val citydata: MutableStateFlow<Resource<CountryResponse>> = MutableStateFlow(Resource.Empty())
    val _citydata: StateFlow<Resource<CountryResponse>> = citydata


    private val vendorHomeData: MutableStateFlow<Resource<VendorDashboardResponse>> = MutableStateFlow(Resource.Empty())
    val _vendorHomeData: StateFlow<Resource<VendorDashboardResponse>> = vendorHomeData



    private val petBreeddata: MutableStateFlow<Resource<CountryResponse>> = MutableStateFlow(Resource.Empty())
    val _petBreeddata: StateFlow<Resource<CountryResponse>> = petBreeddata





    //    Home Page List Api


    fun homeListApi(token: String, page: Int, limit: Int, radius: String,
                    country: String,
                    city: String,
                    state: String,
                    lat: Double,
                    long: Double,petBreedId:String) = viewModelScope.launch {
        homeListData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()) {

            repo.homePagePostListApi(token, page, limit,radius, country, city, state,lat,long,petBreedId)
                .catch { e ->
                    homeListData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    homeListData.value = homeListResponseHandle(data)
                }
        } else {
            homeListData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun homeListResponseHandle(response: Response<HomePageListResponse>): Resource<HomePageListResponse> {
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


    // story list api

    fun storyListApi(token: String) = viewModelScope.launch {
        storyListData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()) {

            repo.storyList(token)
                .catch { e ->
                    storyListData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    storyListData.value = storyListResponseHandle(data)
                }
        } else {
            storyListData.value = Resource.Error(NO_INTERNET)
        }

    }
    private fun storyListResponseHandle(response: Response<UserStoriesResponse>): Resource<UserStoriesResponse> {
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


//     AddLike Api

    fun addLikeApi(token: String, typeOfPost: String, postId: String, commentId: String) =
        viewModelScope.launch {
            addLikeData.value = Resource.Loading()

            if (networkHelper.hasInternetConnection()) {

                repo.addLikeApi(token, typeOfPost, postId, commentId)
                    .catch { e ->
                        addLikeData.value = Resource.Error(e.message.toString())
                    }.collect { data ->
                        addLikeData.value = addLikeResponseHandle(data)
                    }
            } else {
                addLikeData.value = Resource.Error(NO_INTERNET)
            }

        }
    private fun addLikeResponseHandle(response: Response<AddLikeResponse>): Resource<AddLikeResponse> {
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


//    Hide Post Api

    fun hidePostApi(token: String,id:String) = viewModelScope.launch {
            hidePostData.value = Resource.Loading()

            if (networkHelper.hasInternetConnection()) {

                repo.hidePostApi(token, id)
                    .catch { e ->
                        hidePostData.value = Resource.Error(e.message.toString())
                    }.collect { data ->
                        hidePostData.value = hidePostResponseHandle(data)
                    }
            } else {
                hidePostData.value = Resource.Error(NO_INTERNET)
            }

        }
    private fun hidePostResponseHandle(response: Response<AddEventResponse>): Resource<AddEventResponse> {
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


//     Delete post Api


    fun deletePostApi(token: String,id:String) = viewModelScope.launch {
        deletePostData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()) {

            repo.removePostApi(token, id)
                .catch { e ->
                    deletePostData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    deletePostData.value = deletePostResponseHandle(data)
                }
        } else {
            deletePostData.value = Resource.Error(NO_INTERNET)
        }

    }
    private fun deletePostResponseHandle(response: Response<AddEventResponse>): Resource<AddEventResponse> {
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



//    Report Post API


    fun reportPostApi(token: String,id:String,description:String) = viewModelScope.launch {
        reportPostData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()) {

            repo.createReportApi(token, id,description)
                .catch { e ->
                    reportPostData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    reportPostData.value = reportPostResponseHandle(data)
                }
        } else {
            reportPostData.value = Resource.Error(NO_INTERNET)
        }

    }
    private fun reportPostResponseHandle(response: Response<AddEventResponse>): Resource<AddEventResponse> {
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


//         Home vendor Api

    fun vendorHomeApi(token:String) = viewModelScope.launch {
        vendorHomeData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.vendorDashboard(token)
                .catch { e ->
                    vendorHomeData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    vendorHomeData.value = vendorHomeResponseHandle(data)
                }
        }else{
            vendorHomeData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun vendorHomeResponseHandle(response: Response<VendorDashboardResponse>): Resource<VendorDashboardResponse> {
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




}