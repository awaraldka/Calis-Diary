package com.callisdairy.Repositry


import com.callisdairy.api.Interface_Implement
import com.callisdairy.api.request.*
import com.callisdairy.api.response.*
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Inject

class CalisRespository @Inject constructor(private val apiServiceImpl: Interface_Implement) {


    fun signUpAPi(request: SignUpRequest): Flow<Response<SignUpResponse>> = flow {
        emit(apiServiceImpl.signUpAPi(request))
    }.flowOn(Dispatchers.IO)

    fun signInApiVendor(request: SignUpRequestVendor): Flow<Response<SignUpResponse>> = flow {
        emit(apiServiceImpl.signInApiVendor(request))
    }.flowOn(Dispatchers.IO)

    fun signInApiVendorDoctor(request: SignUpRequestVendorDoctor): Flow<Response<SignUpResponse>> = flow {
        emit(apiServiceImpl.signInApiVendorDoctor(request))
    }.flowOn(Dispatchers.IO)

    fun getAllCountryApi(): Flow<Response<CountryResponse>> = flow {
        emit(apiServiceImpl.getAllCountryApi())
    }.flowOn(Dispatchers.IO)


    fun petCategoryApi(): Flow<Response<CountryResponse>> = flow {
        emit(apiServiceImpl.petCategoryApi())
    }.flowOn(Dispatchers.IO)


    fun getAllStateApi(countryCode: String): Flow<Response<CountryResponse>> = flow {
        emit(apiServiceImpl.getAllStateApi(countryCode))
    }.flowOn(Dispatchers.IO)


    fun getAllCityApi(countryCode: String, stateCode: String): Flow<Response<CountryResponse>> =
        flow {
            emit(apiServiceImpl.getAllCityApi(countryCode, stateCode))
        }.flowOn(Dispatchers.IO)


    fun login(request: LoginRequest): Flow<Response<LogInResponse>> = flow {
        emit(apiServiceImpl.login(request))
    }.flowOn(Dispatchers.IO)

    fun socialLogin(request: JsonObject): Flow<Response<LogInResponse>> = flow {
        emit(apiServiceImpl.socialLogin(request))
    }.flowOn(Dispatchers.IO)

    fun verifyOTPApi(jsonObject: JsonObject): Flow<Response<VerifyOtpResponse>> = flow {
        emit(apiServiceImpl.verifyOTPApi(jsonObject))
    }.flowOn(Dispatchers.IO)


    fun resendOTPApi(jsonObject: JsonObject): Flow<Response<VerifyOtpResponse>> = flow {
        emit(apiServiceImpl.resendOTPApi(jsonObject))
    }.flowOn(Dispatchers.IO)


    fun forgotPasswordApi(jsonObject: JsonObject): Flow<Response<SignUpResponse>> = flow {
        emit(apiServiceImpl.forgotPasswordApi(jsonObject))
    }.flowOn(Dispatchers.IO)


    fun resetPasswordApi(
        token: String,
        password: String,
        confirmPassword: String
    ): Flow<Response<SignUpResponse>> = flow {
        emit(apiServiceImpl.resetPasswordApi(token, password, confirmPassword))
    }.flowOn(Dispatchers.IO)


    fun tagPeopleListApi(token: String, search: String): Flow<Response<TagPeopleResponse>> = flow {
        emit(apiServiceImpl.tagPeopleListApi(token, search))
    }.flowOn(Dispatchers.IO)


    fun listCategoryApi(
        categoryTypes: String,
        page: Int,
        limit: Int
    ): Flow<Response<ListCategoryResponse>> = flow {
        emit(apiServiceImpl.listCategoryApi(categoryTypes, page, limit))
    }.flowOn(Dispatchers.IO)


    fun listProductApi(status: String): Flow<Response<ProductListResponse>> = flow {
        emit(apiServiceImpl.listProductApi(status))
    }.flowOn(Dispatchers.IO)


    fun changePassword(token: String, jsonObject: JsonObject): Flow<Response<SignUpResponse>> =
        flow {
            emit(apiServiceImpl.changePassword(token, jsonObject))
        }.flowOn(Dispatchers.IO)


    fun addPetApi(token: String, request: AddPetRequest): Flow<Response<AddToIntrestedResponse>> =
        flow {
            emit(apiServiceImpl.addPetApi(token, request))
        }.flowOn(Dispatchers.IO)


    fun updatePetApi(
        token: String,
        petId: String,
        request: UpdatePetRequest
    ): Flow<Response<AddToIntrestedResponse>> = flow {
        emit(apiServiceImpl.updatePetApi(token, petId, request))
    }.flowOn(Dispatchers.IO)


