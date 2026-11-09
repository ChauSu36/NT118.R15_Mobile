package com.example.bookingmovietickets;

import android.content.Intent;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
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
import com.google.gson.Gson;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        // Cài đặt con mắt bật/tắt hiển thị mật khẩu
        setupPasswordToggles();

        // Xử lý khi nhấn nút THAY ĐỔI MẬT KHẨU
        if (btnResetPassword != null) {
            btnResetPassword.setOnClickListener(v -> handleForgotPasswordRequest());
        }

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

    private void handleForgotPasswordRequest() {
        String email = edtEmail != null ? edtEmail.getText().toString().trim() : "";
        String newPass = edtNewPassword != null ? edtNewPassword.getText().toString().trim() : "";
        String confirmPass = edtConfirmPassword != null ? edtConfirmPassword.getText().toString().trim() : "";

        // 1. Validation kiểm tra nhập đầy đủ thông tin
        if (email.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
            Toast.makeText(ResetPasswordActivity.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        // 2. Kiểm tra các tiêu chí bảo mật mật khẩu mới
        boolean hasMinLength = newPass.length() >= 8;
        boolean hasDigit = newPass.matches(".*\\d.*");
        boolean hasUppercase = newPass.matches(".*[A-Z].*");
        boolean hasSpecial = newPass.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*");

        if (!hasMinLength || !hasDigit || !hasUppercase || !hasSpecial) {
            Toast.makeText(this, "Mật khẩu mới chưa đáp ứng đầy đủ yêu cầu bảo mật!", Toast.LENGTH_SHORT).show();
            return;
        }

        // 3. Kiểm tra mật khẩu xác nhận
        if (!newPass.equals(confirmPass)) {
            Toast.makeText(ResetPasswordActivity.this, "Mật khẩu xác nhận không trùng khớp!", Toast.LENGTH_SHORT).show();
            return;
        }

        // 4. Gọi API Quên mật khẩu gửi OTP về Gmail: POST /api/users/forgot-pass
        ResendOtpRequest request = new ResendOtpRequest(email);
        ApiClient.getApiService().forgotPass(request).enqueue(new Callback<ResendOtpResponse>() {
            @Override
            public void onResponse(Call<ResendOtpResponse> call, Response<ResendOtpResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ResendOtpResponse res = response.body();
                    String msg = res.getThongBao() != null ? res.getThongBao() : "Mã OTP khôi phục mật khẩu đã được gửi về Gmail!";
                    Toast.makeText(ResetPasswordActivity.this, msg, Toast.LENGTH_LONG).show();

                    // Chuyển sang màn hình nhập mã OTP VerifyCode truyền kèm Email và Mật khẩu mới
                    Intent intent = new Intent(ResetPasswordActivity.this, VerifyCode.class);
                    intent.putExtra(VerifyCode.EXTRA_EMAIL, email);
                    intent.putExtra(VerifyCode.EXTRA_NEW_PASSWORD, newPass);
                    intent.putExtra(VerifyCode.EXTRA_FLOW_TYPE, VerifyCode.FLOW_FORGOT_PASSWORD);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                } else {
                    try (ResponseBody errorBody = response.errorBody()) {
                        if (errorBody != null) {
                            String errJson = errorBody.string();
                            ResendOtpResponse errRes = new Gson().fromJson(errJson, ResendOtpResponse.class);
                            if (errRes != null && errRes.getThongBao() != null && !errRes.getThongBao().isEmpty()) {
                                Toast.makeText(ResetPasswordActivity.this, errRes.getThongBao(), Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    } catch (Exception ignored) {}

                    Toast.makeText(ResetPasswordActivity.this, "Email không tồn tại hoặc lỗi kết nối!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResendOtpResponse> call, Throwable t) {
                Toast.makeText(ResetPasswordActivity.this, "Lỗi kết nối máy chủ: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
