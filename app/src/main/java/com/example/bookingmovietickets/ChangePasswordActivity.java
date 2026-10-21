package com.example.bookingmovietickets;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ChangePasswordActivity extends AppCompatActivity {

    private ImageView btnBack;
    private EditText etCurrentPassword, etNewPassword, etConfirmPassword;
    private RelativeLayout containerCurrentPassword, containerNewPassword, containerConfirmPassword;
    private ImageView ivToggleCurrent, ivToggleNew, ivToggleConfirm;
    private TextView tvForgotPassword, btnConfirmChange, btnCancel;
    private TextView tvRuleMinLength, tvRuleDigit, tvRuleUppercase, tvRuleSpecial;

    // Trạng thái hiển thị / ẩn mật khẩu (mặc định false là ẩn dấu chấm)
    private boolean isCurrentVisible = false;
    private boolean isNewVisible = false;
    private boolean isConfirmVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_password);

        View mainView = findViewById(R.id.main);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        bindViews();
        setupFocusBorders();
        setupPasswordToggles();
        setupLiveValidation();
        setupEvents();
    }

    private void bindViews() {
        btnBack = findViewById(R.id.btn_back);

        containerCurrentPassword = findViewById(R.id.container_current_password);
        containerNewPassword = findViewById(R.id.container_new_password);
        containerConfirmPassword = findViewById(R.id.container_confirm_password);

        etCurrentPassword = findViewById(R.id.et_current_password);
        etNewPassword = findViewById(R.id.et_new_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);

        ivToggleCurrent = findViewById(R.id.iv_toggle_current);
        ivToggleNew = findViewById(R.id.iv_toggle_new);
        ivToggleConfirm = findViewById(R.id.iv_toggle_confirm);

        tvForgotPassword = findViewById(R.id.tv_forgot_password);
        btnConfirmChange = findViewById(R.id.btn_confirm_change);
        btnCancel = findViewById(R.id.btn_cancel);

        tvRuleMinLength = findViewById(R.id.tv_rule_min_length);
        tvRuleDigit = findViewById(R.id.tv_rule_digit);
        tvRuleUppercase = findViewById(R.id.tv_rule_uppercase);
        tvRuleSpecial = findViewById(R.id.tv_rule_special);
    }

    /**
     * Cài đặt viền đỏ khi người dùng bấm/focus vào ô nhập.
     * Khi bỏ focus, ô nhập quay lại viền xám bình thường.
     */
    private void setupFocusBorders() {
        if (etCurrentPassword != null && containerCurrentPassword != null) {
            etCurrentPassword.setOnFocusChangeListener((v, hasFocus) ->
                    containerCurrentPassword.setBackgroundResource(hasFocus ? R.drawable.bg_option_card_selected : R.drawable.bg_edit_text_field));
        }

        if (etNewPassword != null && containerNewPassword != null) {
            etNewPassword.setOnFocusChangeListener((v, hasFocus) ->
                    containerNewPassword.setBackgroundResource(hasFocus ? R.drawable.bg_option_card_selected : R.drawable.bg_edit_text_field));
        }

        if (etConfirmPassword != null && containerConfirmPassword != null) {
            etConfirmPassword.setOnFocusChangeListener((v, hasFocus) ->
                    containerConfirmPassword.setBackgroundResource(hasFocus ? R.drawable.bg_option_card_selected : R.drawable.bg_edit_text_field));
        }
    }

    /** Cài đặt sự kiện bấm vào icon con mắt để bật/tắt hiển thị mật khẩu. */
    private void setupPasswordToggles() {
        if (ivToggleCurrent != null && etCurrentPassword != null) {
            ivToggleCurrent.setOnClickListener(v -> {
                isCurrentVisible = !isCurrentVisible;
                toggleVisibility(etCurrentPassword, ivToggleCurrent, isCurrentVisible);
            });
        }

        if (ivToggleNew != null && etNewPassword != null) {
            ivToggleNew.setOnClickListener(v -> {
                isNewVisible = !isNewVisible;
                toggleVisibility(etNewPassword, ivToggleNew, isNewVisible);
            });
        }

        if (ivToggleConfirm != null && etConfirmPassword != null) {
            ivToggleConfirm.setOnClickListener(v -> {
                isConfirmVisible = !isConfirmVisible;
                toggleVisibility(etConfirmPassword, ivToggleConfirm, isConfirmVisible);
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

    /**
     * Kiểm tra các yêu cầu mật khẩu realtime khi gõ:
     * Đạt yêu cầu -> chuyển thành ✓ màu XANH.
     * Chưa đạt yêu cầu -> giữ ○ màu XÁM bình thường.
     */
    private void setupLiveValidation() {
        if (etNewPassword == null) return;

        etNewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String pass = s.toString();

                boolean hasMinLength = pass.length() >= 8;
                boolean hasDigit = pass.matches(".*\\d.*");
                boolean hasUppercase = pass.matches(".*[A-Z].*");
                boolean hasSpecial = pass.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*");

                updateRuleUI(tvRuleMinLength, "Tối thiểu 8 ký tự", hasMinLength);
                updateRuleUI(tvRuleDigit, "Có ít nhất 1 chữ số", hasDigit);
                updateRuleUI(tvRuleUppercase, "Có ký tự in hoa (A-Z)", hasUppercase);
                updateRuleUI(tvRuleSpecial, "Có ký tự đặc biệt (@,#...)", hasSpecial);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void updateRuleUI(TextView tv, String labelText, boolean isValid) {
        if (tv == null) return;
        if (isValid) {
            tv.setText("● " + labelText);
            tv.setTextColor(Color.parseColor("#2E7D32")); // Màu xanh lá rực rỡ khi thỏa điều kiện
        } else {
            tv.setText("○ " + labelText);
            tv.setTextColor(Color.parseColor("#757575")); // Màu xám bình thường khi chưa thỏa
        }
    }

    private void setupEvents() {
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> closeActivity());
        }

        if (btnCancel != null) {
            btnCancel.setOnClickListener(v -> closeActivity());
        }

        if (tvForgotPassword != null) {
            tvForgotPassword.setOnClickListener(v -> {
                Intent intent = new Intent(ChangePasswordActivity.this, ResetPasswordActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            });
        }

        if (btnConfirmChange != null) {
            btnConfirmChange.setOnClickListener(v -> handleChangePassword());
        }
    }

    private void handleChangePassword() {
        String currentPass = etCurrentPassword != null ? etCurrentPassword.getText().toString().trim() : "";
        String newPass = etNewPassword != null ? etNewPassword.getText().toString().trim() : "";
        String confirmPass = etConfirmPassword != null ? etConfirmPassword.getText().toString().trim() : "";

        if (currentPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (newPass.length() < 8) {
            Toast.makeText(this, "Mật khẩu mới phải có ít nhất 8 ký tự!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPass.equals(confirmPass)) {
            Toast.makeText(this, "Mật khẩu xác nhận không trùng khớp!", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
        closeActivity();
    }

    private void closeActivity() {
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
