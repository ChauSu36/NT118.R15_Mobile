package com.example.bookingmovietickets;
// LƯU Ý: đổi dòng package ở trên cho khớp với package thật trong project của bạn
// (xem dòng package ở đầu file MainActivity.java bạn đang có).

/**
 * Model đơn giản cho 1 phim hiển thị trên Trang chủ.
 * Đây là dữ liệu TĨNH (hard-code) để dựng xong giao diện trước;
 * sau này khi nối API thật, đổi nguồn dữ liệu trong HomeActivity
 * từ "danh sách tĩnh" sang "gọi MovieRepository" là đủ, không cần
 * sửa lại Adapter hay layout.
 */
public class Movie {

    private String title;
    private String genreAndDuration; // ví dụ: "Tình cảm • 131p"
    private String ageRating;        // "C16" hoặc "P"
    private String rating;           // ví dụ: "4.8"

    public Movie(String title, String genreAndDuration, String ageRating, String rating) {
        this.title = title;
        this.genreAndDuration = genreAndDuration;
        this.ageRating = ageRating;
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public String getGenreAndDuration() {
        return genreAndDuration;
    }

    public String getAgeRating() {
        return ageRating;
    }

    public String getRating() {
        return rating;
    }
}
