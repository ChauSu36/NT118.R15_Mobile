package com.example.bookingmovietickets;
// LƯU Ý: đổi dòng package ở trên cho khớp với package thật trong project của bạn
// (copy đúng dòng package từ MainActivity.java hiện tại của bạn).

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import androidx.appcompat.app.AppCompatActivity;
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

    // ====== Dữ liệu (TĨNH — sau này thay bằng gọi API) ======
    private List<Banner> bannerList;
    private List<Movie> nowShowingList;
    private List<Movie> comingSoonList;
    private MovieAdapter movieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        // 2. Cấu hình tự động căn chỉnh lề theo thanh trạng thái & thanh điều hướng của hệ thống
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

    /**
     * Dữ liệu tạm để dựng xong giao diện giống ảnh thiết kế.
     * Khi nối API thật (GET /api/movies, server trả JSON),
     * chỉ cần thay 3 hàm "new Movie(...)" này bằng kết quả
     * MovieRepository trả về — không cần sửa layout hay Adapter.
     */
    private void loadDummyData() {
        bannerList = new ArrayList<>();
        bannerList.add(new Banner("HOT PHIM THÁNG", "LẬT MẶT 7: ONE WISH",
                "Khởi chiếu toàn quốc tại CQS Cinema"));
        // 2 banner dưới là CHỖ TRỐNG để bạn thay nội dung thật sau này —
        // hiện để tạm giống banner 1 cho đủ 3 dot như ảnh thiết kế.
        bannerList.add(new Banner("HOT PHIM THÁNG", "LẬT MẶT 7: ONE WISH",
                "Khởi chiếu toàn quốc tại CQS Cinema"));
        bannerList.add(new Banner("HOT PHIM THÁNG", "LẬT MẶT 7: ONE WISH",
                "Khởi chiếu toàn quốc tại CQS Cinema"));

        nowShowingList = new ArrayList<>();
        nowShowingList.add(new Movie("MAI (2024)", "Tình cảm • 131p", "C16", "4.8"));
        nowShowingList.add(new Movie("KUNG FU PANDA 4", "Hoạt hình • 94p", "P", "4.5"));

        // Danh sách "Sắp chiếu" — tạm để trống, bạn thêm phim thật sau.
        comingSoonList = new ArrayList<>();
    }

    private void setupHeader() {
        // Tên người dùng thật nên lấy từ SessionManager sau khi đăng nhập.
        // Hiện để tạm đúng chữ trong ảnh thiết kế.
        tvUsername.setText("Nguyễn Văn A");
    }

    private void setupBanner() {
        BannerAdapter bannerAdapter = new BannerAdapter(bannerList, banner ->
                Toast.makeText(this, "Mở chi tiết: " + banner.getTitle(), Toast.LENGTH_SHORT).show()
        );
        vpBanner.setAdapter(bannerAdapter);

        setupDots(bannerList.size());
        vpBanner.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                updateDots(position);
            }
        });
    }

    /** Vẽ các dấu chấm tròn dưới banner (bắt chước dot indicator trong ảnh). */
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

    private void openMovieDetail(Movie movie) {
        // TODO: thay bằng Intent mở MovieDetailActivity, kèm putExtra("movie_id", ...)
        Toast.makeText(this, "Mở chi tiết: " + movie.getTitle(), Toast.LENGTH_SHORT).show();
    }

    private void setupBottomNav() {
        findViewById(R.id.nav_home).setOnClickListener(v ->
                Toast.makeText(this, "Đang ở Trang chủ", Toast.LENGTH_SHORT).show());

        findViewById(R.id.nav_cinema).setOnClickListener(v -> {
            // TODO: mở màn hình Rạp chiếu
            Toast.makeText(this, "Rạp chiếu (chưa làm)", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.nav_notification).setOnClickListener(v -> {
            // TODO: mở màn hình Thông báo
            Toast.makeText(this, "Thông báo (chưa làm)", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.nav_profile).setOnClickListener(v -> {
            // TODO: mở màn hình Hồ sơ
            Toast.makeText(this, "Hồ sơ (chưa làm)", Toast.LENGTH_SHORT).show();
        });
    }
}