package com.example.bookingmovietickets;

import com.google.gson.annotations.SerializedName;

public class ChangePasswordResponse {

    @SerializedName("trangThai")
    private String trangThai;

    @SerializedName("thongBao")
    private String thongBao;

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getThongBao() {
        return thongBao;
    }

    public void setThongBao(String thongBao) {
        this.thongBao = thongBao;
    }
}
