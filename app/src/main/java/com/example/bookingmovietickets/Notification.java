package com.example.bookingmovietickets;

public class Notification {
    public static final int TYPE_REMINDER = 1; // Nhắc suất chiếu (có nút Mã QR)
    public static final int TYPE_SUCCESS = 2;  // Đặt vé thành công (có link Hóa đơn)
    public static final int TYPE_VOUCHER = 3;  // Tặng voucher (có link Áp dụng)

    private String title;
    private String subtitle;
    private String detail;
    private String time;
    private int type;
    private String actionText;

    public Notification(String title, String subtitle, String detail, String time, int type, String actionText) {
        this.title = title;
        this.subtitle = subtitle;
        this.detail = detail;
        this.time = time;
        this.type = type;
        this.actionText = actionText;
    }

    public String getTitle() { return title; }
    public String getSubtitle() { return subtitle; }
    public String getDetail() { return detail; }
    public String getTime() { return time; }
    public int getType() { return type; }
    public String getActionText() { return actionText; }
}