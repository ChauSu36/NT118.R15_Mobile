package com.example.bookingmovietickets;

import android.content.Intent;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

public class ResetPasswordActivity extends AppCompatActivity {
    private EditText edtEmail, edtNewPassword, edtConfirmPassword;
    private MaterialButton btnResetPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reset_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        TextView tvBannerTitle = findViewById(R.id.tvBannerTitle);
        if (tvBannerTitle != null) {
            tvBannerTitle.post(() -> {
                int topColor = ContextCompat.getColor(this, R.color.lightblue);
                int bottomColor = ContextCompat.getColor(this, R.color.drakblue);
                float height = tvBannerTitle.getPaint().descent() - tvBannerTitle.getPaint().ascent();
                if (height <= 0) {
                    height = tvBannerTitle.getHeight();
                }
                Shader textShader = new LinearGradient(
                        0, 0, 0, height,
                        new int[]{topColor, topColor, bottomColor, bottomColor},
                        new float[]{0.0f, 0.48f, 0.52f, 1.0f},
                        Shader.TileMode.CLAMP
                );
                tvBannerTitle.getPaint().setShader(textShader);
                tvBannerTitle.invalidate();
            });
        }
        // Ánh xạ
        edtEmail = findViewById(R.id.edtEmail);
        edtNewPassword = findViewById(R.id.edtNewPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        btnResetPassword = findViewById(R.id.btnResetPassword);
        TextView btnBack = findViewById(R.id.btnBack);

        // Xử lý khi nhấn nút Reset
        btnResetPassword.setOnClickListener(v -> {
            String email = edtEmail.getText().toString().trim();
            String newPass = edtNewPassword.getText().toString().trim();
            String confirmPass = edtConfirmPassword.getText().toString().trim();

            if (email.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
                Toast.makeText(ResetPasswordActivity.this, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
            } else if (!newPass.equals(confirmPass)) {
                Toast.makeText(ResetPasswordActivity.this, "Mật khẩu không khớp!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ResetPasswordActivity.this, "Cập nhật mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                finish(); // Tự động đóng màn hình này để quay lại Login
            }
        });

        // Xử lý khi nhấn nút quay lại (<)
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> {
                Intent intent = new Intent(ResetPasswordActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            });
        }
    }
}