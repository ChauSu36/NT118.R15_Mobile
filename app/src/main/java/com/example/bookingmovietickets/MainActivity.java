package com.example.bookingmovietickets;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private EditText edtEmail, edtPassword;
    private View btnLogin;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        View mainView = findViewById(R.id.main);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        bindViews();
        setupBannerShader();
        setupPasswordToggle();
        setupNavigationLinks();

        // Bắt sự kiện bấm nút Đăng nhập
        if (btnLogin != null) {
            btnLogin.setOnClickListener(v -> thuHienDangNhap());
        }
    }

    private void bindViews() {
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
    }

    private void setupBannerShader() {
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
    }

    private void setupPasswordToggle() {
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
    }

    private void setupNavigationLinks() {
        // Chuyển sang màn hình Đăng ký
        LinearLayout llSignUp = findViewById(R.id.llSignUp);
        if (llSignUp != null) {
            llSignUp.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            });
        }

        // Chuyển sang màn hình Quên mật khẩu
        TextView tvForgotPassword = findViewById(R.id.tvForgotPassword);
        if (tvForgotPassword != null) {
            tvForgotPassword.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, ResetPasswordActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            });
        }
    }

    private void thuHienDangNhap() {
        String email = edtEmail != null ? edtEmail.getText().toString().trim() : "";
        String password = edtPassword != null ? edtPassword.getText().toString().trim() : "";

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập Email và Mật khẩu!", Toast.LENGTH_SHORT).show();
            return;
        }

        LoginRequest request = new LoginRequest(email, password);

        // Gọi API Đăng nhập qua Retrofit POST api/users/login
        ApiClient.getApiService().dangNhap(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse res = response.body();

                    // 1. Lưu JWT Token và toàn bộ thông tin User từ Database vào SharedPrefManager
                    if (res.getUser() != null) {
                        User u = res.getUser();
                        SharedPrefManager pref = SharedPrefManager.getInstance(MainActivity.this);
                        pref.saveUserSession(
                                u.getUser_id(),
                                u.getFull_name(),
                                u.getEmail(),
                                u.getPhone(),
                                u.getRole(),
                                res.getToken(),
                                u.getAvatar_url()
                        );
                        if (u.getGender() != null && !u.getGender().isEmpty()) {
                            pref.saveGender(u.getGender());
                        }
                        if (u.getDob() != null && !u.getDob().isEmpty()) {
                            String rawDob = u.getDob();
                            if (rawDob.contains("T")) {
                                rawDob = rawDob.split("T")[0];
                            }
                            if (rawDob.contains("-")) {
                                String[] parts = rawDob.split("-");
                                if (parts.length == 3) {
                                    rawDob = String.format("%s/%s/%s", parts[2], parts[1], parts[0]);
                                }
                            }
                            pref.saveDob(rawDob);
                        }
                    }

                    String message = res.getThongBao() != null ? res.getThongBao() : "Đăng nhập thành công!";
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();

                    // 2. Chuyển hướng sang Trang chủ HomeActivity
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                } else {
                    // Đọc lỗi trả về từ Backend (Mật khẩu sai hoặc Tài khoản chưa được kích hoạt OTP)
                    try (ResponseBody errorBody = response.errorBody()) {
                        if (errorBody != null) {
                            String errJson = errorBody.string();
                            LoginResponse errRes = new Gson().fromJson(errJson, LoginResponse.class);
                            if (errRes != null && errRes.getThongBao() != null && !errRes.getThongBao().isEmpty()) {
                                String serverMsg = errRes.getThongBao();
                                Toast.makeText(MainActivity.this, serverMsg, Toast.LENGTH_LONG).show();

                                String lowerMsg = serverMsg.toLowerCase();
                                // Nếu là thông báo chưa kích hoạt tài khoản OTP (mã 403) -> Gửi mã OTP mới và mở màn hình VerifyCode
                                if (lowerMsg.contains("xác thực otp") || lowerMsg.contains("chưa được kích hoạt") || lowerMsg.contains("chưa kích hoạt") || response.code() == 403) {
                                    ResendOtpRequest resendReq = new ResendOtpRequest(email);
                                    ApiClient.getApiService().resendOtp(resendReq).enqueue(new Callback<ResendOtpResponse>() {
                                        @Override
                                        public void onResponse(Call<ResendOtpResponse> call, Response<ResendOtpResponse> resendResponse) {
                                            Intent intent = new Intent(MainActivity.this, VerifyCode.class);
                                            intent.putExtra(VerifyCode.EXTRA_EMAIL, email);
                                            intent.putExtra(VerifyCode.EXTRA_FLOW_TYPE, VerifyCode.FLOW_REGISTER);
                                            startActivity(intent);
                                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                        }

                                        @Override
                                        public void onFailure(Call<ResendOtpResponse> call, Throwable t) {
                                            Intent intent = new Intent(MainActivity.this, VerifyCode.class);
                                            intent.putExtra(VerifyCode.EXTRA_EMAIL, email);
                                            intent.putExtra(VerifyCode.EXTRA_FLOW_TYPE, VerifyCode.FLOW_REGISTER);
                                            startActivity(intent);
                                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                        }
                                    });
                                    return;
                                }

                                return;
                            }
                        }
                    } catch (Exception ignored) {}

                    Toast.makeText(MainActivity.this, "Email hoặc mật khẩu không chính xác!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Lỗi kết nối máy chủ: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
