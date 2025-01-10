package com.callisdairy.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.callisdairy.ModalClass.PojoClass
import com.callisdairy.Repositry.CalisRespository
import com.callisdairy.Utils.NetworkHelper
import com.callisdairy.Utils.Resource
import com.callisdairy.api.Constants.NO_INTERNET
import com.callisdairy.api.request.AddMissingPetRequest
import com.callisdairy.api.response.AddToIntrestedResponse
import com.callisdairy.api.response.CountryResponse
import com.callisdairy.api.response.ImageUploadResponse
import com.callisdairy.api.response.MyPetListResponse
import com.callisdairy.api.response.ViewMissingPetResponse
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
class AddMissingPetViewModel @Inject constructor(app: Application, private val repo: CalisRespository,private val networkHelper: NetworkHelper) : AndroidViewModel(app) {

    private val addMissingPetData: MutableStateFlow<Resource<AddToIntrestedResponse>> = MutableStateFlow(Resource.Empty())
    val _addMissingPetData: StateFlow<Resource<AddToIntrestedResponse>> = addMissingPetData


    private val viewMissingPetData: MutableStateFlow<Resource<ViewMissingPetResponse>> = MutableStateFlow(Resource.Empty())
    val _viewMissingPetData: StateFlow<Resource<ViewMissingPetResponse>> = viewMissingPetData


    private val editMissingPetData: MutableStateFlow<Resource<AddToIntrestedResponse>> = MutableStateFlow(Resource.Empty())
    val _editMissingPetData: StateFlow<Resource<AddToIntrestedResponse>> = editMissingPetData


    private val uploadImagesData: MutableStateFlow<Resource<ImageUploadResponse>> = MutableStateFlow(Resource.Empty())
    val _uploadImagesData: StateFlow<Resource<ImageUploadResponse>> = uploadImagesData


    private val petCategoryData: MutableStateFlow<Resource<CountryResponse>> = MutableStateFlow(Resource.Empty())
    val _petCategoryData: StateFlow<Resource<CountryResponse>> = petCategoryData

    private val petBreeddata: MutableStateFlow<Resource<CountryResponse>> = MutableStateFlow(Resource.Empty())
    val _petBreeddata: StateFlow<Resource<CountryResponse>> = petBreeddata


    private val myPetData: MutableStateFlow<Resource<MyPetListResponse>> = MutableStateFlow(Resource.Empty())
    val _myPetData: StateFlow<Resource<MyPetListResponse>> = myPetData


   //    Add Missing Pet Api


    fun addMissingPetApi(token: String,request: AddMissingPetRequest) = viewModelScope.launch {
        addMissingPetData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.addMissingPetApi(token, request)
                .catch { e ->
                    addMissingPetData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    addMissingPetData.value = addMissingPetResponseHandle(data)
                }
        }else{
            addMissingPetData.value = Resource.Error(NO_INTERNET)
        }

    }
    private fun addMissingPetResponseHandle(response: Response<AddToIntrestedResponse>): Resource<AddToIntrestedResponse> {
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


    //  View  Missing Pet Api


    fun viewMissingPetApi(token:String,petId: String) = viewModelScope.launch {
        viewMissingPetData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.viewMissingPetApi(token, petId)
                .catch { e ->
                    viewMissingPetData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    viewMissingPetData.value = viewMissingPetResponseHandle(data)
                }
        }else{
            viewMissingPetData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun viewMissingPetResponseHandle(response: Response<ViewMissingPetResponse>): Resource<ViewMissingPetResponse> {
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



//    Edit Missing Pet

    fun editMissingPetApi(token: String,id:String,request: AddMissingPetRequest) = viewModelScope.launch {
        editMissingPetData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.editMissingPetApi(token,id, request)
                .catch { e ->
                    editMissingPetData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    editMissingPetData.value = editMissingPetResponseHandle(data)
                }
        }else{
            editMissingPetData.value = Resource.Error(NO_INTERNET)
        }

    }
    private fun editMissingPetResponseHandle(response: Response<AddToIntrestedResponse>): Resource<AddToIntrestedResponse> {
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


    fun myPetApi(token:String,search: String,page:Int, limit:Int,fromDate: String,toDate: String,publishStatus:String) = viewModelScope.launch {
        myPetData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.myPetListApi(token,search,page, limit,fromDate, toDate,publishStatus)
                .catch { e ->
                    myPetData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    myPetData.value = myPetResponseHandle(data)
                }
        }else{
            myPetData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun myPetResponseHandle(response: Response<MyPetListResponse>): Resource<MyPetListResponse> {
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