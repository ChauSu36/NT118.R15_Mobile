package com.example.bookingmovietickets;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
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

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {

    private ImageView btnBack, ivAvatarPreview;
    private View btnChangeAvatar;
    private TextView tvAvatarPlaceholder, btnSave;
    private EditText etFullName, etPhone, etDob;
    private RadioGroup rgGender;
    private RadioButton rbMale, rbFemale, rbOther;

    private ActivityResultLauncher<String> imagePickerLauncher;

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
        setupImagePicker();
        loadCurrentUserData();
        setupDatePicker();
        setupEvents();
    }

    private void bindViews() {
        btnBack = findViewById(R.id.btn_back);
        tvAvatarPlaceholder = findViewById(R.id.tv_avatar_placeholder);
        ivAvatarPreview = findViewById(R.id.iv_avatar_preview);
        btnChangeAvatar = findViewById(R.id.btn_change_avatar);
        etFullName = findViewById(R.id.et_full_name);
        etPhone = findViewById(R.id.et_phone);
        etDob = findViewById(R.id.et_dob);
        rgGender = findViewById(R.id.rg_gender);
        rbMale = findViewById(R.id.rb_male);
        rbFemale = findViewById(R.id.rb_female);
        rbOther = findViewById(R.id.rb_other);
        btnSave = findViewById(R.id.btn_save);
    }

    private void setupImagePicker() {
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        uploadAvatarImage(uri);
                    }
                }
        );

        View.OnClickListener pickImageListener = v -> imagePickerLauncher.launch("image/*");

        if (btnChangeAvatar != null) btnChangeAvatar.setOnClickListener(pickImageListener);
        if (tvAvatarPlaceholder != null) tvAvatarPlaceholder.setOnClickListener(pickImageListener);
        if (ivAvatarPreview != null) ivAvatarPreview.setOnClickListener(pickImageListener);
    }

    private void loadCurrentUserData() {
        SharedPrefManager prefManager = SharedPrefManager.getInstance(this);
        String fullName = prefManager.getFullName();
        String phone = prefManager.getPhone();
        String gender = prefManager.getGender();
        String dob = prefManager.getDob();
        String avatarUrl = prefManager.getAvatarUrl();

        if (etFullName != null) etFullName.setText(fullName);
        if (etPhone != null) etPhone.setText(phone);
        if (etDob != null) etDob.setText(dob);

        if (avatarUrl != null && !avatarUrl.isEmpty() && ivAvatarPreview != null) {
            ivAvatarPreview.setVisibility(View.VISIBLE);
            if (tvAvatarPlaceholder != null) tvAvatarPlaceholder.setVisibility(View.GONE);

            Glide.with(this)
                    .load(avatarUrl)
                    .placeholder(R.drawable.bg_avatar_circle)
                    .error(R.drawable.bg_avatar_circle)
                    .circleCrop()
                    .into(ivAvatarPreview);
        } else if (tvAvatarPlaceholder != null && fullName != null && !fullName.isEmpty()) {
            tvAvatarPlaceholder.setText(String.valueOf(fullName.charAt(0)).toUpperCase());
        }

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

    private void uploadAvatarImage(Uri uri) {
        int userId = SharedPrefManager.getInstance(this).getUserId();
        if (userId == -1) {
            userId = 1; // ID mặc định thử nghiệm nếu chưa đăng nhập
        }

        File file = getFileFromUri(this, uri);
        if (file == null || !file.exists()) {
            Toast.makeText(this, "Không thể đọc file ảnh đã chọn!", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "Đang tải ảnh đại diện lên...", Toast.LENGTH_SHORT).show();

        RequestBody userIdPart = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(userId));
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part avatarPart = MultipartBody.Part.createFormData("avatar", file.getName(), requestFile);

        ApiClient.getApiService().uploadAvatar(userIdPart, avatarPart).enqueue(new Callback<AvatarResponse>() {
            @Override
            public void onResponse(Call<AvatarResponse> call, Response<AvatarResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AvatarResponse avatarRes = response.body();
                    String newAvatarUrl = avatarRes.getAvatar_url();

                    if (newAvatarUrl != null && !newAvatarUrl.isEmpty()) {
                        SharedPrefManager.getInstance(EditProfileActivity.this).saveAvatarUrl(newAvatarUrl);

                        if (ivAvatarPreview != null) {
                            ivAvatarPreview.setVisibility(View.VISIBLE);
                            if (tvAvatarPlaceholder != null) tvAvatarPlaceholder.setVisibility(View.GONE);

                            Glide.with(EditProfileActivity.this)
                                    .load(newAvatarUrl)
                                    .placeholder(R.drawable.bg_avatar_circle)
                                    .error(R.drawable.bg_avatar_circle)
                                    .circleCrop()
                                    .into(ivAvatarPreview);
                        }
                    }

                    String msg = avatarRes.getThongBao() != null ? avatarRes.getThongBao() : "Cập nhật ảnh đại diện thành công!";
                    Toast.makeText(EditProfileActivity.this, msg, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditProfileActivity.this, "Tải ảnh đại diện thất bại! (Mã: " + response.code() + ")", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AvatarResponse> call, Throwable t) {
                Toast.makeText(EditProfileActivity.this, "Lỗi khi tải ảnh: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private File getFileFromUri(Context context, Uri uri) {
        try {
            String fileName = "temp_avatar_" + System.currentTimeMillis() + ".jpg";
            File cacheDir = context.getCacheDir();
            File tempFile = new File(cacheDir, fileName);

            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            if (inputStream == null) return null;

            FileOutputStream outputStream = new FileOutputStream(tempFile);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
            return tempFile;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
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
                        String formattedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear);
                        etDob.setText(formattedDate);
                    },
                    year, month, day
            );
            datePickerDialog.show();
        });
    }

    private void setupEvents() {
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

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

        String newGender = "nam";
        String displayGender = "Nam";
        if (rgGender != null) {
            int selectedId = rgGender.getCheckedRadioButtonId();
            if (selectedId == R.id.rb_female) {
                newGender = "nu";
                displayGender = "Nữ";
            } else if (selectedId == R.id.rb_other) {
                newGender = "khac";
                displayGender = "Khác";
            }
        }

        // Chuyển định dạng ngày sinh DD/MM/YYYY thành YYYY-MM-DD
        String formattedDob = newDob;
        if (newDob.contains("/")) {
            String[] parts = newDob.split("/");
            if (parts.length == 3) {
                formattedDob = String.format(Locale.getDefault(), "%s-%s-%s", parts[2], parts[1], parts[0]);
            }
        }

        SharedPrefManager prefManager = SharedPrefManager.getInstance(this);
        int userId = prefManager.getUserId();

        // 1. Lưu thông tin mới vào SharedPreferences cục bộ
        prefManager.saveFullName(newFullName);
        prefManager.savePhone(newPhone);
        prefManager.saveGender(displayGender);
        prefManager.saveDob(newDob);

        // 2. Gửi API cập nhật thông tin lên Server Database
        UpdateProfileRequest request = new UpdateProfileRequest(userId, newFullName, newPhone, newGender, formattedDob);
        ApiClient.getApiService().updateProfile(request).enqueue(new Callback<UpdateProfileResponse>() {
            @Override
            public void onResponse(Call<UpdateProfileResponse> call, Response<UpdateProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(EditProfileActivity.this, "Cập nhật hồ sơ thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditProfileActivity.this, "Đã lưu hồ sơ thành công!", Toast.LENGTH_SHORT).show();
                }
                finish();
            }

            @Override
            public void onFailure(Call<UpdateProfileResponse> call, Throwable t) {
                Toast.makeText(EditProfileActivity.this, "Đã lưu hồ sơ thành công!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
