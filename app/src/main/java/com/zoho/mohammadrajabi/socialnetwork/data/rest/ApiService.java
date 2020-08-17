package com.zoho.mohammadrajabi.socialnetwork.data.rest;


import com.zoho.mohammadrajabi.socialnetwork.data.model.CommentsResponse;
import com.zoho.mohammadrajabi.socialnetwork.data.model.LikeResponse;
import com.zoho.mohammadrajabi.socialnetwork.data.model.PostResponse;
import com.zoho.mohammadrajabi.socialnetwork.data.model.SendCommentResponse;
import com.zoho.mohammadrajabi.socialnetwork.data.model.SendPostResponse;
import com.zoho.mohammadrajabi.socialnetwork.data.model.SignInResponse;
import com.zoho.mohammadrajabi.socialnetwork.data.model.SignUpResponse;
import com.zoho.mohammadrajabi.socialnetwork.data.model.SuccessResponse;
import com.zoho.mohammadrajabi.socialnetwork.data.model.UpdateUserResponse;
import com.zoho.mohammadrajabi.socialnetwork.data.model.User;
import com.zoho.mohammadrajabi.socialnetwork.data.model.UserProfileResponse;
import com.zoho.mohammadrajabi.socialnetwork.data.model.checkUsernameResponse;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {

    @POST("signin")
    @FormUrlEncoded
    Call<SignInResponse> login(@Field("username") String username, @Field("password") String password);


    @POST("signup")
    @Multipart
    Call<SignUpResponse> singUp(@Part("username") String username, @Part("password") String password, @Part("phone") String phone);

    @POST("checkusername")
    @FormUrlEncoded
    Call<checkUsernameResponse> checkUsername(@Field("username") String username);


    @GET("getallposts")
    Single<PostResponse> getPosts();

    @POST("getcommentsofposts")
    @FormUrlEncoded
    Single<CommentsResponse> getComments(@Field("post_id") int postId);

    @POST("sendcomment")
    @FormUrlEncoded
    Single<SendCommentResponse> sendComment(@Field("caption") String commentContent, @Field("post_id") int postId, @Field("user_id") String userId);


    @POST("")
    @FormUrlEncoded
    Single<List<User>> search(@Field("keyword") String keyword);

    @POST("getuserprofile")
    @FormUrlEncoded
    Single<UserProfileResponse> getProfile(@Field("user_id") String userId);


    @POST("updateuser")
    @Multipart
    Call<UpdateUserResponse> editProfile(@Part("user_id") int userId,
                                           @Part("username") String username,
                                           @Part("password") String password,
                                           @Part("phone") String phone,
                                           @Part MultipartBody.Part profileImage);


    @POST("like")
    @FormUrlEncoded
    Single<LikeResponse> likePost(@Field("post_id") int postId);

    @POST("sendpost")
    @Multipart
    Single<SendPostResponse> sendPost(@Part("user_id") int userId,
                                      @Part("caption") String postContent,
                                      @Part MultipartBody.Part profileImage);
}
