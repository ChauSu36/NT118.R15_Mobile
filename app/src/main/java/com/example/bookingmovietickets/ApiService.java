package com.example.bookingmovietickets;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {

    @POST("api/users")
    Call<RegisterResponse> registerUser(@Body RegisterRequest request);

    @POST("api/users/login")
    Call<LoginResponse> dangNhap(@Body LoginRequest request);

    @Multipart
    @POST("api/users/upload-avatar")
    Call<AvatarResponse> uploadAvatar(
        @Part("user_id") RequestBody userId,
        @Part MultipartBody.Part avatar
    );

    @POST("api/users/update-profile")
    Call<UpdateProfileResponse> updateProfile(@Body UpdateProfileRequest request);

    @POST("api/users/change-password")
    Call<ChangePasswordResponse> changePassword(@Body ChangePasswordRequest request);
}
