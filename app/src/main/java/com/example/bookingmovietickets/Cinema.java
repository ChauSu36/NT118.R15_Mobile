package com.example.bookingmovietickets;

import java.util.List;

public class Cinema {
    private String name;
    private String address;
    private String distance;
    private boolean isNear; // true nếu rạp gần người dùng (thêm viền đỏ + badge)
    private List<String> showtimes;
    private String selectedShowtime = "";

    public Cinema(String name, String address, String distance, boolean isNear, List<String> showtimes) {
        this.name = name;
        this.address = address;
        this.distance = distance;
        this.isNear = isNear;
        this.showtimes = showtimes;
    }

    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getDistance() { return distance; }
    public boolean isNear() { return isNear; }
    public List<String> getShowtimes() { return showtimes; }

    public String getSelectedShowtime() { return selectedShowtime; }
    public void setSelectedShowtime(String selectedShowtime) { this.selectedShowtime = selectedShowtime; }
}
