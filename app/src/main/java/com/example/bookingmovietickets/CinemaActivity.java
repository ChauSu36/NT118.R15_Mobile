package com.example.bookingmovietickets;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CinemaActivity extends AppCompatActivity {

    private RecyclerView rvCinemas;
    private CinemaAdapter adapter;
    private List<Cinema> cinemaList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cinema);

        View mainView = findViewById(R.id.main);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        rvCinemas = findViewById(R.id.rvCinemas);
        if (rvCinemas != null) {
            rvCinemas.setLayoutManager(new LinearLayoutManager(this));
        }

        initDummyData();

        adapter = new CinemaAdapter(this, cinemaList);
        if (rvCinemas != null) {
            rvCinemas.setAdapter(adapter);
        }

        setupBottomNav();
    }

    private void initDummyData() {
        cinemaList = new ArrayList<>();

        cinemaList.add(new Cinema(
                "CQS Cinema Nguyễn Trãi",
                "235 Nguyễn Trãi, P. Nguyễn Cư Trinh, Q.1",
                "1.2 km",
                true,
                Arrays.asList("18:30", "19:45", "20:30", "21:15")
        ));

        cinemaList.add(new Cinema(
                "CQS Cinema Bến Thành",
                "12 Lê Lai, Phường Bến Thành, Quận 1",
                "2.8 km",
                false,
                Arrays.asList("19:00", "20:15", "21:30")
        ));
    }

    private void setupBottomNav() {
        if (findViewById(R.id.nav_home) != null) {
            findViewById(R.id.nav_home).setOnClickListener(v -> openHome());
        }

        if (findViewById(R.id.nav_cinema) != null) {
            findViewById(R.id.nav_cinema).setOnClickListener(v ->
                    Toast.makeText(this, "Đang ở màn hình Rạp chiếu", Toast.LENGTH_SHORT).show());
        }

        if (findViewById(R.id.nav_notification) != null) {
            findViewById(R.id.nav_notification).setOnClickListener(v -> {
                Intent intent = new Intent(this, NotificationActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            });
        }

        if (findViewById(R.id.nav_profile) != null) {
            findViewById(R.id.nav_profile).setOnClickListener(v -> {
                Intent intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            });
        }
    }

    private void openHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
