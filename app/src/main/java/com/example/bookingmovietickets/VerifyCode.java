package com.example.bookingmovietickets;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyCode extends AppCompatActivity {

    public static final String EXTRA_EMAIL = "EXTRA_EMAIL";
    public static final String EXTRA_NEW_PASSWORD = "EXTRA_NEW_PASSWORD";
    public static final String EXTRA_FLOW_TYPE = "EXTRA_FLOW_TYPE";
    public static final String FLOW_REGISTER = "REGISTER";
    public static final String FLOW_FORGOT_PASSWORD = "FORGOT_PASSWORD";

    private EditText etCode1, etCode2, etCode3, etCode4, etCode5, etCode6;
    private MaterialButton btnVerifyCode;
    private TextView tvSubtitle, tvResendCode;

    private String userEmail = "";
    private String newPassword = "";
    private String flowType = FLOW_REGISTER;

    private CountDownTimer countDownTimer;
    private static final long TIMER_DURATION_MS = 3 * 60 * 1000; // 3 phút = 180.000 ms
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_verify_code);

        View mainView = findViewById(R.id.main);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        readIntentData();
        bindViews();
        setupBannerShader();
        setupOtpFocusNavigation();
        setupEvents();
        startCountdownTimer();
    }

    private void readIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(EXTRA_EMAIL)) {
                userEmail = intent.getStringExtra(EXTRA_EMAIL);
            } else if (intent.hasExtra("email")) {
                userEmail = intent.getStringExtra("email");
            }

            if (intent.hasExtra(EXTRA_NEW_PASSWORD)) {
                newPassword = intent.getStringExtra(EXTRA_NEW_PASSWORD);
            } else if (intent.hasExtra("newPassword")) {
                newPassword = intent.getStringExtra("newPassword");
            }

            if (intent.hasExtra(EXTRA_FLOW_TYPE)) {
                flowType = intent.getStringExtra(EXTRA_FLOW_TYPE);
            } else if (intent.hasExtra("FLOW_TYPE")) {
                flowType = intent.getStringExtra("FLOW_TYPE");
            }
        }
        if (userEmail == null) userEmail = "";
        if (newPassword == null) newPassword = "";
        if (flowType == null) flowType = FLOW_REGISTER;
    }

    private void bindViews() {
        etCode1 = findViewById(R.id.et_code_1);
        etCode2 = findViewById(R.id.et_code_2);
        etCode3 = findViewById(R.id.et_code_3);
        etCode4 = findViewById(R.id.et_code_4);
        etCode5 = findViewById(R.id.et_code_5);
        etCode6 = findViewById(R.id.et_code_6);

        btnVerifyCode = findViewById(R.id.btnVerifyCode);
        tvSubtitle = findViewById(R.id.tvSubtitle);
        tvResendCode = findViewById(R.id.tvResendCode);

        if (tvSubtitle != null && !userEmail.isEmpty()) {
            tvSubtitle.setText("Please enter the 6-digit code sent to " + userEmail);
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang xử lý...");
        progressDialog.setCancelable(false);
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

    /**
     * Tự động chuyển focus:
     * - Khi gõ 1 chữ số ➔ Tự động nhảy sang ô tiếp theo.
     * - Khi nhấn phím Xóa (Backspace) mà ô hiện tại rỗng ➔ Tự động lùi về ô phía trước.
     */
    private void setupOtpFocusNavigation() {
        EditText[] otpBoxes = new EditText[]{etCode1, etCode2, etCode3, etCode4, etCode5, etCode6};

        for (int i = 0; i < otpBoxes.length; i++) {
            final int index = i;
            if (otpBoxes[i] == null) continue;

            otpBoxes[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() == 1 && index < otpBoxes.length - 1) {
                        otpBoxes[index + 1].requestFocus();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });

            otpBoxes[i].setOnKeyListener((v, keyCode, event) -> {
                if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (otpBoxes[index].getText().toString().isEmpty() && index > 0) {
                        otpBoxes[index - 1].requestFocus();
                        otpBoxes[index - 1].setText("");
                        return true;
                    }
                }
                return false;
            });
        }
    }

    private void setupEvents() {
        if (btnVerifyCode != null) {
            btnVerifyCode.setOnClickListener(v -> handleVerifyOtp());
        }

        if (tvResendCode != null) {
            tvResendCode.setOnClickListener(v -> handleResendOtp());
        }
    }

    /** Thu thập mã 6 số OTP từ 6 ô nhập. */
    private String getOtpCodeFromBoxes() {
        StringBuilder sb = new StringBuilder();
        EditText[] otpBoxes = new EditText[]{etCode1, etCode2, etCode3, etCode4, etCode5, etCode6};
        for (EditText box : otpBoxes) {
            if (box != null) {
                sb.append(box.getText().toString().trim());
            }
        }
        return sb.toString();
    }

    /** Bắt đầu bộ đếm ngược 3 phút (180.000 ms). */
    private void startCountdownTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        if (tvResendCode != null) {
            tvResendCode.setEnabled(false);
            tvResendCode.setAlpha(0.5f);
        }

        countDownTimer = new CountDownTimer(TIMER_DURATION_MS, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long totalSeconds = millisUntilFinished / 1000;
                long minutes = totalSeconds / 60;
                long seconds = totalSeconds % 60;
                String timeStr = String.format(Locale.getDefault(), " (%02d:%02d)", minutes, seconds);
                if (tvResendCode != null) {
                    tvResendCode.setText("Resend Code" + timeStr);
                }
            }

            @Override
            public void onFinish() {
                if (tvResendCode != null) {
                    tvResendCode.setText("Resend Code");
                    tvResendCode.setEnabled(true);
                    tvResendCode.setAlpha(1.0f);
                }
            }
        };
        countDownTimer.start();
    }

    /** Xử lý khi nhấn nút Verify Code. */
    private void handleVerifyOtp() {
        String otpCode = getOtpCodeFromBoxes();

        if (otpCode.length() < 6) {
            Toast.makeText(this, "Vui lòng nhập đủ mã OTP 6 chữ số!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (userEmail.isEmpty()) {
            Toast.makeText(this, "Email không hợp lệ!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (FLOW_FORGOT_PASSWORD.equalsIgnoreCase(flowType)) {
            if (newPassword != null && !newPassword.isEmpty()) {
                // Đã có Mật khẩu mới được nhập từ ResetPasswordActivity -> Gọi ngay API reset-pass
                executeResetPassword(userEmail, otpCode, newPassword);
            } else {
                showNewPasswordDialog(userEmail, otpCode);
            }
        } else {
            // Luồng Đăng Ký Tài Khoản: Gọi API POST /api/users/verify-otp
            executeVerifyRegisterOtp(userEmail, otpCode);
        }
    }

    /** Gọi API xác thực OTP Đăng Ký (`POST /api/users/verify-otp`). */
    private void executeVerifyRegisterOtp(String email, String otpCode) {
        showLoading(true);

        VerifyOtpRequest request = new VerifyOtpRequest(email, otpCode);
        ApiClient.getApiService().verifyOtp(request).enqueue(new Callback<VerifyOtpResponse>() {
            @Override
            public void onResponse(Call<VerifyOtpResponse> call, Response<VerifyOtpResponse> response) {
                showLoading(false);
                if (response.isSuccessful() && response.body() != null) {
                    VerifyOtpResponse res = response.body();
                    String msg = res.getThongBao() != null ? res.getThongBao() : "Kích hoạt tài khoản thành công!";
                    Toast.makeText(VerifyCode.this, msg, Toast.LENGTH_LONG).show();

                    // Chuyển người dùng về màn hình Đăng Nhập
                    Intent intent = new Intent(VerifyCode.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                } else {
                    try (ResponseBody errorBody = response.errorBody()) {
                        if (errorBody != null) {
                            String errJson = errorBody.string();
                            VerifyOtpResponse errRes = new Gson().fromJson(errJson, VerifyOtpResponse.class);
                            if (errRes != null && errRes.getThongBao() != null && !errRes.getThongBao().isEmpty()) {
                                Toast.makeText(VerifyCode.this, errRes.getThongBao(), Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    } catch (Exception ignored) {}

                    Toast.makeText(VerifyCode.this, "Mã OTP không chính xác hoặc đã hết hạn!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<VerifyOtpResponse> call, Throwable t) {
                showLoading(false);
                Toast.makeText(VerifyCode.this, "Lỗi kết nối máy chủ: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Bật AlertDialog cho luồng Quên mật khẩu nếu chưa truyền mật khẩu mới từ màn hình trước.
     */
    private void showNewPasswordDialog(String email, String otpCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Đặt Lại Mật Khẩu Mới");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(48, 24, 48, 24);

        final EditText etNewPassword = new EditText(this);
        etNewPassword.setHint("Mật khẩu mới (Tối thiểu 8 ký tự)");
        etNewPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        layout.addView(etNewPassword);

        final EditText etConfirmPassword = new EditText(this);
        etConfirmPassword.setHint("Xác nhận mật khẩu mới");
        etConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        layout.addView(etConfirmPassword);

        builder.setView(layout);

        builder.setPositiveButton("XÁC NHẬN", (dialog, which) -> {
            String newPass = etNewPassword.getText().toString().trim();
            String confirmPass = etConfirmPassword.getText().toString().trim();

            if (newPass.isEmpty() || confirmPass.isEmpty()) {
                Toast.makeText(VerifyCode.this, "Vui lòng nhập mật khẩu mới!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newPass.equals(confirmPass)) {
                Toast.makeText(VerifyCode.this, "Mật khẩu xác nhận không trùng khớp!", Toast.LENGTH_SHORT).show();
                return;
            }

            executeResetPassword(email, otpCode, newPass);
        });

        builder.setNegativeButton("HỦY", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    /** Gọi API `POST /api/users/reset-pass`. */
    private void executeResetPassword(String email, String otpCode, String newPassword) {
        showLoading(true);

        ResetPassRequest request = new ResetPassRequest(email, otpCode, newPassword);
        ApiClient.getApiService().resetPass(request).enqueue(new Callback<ResetPassResponse>() {
            @Override
            public void onResponse(Call<ResetPassResponse> call, Response<ResetPassResponse> response) {
                showLoading(false);
                if (response.isSuccessful() && response.body() != null) {
                    ResetPassResponse res = response.body();
                    String msg = res.getThongBao() != null ? res.getThongBao() : "Đặt lại mật khẩu thành công!";
                    Toast.makeText(VerifyCode.this, msg, Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(VerifyCode.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                } else {
                    try (ResponseBody errorBody = response.errorBody()) {
                        if (errorBody != null) {
                            String errJson = errorBody.string();
                            ResetPassResponse errRes = new Gson().fromJson(errJson, ResetPassResponse.class);
                            if (errRes != null && errRes.getThongBao() != null && !errRes.getThongBao().isEmpty()) {
                                Toast.makeText(VerifyCode.this, errRes.getThongBao(), Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    } catch (Exception ignored) {}

                    Toast.makeText(VerifyCode.this, "Đặt lại mật khẩu thất bại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResetPassResponse> call, Throwable t) {
                showLoading(false);
                Toast.makeText(VerifyCode.this, "Lỗi kết nối máy chủ: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /** Gọi API Gửi lại mã OTP (`POST /api/users/resend-otp` hoặc `POST /api/users/forgot-pass`). */
    private void handleResendOtp() {
        if (userEmail.isEmpty()) {
            Toast.makeText(this, "Email không hợp lệ!", Toast.LENGTH_SHORT).show();
            return;
        }

        showLoading(true);

        ResendOtpRequest request = new ResendOtpRequest(userEmail);

        Callback<ResendOtpResponse> resendCallback = new Callback<ResendOtpResponse>() {
            @Override
            public void onResponse(Call<ResendOtpResponse> call, Response<ResendOtpResponse> response) {
                showLoading(false);
                if (response.isSuccessful() && response.body() != null) {
                    ResendOtpResponse res = response.body();
                    String msg = res.getThongBao() != null ? res.getThongBao() : "Mã OTP mới đã được gửi vào Email!";
                    Toast.makeText(VerifyCode.this, msg, Toast.LENGTH_LONG).show();

                    clearOtpBoxes();
                    startCountdownTimer();
                } else {
                    Toast.makeText(VerifyCode.this, "Gửi lại mã OTP thất bại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResendOtpResponse> call, Throwable t) {
                showLoading(false);
                Toast.makeText(VerifyCode.this, "Lỗi kết nối máy chủ: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };

        if (FLOW_FORGOT_PASSWORD.equalsIgnoreCase(flowType)) {
            ApiClient.getApiService().forgotPass(request).enqueue(resendCallback);
        } else {
            ApiClient.getApiService().resendOtp(request).enqueue(resendCallback);
        }
    }

    private void clearOtpBoxes() {
        EditText[] otpBoxes = new EditText[]{etCode1, etCode2, etCode3, etCode4, etCode5, etCode6};
        for (EditText box : otpBoxes) {
            if (box != null) {
                box.setText("");
            }
        }
        if (etCode1 != null) {
            etCode1.requestFocus();
        }
    }

    private void showLoading(boolean isLoading) {
        if (progressDialog == null) return;
        if (isLoading) {
            progressDialog.show();
        } else {
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
