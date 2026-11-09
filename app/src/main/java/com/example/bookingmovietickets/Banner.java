package com.example.bookingmovietickets;

public class Banner {

    private String badge;
    private String title;
    private String subtitle;
    private String posterUrl;

    public Banner(String badge, String title, String subtitle) {
        this.badge = badge;
        this.title = title;
        this.subtitle = subtitle;
        this.posterUrl = null;
    }

    public Banner(String badge, String title, String subtitle, String posterUrl) {
        this.badge = badge;
        this.title = title;
        this.subtitle = subtitle;
        this.posterUrl = posterUrl;
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

    public String getPosterUrl() {
        return posterUrl;
    }
}
