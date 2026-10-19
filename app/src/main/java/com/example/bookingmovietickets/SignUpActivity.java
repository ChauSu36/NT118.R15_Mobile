package com.example.bookingmovietickets;

import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;

import java.util.Calendar;
import java.util.Locale;

public class SignUpActivity extends AppCompatActivity {

    // 1. Khai báo các thành phần giao diện
    private EditText edtFullName, edtDob, edtPhone;
    private EditText edtEmail, edtPassword, edtConfirmPassword;
    private RadioGroup rgGender;
    private MaterialButton btnSignUp;
    private LinearLayout llLogIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_sign_up);

        View mainView = findViewById(R.id.main);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(
                    mainView,
                    (v, insets) -> {
                        Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                        v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                        return insets;
                    }
            );
        }

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

        // 2. Ánh xạ các thành phần từ file XML qua ID
        bindViews();

        // 3. Mở BottomSheet chọn ngày sinh từ dưới lên khi bấm vào ô Date of Birth (hoặc mũi tên)
        setupDatePicker();

        // 4. Xử lý logic khi người dùng bấm nút "Sign Up"
        if (btnSignUp != null) {
            btnSignUp.setOnClickListener(v -> handleSignUp());
        }

        // 5. Xử lý khi nhấn chữ "Log in" để quay lại màn hình Đăng nhập
        if (llLogIn != null) {
            llLogIn.setOnClickListener(v -> {
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            });
        }
    }

    private void bindViews() {
        edtFullName = findViewById(R.id.edtFullName);
        edtDob = findViewById(R.id.edtDob);
        rgGender = findViewById(R.id.rgGender);
        edtPhone = findViewById(R.id.edtPhone);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        llLogIn = findViewById(R.id.llLogIn);
    }

    /**
     * Mở BottomSheetDialog trượt từ dưới lên chứa dialog_select_date.xml
     * khi bấm vào ô Date of Birth hoặc mũi tên hướng xuống.
     */
    private void setupDatePicker() {
        if (edtDob != null) {
            edtDob.setOnClickListener(v -> showDatePickerBottomSheet());
        }
    }

    private void showDatePickerBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_select_date, null);
        bottomSheetDialog.setContentView(dialogView);

        DatePicker datePicker = dialogView.findViewById(R.id.datePicker);
        Button btnConfirm = dialogView.findViewById(R.id.btnConfirmDate);

        // Mặc định chọn ngày hiện tại hoặc 01/01/2000
        if (datePicker != null) {
            Calendar cal = Calendar.getInstance();
            cal.set(2000, Calendar.JANUARY, 1);
            datePicker.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        }

        if (btnConfirm != null) {
            btnConfirm.setOnClickListener(v -> {
                if (datePicker != null) {
                    int day = datePicker.getDayOfMonth();
                    int month = datePicker.getMonth() + 1; // Tháng tính từ 0
                    int year = datePicker.getYear();

                    String formatted = String.format(Locale.getDefault(), "%02d/%02d/%d", day, month, year);
                    if (edtDob != null) {
                        edtDob.setText(formatted);
                    }
                }
                bottomSheetDialog.dismiss();
            });
        }

        bottomSheetDialog.show();
    }

    private void handleSignUp() {
        String fullName = edtFullName != null ? edtFullName.getText().toString().trim() : "";
        String dob = edtDob != null ? edtDob.getText().toString().trim() : "";
        String phone = edtPhone != null ? edtPhone.getText().toString().trim() : "";
        String email = edtEmail != null ? edtEmail.getText().toString().trim() : "";
        String password = edtPassword != null ? edtPassword.getText().toString().trim() : "";
        String confirmPassword = edtConfirmPassword != null ? edtConfirmPassword.getText().toString().trim() : "";

        if (fullName.isEmpty() || dob.isEmpty() || phone.isEmpty()
                || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(SignUpActivity.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(SignUpActivity.this, "Mật khẩu xác nhận không trùng khớp!", Toast.LENGTH_SHORT).show();
            return;
        }

        String gender = getSelectedGender();

        Toast.makeText(SignUpActivity.this, "Đăng ký tài khoản thành công!", Toast.LENGTH_SHORT).show();
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    private String getSelectedGender() {
        if (rgGender == null) return "Male";
        int checkedId = rgGender.getCheckedRadioButtonId();
        if (checkedId == R.id.rbFemale) return "Female";
        if (checkedId == R.id.rbOther) return "Other";
        return "Male";
    }
}
