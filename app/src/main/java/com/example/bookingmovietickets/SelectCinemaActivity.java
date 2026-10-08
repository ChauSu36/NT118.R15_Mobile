package com.example.bookingmovietickets;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SelectCinemaActivity extends AppCompatActivity {

    private ImageView btnBack;
    private View btnContinue;
    private TextView tvMovieTitleHeader;
    private String movieTitle = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_select_cinema);

        View mainView = findViewById(R.id.main);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        bindViews();
        displayIntentData();
        setupEvents();
    }

    private void bindViews() {
        btnBack = findViewById(R.id.btn_back);
        btnContinue = findViewById(R.id.btn_continue_seat);
        tvMovieTitleHeader = findViewById(R.id.tv_movie_title_header);
    }

    private void displayIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            movieTitle = intent.getStringExtra("EXTRA_MOVIE_TITLE");
            if (tvMovieTitleHeader != null && movieTitle != null && !movieTitle.isEmpty()) {
                tvMovieTitleHeader.setText(movieTitle);
            }
        }
    }

    private void setupEvents() {
        // 1. Nút Quay lại (<): Trở về màn hình MovieDetailActivity
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        // 2. Nút Tiếp tục chọn ghế: Mở SelectSeatActivity
        if (btnContinue != null) {
            btnContinue.setOnClickListener(v -> {
                Intent intent = new Intent(SelectCinemaActivity.this, SelectSeatActivity.class);
                if (movieTitle != null && !movieTitle.isEmpty()) {
                    intent.putExtra("EXTRA_MOVIE_TITLE", movieTitle);
                }
                startActivity(intent);
            });
        }
    }
}
