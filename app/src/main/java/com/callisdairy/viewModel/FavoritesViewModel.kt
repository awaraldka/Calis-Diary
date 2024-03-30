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
class FavoritesViewModel @Inject constructor(app: Application, private val repo: CalisRespository,private val networkHelper: NetworkHelper) : AndroidViewModel(app){

    private val FavoritesPetData: MutableStateFlow<Resource<FavPetResponse>> = MutableStateFlow(Resource.Empty())
    val _FavoritesPetData: StateFlow<Resource<FavPetResponse>> = FavoritesPetData

    private val FavoritesServiceData: MutableStateFlow<Resource<FavServiceResponse>> = MutableStateFlow(Resource.Empty())
    val _FavoritesServiceData: StateFlow<Resource<FavServiceResponse>> = FavoritesServiceData

    private val FavoritesProductData: MutableStateFlow<Resource<FavProductsResponse>> = MutableStateFlow(Resource.Empty())
    val _FavoritesProductData: StateFlow<Resource<FavProductsResponse>> = FavoritesProductData

    private val likeData: MutableStateFlow<Resource<LikeUnlikeProductsResponse>> = MutableStateFlow(Resource.Empty())
    val _likeData: StateFlow<Resource<LikeUnlikeProductsResponse>> = likeData

    private val likeService: MutableStateFlow<Resource<LikeUnlikeProductsResponse>> = MutableStateFlow(Resource.Empty())
    val _likeService: StateFlow<Resource<LikeUnlikeProductsResponse>> = likeService

    private val likePet: MutableStateFlow<Resource<LikeUnlikeProductsResponse>> = MutableStateFlow(Resource.Empty())
    val _likePet: StateFlow<Resource<LikeUnlikeProductsResponse>> = likePet


    private val addToInterestedData: MutableStateFlow<Resource<AddToIntrestedResponse>> = MutableStateFlow(Resource.Empty())
    val _addToInterestedData: StateFlow<Resource<AddToIntrestedResponse>> = addToInterestedData


//    FavoritesPet Api


    fun favoritesPetApi(token:String,page:Int,limit:Int) = viewModelScope.launch {
        FavoritesPetData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.myFavPets(token, page, limit)
                .catch { e ->
                    FavoritesPetData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    FavoritesPetData.value = favoritesPetListResponseHandle(data)
                }
        }else{
            FavoritesPetData.value = Resource.Error(Constants.NO_INTERNET)
        }

    }

    private fun favoritesPetListResponseHandle(response: Response<FavPetResponse>): Resource<FavPetResponse> {
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


//    Fav Service Api

    fun favoritesServiceApi(token:String,page:Int,limit:Int) = viewModelScope.launch {
        FavoritesServiceData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.myLikesServicesApi(token, page, limit)
                .catch { e ->
                    FavoritesServiceData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    FavoritesServiceData.value = favoritesServiceResponseHandle(data)
                }
        }else{
            FavoritesServiceData.value = Resource.Error(Constants.NO_INTERNET)
        }

    }

    private fun favoritesServiceResponseHandle(response: Response<FavServiceResponse>): Resource<FavServiceResponse> {
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


//    FavProduct Api


    fun favoritesProductApi(token:String,page:Int,limit:Int) = viewModelScope.launch {
        FavoritesProductData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.myLikesProductApi(token, page, limit)
                .catch { e ->
                    FavoritesProductData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    FavoritesProductData.value = favoritesProductResponseHandle(data)
                }
        }else{
            FavoritesProductData.value = Resource.Error(Constants.NO_INTERNET)
        }

    }

    private fun favoritesProductResponseHandle(response: Response<FavProductsResponse>): Resource<FavProductsResponse> {
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









   


}