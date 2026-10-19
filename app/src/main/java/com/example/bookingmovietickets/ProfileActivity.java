package com.example.bookingmovietickets;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ProfileActivity extends AppCompatActivity {

    private boolean isAdmin = true;

    private TextView tvRoleBadge, tvGender, tvDob;
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

    private void bindViews() {
        tvRoleBadge = findViewById(R.id.tv_role_badge);
        layoutAdminSection = findViewById(R.id.layout_admin_section);
        labelAdmin = findViewById(R.id.label_admin);
        tvGender = findViewById(R.id.tv_gender);
        tvDob = findViewById(R.id.tv_dob);
    }

    private void setupUserInfo() {
        if (tvRoleBadge != null) {
            tvRoleBadge.setVisibility(isAdmin ? View.VISIBLE : View.GONE);
        }

        findViewById(R.id.btn_edit_profile).setOnClickListener(v -> openEditProfile());
        findViewById(R.id.tv_avatar).setOnClickListener(v -> openEditProfile());
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

        findViewById(R.id.row_dashboard).setOnClickListener(v -> {
            Toast.makeText(this, "Dashboard (chưa làm)", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.row_manage_movies).setOnClickListener(v -> {
            Toast.makeText(this, "Quản lí phim (chưa làm)", Toast.LENGTH_SHORT).show();
        });
    }

    private void setupSettingsRows() {
        findViewById(R.id.row_ticket_history).setOnClickListener(v -> {
            Toast.makeText(this, "Lịch sử đặt vé (chưa làm)", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.row_change_password).setOnClickListener(v -> {
            Toast.makeText(this, "Đổi mật khẩu (chưa làm)", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.row_about).setOnClickListener(v -> {
            Toast.makeText(this, "Về CQS Cinema (chưa làm)", Toast.LENGTH_SHORT).show();
        });

        SwitchCompat switchNotification = findViewById(R.id.switch_notification);
        if (switchNotification != null) {
            switchNotification.setOnCheckedChangeListener((buttonView, isChecked) -> {
                Toast.makeText(this,
                        isChecked ? "Đã bật thông báo" : "Đã tắt thông báo",
                        Toast.LENGTH_SHORT).show();
            });
        }
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
        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void setupBottomNav() {
        findViewById(R.id.nav_home).setOnClickListener(v -> openHome());

        findViewById(R.id.nav_cinema).setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, CinemaActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });

        findViewById(R.id.nav_notification).setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, NotificationActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });
    }

    private void openHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
