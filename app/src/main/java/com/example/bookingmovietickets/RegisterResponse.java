package com.example.bookingmovietickets;

import com.google.gson.annotations.SerializedName;

public class RegisterResponse {

    @SerializedName("trangThai")
    private String trangThai;

    @SerializedName("thongBao")
    private String thongBao;

    @SerializedName("duLieu")
    private UserData duLieu;

    public static class UserData {
        @SerializedName("id")
        private int id;

        @SerializedName("email")
        private String email;

        public int getId() { return id; }
        public String getEmail() { return email; }
    }

    public String getTrangThai() { return trangThai; }
    public String getThongBao() { return thongBao; }
    public UserData getDuLieu() { return duLieu; }
}
