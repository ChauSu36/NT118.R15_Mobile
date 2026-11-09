package com.example.bookingmovietickets;

import com.google.gson.annotations.SerializedName;

public class ResetPassResponse {

    @SerializedName("trangThai")
    private String trangThai;

    @SerializedName("thongBao")
    private String thongBao;

    public String getTrangThai() {
        return trangThai;
    }

    public String getThongBao() {
        return thongBao;
    }
}
