package com.example.bookingmovietickets;

import com.google.gson.annotations.SerializedName;

public class VerifyOtpRequest {

    @SerializedName("email")
    private String email;

    @SerializedName("otpCode")
    private String otpCode;

    public VerifyOtpRequest(String email, String otpCode) {
        this.email = email;
        this.otpCode = otpCode;
    }

    public String getEmail() {
        return email;
    }

    public String getOtpCode() {
        return otpCode;
    }
}
