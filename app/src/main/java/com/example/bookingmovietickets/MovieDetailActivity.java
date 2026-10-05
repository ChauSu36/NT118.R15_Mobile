package com.example.bookingmovietickets;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MovieDetailActivity extends AppCompatActivity {

    private ImageView btnBack, btnFavorite, imgTrailerThumbnail;
    private LinearLayout btnPlayTrailer;
    private VideoView videoViewTrailer;
    private TextView tvMovieTitle, tvMovieGenre, tvMovieRating, tvMovieAge;

    private static final String SAMPLE_TRAILER_URL = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_movie_detail);

        View mainView = findViewById(R.id.main);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
                return insets;
            });
        }

        bindViews();
        displayIntentData();
        setupEvents();
    }

    private void bindViews() {
        btnBack = findViewById(R.id.btn_back);
        btnFavorite = findViewById(R.id.btn_favorite);
        imgTrailerThumbnail = findViewById(R.id.img_trailer_thumbnail);
        btnPlayTrailer = findViewById(R.id.btn_play_trailer);
        videoViewTrailer = findViewById(R.id.video_view_trailer);

        tvMovieTitle = findViewById(R.id.tv_detail_title);
        tvMovieGenre = findViewById(R.id.tv_detail_genre);
        tvMovieRating = findViewById(R.id.tv_detail_rating);
        tvMovieAge = findViewById(R.id.tv_detail_age);
    }

    private void displayIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            String title = intent.getStringExtra("EXTRA_MOVIE_TITLE");
            String genre = intent.getStringExtra("EXTRA_MOVIE_GENRE");
            String rating = intent.getStringExtra("EXTRA_MOVIE_RATING");
            String age = intent.getStringExtra("EXTRA_MOVIE_AGE");

            if (tvMovieTitle != null && title != null) {
                tvMovieTitle.setText(title);
            }
            if (tvMovieGenre != null && genre != null) {
                tvMovieGenre.setText("Thể loại: " + genre);
            }
            if (tvMovieRating != null && rating != null) {
                tvMovieRating.setText("Đánh giá: ★ " + rating + "/5");
            }
            if (tvMovieAge != null && age != null) {
                tvMovieAge.setText(age);
            }
        }
    }

    private void setupEvents() {
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        if (btnPlayTrailer != null) {
            btnPlayTrailer.setOnClickListener(v -> playTrailer());
        }

        if (btnFavorite != null) {
            btnFavorite.setOnClickListener(v ->
                    Toast.makeText(this, "Đã thêm vào danh sách yêu thích", Toast.LENGTH_SHORT).show());
        }

        View btnBook = findViewById(R.id.btn_book_ticket);
        if (btnBook != null) {
            btnBook.setOnClickListener(v ->
                    Toast.makeText(this, "Mở màn hình chọn ghế...", Toast.LENGTH_SHORT).show());
        }
    }

    private void playTrailer() {
        if (videoViewTrailer == null) return;

        if (btnPlayTrailer != null) btnPlayTrailer.setVisibility(View.GONE);
        if (imgTrailerThumbnail != null) imgTrailerThumbnail.setVisibility(View.GONE);
        videoViewTrailer.setVisibility(View.VISIBLE);

        Uri videoUri = Uri.parse(SAMPLE_TRAILER_URL);
        videoViewTrailer.setVideoURI(videoUri);

        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoViewTrailer);
        videoViewTrailer.setMediaController(mediaController);

        videoViewTrailer.setOnPreparedListener(mp -> videoViewTrailer.start());
        videoViewTrailer.setOnCompletionListener(mp -> {
            videoViewTrailer.setVisibility(View.GONE);
            if (btnPlayTrailer != null) btnPlayTrailer.setVisibility(View.VISIBLE);
            if (imgTrailerThumbnail != null) imgTrailerThumbnail.setVisibility(View.VISIBLE);
        });
    }
}
