package com.example.bookingmovietickets;

import com.google.gson.annotations.SerializedName;

public class ResetPassRequest {

    @SerializedName("email")
    private String email;

    @SerializedName("otp")
    private String otp;

    @SerializedName("matKhauMoi")
    private String matKhauMoi;

    public ResetPassRequest(String email, String otp, String matKhauMoi) {
        this.email = email;
        this.otp = otp;
        this.matKhauMoi = matKhauMoi;
    }

    public String getEmail() {
        return email;
    }

    public String getOtp() {
        return otp;
    }

    public String getMatKhauMoi() {
        return matKhauMoi;
    }
}
