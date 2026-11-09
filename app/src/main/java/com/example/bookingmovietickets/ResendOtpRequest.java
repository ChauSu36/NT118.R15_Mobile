package com.example.bookingmovietickets;

import com.google.gson.annotations.SerializedName;

public class ResendOtpRequest {

    @SerializedName("email")
    private String email;

    public ResendOtpRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
