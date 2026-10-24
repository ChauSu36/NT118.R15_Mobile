package com.example.bookingmovietickets;

import android.content.Intent;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.ImageView;
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
    private ImageView ivToggleNewPassword, ivToggleConfirmPassword;
    private MaterialButton btnResetPassword;

    private boolean isNewPassVisible = false;
    private boolean isConfirmPassVisible = false;

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

        ivToggleNewPassword = findViewById(R.id.ivToggleNewPassword);
        ivToggleConfirmPassword = findViewById(R.id.ivToggleConfirmPassword);

        btnResetPassword = findViewById(R.id.btnResetPassword);
        TextView btnBack = findViewById(R.id.btnBack);

        // Bật/tắt hiển thị mật khẩu bằng icon con mắt
        setupPasswordToggles();

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
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        // Xử lý khi nhấn nút quay lại (<)
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> {
                Intent intent = new Intent(ResetPasswordActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            });
        }
    }

    private void setupPasswordToggles() {
        if (ivToggleNewPassword != null && edtNewPassword != null) {
            ivToggleNewPassword.setOnClickListener(v -> {
                isNewPassVisible = !isNewPassVisible;
                toggleVisibility(edtNewPassword, ivToggleNewPassword, isNewPassVisible);
            });
        }

        if (ivToggleConfirmPassword != null && edtConfirmPassword != null) {
            ivToggleConfirmPassword.setOnClickListener(v -> {
                isConfirmPassVisible = !isConfirmPassVisible;
                toggleVisibility(edtConfirmPassword, ivToggleConfirmPassword, isConfirmPassVisible);
            });
        }
    }

    private void toggleVisibility(EditText editText, ImageView toggleIcon, boolean isVisible) {
        if (isVisible) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            toggleIcon.setImageResource(R.drawable.ic_eye_visible);
        } else {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            toggleIcon.setImageResource(R.drawable.ic_eye_invisible);
        }
        editText.setSelection(editText.getText().length());
    }
}