    fun addPostApi(token: String,file: ArrayList<MultipartBody.Part>,caption:String,requestMetaWords:ArrayList<String>,
                   requestTagPeople:ArrayList<String>,latitude:Double,longitude:Double,LocationName:String): Flow<Response<AddPostResponse>> = flow {
        emit(apiServiceImpl.addPostApi(token,file,caption,requestMetaWords,requestTagPeople,latitude,longitude,LocationName))
    }.flowOn(Dispatchers.IO)


    fun editProfileApi(
        token: String,
        request: EditProfileRequest
    ): Flow<Response<EditProfileResponse>> = flow {
        emit(apiServiceImpl.editProfileApi(token, request))
    }.flowOn(Dispatchers.IO)

    fun editProfileVendorApi(
        token: String,
        request: EditProfileVendorRequest
    ): Flow<Response<EditProfileResponse>> = flow {
        emit(apiServiceImpl.editProfileVendorApi(token, request))
    }.flowOn(Dispatchers.IO)


    fun getProfileApi(token: String): Flow<Response<EditProfileResponse>> = flow {
        emit(apiServiceImpl.getProfileApi(token))
    }.flowOn(Dispatchers.IO)


    fun faqListApi(search:String): Flow<Response<FaqResponse>> = flow {
        emit(apiServiceImpl.faqListApi(search))
    }.flowOn(Dispatchers.IO)


    fun addEventApi(token: String, request: AddEventRequest): Flow<Response<AddEventResponse>> =
        flow {
            emit(apiServiceImpl.addEventApi(token, request))
        }.flowOn(Dispatchers.IO)


    fun updateEventApi(token: String, request: EditEventRequest): Flow<Response<AddEventResponse>> =
        flow {
            emit(apiServiceImpl.updateEventApi(token, request))
        }.flowOn(Dispatchers.IO)


    fun addStoryApi(token: String, file: ArrayList<MultipartBody.Part>,caption:String): Flow<Response<AddStoryResponse>> =
        flow {
            emit(apiServiceImpl.addStoryApi(token, file, caption))
        }.flowOn(Dispatchers.IO)


    fun uploadMultipleFileApi(file: ArrayList<MultipartBody.Part>): Flow<Response<UploadFileResponse>> =
        flow {
            emit(apiServiceImpl.uploadMultipleFileApi(file))
        }.flowOn(Dispatchers.IO)


    fun homePagePostListApi(
        token: String,
        page: Int,
        limit: Int,
        radius: String,
        country: String,
        city: String,
        state: String,
        lat: Double,
        long: Double,
        petBreedId:String
    ): Flow<Response<HomePageListResponse>> = flow {
        emit(apiServiceImpl.homePagePostListApi(token, page, limit,radius, country, city, state, lat, long,petBreedId))
    }.flowOn(Dispatchers.IO)


    fun commentListApi(
        token: String,
        postId: String,
        page: Int,
        limit: Int
    ): Flow<Response<CommentListResponse>> = flow {
        emit(apiServiceImpl.commentListApi(token, postId, page, limit))
    }.flowOn(Dispatchers.IO)


    fun repliesListApi(
        token:String,
        commentId: String,
        page: Int,
        limit: Int
    ): Flow<Response<RepliesListResponse>> = flow {
        emit(apiServiceImpl.repliesListApi(token,commentId, page, limit))
    }.flowOn(Dispatchers.IO)


    fun addCommentApi(
        token: String, typeOfPost: String,
        postId: String, comment: String, commentId: String, reply: String
    ): Flow<Response<HomePageListResponse>> = flow {
        emit(apiServiceImpl.addCommentApi(token, typeOfPost, postId, comment, commentId, reply))
    }.flowOn(Dispatchers.IO)


    fun updateCommentApi(
        token: String, typeOfPost: String,
        postId: String, comment: String, commentId: String, reply: String
    ): Flow<Response<updateCommentResponse>> = flow {
        emit(apiServiceImpl.updateCommentApi(token, typeOfPost, postId, comment, commentId, reply))
    }.flowOn(Dispatchers.IO)

    fun addStoryCommentApi(
        token: String, typeOfPost: String,
        storyId: String, reply: String
    ): Flow<Response<AddEventResponse>> = flow {
        emit(apiServiceImpl.addStoryCommentApi(token, typeOfPost, storyId, reply))
    }.flowOn(Dispatchers.IO)


    fun addLikeApi(
        token: String,
        typeOfPost: String,
        postId: String,
        commentId: String
    ): Flow<Response<AddLikeResponse>> =
        flow { emit(apiServiceImpl.addLikeApi(token, typeOfPost, postId, commentId)) }.flowOn(
            Dispatchers.IO
        )


