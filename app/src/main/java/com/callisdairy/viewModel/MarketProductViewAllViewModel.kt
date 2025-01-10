package com.callisdairy.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.callisdairy.ModalClass.PojoClass
import com.callisdairy.Repositry.CalisRespository
import com.callisdairy.Utils.NetworkHelper
import com.callisdairy.Utils.Resource
import com.callisdairy.api.Constants
import com.callisdairy.api.response.AddToIntrestedResponse
import com.callisdairy.api.response.CountryResponse
import com.callisdairy.api.response.LikeUnlikeProductsResponse
import com.callisdairy.api.response.ListCategoryResponse
import com.callisdairy.api.response.ProductListResponse
import com.callisdairy.api.response.SubCategoryResponse
import com.google.gson.GsonBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MarketProductViewAllViewModel @Inject constructor(app: Application, private val repo: CalisRespository,private val networkHelper: NetworkHelper) : AndroidViewModel(app){

    private val listCategoryData: MutableStateFlow<Resource<ListCategoryResponse>> = MutableStateFlow(
        Resource.Empty())
    val _listCategoryData: StateFlow<Resource<ListCategoryResponse>> = listCategoryData

    private val productListData: MutableStateFlow<Resource<ProductListResponse>> = MutableStateFlow(Resource.Empty())
    val _productListData: StateFlow<Resource<ProductListResponse>> = productListData

    private val listSubCategoryData: MutableStateFlow<Resource<SubCategoryResponse>> = MutableStateFlow(Resource.Empty())
    val _listSubCategoryData: StateFlow<Resource<SubCategoryResponse>> =  listSubCategoryData

    private val likeData: MutableStateFlow<Resource<LikeUnlikeProductsResponse>> = MutableStateFlow(Resource.Empty())
    val _likeData: StateFlow<Resource<LikeUnlikeProductsResponse>> = likeData

    private val addToInterestedData: MutableStateFlow<Resource<AddToIntrestedResponse>> = MutableStateFlow(Resource.Empty())
    val _addToInterestedData: StateFlow<Resource<AddToIntrestedResponse>> = addToInterestedData




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
            listCategoryData.value = Resource.Error(Constants.NO_INTERNET)
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



//    List Sub Category


    fun listSubCategoryApi(categoryId:String) = viewModelScope.launch {
        listSubCategoryData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.listSubCategoryApi(categoryId)
                .catch { e ->
                    listSubCategoryData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    listSubCategoryData.value = lisSubCategoryResponseHandle(data)
                }
        }else{
            listSubCategoryData.value = Resource.Error(Constants.NO_INTERNET)
        }

    }

    private fun lisSubCategoryResponseHandle(response: Response<SubCategoryResponse>): Resource<SubCategoryResponse> {
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

    fun productListApi(token:String,lat:Double,lng:Double,maxDistance:Int,categoryId:String,subCategoryId:String,page:Int,limit:Int,radius : String,
                       country : String,
                       state : String,
                       city : String,search: String) = viewModelScope.launch {
        productListData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.listProductV2Api(token,lat, lng, maxDistance, categoryId, subCategoryId, page, limit,radius, country, state, city,search)
                .catch { e ->
                    productListData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    productListData.value = productListResponseHandle(data)
                }
        }else{
            productListData.value = Resource.Error(Constants.NO_INTERNET)
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