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
import com.callisdairy.api.response.CommentListResponse
import com.callisdairy.api.response.HomePageListResponse
import com.callisdairy.api.response.RepliesListResponse
import com.callisdairy.api.response.updateCommentResponse
import com.google.gson.GsonBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class CommentsViewModel @Inject constructor(app: Application, private val repo: CalisRespository,private val networkHelper: NetworkHelper) : AndroidViewModel(app) {

    private val commentsListData: MutableStateFlow<Resource<CommentListResponse>> = MutableStateFlow(Resource.Empty())
    val _commentsListData: StateFlow<Resource<CommentListResponse>> = commentsListData

    private val addCommentData: MutableStateFlow<Resource<HomePageListResponse>> = MutableStateFlow(Resource.Empty())
    val _addCommentData: StateFlow<Resource<HomePageListResponse>> = addCommentData

    private val updateCommentData: MutableStateFlow<Resource<updateCommentResponse>> = MutableStateFlow(Resource.Empty())
    val update_commentData: StateFlow<Resource<updateCommentResponse>> = updateCommentData

    private val viewRepliedCommentData: MutableStateFlow<Resource<RepliesListResponse>> = MutableStateFlow(Resource.Empty())
    val _viewRepliedCommentData: StateFlow<Resource<RepliesListResponse>> = viewRepliedCommentData

    private val addLikeData: MutableStateFlow<Resource<AddLikeResponse>> = MutableStateFlow(Resource.Empty())
    val _addLikeData: StateFlow<Resource<AddLikeResponse>> = addLikeData

    private val deleteCommentData: MutableStateFlow<Resource<AddEventResponse>> = MutableStateFlow(Resource.Empty())
    val deleteComment_data: StateFlow<Resource<AddEventResponse>> = deleteCommentData







   //    Comment List Api


    fun commentListApi(token: String,postId:String,page:Int,limit:Int) = viewModelScope.launch {
        commentsListData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.commentListApi(token, postId, page, limit)
                .catch { e ->
                    commentsListData.value = Resource.Error(e.message.toString())
                }.collectLatest { data ->
                    commentsListData.value = commentResponseHandle(data)
                }
        }else{
            commentsListData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun commentResponseHandle(response: Response<CommentListResponse>): Resource<CommentListResponse> {
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


//  Add Comment and Reply of a comment Api

    fun addCommentApi(token: String,typeOfPost :String,
                      postId :String,comment :String, commentId :String,reply :String) = viewModelScope.launch {
        addCommentData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.addCommentApi(token, typeOfPost, postId, comment, commentId, reply)
                .catch { e ->
                    addCommentData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    addCommentData.value = addCommentResponseHandle(data)
                }
        }else{
            addCommentData.value = Resource.Error(NO_INTERNET)
        }

    }
    private fun addCommentResponseHandle(response: Response<HomePageListResponse>): Resource<HomePageListResponse> {
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



//  Update Comment and Reply of a comment Api

    fun updateCommentApi(token: String,typeOfPost :String,
                      postId :String,comment :String, commentId :String,reply :String) = viewModelScope.launch {
        updateCommentData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.updateCommentApi(token, typeOfPost, postId, comment, commentId, reply)
                .catch { e ->
                    updateCommentData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    updateCommentData.value = updateCommentResponseHandle(data)
                }
        }else{
            updateCommentData.value = Resource.Error(NO_INTERNET)
        }

    }
    private fun updateCommentResponseHandle(response: Response<updateCommentResponse>): Resource<updateCommentResponse> {
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


//  replied Comment list Api

    fun repliedCommentApi(token:String,_id: String,page:Int,limit:Int) = viewModelScope.launch {
        viewRepliedCommentData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.repliesListApi(token,_id,page,limit)
                .catch { e ->
                        viewRepliedCommentData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    viewRepliedCommentData.value = repliedCommentResponseHandle(data)
                }
        }else{
            viewRepliedCommentData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun repliedCommentResponseHandle(response: Response<RepliesListResponse>): Resource<RepliesListResponse> {
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

    fun addLikeApi(token:String,typeOfPost :String,postId :String, commentId:String) = viewModelScope.launch {
        addLikeData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.addLikeApi(token, typeOfPost, postId, commentId)
                .catch { e ->
                    addLikeData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    addLikeData.value = addLikeResponseHandle(data)
                }
        }else{
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



//     Delete Comment Api

    fun deleteCommentApi(token: String,typeOfPost :String,
                         postId :String,comment :String, commentId :String,reply :String) = viewModelScope.launch {
        deleteCommentData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.deleteCommentApi(token, typeOfPost, postId, comment, commentId, reply)
                .catch { e ->
                    deleteCommentData.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    deleteCommentData.value = deleteCommentResponseHandle(data)
                }
        }else{
            deleteCommentData.value = Resource.Error(NO_INTERNET)
        }

    }
    private fun deleteCommentResponseHandle(response: Response<AddEventResponse>): Resource<AddEventResponse> {
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