    fun listInterestedApi(
        token: String,
        type: String,
        page: Int,
        limit: Int
    ): Flow<Response<IntrestedPetResponse>> = flow {
        emit(
            apiServiceImpl.listInterestedApi(
                token,
                type,
                page,
                limit
            )
        )
    }.flowOn(Dispatchers.IO)


    fun viewPetApi(token: String, id: String): Flow<Response<ViewPetResponse>> =
        flow { emit(apiServiceImpl.viewPetApi(token, id)) }.flowOn(Dispatchers.IO)


    fun viewServiceApi(token: String, id: String): Flow<Response<ViewServiceResponse>> =
        flow { emit(apiServiceImpl.viewServiceApi(id, token)) }.flowOn(Dispatchers.IO)


    fun viewProductApi(token: String, id: String): Flow<Response<ViewProductResponse>> =
        flow { emit(apiServiceImpl.viewProductApi(id, token)) }.flowOn(Dispatchers.IO)


    fun likeUnlikeProductApi(
        token: String,
        id: String
    ): Flow<Response<LikeUnlikeProductsResponse>> =
        flow { emit(apiServiceImpl.likeUnlikeProductApi(token, id)) }.flowOn(Dispatchers.IO)


    fun likeUnlikeServicesApi(
        token: String,
        id: String
    ): Flow<Response<LikeUnlikeProductsResponse>> =
        flow { emit(apiServiceImpl.likeUnlikeServicesApi(token, id)) }.flowOn(Dispatchers.IO)


    fun favUnfavPetApi(token: String, id: String): Flow<Response<LikeUnlikeProductsResponse>> =
        flow { emit(apiServiceImpl.favUnfavPetApi(token, id)) }.flowOn(Dispatchers.IO)


    fun myFavPets(token: String, page: Int, limit: Int): Flow<Response<FavPetResponse>> =
        flow { emit(apiServiceImpl.myFavPets(token, page, limit)) }.flowOn(Dispatchers.IO)


    fun myLikesServicesApi(
        token: String,
        page: Int,
        limit: Int
    ): Flow<Response<FavServiceResponse>> =
        flow { emit(apiServiceImpl.myLikesServicesApi(token, page, limit)) }.flowOn(Dispatchers.IO)


    fun myLikesProductApi(
        token: String,
        page: Int,
        limit: Int
    ): Flow<Response<FavProductsResponse>> =
        flow { emit(apiServiceImpl.myLikesProductApi(token, page, limit)) }.flowOn(Dispatchers.IO)


