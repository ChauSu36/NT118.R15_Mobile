package com.example.bookingmovietickets;
// LƯU Ý: đổi dòng package ở trên cho khớp với package thật trong project của bạn.

/** Model đơn giản cho 1 banner khuyến mãi trên Trang chủ. */
public class Banner {

    private String badge;
    private String title;
    private String subtitle;

    public Banner(String badge, String title, String subtitle) {
        this.badge = badge;
        this.title = title;
        this.subtitle = subtitle;
    }

    public String getBadge() {
        return badge;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }
}
