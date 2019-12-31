package com.ygaps.travelapp.Network;


import com.ygaps.travelapp.Model.AllReviewResponse;
import com.ygaps.travelapp.Model.CommentResponse;
import com.ygaps.travelapp.Model.LoginResponse;
import com.ygaps.travelapp.Model.MultiCoordinateRequest;
import com.ygaps.travelapp.Model.NotificationListResponse;
import com.ygaps.travelapp.Model.PointStatResponse;
import com.ygaps.travelapp.Model.RegisterResponse;
import com.ygaps.travelapp.Model.RemoveStopPointsRequest;
import com.ygaps.travelapp.Model.SingleCoordinateRequest;
import com.ygaps.travelapp.Model.StopPointRequest;
import com.ygaps.travelapp.Model.TourInfoResponse;
import com.ygaps.travelapp.Model.UserInfoRequest;


import java.util.Date;
import java.util.Map;


import okhttp3.ResponseBody;
import retrofit2.Call;

import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;


public interface UserService {

    @FormUrlEncoded
    @POST("/user/register")
    Call<RegisterResponse>  createUser (
            @Field("fullName") String _fullName,
            @Field("email") String _email,
            @Field("password") String _password,
            @Field("phone") String _phone,
            @Field("gender") Number gender
    );

    @FormUrlEncoded
    @POST("/user/login")
    Call<LoginResponse>  login (
            @Field( "emailPhone" ) String user,
            @Field( "password" ) String pass
    );

    @GET("/user/info")
    Call<ResponseBody>  getUserInfo (
            @Header("Authorization") String Authorization
    );

    @Headers( "Content-Type: application/json" )
    @POST("/user/edit-info")
    Call<ResponseBody>  updateUserInfo (
            @Header("Authorization") String Authorization,
            @Body UserInfoRequest userInfoRequest
    );

    @FormUrlEncoded
    @POST("/tour/create")
            Call<ResponseBody>  createTour (
            @Header("Authorization") String Authorization,
            @Field( "name" ) String name,
            @Field( "startDate" ) Number startDate,
            @Field( "endDate" ) Number endDate,
            @Field( "sourceLat" ) Number sourceLat,
            @Field( "sourceLong" ) Number sourceLong,
            @Field( "desLat" ) Number desLat,
            @Field( "desLong" ) Number desLong,
            @Field( "isPrivate" ) boolean isPrivate,
            @Field( "adults" ) Number adults,
            @Field( "childs" ) Number childs,
            @Field( "minCost" ) Number minCost,
            @Field( "maxCost" ) Number maxCost

    );

    @GET("/tour/list")
    Call<ResponseBody> getListTour(
            @Header("Authorization") String Authorization,
            @QueryMap Map<String,String> Params
    );

    @GET("/tour/history-user")
    Call<ResponseBody> getListTourHistory(
            @Header("Authorization") String Authorization,
            @QueryMap Map<String,Number> Params
    );

    @FormUrlEncoded
    @POST("/user/login/by-facebook")
    Call<LoginResponse>  loginFB (
            @Field( "accessToken" ) String accessToken
    );

    @Headers( "Content-Type: application/json" )
    @POST("/tour/set-stop-points")
    Call<ResponseBody>  addStopPoints (
            @Header( "Authorization" ) String token,
            @Body StopPointRequest body
            );
    @GET("/tour/info")
    Call<TourInfoResponse> getTourDetail (
            @Header("Authorization") String Authorization,
            @Query("tourId") Number tourId
    );

    @GET("tour/info")
    Call<ResponseBody> getTourInfo(
            @Header("Authorization") String Authorization,
            @Query( "tourId" )  int tourId
    );

    @FormUrlEncoded
    @POST("/tour/add/member")
    Call<ResponseBody>  addMemberToTour (
            @Header("Authorization") String Authorization,
            @Field("tourId") String tourId,
            @Field("invitedUserId") String invitedUserId,
            @Field("isInvited") Boolean isInvited

    );

    @GET("/user/search")
    Call<ResponseBody> searchUser(
            @Query("searchKey") String searchKey,
            @Query("pageIndex") Number pageIndex,
            @Query("pageSize") Number pageSize
    );

