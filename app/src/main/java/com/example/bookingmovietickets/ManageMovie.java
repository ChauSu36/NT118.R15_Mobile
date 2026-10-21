package com.example.bookingmovietickets;

import java.util.List;

public class ManageMovie {
    private String title;
    private String ageRating;
    private String duration;
    private String genre;
    private String releaseDate;
    private String status; // "ĐANG CHIẾU", "SẮP CHIẾU", "TẮT"
    private String showtimeHall;
    private String showtimeDate;
    private List<ShowtimeSlot> showtimes;
    private int dayOffset = 0; // 0 = Hôm nay (30/09), 1 = Ngày tiếp theo (01/10), -1 = Ngày trước đó (29/09)

    public static class ShowtimeSlot {
        public String time;
        public String seatStatus;
        public boolean isFull;

        public ShowtimeSlot(String time, String seatStatus, boolean isFull) {
            this.time = time;
            this.seatStatus = seatStatus;
            this.isFull = isFull;
        }
    }

    public ManageMovie(String title, String ageRating, String duration, String genre,
                       String releaseDate, String status, String showtimeHall,
                       String showtimeDate, List<ShowtimeSlot> showtimes) {
        this.title = title;
        this.ageRating = ageRating;
        this.duration = duration;
        this.genre = genre;
        this.releaseDate = releaseDate;
        this.status = status;
        this.showtimeHall = showtimeHall;
        this.showtimeDate = showtimeDate;
        this.showtimes = showtimes;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAgeRating() { return ageRating; }
    public String getDuration() { return duration; }
    public String getGenre() { return genre; }
    public String getReleaseDate() { return releaseDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getShowtimeHall() { return showtimeHall; }
    public String getShowtimeDate() { return showtimeDate; }
    public List<ShowtimeSlot> getShowtimes() { return showtimes; }

    public int getDayOffset() { return dayOffset; }
    public void setDayOffset(int dayOffset) { this.dayOffset = dayOffset; }
}
