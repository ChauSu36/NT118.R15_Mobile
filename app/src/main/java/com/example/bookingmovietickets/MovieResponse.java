package com.example.bookingmovietickets;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class MovieResponse {

    @SerializedName("trangThai")
    private String trangThai;

    @SerializedName("soLuong")
    private int soLuong;

    @SerializedName("duLieu")
    private List<Movie> duLieu;

    public String getTrangThai() {
        return trangThai;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public List<Movie> getDuLieu() {
        return duLieu;
    }
}
