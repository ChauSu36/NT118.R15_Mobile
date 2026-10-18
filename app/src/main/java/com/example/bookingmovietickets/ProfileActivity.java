package com.example.bookingmovietickets;
// LƯU Ý: đổi dòng package ở trên cho khớp với package thật trong project của bạn.

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

public class ProfileActivity extends AppCompatActivity {

    // TODO: thay bằng cờ thật lấy từ SessionManager (đọc role trả về lúc đăng
    // nhập từ server). Để tạm true để khớp đúng ảnh thiết kế (tài khoản ADMIN).
    private boolean isAdmin = true;

    private TextView tvRoleBadge, tvGender, tvDob;
    private View layoutAdminSection, labelAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        bindViews();
        setupUserInfo();
        setupAdminSection();
        setupSettingsRows();
        setupLogout();
        setupBottomNav();
    }

    private void bindViews() {
        tvRoleBadge = findViewById(R.id.tv_role_badge);
        layoutAdminSection = findViewById(R.id.layout_admin_section);
        labelAdmin = findViewById(R.id.label_admin);
        tvGender = findViewById(R.id.tv_gender);
        tvDob = findViewById(R.id.tv_dob);
    }

    /**
     * Nạp dữ liệu người dùng lên giao diện.
     * TODO: thay bằng dữ liệu thật đọc từ SessionManager / API GET /api/users/me
     * (xem PROJECT_CONTEXT.md mục 5.7) thay vì các chuỗi hard-code trong layout.
     */
    private void setupUserInfo() {
        // tv_name, tv_email, tv_phone đã có sẵn giá trị đúng ảnh thiết kế trong
        // activity_profile.xml — khi nối API thật, set lại text cho chúng ở đây.

        tvRoleBadge.setVisibility(isAdmin ? View.VISIBLE : View.GONE);

        // Bấm vào avatar hoặc nút bút chì đều mở màn Sửa hồ sơ
        findViewById(R.id.btn_edit_profile).setOnClickListener(v -> openEditProfile());
        findViewById(R.id.tv_avatar).setOnClickListener(v -> openEditProfile());
    }

    private void openEditProfile() {
        Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
        startActivity(intent);
    }

    /** Ẩn toàn bộ khối "QUẢN TRỊ VIÊN" nếu tài khoản không phải ADMIN. */
    private void setupAdminSection() {
        int visibility = isAdmin ? View.VISIBLE : View.GONE;
        layoutAdminSection.setVisibility(visibility);
        labelAdmin.setVisibility(visibility);

        if (!isAdmin) return;

        findViewById(R.id.row_dashboard).setOnClickListener(v -> {
            // TODO: tạo DashboardActivity (màn thống kê doanh thu, SUM/COUNT từ
            // bookings) rồi đổi dòng dưới thành mở đúng Activity đó.
            Toast.makeText(this, "Dashboard (chưa làm)", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.row_manage_movies).setOnClickListener(v -> {
            // TODO: tạo màn Quản lí Phim & Suất chiếu dành cho admin.
            Toast.makeText(this, "Quản lí phim (chưa làm)", Toast.LENGTH_SHORT).show();
        });
    }

    private void setupSettingsRows() {
        findViewById(R.id.row_ticket_history).setOnClickListener(v -> {
            // TODO: mở màn Lịch sử đặt vé (danh sách bookings của user hiện tại).
            Toast.makeText(this, "Lịch sử đặt vé (chưa làm)", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.row_change_password).setOnClickListener(v -> {
            // TODO: tạo ChangePasswordActivity riêng (khác ResetPasswordActivity,
            // vì đổi mật khẩu lúc đã đăng nhập cần nhập mật khẩu cũ để xác nhận).
            Toast.makeText(this, "Đổi mật khẩu (chưa làm)", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.row_about).setOnClickListener(v -> {
            Toast.makeText(this, "Về CQS Cinema (chưa làm)", Toast.LENGTH_SHORT).show();
        });

        SwitchCompat switchNotification = findViewById(R.id.switch_notification);
        switchNotification.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // TODO: lưu lựa chọn này vào SharedPreferences, và AlarmReceiver nên
            // đọc cờ này trước khi hiển thị Notification (đã ghi trong checklist
            // Giai đoạn 10, bước "Bật/tắt thông báo").
            Toast.makeText(this,
                    isChecked ? "Đã bật thông báo" : "Đã tắt thông báo",
                    Toast.LENGTH_SHORT).show();
        });
    }

    private void setupLogout() {
        findViewById(R.id.btn_logout).setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Đăng xuất")
                    .setMessage("Bạn có chắc muốn đăng xuất tài khoản?")
                    .setNegativeButton("Hủy", null)
                    .setPositiveButton("Đăng xuất", (dialog, which) -> performLogout())
                    .show();
        });
    }

    private void performLogout() {
        // TODO: xóa JWT token + role đã lưu, thay vì chỉ demo bằng Toast.
        // Ví dụ khi đã có SessionManager:
        //   new SessionManager(this).clearSession();

        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void setupBottomNav() {
        // Đang ở tab Hồ sơ nên chỉ cần đóng màn này để quay lại đúng HomeActivity
        // (vì HomeActivity mở ProfileActivity bằng startActivity, không finish()).
        findViewById(R.id.nav_home).setOnClickListener(v -> finish());

        findViewById(R.id.nav_cinema).setOnClickListener(v ->
                Toast.makeText(this, "Rạp chiếu (chưa làm)", Toast.LENGTH_SHORT).show());

        findViewById(R.id.nav_notification).setOnClickListener(v ->
                Toast.makeText(this, "Thông báo (chưa làm)", Toast.LENGTH_SHORT).show());

        // nav_profile không cần gắn gì — đang đứng sẵn ở đây rồi.
    }
}
