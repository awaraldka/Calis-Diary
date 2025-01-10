package com.callisdairy.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.callisdairy.ModalClass.PojoClass
import com.callisdairy.Repositry.CalisRespository
import com.callisdairy.Utils.NetworkHelper
import com.callisdairy.Utils.Resource
import com.callisdairy.api.Constants
import com.callisdairy.api.response.SearchPeopleResponse
import com.google.gson.GsonBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class peopleSearchViewModel @Inject constructor(app: Application, private val repo: CalisRespository,private val networkHelper: NetworkHelper) : AndroidViewModel(app) {

    private val searchPeopleData: MutableStateFlow<Resource<SearchPeopleResponse>> = MutableStateFlow(
        Resource.Empty())
    val _serach_PeopleData: StateFlow<Resource<SearchPeopleResponse>> = searchPeopleData



    //    Signup Api


    fun searchPeople(token:String, search:String) = viewModelScope.launch {
        searchPeopleData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.peopleSearchApi(token,search)
                .catch { e ->
                    searchPeopleData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    searchPeopleData.value = searchPeopleResponseHandle(data)
                }
        }else{
            searchPeopleData.value = Resource.Error(Constants.NO_INTERNET)
        }

    }

    private fun searchPeopleResponseHandle(response: Response<SearchPeopleResponse>): Resource<SearchPeopleResponse> {
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