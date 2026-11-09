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

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    // ====== View ======
    private TextView tvAvatar, tvUsername;
    private ImageView imgHomeAvatar;
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

        // Tải danh sách phim từ Database MySQL qua API Backend (Now showing & Coming soon)
        fetchMoviesFromDatabase();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupHeader();
    }

    private void bindViews() {
        tvAvatar = findViewById(R.id.tv_avatar);
        imgHomeAvatar = findViewById(R.id.imgHomeAvatar);
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
        comingSoonList = new ArrayList<>();
    }

    private void fetchMoviesFromDatabase() {
        // 1. Gọi API lấy phim đang chiếu (/api/movies/now-showing)
        ApiClient.getApiService().getNowShowingMovies().enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Movie> list = response.body().getDuLieu();
                    if (list != null && !list.isEmpty()) {
                        nowShowingList.clear();
                        nowShowingList.addAll(list);

                        // Cập nhật Banner top từ phim đang chiếu kèm posterUrl thật
                        bannerList.clear();
                        for (int i = 0; i < Math.min(3, list.size()); i++) {
                            Movie m = list.get(i);
                            bannerList.add(new Banner("HOT PHIM THÁNG", m.getTitle(), m.getGenreAndDuration(), m.getPosterUrl()));
                        }
                        setupBanner();

                        // Hiển thị danh sách lên RecyclerView mặc định tab Đang chiếu
                        switchTab(true);
                    }
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Lỗi tải phim đang chiếu: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // 2. Gọi API lấy phim sắp chiếu (/api/movies/coming-soon)
        ApiClient.getApiService().getComingSoonMovies().enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Movie> list = response.body().getDuLieu();
                    if (list != null) {
                        comingSoonList.clear();
                        comingSoonList.addAll(list);
                    }
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                // Không bắt buộc lỗi sắp chiếu
            }
        });
    }

    private void setupHeader() {
        SharedPrefManager prefManager = SharedPrefManager.getInstance(this);
        String fullName = prefManager.getFullName();
        String avatarUrl = prefManager.getAvatarUrl();

        if (tvUsername != null) {
            tvUsername.setText(fullName);
        }

        if (avatarUrl != null && !avatarUrl.isEmpty() && imgHomeAvatar != null) {
            imgHomeAvatar.setVisibility(View.VISIBLE);
            if (tvAvatar != null) tvAvatar.setVisibility(View.GONE);

            Glide.with(this)
                    .load(avatarUrl)
                    .placeholder(R.drawable.bg_avatar_circle)
                    .error(R.drawable.bg_avatar_circle)
                    .circleCrop()
                    .into(imgHomeAvatar);
        } else if (tvAvatar != null && fullName != null && !fullName.isEmpty()) {
            tvAvatar.setVisibility(View.VISIBLE);
            if (imgHomeAvatar != null) imgHomeAvatar.setVisibility(View.GONE);
            tvAvatar.setText(String.valueOf(fullName.charAt(0)).toUpperCase());
        }
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

        findViewById(R.id.nav_cinema).setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, CinemaActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });

        findViewById(R.id.nav_notification).setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, NotificationActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });

        findViewById(R.id.nav_profile).setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });
    }
}