    @FormUrlEncoded
    @POST("/tour/add/review")
    Call<ResponseBody>  sendReviewTour (
            @Header("Authorization") String Authorization,
            @Field("tourId") Number tourId,
            @Field("point") Number point,
            @Field("review") String review
    );

    @GET("/tour/get/review-point-stats")
    Call<PointStatResponse> getPointStatsReview(
            @Header("Authorization") String Authorization,
            @Query("tourId") Number tourId
    );

    @GET("/tour/get/review-list")
    Call<AllReviewResponse> getReviewList(
            @Header("Authorization") String Authorization,
            @Query("tourId") Number tourId,
            @Query("pageIndex") Number pageIndex,
            @Query("pageSize") Number pageSize
    );
    @FormUrlEncoded
    @POST("/tour/report/review")
    Call<ResponseBody>  reportReview (
            @Header("Authorization") String Authorization,
            @Field("reviewId") Number reviewId
    );

    @GET("/tour/comment-list")
    Call<CommentResponse> getCommentList(
            @Header("Authorization") String Authorization,
            @Query("tourId") Number tourId,
            @Query("pageIndex") Number pageIndex,
            @Query("pageSize") Number pageSize
    );

    @FormUrlEncoded
    @POST("/tour/comment")
    Call<ResponseBody>  sendComment (
            @Header("Authorization") String Authorization,
            @Field("tourId") Number tourId,
            @Field("userId") String userId,
            @Field("comment") String comment
    );

    @FormUrlEncoded
    @POST("/user/request-otp-recovery")
    Call<ResponseBody>  RequestOTP (
            @Field("type") String type,
            @Field("value") String value
    );

    @FormUrlEncoded
    @POST("/user/verify-otp-recovery")
    Call<ResponseBody>  VerifyOTP (
            @Field("userId") Number userId,
            @Field("newPassword") String newPassword,
            @Field("verifyCode") String verifyCode
    );

    @GET("/tour/search/service")
    Call<ResponseBody> searchDestination(
            @Header("Authorization") String Authorization,
            @Query("searchKey") String searchKey,
            @Query("pageIndex") Number pageIndex,
            @Query("pageSize") Number pageSize
    );

    @GET("/tour/get/service-detail")
    Call<ResponseBody> getServiceDetail(
            @Header("Authorization") String Authorization,
            @Query("serviceId") Number serviceId
    );

    @GET("/tour/get/feedback-service")
    Call<ResponseBody> getListFeedBack(
            @Header("Authorization") String Authorization,
            @Query("serviceId") Number serviceId,
            @Query("pageIndex") String pageIndex,
            @Query("pageSize") String pageSize
    );

    @GET("/tour/get/feedback-point-stats")
    Call<ResponseBody> getFeedbackPointStart(
            @Header("Authorization") String Authorization,
            @Query("serviceId") Number serviceId
    );

    @FormUrlEncoded
    @POST("/tour/add/feedback-service")
    Call<ResponseBody>  sendReviewService (
            @Header("Authorization") String Authorization,
            @Field("serviceId") Number serviceId,
            @Field("feedback") String feedback,
            @Field("point") Number point
    );

    @FormUrlEncoded
    @POST("/tour/report/feedback")
    Call<ResponseBody>  reportFeedbackService (
            @Header("Authorization") String Authorization,
            @Field("feedbackId") Number feedbackId
    );

    @POST("/tour/suggested-destination-list")
    Call<ResponseBody> aroundCoordinateSuggest(
            @Header("Authorization") String token,
            @Body SingleCoordinateRequest body
    );

    @POST("/tour/suggested-destination-list")
    Call<ResponseBody> betweenCoordinateSuggest(
            @Header("Authorization") String token,
            @Body MultiCoordinateRequest body
    );

    @FormUrlEncoded
    @POST("/user/notification/put-token")
    Call<ResponseBody> registerFirebase(
            @Header("Authorization") String Authorization,
            @Field("fcmToken") String fcmToken,
            @Field("deviceId") String deviceId,
            @Field("platform") Number platform,
            @Field("appVersion") String appVersion
    );

