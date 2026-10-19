package com.example.bookingmovietickets;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    private RecyclerView rvNotifications;
    private NotificationAdapter adapter;
    private List<Notification> notificationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        rvNotifications = findViewById(R.id.rvNotifications);
        if (rvNotifications != null) {
            rvNotifications.setLayoutManager(new LinearLayoutManager(this));
        }

        // 1. Khởi tạo dữ liệu mẫu
        initDummyData();

        // 2. Gán Adapter đổ dữ liệu vào RecyclerView
        adapter = new NotificationAdapter(this, notificationList);
        if (rvNotifications != null) {
            rvNotifications.setAdapter(adapter);
        }

        // 3. Cài đặt click chuyển tab Bottom Nav
        setupBottomNav();
    }

    private void initDummyData() {
        notificationList = new ArrayList<>();

        notificationList.add(new Notification(
                "NHẮC NHỚ SUẤT CHIẾU SẮP ĐẾN",
                "LẬT MẶT 7: ONE WISH",
                "Suất: 18:30 • Phòng 01",
                "30 phút nữa",
                Notification.TYPE_REMINDER,
                "XEM MÃ QR SOÁT VÉ"
        ));

        notificationList.add(new Notification(
                "ĐẶT VÉ THÀNH CÔNG",
                "LẬT MẶT 7: ONE WISH",
                "Vé: F5, F6 • CQS Nguyễn Trãi",
                "10 phút trước",
                Notification.TYPE_SUCCESS,
                "Xem chi tiết hóa đơn >"
        ));

        notificationList.add(new Notification(
                "TẶNG VOUCHER GIẢM 30K",
                "Mã CQS30K",
                "Áp dụng cho tất cả phim tại CQS Cinema",
                "2 giờ trước",
                Notification.TYPE_VOUCHER,
                "Áp dụng ngay >"
        ));
    }

    private void setupBottomNav() {
        if (findViewById(R.id.nav_home) != null) {
            findViewById(R.id.nav_home).setOnClickListener(v -> finish());
        }

        if (findViewById(R.id.nav_cinema) != null) {
            findViewById(R.id.nav_cinema).setOnClickListener(v -> {
                Intent intent = new Intent(this, SelectCinemaActivity.class);
                startActivity(intent);
            });
        }

        if (findViewById(R.id.nav_notification) != null) {
            findViewById(R.id.nav_notification).setOnClickListener(v ->
                    Toast.makeText(this, "Đang ở màn hình Thông báo", Toast.LENGTH_SHORT).show());
        }

        if (findViewById(R.id.nav_profile) != null) {
            findViewById(R.id.nav_profile).setOnClickListener(v -> {
                Intent intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
            });
        }
    }
}
