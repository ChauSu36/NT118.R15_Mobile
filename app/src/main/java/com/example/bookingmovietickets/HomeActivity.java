package com.example.bookingmovietickets;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    // ====== View ======
    private TextView tvUsername;
    private ViewPager2 vpBanner;
    private LinearLayout layoutDots;
    private TextView tabNowShowing, tabComingSoon;
    private RecyclerView rvMovies;

    // ====== Dữ liệu ======
    private List<Banner> bannerList;
    private List<Movie> nowShowingList;
    private List<Movie> comingSoonList;
    private MovieAdapter movieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        // Cấu hình tự động căn chỉnh lề theo thanh trạng thái & thanh điều hướng của hệ thống
        View mainView = findViewById(R.id.main);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        bindViews();
        loadDummyData();
        setupHeader();
        setupBanner();
        setupTabs();
        setupMovieList();
        setupBottomNav();
    }

    private void bindViews() {
        tvUsername = findViewById(R.id.tv_username);
        vpBanner = findViewById(R.id.vp_banner);
        layoutDots = findViewById(R.id.layout_dots);
        tabNowShowing = findViewById(R.id.tab_now_showing);
        tabComingSoon = findViewById(R.id.tab_coming_soon);
        rvMovies = findViewById(R.id.rv_movies);
    }

    private void loadDummyData() {
        bannerList = new ArrayList<>();
        bannerList.add(new Banner("HOT PHIM THÁNG", "LẬT MẶT 7: ONE WISH",
                "Gia đình, Tâm lý, Hài • 138p"));
        bannerList.add(new Banner("HOT PHIM THÁNG", "LẬT MẶT 7: ONE WISH",
                "Gia đình, Tâm lý, Hài • 138p"));
        bannerList.add(new Banner("HOT PHIM THÁNG", "LẬT MẶT 7: ONE WISH",
                "Gia đình, Tâm lý, Hài • 138p"));

        nowShowingList = new ArrayList<>();
        nowShowingList.add(new Movie("MAI (2024)", "Tình cảm • 131p", "C16", "4.8"));
        nowShowingList.add(new Movie("KUNG FU PANDA 4", "Hoạt hình • 94p", "P", "4.5"));

        comingSoonList = new ArrayList<>();
    }

    private void setupHeader() {
        tvUsername.setText("Nguyễn Văn A");
    }

    private void setupBanner() {
        BannerAdapter bannerAdapter = new BannerAdapter(bannerList, this::openMovieDetailFromBanner);
        vpBanner.setAdapter(bannerAdapter);

        setupDots(bannerList.size());
        vpBanner.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                updateDots(position);
            }
        });
    }

    private void openMovieDetailFromBanner(Banner banner) {
        Intent intent = new Intent(HomeActivity.this, MovieDetailActivity.class);
        intent.putExtra("EXTRA_MOVIE_TITLE", banner.getTitle());
        intent.putExtra("EXTRA_MOVIE_GENRE", banner.getSubtitle());
        intent.putExtra("EXTRA_MOVIE_RATING", "4.9");
        intent.putExtra("EXTRA_MOVIE_AGE", "K16");
        startActivity(intent);
    }

    /** Vẽ các dấu chấm tròn dưới banner. */
    private void setupDots(int count) {
        layoutDots.removeAllViews();
        int sizePx = dpToPx(8);
        int marginPx = dpToPx(4);
        for (int i = 0; i < count; i++) {
            ImageView dot = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(sizePx, sizePx);
            params.setMargins(marginPx, 0, marginPx, 0);
            dot.setLayoutParams(params);
            dot.setImageResource(i == 0 ? R.drawable.dot_active : R.drawable.dot_inactive);
            layoutDots.addView(dot);
        }
    }

    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }

    private void updateDots(int selectedPosition) {
        for (int i = 0; i < layoutDots.getChildCount(); i++) {
            ImageView dot = (ImageView) layoutDots.getChildAt(i);
            dot.setImageResource(i == selectedPosition ? R.drawable.dot_active : R.drawable.dot_inactive);
        }
    }

    private void openMovieDetail(Movie movie) {
        Intent intent = new Intent(HomeActivity.this, MovieDetailActivity.class);

        intent.putExtra("EXTRA_MOVIE_TITLE", movie.getTitle());
        intent.putExtra("EXTRA_MOVIE_GENRE", movie.getGenreAndDuration());
        intent.putExtra("EXTRA_MOVIE_RATING", movie.getRating());
        intent.putExtra("EXTRA_MOVIE_AGE", movie.getAgeRating());

        startActivity(intent);
    }

    private void setupTabs() {
        tabNowShowing.setOnClickListener(v -> switchTab(true));
        tabComingSoon.setOnClickListener(v -> switchTab(false));
    }

    private void switchTab(boolean isNowShowing) {
        if (isNowShowing) {
            tabNowShowing.setBackgroundResource(R.drawable.bg_tab_active);
            tabNowShowing.setTextColor(getColor(android.R.color.white));
            tabComingSoon.setBackgroundResource(R.drawable.bg_tab_inactive);
            tabComingSoon.setTextColor(getColor(R.color.color_tab_inactive_text));
            movieAdapter = new MovieAdapter(nowShowingList, this::openMovieDetail);
        } else {
            tabComingSoon.setBackgroundResource(R.drawable.bg_tab_active);
            tabComingSoon.setTextColor(getColor(android.R.color.white));
            tabNowShowing.setBackgroundResource(R.drawable.bg_tab_inactive);
            tabNowShowing.setTextColor(getColor(R.color.color_tab_inactive_text));
            movieAdapter = new MovieAdapter(comingSoonList, this::openMovieDetail);
        }
        rvMovies.setAdapter(movieAdapter);
    }

    private void setupMovieList() {
        rvMovies.setLayoutManager(new GridLayoutManager(this, 2));
        movieAdapter = new MovieAdapter(nowShowingList, this::openMovieDetail);
        rvMovies.setAdapter(movieAdapter);
    }

    private void setupBottomNav() {
        findViewById(R.id.nav_home).setOnClickListener(v ->
                Toast.makeText(this, "Đang ở Trang chủ", Toast.LENGTH_SHORT).show());

        findViewById(R.id.nav_cinema).setOnClickListener(v ->
                Toast.makeText(this, "Rạp chiếu (chưa làm)", Toast.LENGTH_SHORT).show());

        findViewById(R.id.nav_notification).setOnClickListener(v ->
                Toast.makeText(this, "Thông báo (chưa làm)", Toast.LENGTH_SHORT).show());

        findViewById(R.id.nav_profile).setOnClickListener(v ->
                Toast.makeText(this, "Hồ sơ (chưa làm)", Toast.LENGTH_SHORT).show());
    }
}
