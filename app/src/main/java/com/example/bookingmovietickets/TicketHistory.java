package com.example.bookingmovietickets;

public class TicketHistory {
    private String orderCode;
    private String movieTitle;
    private String cinemaName;
    private String showtime;
    private String hallName;
    private String seats;
    private String totalPrice;
    private String status; // "SẮP XEM", "ĐÃ XEM", "ĐÃ HỦY"
    private String subType; // "2D Phụ đề"

    public TicketHistory(String orderCode, String movieTitle, String cinemaName,
                         String showtime, String hallName, String seats,
                         String totalPrice, String status, String subType) {
        this.orderCode = orderCode;
        this.movieTitle = movieTitle;
        this.cinemaName = cinemaName;
        this.showtime = showtime;
        this.hallName = hallName;
        this.seats = seats;
        this.totalPrice = totalPrice;
        this.status = status;
        this.subType = subType;
    }

    public String getOrderCode() { return orderCode; }
    public String getMovieTitle() { return movieTitle; }
    public String getCinemaName() { return cinemaName; }
    public String getShowtime() { return showtime; }
    public String getHallName() { return hallName; }
    public String getSeats() { return seats; }
    public String getTotalPrice() { return totalPrice; }
    public String getStatus() { return status; }
    public String getSubType() { return subType; }
}
