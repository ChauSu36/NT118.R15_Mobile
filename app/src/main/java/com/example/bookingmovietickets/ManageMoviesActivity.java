package com.example.bookingmovietickets;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ManageMoviesActivity extends AppCompatActivity {

    private ImageView btnBack;
    private EditText edtSearch;
    private TextView btnAddNewMovie;
    private TextView tabAll, tabNowShowing, tabComingSoon;
    private RecyclerView rvManageMovies;
    private ManageMovieAdapter adapter;

    private List<ManageMovie> allMovies;
    private List<ManageMovie> filteredMovies;

    private static final int TAB_ALL = 0;
    private static final int TAB_NOW_SHOWING = 1;
    private static final int TAB_COMING_SOON = 2;
    private int currentTab = TAB_ALL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manage_movies);

        View mainView = findViewById(R.id.main);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        bindViews();
        initDummyData();
        setupRecyclerView();
        setupTabs();
        setupSearch();
        setupEvents();

        switchTab(TAB_ALL);
    }

    private void bindViews() {
        btnBack = findViewById(R.id.btn_back);
        edtSearch = findViewById(R.id.edt_search_manage_movie);
        btnAddNewMovie = findViewById(R.id.btn_add_new_movie);

        tabAll = findViewById(R.id.tab_all);
        tabNowShowing = findViewById(R.id.tab_now_showing);
        tabComingSoon = findViewById(R.id.tab_coming_soon);

        rvManageMovies = findViewById(R.id.rv_manage_movies);
    }

    private void initDummyData() {
        allMovies = new ArrayList<>();

        // Showtime slots dummy
        List<ManageMovie.ShowtimeSlot> slots1 = new ArrayList<>();
        slots1.add(new ManageMovie.ShowtimeSlot("18:00", "Còn 35/50 ghế", false));
        slots1.add(new ManageMovie.ShowtimeSlot("20:00", "ĐÃ KÍN CHỖ", true));
        slots1.add(new ManageMovie.ShowtimeSlot("22:15", "Còn 48/50 ghế", false));

        allMovies.add(new ManageMovie(
                "Lật Mặt 7: Một Điều Ước",
                "C16", "138p",
                "Gia đình, Tâm lý, Hài",
                "26/04/2024",
                "ĐANG CHIẾU",
                "RẠP 03",
                "30/09",
                slots1
        ));

        List<ManageMovie.ShowtimeSlot> slots2 = new ArrayList<>();
        slots2.add(new ManageMovie.ShowtimeSlot("19:00", "Còn 20/50 ghế", false));
        slots2.add(new ManageMovie.ShowtimeSlot("21:30", "Còn 12/50 ghế", false));

        allMovies.add(new ManageMovie(
                "MAI (2024)",
                "C18", "131p",
                "Tình cảm, Tâm lý",
                "10/02/2024",
                "ĐANG CHIẾU",
                "RẠP 01",
                "30/09",
                slots2
        ));

        List<ManageMovie.ShowtimeSlot> slots3 = new ArrayList<>();
        slots3.add(new ManageMovie.ShowtimeSlot("18:30", "Dự kiến khởi chiếu", false));

        allMovies.add(new ManageMovie(
                "KUNG FU PANDA 4",
                "P", "94p",
                "Hoạt hình, Hành động",
                "15/11/2026",
                "SẮP CHIẾU",
                "RẠP 02",
                "15/11",
                slots3
        ));
    }

    private void setupRecyclerView() {
        filteredMovies = new ArrayList<>();
        adapter = new ManageMovieAdapter(this, filteredMovies);
        if (rvManageMovies != null) {
            rvManageMovies.setLayoutManager(new LinearLayoutManager(this));
            rvManageMovies.setAdapter(adapter);
        }
    }

    private void setupTabs() {
        if (tabAll != null) tabAll.setOnClickListener(v -> switchTab(TAB_ALL));
        if (tabNowShowing != null) tabNowShowing.setOnClickListener(v -> switchTab(TAB_NOW_SHOWING));
        if (tabComingSoon != null) tabComingSoon.setOnClickListener(v -> switchTab(TAB_COMING_SOON));
    }

    private void switchTab(int tabIndex) {
        currentTab = tabIndex;
        setTabStyle(tabAll, tabIndex == TAB_ALL);
        setTabStyle(tabNowShowing, tabIndex == TAB_NOW_SHOWING);
        setTabStyle(tabComingSoon, tabIndex == TAB_COMING_SOON);

        filterList();
    }

    private void setTabStyle(TextView tab, boolean active) {
        if (tab == null) return;
        if (active) {
            tab.setBackgroundResource(R.drawable.bg_card_rounded);
            tab.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#1A1A1A")));
            tab.setTextColor(Color.WHITE);
        } else {
            tab.setBackgroundResource(R.drawable.bg_button_outline);
            tab.setBackgroundTintList(null);
            tab.setTextColor(Color.parseColor("#424242"));
        }
    }

    private void setupSearch() {
        if (edtSearch == null) return;
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterList();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void filterList() {
        filteredMovies.clear();
        String keyword = edtSearch != null ? edtSearch.getText().toString().trim().toLowerCase() : "";

        for (ManageMovie movie : allMovies) {
            boolean matchTab = false;
            if (currentTab == TAB_ALL) {
                matchTab = true;
            } else if (currentTab == TAB_NOW_SHOWING) {
                matchTab = "ĐANG CHIẾU".equalsIgnoreCase(movie.getStatus());
            } else if (currentTab == TAB_COMING_SOON) {
                matchTab = "SẮP CHIẾU".equalsIgnoreCase(movie.getStatus());
            }

            boolean matchKeyword = keyword.isEmpty() || movie.getTitle().toLowerCase().contains(keyword);

            if (matchTab && matchKeyword) {
                filteredMovies.add(movie);
            }
        }

        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    private void setupEvents() {
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> {
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            });
        }

        // Nút + THÊM PHIM MỚI
        if (btnAddNewMovie != null) {
            btnAddNewMovie.setOnClickListener(v -> new AlertDialog.Builder(this)
                    .setTitle("Thêm phim mới")
                    .setMessage("Mở Form nhập liệu thêm phim mới vào hệ thống CQS Cinema.")
                    .setPositiveButton("Tiếp tục", (dialog, which) ->
                            Toast.makeText(this, "Mở form thêm phim mới", Toast.LENGTH_SHORT).show())
                    .setNegativeButton("Hủy", null)
                    .show());
        }
    }
}
