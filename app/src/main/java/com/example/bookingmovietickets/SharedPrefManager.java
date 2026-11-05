package com.example.bookingmovietickets;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {

    private static final String PREF_NAME = "UserSession";

    private static final String KEY_USER_ID = "USER_ID";
    private static final String KEY_AVATAR_URL = "AVATAR_URL";
    private static final String KEY_FULL_NAME = "FULL_NAME";
    private static final String KEY_EMAIL = "EMAIL";
    private static final String KEY_PHONE = "PHONE";
    private static final String KEY_GENDER = "GENDER";
    private static final String KEY_DOB = "DOB";
    private static final String KEY_ROLE = "ROLE";
    private static final String KEY_JWT_TOKEN = "JWT_TOKEN";

    private static SharedPrefManager instance;
    private final SharedPreferences sharedPreferences;

    private SharedPrefManager(Context context) {
        sharedPreferences = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPrefManager(context);
        }
        return instance;
    }

    public void saveAvatarUrl(String avatarUrl) {
        sharedPreferences.edit().putString(KEY_AVATAR_URL, avatarUrl).apply();
    }

    public String getAvatarUrl() {
        return sharedPreferences.getString(KEY_AVATAR_URL, null);
    }

    public void saveUserId(int userId) {
        sharedPreferences.edit().putInt(KEY_USER_ID, userId).apply();
    }

    public int getUserId() {
        return sharedPreferences.getInt(KEY_USER_ID, -1);
    }

    public void saveFullName(String fullName) {
        sharedPreferences.edit().putString(KEY_FULL_NAME, fullName).apply();
    }

    public String getFullName() {
        return sharedPreferences.getString(KEY_FULL_NAME, "Người dùng");
    }

    public void saveEmail(String email) {
        sharedPreferences.edit().putString(KEY_EMAIL, email).apply();
    }

    public String getEmail() {
        return sharedPreferences.getString(KEY_EMAIL, "");
    }

    public void savePhone(String phone) {
        sharedPreferences.edit().putString(KEY_PHONE, phone).apply();
    }

    public String getPhone() {
        return sharedPreferences.getString(KEY_PHONE, "");
    }

    public void saveGender(String gender) {
        sharedPreferences.edit().putString(KEY_GENDER, gender).apply();
    }

    public String getGender() {
        return sharedPreferences.getString(KEY_GENDER, "Nam");
    }

    public void saveDob(String dob) {
        sharedPreferences.edit().putString(KEY_DOB, dob).apply();
    }

    public String getDob() {
        return sharedPreferences.getString(KEY_DOB, "01/01/2000");
    }

    public void saveRole(String role) {
        sharedPreferences.edit().putString(KEY_ROLE, role).apply();
    }

    public String getRole() {
        return sharedPreferences.getString(KEY_ROLE, "user");
    }

    public void saveJwtToken(String token) {
        sharedPreferences.edit().putString(KEY_JWT_TOKEN, token).apply();
    }

    public String getJwtToken() {
        return sharedPreferences.getString(KEY_JWT_TOKEN, null);
    }

    public void saveUserSession(int userId, String fullName, String email, String phone, String role, String token, String avatarUrl) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_USER_ID, userId);
        editor.putString(KEY_FULL_NAME, fullName);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PHONE, phone);
        editor.putString(KEY_ROLE, role);
        editor.putString(KEY_JWT_TOKEN, token);
        if (avatarUrl != null) {
            editor.putString(KEY_AVATAR_URL, avatarUrl);
        }
        editor.apply();
    }

    public void clearSession() {
        sharedPreferences.edit().clear().apply();
    }

    public boolean isLoggedIn() {
        return getUserId() != -1;
    }
}
