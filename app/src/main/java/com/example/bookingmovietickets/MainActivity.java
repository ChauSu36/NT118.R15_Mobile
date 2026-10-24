package com.example.bookingmovietickets;

import android.content.Intent;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
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

        // Bật/tắt hiển thị mật khẩu bằng icon con mắt
        EditText edtPassword = findViewById(R.id.edtPassword);
        ImageView ivTogglePassword = findViewById(R.id.ivTogglePassword);

        if (ivTogglePassword != null && edtPassword != null) {
            ivTogglePassword.setOnClickListener(v -> {
                isPasswordVisible = !isPasswordVisible;
                if (isPasswordVisible) {
                    edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    ivTogglePassword.setImageResource(R.drawable.ic_eye_visible);
                } else {
                    edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    ivTogglePassword.setImageResource(R.drawable.ic_eye_invisible);
                }
                edtPassword.setSelection(edtPassword.getText().length());
            });
        }

        LinearLayout llSignUp = findViewById(R.id.llSignUp);
        if (llSignUp != null) {
            llSignUp.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            });
        }

        // Ánh xạ chữ Forgot Password
        TextView tvForgotPassword = findViewById(R.id.tvForgotPassword);

        // Sự kiện bấm vào chữ Forgot Password -> Chuyển sang trang Reset
        if (tvForgotPassword != null) {
            tvForgotPassword.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, ResetPasswordActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            });
        }

        // Ánh xạ nút Đăng nhập
        View btnLogin = findViewById(R.id.btnLogin);
        if (btnLogin != null) {
            btnLogin.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            });
        }
    }
}
