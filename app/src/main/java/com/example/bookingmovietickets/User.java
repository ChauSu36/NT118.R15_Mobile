package com.example.bookingmovietickets;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("user_id")
    private int user_id;

    @SerializedName("full_name")
    private String full_name;

    @SerializedName("email")
    private String email;

    @SerializedName("phone")
    private String phone;

    @SerializedName("role")
    private String role;

    @SerializedName("gender")
    private String gender;

    @SerializedName("dob")
    private String dob;

    @SerializedName("avatar_url")
    private String avatar_url;

    public int getUser_id() { return user_id; }
    public String getFull_name() { return full_name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getRole() { return role; }
    public String getGender() { return gender; }
    public String getDob() { return dob; }
    public String getAvatar_url() { return avatar_url; }
}
