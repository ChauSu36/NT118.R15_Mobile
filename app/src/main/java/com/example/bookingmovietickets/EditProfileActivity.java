package com.example.bookingmovietickets;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;
import java.util.Locale;

public class EditProfileActivity extends AppCompatActivity {

    private ImageView btnBack;
    private TextView tvAvatarPlaceholder, btnSave;
    private EditText etFullName, etPhone, etDob;
    private RadioGroup rgGender;
    private RadioButton rbMale, rbFemale, rbOther;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile);

        View mainView = findViewById(R.id.main);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        bindViews();
        loadCurrentUserData();
        setupDatePicker();
        setupEvents();
    }

    private void bindViews() {
        btnBack = findViewById(R.id.btn_back);
        tvAvatarPlaceholder = findViewById(R.id.tv_avatar_placeholder);
        etFullName = findViewById(R.id.et_full_name);
        etPhone = findViewById(R.id.et_phone);
        etDob = findViewById(R.id.et_dob);
        rgGender = findViewById(R.id.rg_gender);
        rbMale = findViewById(R.id.rb_male);
        rbFemale = findViewById(R.id.rb_female);
        rbOther = findViewById(R.id.rb_other);
        btnSave = findViewById(R.id.btn_save);
    }

    private void loadCurrentUserData() {
        // 1. Đọc dữ liệu từ SharedPreferences
        SharedPreferences pref = getSharedPreferences("UserSession", MODE_PRIVATE);
        String fullName = pref.getString("FULL_NAME", "");
        String phone = pref.getString("PHONE", "");
        String gender = pref.getString("GENDER", "Nam");
        String dob = pref.getString("DOB", "01/01/2000");

        // 2. Điền thông tin vào giao diện
        if (etFullName != null) etFullName.setText(fullName);
        if (etPhone != null) etPhone.setText(phone);
        if (etDob != null) etDob.setText(dob);

        // Hiển thị Avatar dạng chữ cái đầu
        if (tvAvatarPlaceholder != null && !fullName.isEmpty()) {
            tvAvatarPlaceholder.setText(String.valueOf(fullName.charAt(0)).toUpperCase());
        }

        // Chọn RadioButton theo giới tính hiện tại
        if (rgGender != null) {
            if ("Nữ".equalsIgnoreCase(gender) || "nu".equalsIgnoreCase(gender)) {
                if (rbFemale != null) rbFemale.setChecked(true);
            } else if ("Khác".equalsIgnoreCase(gender) || "khac".equalsIgnoreCase(gender)) {
                if (rbOther != null) rbOther.setChecked(true);
            } else {
                if (rbMale != null) rbMale.setChecked(true);
            }
        }
    }

    private void setupDatePicker() {
        if (etDob == null) return;

        etDob.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    EditProfileActivity.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        // Định dạng ngày theo DD/MM/YYYY
                        String formattedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear);
                        etDob.setText(formattedDate);
                    },
                    year, month, day
            );
            datePickerDialog.show();
        });
    }

    private void setupEvents() {
        // Nút quay lại (<)
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        // Nút LƯU THAY ĐỔI
        if (btnSave != null) {
            btnSave.setOnClickListener(v -> saveUpdatedProfile());
        }
    }

    private void saveUpdatedProfile() {
        String newFullName = etFullName != null ? etFullName.getText().toString().trim() : "";
        String newPhone = etPhone != null ? etPhone.getText().toString().trim() : "";
        String newDob = etDob != null ? etDob.getText().toString().trim() : "";

        if (newFullName.isEmpty() || newPhone.isEmpty()) {
            Toast.makeText(this, "Họ tên và Số điện thoại không được để trống!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Xác định giới tính được chọn
        String newGender = "Nam";
        if (rgGender != null) {
            int selectedId = rgGender.getCheckedRadioButtonId();
            if (selectedId == R.id.rb_female) {
                newGender = "Nữ";
            } else if (selectedId == R.id.rb_other) {
                newGender = "Khác";
            }
        }

        // 3. Ghi đè thông tin mới vào SharedPreferences
        SharedPreferences pref = getSharedPreferences("UserSession", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("FULL_NAME", newFullName);
        editor.putString("PHONE", newPhone);
        editor.putString("GENDER", newGender);
        editor.putString("DOB", newDob);
        editor.apply();

        Toast.makeText(this, "Cập nhật hồ sơ thành công!", Toast.LENGTH_SHORT).show();

        // 4. Đóng Activity để quay về ProfileActivity
        finish();
    }
}