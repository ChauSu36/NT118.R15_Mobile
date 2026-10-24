package com.example.bookingmovietickets; // Thay bằng tên package dự án Android của bạn

import com.google.gson.annotations.SerializedName;

public class RegisterRequest {

    @SerializedName("full_name") // Sửa thành full_name khớp với Backend
    private String fullName;

    @SerializedName("email")
    private String email;

    @SerializedName("password") // Sửa thành password khớp với Backend
    private String password;

    @SerializedName("phone")
    private String phone;

    @SerializedName("gender")
    private String gender;

    @SerializedName("dob")
    private String dob;

    // Constructor chuẩn bị dữ liệu
    public RegisterRequest(String fullName, String email, String password, String phone, String gender, String dob) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.gender = gender;
        this.dob = dob;
    }

    // Getters và Setters
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getDob() { return dob; }
    public void setDob(String dob) { this.dob = dob; }
}