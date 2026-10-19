package com.example.bookingmovietickets;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;
import java.util.Locale;

public class EditProfileActivity extends AppCompatActivity {

    private TextView tvAvatarPlaceholder;
    private ImageView ivAvatarPreview;
    private EditText etFullName, etPhone, etDob;
    private RadioGroup rgGender;

    private final ActivityResultLauncher<String> pickImageLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    showPickedAvatar(uri);
                }
            });

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
        prefillCurrentData();
        setupAvatarPicker();
        setupDobPicker();
        setupSaveButton();

        // Nút quay lại (<) quay về màn hình Hồ sơ (ProfileActivity)
        View btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> {
                if (isTaskRoot()) {
                    Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                    startActivity(intent);
                }
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            });
        }
    }

    private void bindViews() {
        tvAvatarPlaceholder = findViewById(R.id.tv_avatar_placeholder);
        ivAvatarPreview = findViewById(R.id.iv_avatar_preview);
        etFullName = findViewById(R.id.et_full_name);
        etPhone = findViewById(R.id.et_phone);
        etDob = findViewById(R.id.et_dob);
        rgGender = findViewById(R.id.rg_gender);
    }

    private void prefillCurrentData() {
        etDob.setText("01/01/2000");
    }

    private void setupAvatarPicker() {
        View.OnClickListener openPicker = v -> pickImageLauncher.launch("image/*");
        findViewById(R.id.btn_change_avatar).setOnClickListener(openPicker);
        tvAvatarPlaceholder.setOnClickListener(openPicker);
        ivAvatarPreview.setOnClickListener(openPicker);
    }

    private void showPickedAvatar(Uri uri) {
        ivAvatarPreview.setImageURI(uri);
        ivAvatarPreview.setVisibility(View.VISIBLE);
        tvAvatarPlaceholder.setVisibility(View.GONE);
    }

    private void setupDobPicker() {
        etDob.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        String formatted = String.format(Locale.getDefault(),
                                "%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear);
                        etDob.setText(formatted);
                    }, year, month, day);

            dialog.show();
        });
    }

    private void setupSaveButton() {
        findViewById(R.id.btn_save).setOnClickListener(v -> {
            String fullName = etFullName.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();

            if (fullName.isEmpty()) {
                etFullName.setError("Vui lòng nhập họ tên");
                return;
            }
            if (phone.isEmpty()) {
                etPhone.setError("Vui lòng nhập số điện thoại");
                return;
            }

            Toast.makeText(this, "Đã lưu thông tin thay đổi", Toast.LENGTH_SHORT).show();

            if (isTaskRoot()) {
                Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
            finish();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });
    }

    private String getSelectedGender() {
        int checkedId = rgGender.getCheckedRadioButtonId();
        if (checkedId == R.id.rb_female) return "Nữ";
        if (checkedId == R.id.rb_other) return "Khác";
        return "Nam";
    }
}
