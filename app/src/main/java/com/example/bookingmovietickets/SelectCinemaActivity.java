package com.example.bookingmovietickets;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SelectCinemaActivity extends AppCompatActivity {

    private ImageView btnBack;
    private TextView tvMovieTitleHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_select_cinema);

        View mainView = findViewById(R.id.layout_header);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(v.getPaddingLeft(), systemBars.top, v.getPaddingRight(), v.getPaddingBottom());
                return insets;
            });
        }

        bindViews();
        displayIntentData();
        setupEvents();
    }

    private void bindViews() {
        btnBack = findViewById(R.id.btn_back);
        tvMovieTitleHeader = findViewById(R.id.tv_movie_title_header);
    }

    private void displayIntentData() {
        Intent intent = getIntent();
        if (intent != null && tvMovieTitleHeader != null) {
            String title = intent.getStringExtra("EXTRA_MOVIE_TITLE");
            if (title != null && !title.isEmpty()) {
                tvMovieTitleHeader.setText(title);
            }
        }
    }

    private void setupEvents() {
        // Nút quay lại (<) đóng SelectCinemaActivity và tự động quay về MovieDetailActivity
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        // Nút Tiếp tục chọn ghế
        View btnContinue = findViewById(R.id.btn_continue_seat);
        if (btnContinue != null) {
            btnContinue.setOnClickListener(v ->
                    Toast.makeText(this, "Mở màn hình chọn ghế...", Toast.LENGTH_SHORT).show());
        }
    }
}
