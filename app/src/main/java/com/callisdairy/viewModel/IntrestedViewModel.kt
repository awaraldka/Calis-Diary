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
import com.callisdairy.api.response.IntrestedPetResponse
import com.callisdairy.api.response.LikeUnlikeProductsResponse
import com.google.gson.GsonBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class IntrestedViewModel @Inject constructor(app: Application, private val repo: CalisRespository,private val networkHelper: NetworkHelper) : AndroidViewModel(app){

    private val intrestedPetData: MutableStateFlow<Resource<IntrestedPetResponse>> = MutableStateFlow(Resource.Empty())
    val _intrestedPetData: StateFlow<Resource<IntrestedPetResponse>> = intrestedPetData

    private val intrestedProductData: MutableStateFlow<Resource<IntrestedPetResponse>> = MutableStateFlow(Resource.Empty())
    val _intrestedProductData: StateFlow<Resource<IntrestedPetResponse>> = intrestedProductData

    private val intrestedServiceData: MutableStateFlow<Resource<IntrestedPetResponse>> = MutableStateFlow(Resource.Empty())
    val _intrestedServiceData: StateFlow<Resource<IntrestedPetResponse>> = intrestedServiceData

    private val likeData: MutableStateFlow<Resource<LikeUnlikeProductsResponse>> = MutableStateFlow(Resource.Empty())
    val _likeData: StateFlow<Resource<LikeUnlikeProductsResponse>> = likeData

    private val likeService: MutableStateFlow<Resource<LikeUnlikeProductsResponse>> = MutableStateFlow(Resource.Empty())
    val _likeService: StateFlow<Resource<LikeUnlikeProductsResponse>> = likeService

    private val likePet: MutableStateFlow<Resource<LikeUnlikeProductsResponse>> = MutableStateFlow(Resource.Empty())
    val _likePet: StateFlow<Resource<LikeUnlikeProductsResponse>> = likePet

    private val addToInterestedData: MutableStateFlow<Resource<AddToIntrestedResponse>> = MutableStateFlow(Resource.Empty())
    val _addToInterestedData: StateFlow<Resource<AddToIntrestedResponse>> = addToInterestedData

//    IntrestedPet Api


    fun intrestedPetApi(token:String,type:String,page:Int,limit:Int) = viewModelScope.launch {
        intrestedPetData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.listInterestedApi(token, type, page, limit)
                .catch { e ->
                    intrestedPetData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    intrestedPetData.value = intrestedPetListResponseHandle(data)
                }
        }else{
            intrestedPetData.value = Resource.Error(Constants.NO_INTERNET)
        }

    }
    private fun intrestedPetListResponseHandle(response: Response<IntrestedPetResponse>): Resource<IntrestedPetResponse> {
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


//    Intrested Product Api

    fun intrestedProductApi(token:String,type:String,page:Int,limit:Int) = viewModelScope.launch {
        intrestedProductData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.listInterestedApi(token, type, page, limit)
                .catch { e ->
                    intrestedProductData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    intrestedProductData.value = intrestedProductListResponseHandle(data)
                }
        }else{
            intrestedProductData.value = Resource.Error(Constants.NO_INTERNET)
        }

    }
    private fun intrestedProductListResponseHandle(response: Response<IntrestedPetResponse>): Resource<IntrestedPetResponse> {
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


//    Intrested Service Api

    fun intrestedServiceApi(token:String,type:String,page:Int,limit:Int) = viewModelScope.launch {
        intrestedServiceData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.listInterestedApi(token, type, page, limit)
                .catch { e ->
                    intrestedServiceData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    intrestedServiceData.value = intrestedServiceListResponseHandle(data)
                }
        }else{
            intrestedServiceData.value = Resource.Error(Constants.NO_INTERNET)
        }

    }
    private fun intrestedServiceListResponseHandle(response: Response<IntrestedPetResponse>): Resource<IntrestedPetResponse> {
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