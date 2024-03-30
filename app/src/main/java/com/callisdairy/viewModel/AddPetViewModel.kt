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
import com.callisdairy.api.request.AddPetRequest
import com.callisdairy.api.request.UpdatePetRequest
import com.callisdairy.api.response.*
import com.google.gson.GsonBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class AddPetViewModel @Inject constructor(app: Application, private val repo: CalisRespository,private val networkHelper: NetworkHelper) : AndroidViewModel(app) {

    private val addPetData: MutableStateFlow<Resource<AddToIntrestedResponse>> = MutableStateFlow(Resource.Empty())
    val _addPetData : StateFlow<Resource<AddToIntrestedResponse>> = addPetData

    private val updatePetData: MutableStateFlow<Resource<AddToIntrestedResponse>> = MutableStateFlow(Resource.Empty())
    val _updatePetData : StateFlow<Resource<AddToIntrestedResponse>> = updatePetData


    private val uploadImagesData: MutableStateFlow<Resource<ImageUploadResponse>> = MutableStateFlow(Resource.Empty())
    val _uploadImagesData: StateFlow<Resource<ImageUploadResponse>> = uploadImagesData

    private val categoryData: MutableStateFlow<Resource<PetCategoryListResponse>> = MutableStateFlow(Resource.Empty())
    val _categoryData: StateFlow<Resource<PetCategoryListResponse>> = categoryData


    private val listSubCategoryData: MutableStateFlow<Resource<PetCategoryListResponse>> = MutableStateFlow(Resource.Empty())
    val _listSubCategoryData: StateFlow<Resource<PetCategoryListResponse>> =  listSubCategoryData


    private val petDescriptionData: MutableStateFlow<Resource<ViewPetResponse>> = MutableStateFlow(Resource.Empty())
    val _petDescriptionData: StateFlow<Resource<ViewPetResponse>> = petDescriptionData

    private val petCategoryData: MutableStateFlow<Resource<CountryResponse>> = MutableStateFlow(Resource.Empty())
    val _petCategoryData: StateFlow<Resource<CountryResponse>> = petCategoryData


    private val petBreeddata: MutableStateFlow<Resource<CountryResponse>> = MutableStateFlow(Resource.Empty())
    val _petBreeddata: StateFlow<Resource<CountryResponse>> = petBreeddata


    //    add Api


    fun addPetAPi(token:String,request: AddPetRequest) = viewModelScope.launch {
        addPetData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.addPetApi(token,request)
                .catch { e ->
                    addPetData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    addPetData.value = addPetResponseHandle(data)
                }
        }else{
            addPetData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun addPetResponseHandle(response: Response<AddToIntrestedResponse>): Resource<AddToIntrestedResponse> {
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


//    Update pet Api




    fun updatePetAPi(token: String, petId: String, request: UpdatePetRequest) = viewModelScope.launch {
        addPetData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.updatePetApi(token,petId,request)
                .catch { e ->
                    updatePetData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    updatePetData.value = updatePetResponseHandle(data)
                }
        }else{
            updatePetData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun updatePetResponseHandle(response: Response<AddToIntrestedResponse>): Resource<AddToIntrestedResponse> {
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




    //     UploadMultipleImages Api


    fun uploadMultipleImagesApi(file : ArrayList<MultipartBody.Part>) = viewModelScope.launch {
        uploadImagesData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.uploadMultipleFile(file)
                .catch { e ->
                    uploadImagesData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    uploadImagesData.value = uploadMultipleImagesHandle(data)
                }
        }else{
            uploadImagesData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun uploadMultipleImagesHandle(response: Response<ImageUploadResponse>): Resource<ImageUploadResponse> {
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




    //     category list Api


    fun categoryListApi(type:String) = viewModelScope.launch {
        categoryData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.listCategoryApi(type)
                .catch { e ->
                    categoryData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    categoryData.value = categoryListHandle(data)
                }
        }else{
            categoryData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun categoryListHandle(response: Response<PetCategoryListResponse>): Resource<PetCategoryListResponse> {
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

            repo.listSubCategoryApis(categoryId)
                .catch { e ->
                    listSubCategoryData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    listSubCategoryData.value = lisSubCategoryResponseHandle(data)
                }
        }else{
            listSubCategoryData.value = Resource.Error(Constants.NO_INTERNET)
        }

    }

    private fun lisSubCategoryResponseHandle(response: Response<PetCategoryListResponse>): Resource<PetCategoryListResponse> {
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


    //    petDescriptionApi

    fun petDescriptionApi(token:String,id:String) = viewModelScope.launch {
        petDescriptionData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.viewPetApi(token,id)
                .catch { e ->
                    petDescriptionData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    petDescriptionData.value = viewPetResponseHandle(data)
                }
        }else{
            petDescriptionData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun viewPetResponseHandle(response: Response<ViewPetResponse>): Resource<ViewPetResponse> {
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



//     Get Pet Type  List

    fun getPetCategoryApi() = viewModelScope.launch {
        petCategoryData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.petCategoryApi()
                .catch { e ->
                    petCategoryData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    petCategoryData.value = petCategoryResponseHandle(data)
                }
        }else{
            petCategoryData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun petCategoryResponseHandle(response: Response<CountryResponse>): Resource<CountryResponse> {
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