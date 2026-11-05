package com.example.bookingmovietickets;

import com.google.gson.annotations.SerializedName;

public class UpdateProfileRequest {

    @SerializedName("user_id")
    private int userId;

    @SerializedName("full_name")
    private String fullName;

    @SerializedName("phone")
    private String phone;

    @SerializedName("gender")
    private String gender;

    @SerializedName("dob")
    private String dob;

    public UpdateProfileRequest(int userId, String fullName, String phone, String gender, String dob) {
        this.userId = userId;
        this.fullName = fullName;
        this.phone = phone;
        this.gender = gender;
        this.dob = dob;
    }

    public int getUserId() { return userId; }
    public String getFullName() { return fullName; }
    public String getPhone() { return phone; }
    public String getGender() { return gender; }
    public String getDob() { return dob; }
}