    @FormUrlEncoded
    @POST("/tour/update-tour")
    Call<ResponseBody>  deleteTour (
            @Header("Authorization") String Authorization,
            @Field("id") String id,
            @Field("status") Number status
    );

    @FormUrlEncoded
    @POST("/tour/update-tour")
    Call<ResponseBody>  updateTour (
            @Header("Authorization") String Authorization,
            @Field("id") String id,
            @Field( "name" ) String name,
            @Field( "startDate" ) Number startDate,
            @Field( "endDate" ) Number endDate,
            @Field( "sourceLat" ) Number sourceLat,
            @Field( "sourceLong" ) Number sourceLong,
            @Field( "desLat" ) Number desLat,
            @Field( "desLong" ) Number desLong,
            @Field( "adults" ) Number adults,
            @Field( "childs" ) Number childs,
            @Field( "minCost" ) Number minCost,
            @Field( "maxCost" ) Number maxCost,
            @Field( "isPrivate" ) boolean isPrivate,
            @Field("status") Number status
    );

    @Headers("Content-Type: application/json")
    @POST("/tour/set-stop-points")
    Call<ResponseBody> removeStopPoints(
            @Header("Authorization") String token,
            @Body RemoveStopPointsRequest removeStopPointsRequest);

    @FormUrlEncoded
    @POST("/tour/notification")
    Call<ResponseBody> sendMessage (
            @Header("Authorization") String token,
            @Field( "tourId" ) int tourId,
            @Field( "userId" ) int userId,
            @Field( "noti" ) String noti
    );

    @GET("/tour/notification-list")
    Call<ResponseBody> getListChat(
            @Header("Authorization") String token,
            @Query( "tourId" )  int tourId,
            @Query( "pageIndex" )  int pageIndex,
            @Query( "pageSize" )  int pageSize
    );

    @FormUrlEncoded
    @POST("/tour/add/notification-on-road")
    Call<ResponseBody> sendNotification (
            @Header("Authorization") String Authorization,
            @Field( "lat" ) double lat,
            @Field( "long" ) double lng,
            @Field( "tourId" ) int tourId,
            @Field( "userId" ) int userId,
            @Field( "notificationType" ) int type,
            @Field( "speed" ) int speed,
            @Field( "note" ) String note
    );

    @FormUrlEncoded
    @POST("/tour/add/notification-on-road")
    Call<ResponseBody> sendNotification (
            @Header("Authorization") String token,
            @Field( "lat" ) double lat,
            @Field( "long" ) double lng,
            @Field( "tourId" ) int tourId,
            @Field( "userId" ) int userId,
            @Field( "notificationType" ) int type,
            @Field( "note" ) String note
    );
    @GET("/tour/get/noti-on-road")
    Call<ResponseBody> getNotification(
            @Header("Authorization") String token,
            @Query( "tourId" )  int tourId,
            @Query( "pageIndex" )  int pageIndex,
            @Query( "pageSize" )  int pageSize
    );

    @FormUrlEncoded
    @POST("/tour/current-users-coordinate")
    Call<ResponseBody> sendCoordinate (
            @Header("Authorization") String token,
            @Field( "userId" ) int userId,
            @Field( "tourId" ) int tourId,
            @Field( "lat" ) double lat,
            @Field( "long" ) double lng

    );
    @FormUrlEncoded
    @POST("/tour/finish-trip")
    Call<ResponseBody> finishTrip (
            @Header("Authorization") String token,
            @Field( "tourId" ) int tourId
    );

    @FormUrlEncoded
    @POST("/tour/response/invitation")
    Call<ResponseBody> acceptTourInvitation(
            @Header("Authorization") String Authorization,
            @Field("tourId") String tourId,
            @Field("isAccepted") Boolean isAccepted
    );
    @GET("/tour/get/invitation")
    Call<NotificationListResponse> getInvitationList(
            @Header("Authorization") String Authorization,
            @Query("pageIndex") Number pageIndex,
            @Query("pageSize") Number pageSize
    );
}