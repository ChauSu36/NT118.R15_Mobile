package com.example.bookingmovietickets;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

public class ProfileActivity extends AppCompatActivity {

    private boolean isAdmin = false;

    private TextView tvAvatar, tvName, tvEmail, tvPhone, tvRoleBadge, tvGender, tvDob;
    private ImageView ivAvatarProfile;
    private View layoutAdminSection, labelAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        View mainView = findViewById(R.id.main);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        bindViews();
        setupUserInfo();
        setupAdminSection();
        setupSettingsRows();
        setupLogout();
        setupBottomNav();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupUserInfo(); // Tự động load lại dữ liệu mới khi quay lại từ EditProfileActivity
    }

    private void bindViews() {
        tvAvatar = findViewById(R.id.tv_avatar);
        ivAvatarProfile = findViewById(R.id.iv_avatar_profile);
        tvName = findViewById(R.id.tv_name);
        tvEmail = findViewById(R.id.tv_email);
        tvPhone = findViewById(R.id.tv_phone);
        tvRoleBadge = findViewById(R.id.tv_role_badge);
        layoutAdminSection = findViewById(R.id.layout_admin_section);
        labelAdmin = findViewById(R.id.label_admin);
        tvGender = findViewById(R.id.tv_gender);
        tvDob = findViewById(R.id.tv_dob);
    }

    private void setupUserInfo() {
        // 1. Đọc dữ liệu đã lưu khi đăng nhập từ SharedPrefManager
        SharedPrefManager pref = SharedPrefManager.getInstance(this);
        String fullName = pref.getFullName();
        String email = pref.getEmail();
        String phone = pref.getPhone();
        String gender = pref.getGender();
        String dob = pref.getDob();
        String role = pref.getRole();
        String avatarUrl = pref.getAvatarUrl();

        // 2. Phân quyền Admin
        isAdmin = "admin".equalsIgnoreCase(role);

        // 3. Hiển thị lên giao diện
        if (tvName != null) tvName.setText(fullName);
        if (tvEmail != null) tvEmail.setText(email);
        if (tvPhone != null) tvPhone.setText("SĐT: " + phone);
        if (tvGender != null) tvGender.setText("Giới tính: " + gender);
        if (tvDob != null) tvDob.setText("Ngày sinh: " + dob);

        // Nạp Avatar (ảnh từ Glide nếu có, hoặc chữ cái đầu tên)
        if (avatarUrl != null && !avatarUrl.isEmpty() && ivAvatarProfile != null) {
            ivAvatarProfile.setVisibility(View.VISIBLE);
            if (tvAvatar != null) tvAvatar.setVisibility(View.GONE);

            Glide.with(this)
                    .load(avatarUrl)
                    .placeholder(R.drawable.bg_avatar_circle)
                    .error(R.drawable.bg_avatar_circle)
                    .circleCrop()
                    .into(ivAvatarProfile);
        } else if (tvAvatar != null && fullName != null && !fullName.isEmpty()) {
            tvAvatar.setVisibility(View.VISIBLE);
            if (ivAvatarProfile != null) ivAvatarProfile.setVisibility(View.GONE);
            tvAvatar.setText(String.valueOf(fullName.charAt(0)).toUpperCase());
        }

        if (tvRoleBadge != null) {
            tvRoleBadge.setVisibility(isAdmin ? View.VISIBLE : View.GONE);
        }

        View btnEdit = findViewById(R.id.btn_edit_profile);
        if (btnEdit != null) btnEdit.setOnClickListener(v -> openEditProfile());
        if (tvAvatar != null) tvAvatar.setOnClickListener(v -> openEditProfile());
        if (ivAvatarProfile != null) ivAvatarProfile.setOnClickListener(v -> openEditProfile());
    }

    private void openEditProfile() {
        Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
        startActivity(intent);
    }

    private void setupAdminSection() {
        int visibility = isAdmin ? View.VISIBLE : View.GONE;
        if (layoutAdminSection != null) layoutAdminSection.setVisibility(visibility);
        if (labelAdmin != null) labelAdmin.setVisibility(visibility);

        if (!isAdmin) return;

        View rowDashboard = findViewById(R.id.row_dashboard);
        if (rowDashboard != null) {
            rowDashboard.setOnClickListener(v -> {
                Intent intent = new Intent(ProfileActivity.this, DashboardActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            });
        }

        View rowManageMovies = findViewById(R.id.row_manage_movies);
        if (rowManageMovies != null) {
            rowManageMovies.setOnClickListener(v -> {
                Intent intent = new Intent(ProfileActivity.this, ManageMoviesActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            });
        }
    }

    private void setupSettingsRows() {
        View rowTicketHistory = findViewById(R.id.row_ticket_history);
        if (rowTicketHistory != null) {
            rowTicketHistory.setOnClickListener(v -> {
                Intent intent = new Intent(ProfileActivity.this, TicketHistoryActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            });
        }

        View rowChangePassword = findViewById(R.id.row_change_password);
        if (rowChangePassword != null) {
            rowChangePassword.setOnClickListener(v -> {
                Intent intent = new Intent(ProfileActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            });
        }

        View rowAbout = findViewById(R.id.row_about);
        if (rowAbout != null) {
            rowAbout.setOnClickListener(v -> {
                Intent intent = new Intent(ProfileActivity.this, AboutTermsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            });
        }

        SwitchCompat switchNotification = findViewById(R.id.switch_notification);
        if (switchNotification != null) {
            switchNotification.setOnCheckedChangeListener((buttonView, isChecked) -> {
                Toast.makeText(this,
                        isChecked ? "Đã BẬT thông báo nhắc lịch chiếu" : "Đã TẮT thông báo nhắc lịch chiếu",
                        Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void setupLogout() {
        View btnLogout = findViewById(R.id.btn_logout);
        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> {
                new AlertDialog.Builder(this)
                        .setTitle("Đăng xuất")
                        .setMessage("Bạn có chắc muốn đăng xuất tài khoản?")
                        .setNegativeButton("Hủy", null)
                        .setPositiveButton("Đăng xuất", (dialog, which) -> performLogout())
                        .show();
            });
        }
    }

    private void performLogout() {
        // 1. Xóa sạch dữ liệu đăng nhập trong SharedPrefManager
        SharedPrefManager.getInstance(this).clearSession();

        Toast.makeText(this, "Đã đăng xuất tài khoản!", Toast.LENGTH_SHORT).show();

        // 2. Quay lại MainActivity và xóa sạch luồng Activity cũ
        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void setupBottomNav() {
        View navHome = findViewById(R.id.nav_home);
        if (navHome != null) navHome.setOnClickListener(v -> openHome());

        View navCinema = findViewById(R.id.nav_cinema);
        if (navCinema != null) {
            navCinema.setOnClickListener(v -> {
                Intent intent = new Intent(ProfileActivity.this, CinemaActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            });
        }

        View navNotification = findViewById(R.id.nav_notification);
        if (navNotification != null) {
            navNotification.setOnClickListener(v -> {
                Intent intent = new Intent(ProfileActivity.this, NotificationActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            });
        }
    }

    private void openHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
