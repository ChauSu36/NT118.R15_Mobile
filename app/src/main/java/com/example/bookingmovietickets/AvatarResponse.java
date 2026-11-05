package com.example.bookingmovietickets;

import com.google.gson.annotations.SerializedName;

public class AvatarResponse {

    @SerializedName("trangThai")
    private String trangThai;

    @SerializedName("thongBao")
    private String thongBao;

    @SerializedName("avatar_url")
    private String avatarUrl;

    public AvatarResponse() {
    }

    public AvatarResponse(String trangThai, String thongBao, String avatarUrl) {
        this.trangThai = trangThai;
        this.thongBao = thongBao;
        this.avatarUrl = avatarUrl;
    }

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

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getAvatar_url() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
