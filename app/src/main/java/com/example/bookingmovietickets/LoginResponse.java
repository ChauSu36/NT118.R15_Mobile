package com.example.bookingmovietickets;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("trangThai")
    private String trangThai;

    @SerializedName("thongBao")
    private String thongBao;

    @SerializedName("token")
    private String token;

    @SerializedName("duLieu")
    private User user;

    public String getTrangThai() { return trangThai; }
    public String getThongBao() { return thongBao; }
    public String getToken() { return token; }
    public User getUser() { return user; }
}
