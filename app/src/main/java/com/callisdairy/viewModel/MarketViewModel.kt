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
class MarketViewModel @Inject constructor(app: Application, private val repo: CalisRespository,private val networkHelper: NetworkHelper) : AndroidViewModel(app) {

    private val listCategoryData: MutableStateFlow<Resource<ListCategoryResponse>> = MutableStateFlow(Resource.Empty())
    val _listCategoryData: StateFlow<Resource<ListCategoryResponse>> = listCategoryData

    private val productListData: MutableStateFlow<Resource<ProductListResponse>> = MutableStateFlow(Resource.Empty())
    val _productListData: StateFlow<Resource<ProductListResponse>> = productListData


    private val servicelistCategoryData: MutableStateFlow<Resource<ListCategoryResponse>> = MutableStateFlow(Resource.Empty())
    val _servicelistCategoryData: StateFlow<Resource<ListCategoryResponse>> = servicelistCategoryData


    private val latestListedPetData: MutableStateFlow<Resource<PetListResponse>> = MutableStateFlow(Resource.Empty())
    val _latestListedPetData: StateFlow<Resource<PetListResponse>> = latestListedPetData


    private val ServiceListData: MutableStateFlow<Resource<ServiceListResponse>> = MutableStateFlow(Resource.Empty())
    val _ServiceListData: StateFlow<Resource<ServiceListResponse>> = ServiceListData


    private val likeData: MutableStateFlow<Resource<LikeUnlikeProductsResponse>> = MutableStateFlow(Resource.Empty())
    val _likeData: StateFlow<Resource<LikeUnlikeProductsResponse>> = likeData

    private val likeService: MutableStateFlow<Resource<LikeUnlikeProductsResponse>> = MutableStateFlow(Resource.Empty())
    val _likeService: StateFlow<Resource<LikeUnlikeProductsResponse>> = likeService

    private val likePet: MutableStateFlow<Resource<LikeUnlikeProductsResponse>> = MutableStateFlow(Resource.Empty())
    val _likePet: StateFlow<Resource<LikeUnlikeProductsResponse>> = likePet

    private val addToInterestedData: MutableStateFlow<Resource<AddToIntrestedResponse>> = MutableStateFlow(Resource.Empty())
    val _addToInterestedData: StateFlow<Resource<AddToIntrestedResponse>> = addToInterestedData

    private val bannerListData: MutableStateFlow<Resource<BannerListResponse>> = MutableStateFlow(Resource.Empty())
    val _bannerListData: StateFlow<Resource<BannerListResponse>> = bannerListData


    private val countryStateFlow: MutableStateFlow<Resource<CountryResponse>> = MutableStateFlow(Resource.Empty())
    val _countryStateFlow: StateFlow<Resource<CountryResponse>> = countryStateFlow


    private val stateData: MutableStateFlow<Resource<CountryResponse>> = MutableStateFlow(Resource.Empty())
    val _stateData: StateFlow<Resource<CountryResponse>> = stateData


    private val citydata: MutableStateFlow<Resource<CountryResponse>> = MutableStateFlow(Resource.Empty())
    val _citydata: StateFlow<Resource<CountryResponse>> = citydata




//     listCategoryApi

    fun listCategoryApi(categoryTypes:String) = viewModelScope.launch {
        listCategoryData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.listCategoryApi(categoryTypes,1, 10)
                .catch { e ->
                    listCategoryData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    listCategoryData.value = lisCategoryResponseHandle(data)
                }
        }else{
            listCategoryData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun lisCategoryResponseHandle(response: Response<ListCategoryResponse>): Resource<ListCategoryResponse> {
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


//     serviceListCategoryApi

    fun serviceListCategoryApi(categoryTypes:String) = viewModelScope.launch {
        servicelistCategoryData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.listCategoryApi(categoryTypes,1,10)
                .catch { e ->
                    servicelistCategoryData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    servicelistCategoryData.value = serviceLisCategoryResponseHandle(data)
                }
        }else{
            servicelistCategoryData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun serviceLisCategoryResponseHandle(response: Response<ListCategoryResponse>): Resource<ListCategoryResponse> {
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


//    productListApi

    fun productListApi(token:String,lat:Double,lng:Double,maxDistance:Int,categoryId:String,subCategoryId:String,page:Int,limit:Int,
                       radius : String,
                       country : String,
                       state : String,
                       city : String,search: String) = viewModelScope.launch {
        productListData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.listProductV2Api(
                token,
                lat,
                lng,
                maxDistance,
                categoryId,
                subCategoryId,
                page,
                limit,
                radius, country, state, city,search
            )
                .catch { e ->
                    productListData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    productListData.value = productListResponseHandle(data)
                }
        }else{
            productListData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun productListResponseHandle(response: Response<ProductListResponse>): Resource<ProductListResponse> {
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




//    Service list Api

    fun listServiceApi(token:String,page:Int,limit:Int,categoryId: String,
                       subCategoryId: String,lat:Double,lng:Double,maxDistance:Int,radius : String,
                       country : String,
                       state : String,
                       city : String,search: String) = viewModelScope.launch {
        ServiceListData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.listServiceV2Api(token,page, limit,categoryId, subCategoryId, lat, lng, maxDistance,radius, country, state, city,search)
                .catch { e ->
                    ServiceListData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    ServiceListData.value = serviceListResponseHandle(data)
                }
        }else{
            ServiceListData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun serviceListResponseHandle(response: Response<ServiceListResponse>): Resource<ServiceListResponse> {
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



//    Product Like Api


    fun addLikeProductApi(token:String,id:String) = viewModelScope.launch {
        likeData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.likeUnlikeProductApi(token, id)
                .catch { e ->
                    likeData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    likeData.value = likeResponseHandle(data)
                }
        }else{
            likeData.value = Resource.Error(Constants.NO_INTERNET)
        }

    }
    private fun likeResponseHandle(response: Response<LikeUnlikeProductsResponse>): Resource<LikeUnlikeProductsResponse> {
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


//    Service Like Api


    fun likeUnlikeServicesApi(token:String,id:String) = viewModelScope.launch {
        likeService.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.likeUnlikeServicesApi(token, id)
                .catch { e ->
                    likeService.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    likeService.value = likeServiceResponseHandle(data)
                }
        }else{
            likeService.value = Resource.Error(Constants.NO_INTERNET)
        }

    }
    private fun likeServiceResponseHandle(response: Response<LikeUnlikeProductsResponse>): Resource<LikeUnlikeProductsResponse> {
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



//    Banner List Api


    fun bannerListApi() = viewModelScope.launch {
        bannerListData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.listBannerListApi()
                .catch { e ->
                    bannerListData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    bannerListData.value = bannerListResponseHandle(data)
                }
        }else{
            bannerListData.value = Resource.Error(Constants.NO_INTERNET)
        }

    }
    private fun bannerListResponseHandle(response: Response<BannerListResponse>): Resource<BannerListResponse> {
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