package com.tykle.InterfaceClass;

import com.tykle.ModelClassess.CheckUserInfo;
import com.tykle.ModelClassess.FilterUserModel;
import com.tykle.ModelClassess.FriendListModel;
import com.tykle.ModelClassess.FrindIDListModel;
import com.tykle.ModelClassess.GetProfileModel;
import com.tykle.ModelClassess.GetUsers;
import com.tykle.ModelClassess.SocialApi;
import com.tykle.ModelClassess.TykleListModel;
import com.tykle.ModelClassess.UpdateInfo;
import com.tykle.ModelClassess.UserProfileUpdate;
import com.tykle.ModelClassess.UserRegister;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Naveen Devrani on 07-04-2018.
 */

public interface ApiService {

    @FormUrlEncoded
    @POST("checkUserEmail")
    Call<Map> CheckUserEmail(@Field("email") String email);


    @FormUrlEncoded
    @POST("emailVerification")
    Call<Map> EmailVerification(@Field("email") String email);


    @Multipart
    @POST("userRegister")
    Call<UserRegister> UserRegister(@Part("user_name") RequestBody user_name,
                                    @Part("email") RequestBody email,
                                    @Part("password") RequestBody password,
                                    @Part("phone") RequestBody phone,
                                    @Part("device_type") RequestBody device_type,
                                    @Part("latitude") RequestBody latitude,
                                    @Part("longitude") RequestBody longitude,
                                    @Part("reg_id") RequestBody reg_id,
                                    @Part MultipartBody.Part[] image);


    @FormUrlEncoded
    @POST("updateInfo")
    Call<UpdateInfo> UpdateInfo(@Field("user_id") String user_id,
                                @Field("age") String age,
                                @Field("gender") String gender,
                                @Field("looking_for") String looking_for,
                                @Field("interested") String interested,
                                @Field("language") String language,
                                @Field("profession") String profession,
                                @Field("about") String about,
                                @Field("relationshipStatus") String relationshipStatus);


    @FormUrlEncoded
    @POST("checkUserInfo")
    Call<CheckUserInfo> checkUserInfo(@Field("quickbox_id") String quickbox_id,
                                      @Field("reg_id") String reg_id);


    @FormUrlEncoded
    @POST("socialLogin")
    Call<SocialApi> SocialApi(@Field("user_name") String user_name,
                              @Field("email") String email,
                              @Field("device_type") String device_type,
                              @Field("social_id") String social_id,
                              @Field("login_type") String login_type,
                              @Field("latitude") String latitude,
                              @Field("longitude") String longitude,
                              @Field("reg_id") String reg_id,
                              @Field("image1") String image);


    @FormUrlEncoded
    @POST("getUsers")
    Call<GetUsers> GetUsers(@Field("user_id") String user_id,
                            @Field("interested") String interested);

    @FormUrlEncoded
    @POST("userLikeReject")
    Call<Map> UserAction(@Field("user_id") String user_id,
                         @Field("friend_id") String friend_id,
                         @Field("status") String status);


    @FormUrlEncoded
    @POST("filter")
    Call<FilterUserModel> getFilteredUserList(@Field("user_id") String user_id,
                                              @Field("age") String age,
                                              @Field("looking_for") String looking_for,
                                              @Field("lat") String lat,
                                              @Field("long") String longi,
                                              @Field("distance1") String distance1,
                                              @Field("distance2") String distance2);


    @Multipart
    @POST("updateProfile")
    Call<UserProfileUpdate> UpdateProfile(@Part("id") RequestBody id,
                                          @Part("user_name") RequestBody user_name,
                                          @Part("phone") RequestBody phone,
                                          @Part("age") RequestBody age,
                                          @Part("interested") RequestBody interested,
                                          @Part("relationshipStatus") RequestBody relationshipStatus,
                                          @Part("about") RequestBody about,
                                          @Part MultipartBody.Part image1,
                                          @Part MultipartBody.Part image2,
                                          @Part MultipartBody.Part image3,
                                          @Part MultipartBody.Part image4);


    @FormUrlEncoded
    @POST("userFriendsQuickbloxIdsList")
    Call<FrindIDListModel> FrindIdList(@Field("user_id") String user_id);


    @FormUrlEncoded
    @POST("userFriendsList")
    Call<FriendListModel> getFriendList(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("userTykleList")
    Call<TykleListModel> TykleFriendList(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("messageNotification")
    Call<Map> SendMessage(@Field("user_quickblox_id") String user_quickblox_id,
                          @Field("friend_quickblox_id") String friend_quickblox_id,
                          @Field("message") String message,
                          @Field("attachment") String attachment);


    @FormUrlEncoded
    @POST("getProfile")
    Call<GetProfileModel> GetProfile(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("updateOnlineStatus")
    Call<Map> GoOnlineOffline(@Field("onlineStatus") String onlineStatus,
                              @Field("chatOnlineStatus") String chatOnlineStatus,
                              @Field("quickbox_id") String quickbox_id);


}
