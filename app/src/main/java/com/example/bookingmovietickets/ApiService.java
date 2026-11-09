package com.example.bookingmovietickets;

import java.util.List;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiService {

    @POST("api/users")
    Call<RegisterResponse> registerUser(@Body RegisterRequest request);

    @POST("api/users/login")
    Call<LoginResponse> dangNhap(@Body LoginRequest request);

    @Multipart
    @POST("api/users/upload-avatar")
    Call<AvatarResponse> uploadAvatar(
        @Header("Authorization") String token,
        @Part("user_id") RequestBody userId,
        @Part MultipartBody.Part avatar
    );

    @POST("api/users/update-profile")
    Call<UpdateProfileResponse> updateProfile(
        @Header("Authorization") String token,
        @Body UpdateProfileRequest request
    );

    @POST("api/users/change-password")
    Call<ChangePasswordResponse> changePassword(
        @Header("Authorization") String token,
        @Body ChangePasswordRequest request
    );

    @POST("api/users/verify-otp")
    Call<VerifyOtpResponse> verifyOtp(@Body VerifyOtpRequest request);

    @POST("api/users/resend-otp")
    Call<ResendOtpResponse> resendOtp(@Body ResendOtpRequest request);

    @POST("api/users/forgot-pass")
    Call<ResendOtpResponse> forgotPass(@Body ResendOtpRequest request);

    @POST("api/users/reset-pass")
    Call<ResetPassResponse> resetPass(@Body ResetPassRequest request);

    @GET("api/movies")
    Call<List<Movie>> getMovies();

    @GET("api/movies/now-showing")
    Call<MovieResponse> getNowShowingMovies();

    @GET("api/movies/coming-soon")
    Call<MovieResponse> getComingSoonMovies();

    @GET("api/movies/search")
    Call<MovieResponse> searchMovies(@Query("keyword") String keyword);

    @GET("api/movies/filter")
    Call<MovieResponse> filterMovies(
        @Query("status") String status,
        @Query("genre") String genre,
        @Query("year") Integer year
    );
}
