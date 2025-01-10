package com.callisdairy.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.callisdairy.ModalClass.PojoClass
import com.callisdairy.Repositry.CalisRespository
import com.callisdairy.Utils.NetworkHelper
import com.callisdairy.Utils.Resource
import com.callisdairy.api.Constants.NO_INTERNET
import com.callisdairy.api.request.SignUpRequest
import com.callisdairy.api.request.SignUpRequestVendor
import com.callisdairy.api.request.SignUpRequestVendorDoctor
import com.callisdairy.api.response.AddToIntrestedResponse
import com.callisdairy.api.response.CategoryBasedResponse
import com.callisdairy.api.response.CountryResponse
import com.callisdairy.api.response.ImageUploadResponse
import com.callisdairy.api.response.SignUpResponse
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
class SignUpViewModel @Inject constructor(app: Application, private val repo: CalisRespository,private val networkHelper: NetworkHelper) : AndroidViewModel(app) {

    private val SignInStateFlow: MutableStateFlow<Resource<SignUpResponse>> = MutableStateFlow(Resource.Empty())
    val _signInStateFlow: StateFlow<Resource<SignUpResponse>> = SignInStateFlow


    private val countryStateFlow: MutableStateFlow<Resource<CountryResponse>> = MutableStateFlow(Resource.Empty())
    val _countryStateFlow: StateFlow<Resource<CountryResponse>> = countryStateFlow


    private val stateData: MutableStateFlow<Resource<CountryResponse>> = MutableStateFlow(Resource.Empty())
    val _stateData: StateFlow<Resource<CountryResponse>> = stateData


    private val citydata: MutableStateFlow<Resource<CountryResponse>> = MutableStateFlow(Resource.Empty())
    val _citydata: StateFlow<Resource<CountryResponse>> = citydata


    private val petBreeddata: MutableStateFlow<Resource<CountryResponse>> = MutableStateFlow(Resource.Empty())
    val _petBreeddata: StateFlow<Resource<CountryResponse>> = petBreeddata


    private val petCategoryData: MutableStateFlow<Resource<CountryResponse>> = MutableStateFlow(Resource.Empty())
    val _petCategoryData: StateFlow<Resource<CountryResponse>> = petCategoryData


    private val petCategoryListData: MutableStateFlow<Resource<CategoryBasedResponse>> = MutableStateFlow(Resource.Empty())
    val _petCategoryListData: StateFlow<Resource<CategoryBasedResponse>> = petCategoryListData




    private val addPetProfileData: MutableStateFlow<Resource<AddToIntrestedResponse>> = MutableStateFlow(Resource.Empty())
    val _addPetProfileData: StateFlow<Resource<AddToIntrestedResponse>> = addPetProfileData


    private val uploadImagesData: MutableStateFlow<Resource<ImageUploadResponse>> = MutableStateFlow(Resource.Empty())
    val _uploadImagesData: StateFlow<Resource<ImageUploadResponse>> = uploadImagesData




//    Signup Api


    fun signInApi(request: SignUpRequest) = viewModelScope.launch {
        SignInStateFlow.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.signUpAPi(request)
                .catch { e ->
                    SignInStateFlow.value = Resource.Error(e.message.toString())
                }.collect { it ->
                    SignInStateFlow.value = signInResponseHandle(it)
                }
        }else{
            SignInStateFlow.value = Resource.Error(NO_INTERNET)
        }

    }

//    Signup Api Vendor or Doctor


    fun signInApiVendor(request: SignUpRequestVendor) = viewModelScope.launch {
        SignInStateFlow.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.signInApiVendor(request)
                .catch { e ->
                    SignInStateFlow.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    SignInStateFlow.value = signInResponseHandle(data)
                }
        }else{
            SignInStateFlow.value = Resource.Error(NO_INTERNET)
        }

    }
    fun signInApiVendorDoctor(request: SignUpRequestVendorDoctor) = viewModelScope.launch {
        SignInStateFlow.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.signInApiVendorDoctor(request)
                .catch { e ->
                    SignInStateFlow.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    SignInStateFlow.value = signInResponseHandle(data)
                }
        }else{
            SignInStateFlow.value = Resource.Error(NO_INTERNET)
        }

    }


    private fun signInResponseHandle(response: Response<SignUpResponse>): Resource<SignUpResponse> {
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



//     Get Pet Category  List

    fun petCategoryApi(petTypeId :String) = viewModelScope.launch {
        petCategoryListData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.petCategoryDetailsApi(petTypeId)
                .catch { e ->
                    petCategoryListData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    petCategoryListData.value = petCategoryListResponseHandle(data)
                }
        }else{
            petCategoryListData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun petCategoryListResponseHandle(response: Response<CategoryBasedResponse>): Resource<CategoryBasedResponse> {
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


//    Add Pert Profile Api


    fun addPetProfileApi(
        token: String, petCategoryType: String,
        name: String, userProfileImage: String, petCategoryId: String, petBreedId: String
    ) = viewModelScope.launch {
        addPetProfileData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()) {

            repo.addUserProfileApi(token, petCategoryType, name, userProfileImage, petCategoryId,petBreedId)
                .catch { e ->
                    addPetProfileData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    addPetProfileData.value = petProfileResponseHandle(data)
                }
        }else{
            addPetProfileData.value = Resource.Error(NO_INTERNET)
        }





    }

    private fun petProfileResponseHandle(response: Response<AddToIntrestedResponse>): Resource<AddToIntrestedResponse> {
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