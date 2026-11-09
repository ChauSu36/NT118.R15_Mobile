package com.example.bookingmovietickets;

import com.google.gson.annotations.SerializedName;

public class Movie {

    @SerializedName("movie_id")
    private int movieId;

    @SerializedName("title")
    private String title;

    @SerializedName("genre")
    private String genre;

    @SerializedName("duration_minutes")
    private int durationMinutes;

    @SerializedName("poster_url")
    private String posterUrl;

    @SerializedName("age_rating")
    private String ageRating;

    @SerializedName("status")
    private String status;

    public Movie() {}

    public Movie(int movieId, String title, String genre, int durationMinutes, String posterUrl, String ageRating, String status) {
        this.movieId = movieId;
        this.title = title;
        this.genre = genre;
        this.durationMinutes = durationMinutes;
        this.posterUrl = posterUrl;
        this.ageRating = ageRating;
        this.status = status;
    }

    public int getMovieId() {
        return movieId;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public String getAgeRating() {
        return ageRating;
    }

    public String getStatus() {
        return status;
    }

    public String getGenreAndDuration() {
        return (genre != null ? genre : "") + " • " + durationMinutes + "p";
    }

    public String getRating() {
        return "4.8";
    }
}