    fun listProductV2Api(
        token: String,
        lat: Double,
        lng: Double,
        maxDistance: Int,
        categoryId: String,
        subCategoryId: String,
        page: Int,
        limit: Int,
        radius : String,
        country : String,
        state : String,
        city : String,search: String
    ): Flow<Response<ProductListResponse>> = flow {
        emit(
            apiServiceImpl.listProductV2Api(
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
        )
    }.flowOn(Dispatchers.IO)


    fun listPetApi(
        token: String,
        page: Int,
        limit: Int,
        lat: Double,
        lng: Double,
        maxDistance: Int,
        radius : String,
        country : String,
        state : String,
        city : String,search: String
    ): Flow<Response<PetListResponse>> =
        flow { emit(apiServiceImpl.listPetApi(token, page, limit, lat, lng, maxDistance,radius, country, state, city,search)) }.flowOn(
            Dispatchers.IO
        )


    fun addToInterestedApi(
        token: String,
        type: String,
        petId: String,
        productId: String,
        serviceId: String
    ): Flow<Response<AddToIntrestedResponse>> = flow {
        emit(
            apiServiceImpl.addToInterestedApi(
                token,
                type,
                petId,
                productId,
                serviceId
            )
        )
    }.flowOn(Dispatchers.IO)


    fun listServiceV2Api(
        token: String, page: Int, limit: Int, categoryId: String,
        subCategoryId: String, lat: Double, lng: Double, maxDistance: Int,radius : String,
        country : String,
        state : String,
        city : String,search: String
    ): Flow<Response<ServiceListResponse>> = flow {
        emit(
            apiServiceImpl.listServiceV2Api(
                token,
                page,
                limit,
                categoryId,
                subCategoryId,
                lat,
                lng,
                maxDistance,radius, country, state, city,search
            )
        )
    }.flowOn(Dispatchers.IO)


    fun listSuggestedUserApi(
        token: String,
        search: String,
        page: Int,
        limit: Int,
        discover:String
    ): Flow<Response<suggestionListResponse>> =
        flow { emit(apiServiceImpl.listSuggestedUserApi(token, search, page, limit,discover)) }.flowOn(
            Dispatchers.IO
        )


    fun listSubCategoryApi(categoryId: String): Flow<Response<SubCategoryResponse>> =
        flow { emit(apiServiceImpl.listSubCategoryApi(categoryId)) }.flowOn(Dispatchers.IO)

    fun listSubCategoryApis(categoryId: String): Flow<Response<PetCategoryListResponse>> =
        flow { emit(apiServiceImpl.listSubCategoryApis(categoryId)) }.flowOn(Dispatchers.IO)


    fun listEventApi(token: String,search: String, page: Int, limit: Int,radius : String,
                     country : String,
                     state : String,
                     city : String, lat: Double, lng: Double,petBreedId:String): Flow<Response<ListEventResponse>> =
        flow { emit(apiServiceImpl.listEventApi(token,search, page, limit,radius, country, state, city,lat,lng,petBreedId)) }.flowOn(Dispatchers.IO)


    fun myListEventApi(
        token: String,
        search: String,
        page: Int,
        limit: Int
    ): Flow<Response<ListEventResponse>> = flow {
        emit(
            apiServiceImpl.myListEventApi(
                token,
                search,
                page,
                limit
            )
        )
    }.flowOn(Dispatchers.IO)


    fun viewEvent(eventId: String): Flow<Response<ViewEventResponse>> =
        flow { emit(apiServiceImpl.viewEvent(eventId)) }.flowOn(Dispatchers.IO)


    fun deleteEventApi(token: String, eventId: String): Flow<Response<AddEventResponse>> =
        flow { emit(apiServiceImpl.deleteEventApi(token, eventId)) }.flowOn(Dispatchers.IO)


    fun missingPetListApi(
        token: String,
        search: String,
        type: String,
        page: Int,
        limit: Int,
        radius : String,
        country : String,
        state : String,
        city : String, lat: Double, lng: Double,petBreedId:String
    ): Flow<Response<MissingPetResponse>> =
        flow { emit(apiServiceImpl.missingPetListApi(token, search, type, page, limit,radius,country,state,city,lat, lng,petBreedId)) }.flowOn(
            Dispatchers.IO
        )


    fun viewMissingPetApi(token: String, petId: String): Flow<Response<ViewMissingPetResponse>> =
        flow { emit(apiServiceImpl.viewMissingPetApi(token, petId)) }.flowOn(Dispatchers.IO)


    fun addMissingPetApi(
        token: String,
        request: AddMissingPetRequest
    ): Flow<Response<AddToIntrestedResponse>> =
        flow { emit(apiServiceImpl.addMissingPetApi(token, request)) }.flowOn(Dispatchers.IO)


    fun editMissingPetApi(
        token: String,
        id: String,
        request: AddMissingPetRequest
    ): Flow<Response<AddToIntrestedResponse>> =
        flow { emit(apiServiceImpl.editMissingPetApi(token, id, request)) }.flowOn(Dispatchers.IO)


    fun peopleSearchApi(token: String, search: String): Flow<Response<SearchPeopleResponse>> =
        flow {
            emit(apiServiceImpl.peopleSearchApi(token, search))
        }.flowOn(Dispatchers.IO)


    fun deleteMissingPetApi(token: String, id: String): Flow<Response<AddEventResponse>> = flow {
        emit(apiServiceImpl.deleteMissingPetApi(token, id))
    }.flowOn(Dispatchers.IO)


    fun viewRewardsPointsApi(token: String): Flow<Response<RewardsPointResponse>> = flow {
        emit(apiServiceImpl.viewRewardsPointsApi(token))
    }.flowOn(Dispatchers.IO)

    fun staticContentApi(type: String): Flow<Response<StaticContentResponse>> = flow {
        emit(apiServiceImpl.staticContentApi(type))
    }.flowOn(Dispatchers.IO)

    fun viewStatusApi(token: String): Flow<Response<ViewStatusResponse>> = flow {
        emit(apiServiceImpl.viewStatusApi(token))
    }.flowOn(Dispatchers.IO)


    fun updateStatusApi(token: String): Flow<Response<AddEventResponse>> = flow {
        emit(apiServiceImpl.updateStatusApi(token))
    }.flowOn(Dispatchers.IO)


    fun followUnfollowUserApi(
        token: String,
        userId: String
    ): Flow<Response<AddToIntrestedResponse>> = flow {
        emit(apiServiceImpl.followUnfollowUserApi(token, userId))
    }.flowOn(Dispatchers.IO)


    fun followingUserListApi(token: String,search: String, page: Int, limit: Int): Flow<Response<FollowingUserListResponse>> = flow {
        emit(apiServiceImpl.followingUserListApi(token,search, page, limit))
    }.flowOn(Dispatchers.IO)


    fun myPostListApi(token: String, page: Int, limit: Int): Flow<Response<MyPostResponse>> = flow {
        emit(apiServiceImpl.myPostListApi(token, page, limit))
    }.flowOn(Dispatchers.IO)


    fun viewPostApi(token: String, postId: String): Flow<Response<ViewPostResponse>> = flow {
        emit(apiServiceImpl.viewPostApi(token, postId))
    }.flowOn(Dispatchers.IO)


    fun myPetListApi(token: String,search: String, page: Int, limit: Int,fromDate: String,toDate: String,publishStatus: String): Flow<Response<MyPetListResponse>> =
        flow {
            emit(apiServiceImpl.myPetListApi(token,search, page, limit,fromDate, toDate,publishStatus))
        }.flowOn(Dispatchers.IO)


    fun othersProfile(token: String, id: String): Flow<Response<OtherUserResponse>> = flow {
        emit(apiServiceImpl.othersProfile(token, id))
    }.flowOn(Dispatchers.IO)


    fun otherUserPetListApi(
        token: String,
        id: String,
        page: Int,
        limit: Int
    ): Flow<Response<MyPetListResponse>> = flow {
        emit(apiServiceImpl.otherUserPetListApi(token, id, page, limit))
    }.flowOn(Dispatchers.IO)


    fun otherUserPostListApi(
        token: String,
        userId: String,
        page: Int,
        limit: Int
    ): Flow<Response<MyPostResponse>> = flow {
        emit(apiServiceImpl.otherUserPostListApi(token, userId, page, limit))
    }.flowOn(Dispatchers.IO)


    fun otherfollowerUserListApi(token: String, id: String,search: String, page: Int, limit: Int): Flow<Response<FollowersListResponse>> =
        flow {
            emit(apiServiceImpl.otherfollowerUserListApi(token, id,search, page, limit))
        }.flowOn(Dispatchers.IO)


    fun followerUserListApi(token: String,search: String, page: Int, limit: Int): Flow<Response<FollowersListResponse>> = flow {
        emit(apiServiceImpl.followerUserListApi(token,search, page, limit))
    }.flowOn(Dispatchers.IO)


    fun otherfollowingUserListApi(
        token: String,
        id: String, page: Int, limit: Int,search: String
    ): Flow<Response<FollowingUserListResponse>> = flow {
        emit(apiServiceImpl.otherfollowingUserListApi(token, id,page, limit, search))
    }.flowOn(Dispatchers.IO)


    fun globalSearchApi(token: String, search: String): Flow<Response<GlobalSearchResponse>> =
        flow {
            emit(apiServiceImpl.globalSearchApi(token, search))
        }.flowOn(Dispatchers.IO)


    fun uploadMultipleFile(file: ArrayList<MultipartBody.Part>): Flow<Response<ImageUploadResponse>> =
        flow {
            emit(apiServiceImpl.uploadMultipleFile(file))
        }.flowOn(Dispatchers.IO)


    fun notificationListApi(token: String,
                            page: Int,
                            limit: Int): Flow<Response<NotificationListResponse>> = flow {
        emit(apiServiceImpl.notificationListApi(token,page, limit))
    }.flowOn(Dispatchers.IO)


    fun clearNotificationApi(
        token: String,
        notificationId: String
    ): Flow<Response<AddEventResponse>> = flow {
        emit(apiServiceImpl.clearNotificationApi(token, notificationId))
    }.flowOn(Dispatchers.IO)


    fun deleteAccount(
        token: String, ): Flow<Response<AddEventResponse>> = flow {
        emit(apiServiceImpl.deleteAccount(token))
    }.flowOn(Dispatchers.IO)

    fun deactivateAccount(
        token: String, ): Flow<Response<AddEventResponse>> = flow {
        emit(apiServiceImpl.deactivateAccount(token))
    }.flowOn(Dispatchers.IO)


    fun listCategoryApi(type: String): Flow<Response<PetCategoryListResponse>> = flow {
        emit(apiServiceImpl.listCategoryApi(type))
    }.flowOn(Dispatchers.IO)


    fun requestListApi(
        token: String,
        page: Int,
        limit: Int
    ): Flow<Response<RequestedListResponse>> = flow {
        emit(apiServiceImpl.requestListApi(token, page, limit))
    }.flowOn(Dispatchers.IO)


    fun acceptOrDenyRequestApi(
        token: String,
        type: String,
        requestId: String
    ): Flow<Response<AddEventResponse>> = flow {
        emit(apiServiceImpl.acceptOrDenyRequestApi(token, type, requestId))
    }.flowOn(Dispatchers.IO)


    fun privateOrPublicAccountApi(token: String): Flow<Response<AddEventResponse>> = flow {
        emit(apiServiceImpl.privateOrPublicAccountApi(token))
    }.flowOn(Dispatchers.IO)


    fun addPetToNFTMarketPlaceApi(
        token: String,
        id: String,
        price: String,
        currency: String
    ): Flow<Response<AddEventResponse>> = flow {
        emit(apiServiceImpl.addPetToNFTMarketPlaceApi(token, id, price,currency))
    }.flowOn(Dispatchers.IO)


    fun removePetToMarketPlaceApi(token: String, id: String): Flow<Response<AddEventResponse>> =
        flow {
            emit(apiServiceImpl.removePetToMarketPlaceApi(token, id))
        }.flowOn(Dispatchers.IO)


    fun removePetApi(token: String, id: String): Flow<Response<AddEventResponse>> = flow {
        emit(apiServiceImpl.removePetApi(token, id))
    }.flowOn(Dispatchers.IO)

    fun storyList(token: String): Flow<Response<UserStoriesResponse>> = flow {
        emit(apiServiceImpl.storyList(token))
    }.flowOn(Dispatchers.IO)

    fun hidePostApi(token: String, id: String): Flow<Response<AddEventResponse>> = flow {
        emit(apiServiceImpl.hidePostApi(token, id))
    }.flowOn(Dispatchers.IO)

    fun removePostApi(token: String, id: String): Flow<Response<AddEventResponse>> = flow {
        emit(apiServiceImpl.removePostApi(token, id))
    }.flowOn(Dispatchers.IO)

    fun createReportApi(token: String, id: String, description: String): Flow<Response<AddEventResponse>> = flow {
        emit(apiServiceImpl.createReportApi(token, id,description))
    }.flowOn(Dispatchers.IO)

    fun agoraTokenGeneratorApi(token: String): Flow<Response<AgoraCallingResponse>> = flow {
        emit(apiServiceImpl.agoraTokenGeneratorApi(token))
    }.flowOn(Dispatchers.IO)

    fun callUserApi(
        token: String,
        receiverId: String,
        type: String
    ): Flow<Response<AddToIntrestedResponse>> = flow {
        emit(apiServiceImpl.callUserApi(token, receiverId, type))
    }.flowOn(Dispatchers.IO)


    fun deleteCommentApi(
        token: String, typeOfPost: String,
        postId: String, comment: String, commentId: String, reply: String
    ): Flow<Response<AddEventResponse>> = flow {
        emit(apiServiceImpl.deleteCommentApi(token, typeOfPost, postId, comment, commentId, reply))
    }.flowOn(Dispatchers.IO)


    fun addUserProfileApi(
        token: String, petCategoryType: String,
        name: String, userProfileImage: String, petCategoryId: String, petBreedId: String
    ): Flow<Response<AddToIntrestedResponse>> = flow {
        emit(apiServiceImpl.addUserProfileApi(token, petCategoryType, name, userProfileImage, petCategoryId,petBreedId))
    }.flowOn(Dispatchers.IO)


    fun userProfileListApi(
        token: String ): Flow<Response<PetProfileResponse>> = flow {
        emit(apiServiceImpl.userProfileListApi(token))}.flowOn(Dispatchers.IO)


    fun setDefaultUserProfileApi(
        token: String,petCategoryId :String): Flow<Response<AddToIntrestedResponse>> = flow {
        emit(apiServiceImpl.setDefaultUserProfileApi(token,petCategoryId))}.flowOn(Dispatchers.IO)


    fun petCategoryDetailsApi(petTypeId :String): Flow<Response<CategoryBasedResponse>> = flow {
        emit(apiServiceImpl.petCategoryDetailsApi(petTypeId))}.flowOn(Dispatchers.IO)


    fun listBannerListApi(): Flow<Response<BannerListResponse>> = flow {
        emit(apiServiceImpl.listBannerListApi())}.flowOn(Dispatchers.IO)

    fun setDefaultUserLanguageApi(token: String,languageCode :String): Flow<Response<AddPostResponse>> = flow {
        emit(apiServiceImpl.setDefaultUserLanguageApi(token, languageCode))}.flowOn(Dispatchers.IO)

    fun userLogout(token: String,fireToken: String): Flow<Response<AddEventResponse>> = flow {
        emit(apiServiceImpl.userLogout(token,fireToken))}.flowOn(Dispatchers.IO)

    fun listExploreApi(search:String,page:Int, limit:Int): Flow<Response<ExploreListResponse>> = flow {
        emit(apiServiceImpl.listExploreApi(search,page, limit))}.flowOn(Dispatchers.IO)

    fun vendorDashboard(token: String): Flow<Response<VendorDashboardResponse>> = flow {
        emit(apiServiceImpl.vendorDashboard(token))}.flowOn(Dispatchers.IO)

    fun myProductListApi(token: String,search:String,fromDate:String,toDate:String,page:Int,
                         limit:Int,approveStatus:String): Flow<Response<ProductsResponse>> = flow {
        emit(apiServiceImpl.myProductListApi(token,search, fromDate, toDate, page, limit, approveStatus))}.flowOn(Dispatchers.IO)



    fun addProductApi(token: String,request: AddProductRequest): Flow<Response<AddPostResponse>> = flow {
    emit(apiServiceImpl.addProductApi(token,request))}.flowOn(Dispatchers.IO)

    fun myServiceListApi(token: String,search:String,fromDate:String,toDate:String,page:Int,
                         limit:Int,approveStatus:String): Flow<Response<ProductsResponse>> = flow {
        emit(apiServiceImpl.myServiceListApi(token,search, fromDate, toDate, page, limit, approveStatus))}.flowOn(Dispatchers.IO)


    fun addServiceApi(token: String,request: AddServiceRequest): Flow<Response<AddPostResponse>> = flow {
        emit(apiServiceImpl.addServiceApi(token,request))}.flowOn(Dispatchers.IO)

    fun deleteServiceApi(token: String,rserviceId: String): Flow<Response<AddPostResponse>> = flow {
        emit(apiServiceImpl.deleteServiceApi(token,rserviceId))}.flowOn(Dispatchers.IO)

    fun deleteProductApi(token: String,productId: String): Flow<Response<AddPostResponse>> = flow {
        emit(apiServiceImpl.deleteProductApi(token,productId))}.flowOn(Dispatchers.IO)

    fun interestedClientListApi(token: String,type :String,page :Int,limit :Int,productId:String,serviceId:String,petId:String): Flow<Response<InterestedUserResponse>> = flow {
        emit(apiServiceImpl.interestedClientListApi(token,type, page, limit,productId, serviceId, petId))}.flowOn(Dispatchers.IO)

    fun interestedProductListApi(token: String,type :String,page :Int,limit :Int,productId:String,serviceId:String,petId:String): Flow<Response<InterestedUserResponse>> = flow {
        emit(apiServiceImpl.interestedProductListApi(token,type, page, limit,productId, serviceId, petId))}.flowOn(Dispatchers.IO)

    fun updateProductApi(token: String,request: UpdateProductRequest): Flow<Response<AddPostResponse>> = flow {
        emit(apiServiceImpl.updateProductApi(token,request))}.flowOn(Dispatchers.IO)

    fun updateServiceApi(token: String,request: UpdateServiceRequest): Flow<Response<AddPostResponse>> = flow {
        emit(apiServiceImpl.updateServiceApi(token,request))}.flowOn(Dispatchers.IO)

    fun redeemPointsListApi(token: String,search: String,fromDate: String,toDate: String,page: Int,limit: Int): Flow<Response<RewardsResponseVendor>> = flow {
        emit(apiServiceImpl.redeemPointsListApi(token,search, fromDate, toDate, page, limit))}.flowOn(Dispatchers.IO)

    fun editDoctorVetProfile(token: String,request: EditProfileVetDoctorRequest): Flow<Response<AddPostResponse>> = flow {
        emit(apiServiceImpl.editDoctorVetProfile(token,request))}.flowOn(Dispatchers.IO)
    fun availableSlotApi(token: String,date:String,doctorVetId:String): Flow<Response<AppointmentDateResponse>> = flow {
        emit(apiServiceImpl.availableSlotApi(token,date, doctorVetId))}.flowOn(Dispatchers.IO)

    fun listVendorApi(token: String,userTypes:String,search:String,page: Int,limit: Int,
                      radius : String,
                      country : String,
                      state : String,
                      city : String, lat: Double, lng: Double): Flow<Response<VetOrDoctorResponse>> = flow {
        emit(apiServiceImpl.listVendorApi(token,userTypes,search,page, limit,radius, country, state, city, lat, lng))}.flowOn(Dispatchers.IO)


    fun viewUser(token: String,id:String): Flow<Response<EditProfileResponse>> = flow {
        emit(apiServiceImpl.viewUser(token,id))}.flowOn(Dispatchers.IO)

    fun addAppointmentApi(token: String,request:AddAppointmentRequest): Flow<Response<AddPostResponse>> = flow {
        emit(apiServiceImpl.addAppointmentApi(token,request))}.flowOn(Dispatchers.IO)

    fun listUserAppointmentApi(token: String,search:String,page: Int,limit: Int,consultingType:String,appointmentStatus:String): Flow<Response<AppointmentListResponse>> = flow {
        emit(apiServiceImpl.listUserAppointmentApi(token,search, page, limit,consultingType,appointmentStatus))}.flowOn(Dispatchers.IO)

    fun viewAppointmentApi(token: String,id:String): Flow<Response<ViewAppointmentResponse>> = flow {
        emit(apiServiceImpl.viewAppointmentApi(token,id))}.flowOn(Dispatchers.IO)

    fun addBlockDoctorUserAPi(token: String,id:String): Flow<Response<AddPostResponse>> = flow {
        emit(apiServiceImpl.addBlockDoctorUserAPi(token,id))}.flowOn(Dispatchers.IO)

    fun markAsDoneAppointmentApi(token: String,id:String): Flow<Response<AddPostResponse>> = flow {
        emit(apiServiceImpl.markAsDoneAppointmentApi(token,id))}.flowOn(Dispatchers.IO)

    fun listBlockDoctorUserApi(token: String,search:String,page: Int,limit: Int): Flow<Response<BlockedUserResponse>> = flow {
        emit(apiServiceImpl.listBlockDoctorUserApi(token,search, page, limit))}.flowOn(Dispatchers.IO)

    fun addFeedbacknratingApi(token: String,appointmentId:String,doctorId: String,title: String,rating: Double,message: String): Flow<Response<AddPostResponse>> = flow {
        emit(apiServiceImpl.addFeedbacknratingApi(token,appointmentId,doctorId, title, rating, message))}.flowOn(Dispatchers.IO)

    fun listfeedbacknratingApi(token: String,search:String,page: Int,limit: Int): Flow<Response<UserFeedBackListResponse>> = flow {
        emit(apiServiceImpl.listfeedbacknratingApi(token,search, page, limit))}.flowOn(Dispatchers.IO)

    fun cancelAppointmentApi(token: String,id: String): Flow<Response<AddPostResponse>> = flow {
        emit(apiServiceImpl.cancelAppointmentApi(token = token, id = id))}.flowOn(Dispatchers.IO)

    fun canceleAppointmentByday(token: String,date: String): Flow<Response<AddPostResponse>> = flow {
        emit(apiServiceImpl.canceleAppointmentByday(token = token, date = date))}.flowOn(Dispatchers.IO)

    fun petBreedListApi(petCategoryId:String): Flow<Response<CountryResponse>> = flow {
        emit(apiServiceImpl.petBreedListApi(petCategoryId = petCategoryId))}.flowOn(Dispatchers.IO)
    fun appConfig(): Flow<Response<AppConfigResponse>> = flow {
        emit(apiServiceImpl.appConfig())}.flowOn(Dispatchers.IO)
    fun getSubscriptionApi(token: String): Flow<Response<GetSubscriptionDetailsResponse>> = flow {
        emit(apiServiceImpl.getSubscriptionApi(token = token))}.flowOn(Dispatchers.IO)
    fun makePaymentApi(token: String,stripeToken:String,totalCost:String,curr:String,plan_id:String): Flow<Response<AddPostResponse>> = flow {
        emit(apiServiceImpl.makePaymentApi(token=token, stripeToken=stripeToken, totalCost=totalCost, curr=curr, plan_id = plan_id))}.flowOn(Dispatchers.IO)

    fun checkPlanApi(token: String): Flow<Response<CheckLimitPlanResponse>> = flow {
        emit(apiServiceImpl.checkPlanApi(token=token))}.flowOn(Dispatchers.IO)
    fun listSubscriptionApi(type: String,userType: String,moduleName: String): Flow<Response<PlanListResponse>> = flow {
        emit(apiServiceImpl.listSubscriptionApi(type=type,userType=userType,moduleName=moduleName))}.flowOn(Dispatchers.IO)



    fun createContactUsApi(token: String,name: String,email: String,mobileNumber: String,reason: String): Flow<Response<AddPostResponse>> = flow {
        emit(apiServiceImpl.createContactUsApi(token=token,name=name,email=email,mobileNumber=mobileNumber,reason=reason))}.flowOn(Dispatchers.IO)



}