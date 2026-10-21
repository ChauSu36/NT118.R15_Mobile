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
    private TextView tvMovieTitleHeader, tvSelectedInfo;
    private String movieTitle = "LẬT MẶT 7: ONE WISH";
    private String selectedShowtime = "18:30";
    private String selectedCinemaName = "CQS Nguyễn Trãi";

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
        setupShowtimeSelection();
        setupEvents();
    }

    private void bindViews() {
        btnBack = findViewById(R.id.btn_back);
        btnContinue = findViewById(R.id.btn_continue_seat);
        tvMovieTitleHeader = findViewById(R.id.tv_cinema_header_title);
        tvSelectedInfo = findViewById(R.id.tv_selected_info);
    }

    private void displayIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            String title = intent.getStringExtra("EXTRA_MOVIE_TITLE");
            String cinemaName = intent.getStringExtra("EXTRA_CINEMA_NAME");
            String showtime = intent.getStringExtra("EXTRA_SHOWTIME");

            if (title != null && !title.isEmpty()) {
                movieTitle = title;
            }
            if (cinemaName != null && !cinemaName.isEmpty()) {
                selectedCinemaName = cinemaName;
                if (tvMovieTitleHeader != null) {
                    tvMovieTitleHeader.setText(cinemaName.toUpperCase());
                }
            }
            if (showtime != null && !showtime.isEmpty()) {
                selectedShowtime = showtime;
                highlightMatchingShowtime(showtime);
            }
        }
        updateSelectedInfoUI();
    }

    private void highlightMatchingShowtime(String targetTime) {
        View cardLatmat = findViewById(R.id.card_movie_latmat);
        View cardMada = findViewById(R.id.card_movie_mada);

        View slot1 = findViewById(R.id.slot_latmat_1);
        View slot2 = findViewById(R.id.slot_latmat_2);
        View slot3 = findViewById(R.id.slot_latmat_3);
        View slot4 = findViewById(R.id.slot_latmat_4);

        View slotMada1 = findViewById(R.id.slot_mada_1);
        View slotMada2 = findViewById(R.id.slot_mada_2);
        View slotMada3 = findViewById(R.id.slot_mada_3);

        View[] latmatSlots = {slot1, slot2, slot3, slot4};
        String[] latmatTimes = {"18:30", "19:45", "20:30", "21:15"};

        View[] madaSlots = {slotMada1, slotMada2, slotMada3};
        String[] madaTimes = {"19:00", "20:15", "21:30"};

        boolean isLatmatMatch = false;
        boolean isMadaMatch = false;

        for (String t : latmatTimes) {
            if (t.equals(targetTime)) isLatmatMatch = true;
        }
        for (String t : madaTimes) {
            if (t.equals(targetTime)) isMadaMatch = true;
        }

        if (isLatmatMatch) {
            if (cardLatmat != null) cardLatmat.setVisibility(View.VISIBLE);
            if (cardMada != null) cardMada.setVisibility(View.GONE);

            for (int i = 0; i < latmatSlots.length; i++) {
                if (latmatSlots[i] != null) {
                    if (latmatTimes[i].equals(targetTime)) {
                        latmatSlots[i].setBackgroundResource(R.drawable.bg_option_card_selected);
                    } else {
                        latmatSlots[i].setBackgroundResource(R.drawable.bg_option_card_unselected);
                    }
                }
            }
        } else if (isMadaMatch) {
            if (cardMada != null) cardMada.setVisibility(View.VISIBLE);
            if (cardLatmat != null) cardLatmat.setVisibility(View.GONE);

            for (int i = 0; i < madaSlots.length; i++) {
                if (madaSlots[i] != null) {
                    if (madaTimes[i].equals(targetTime)) {
                        madaSlots[i].setBackgroundResource(R.drawable.bg_option_card_selected);
                    } else {
                        madaSlots[i].setBackgroundResource(R.drawable.bg_option_card_unselected);
                    }
                }
            }
        } else {
            if (cardLatmat != null) cardLatmat.setVisibility(View.VISIBLE);
            if (cardMada != null) cardMada.setVisibility(View.VISIBLE);
        }
    }

    private void setupShowtimeSelection() {
        View slot1 = findViewById(R.id.slot_latmat_1);
        View slot2 = findViewById(R.id.slot_latmat_2);
        View slot3 = findViewById(R.id.slot_latmat_3);
        View slot4 = findViewById(R.id.slot_latmat_4);

        View slotMada1 = findViewById(R.id.slot_mada_1);
        View slotMada2 = findViewById(R.id.slot_mada_2);
        View slotMada3 = findViewById(R.id.slot_mada_3);

        View[] allSlots = {slot1, slot2, slot3, slot4, slotMada1, slotMada2, slotMada3};
        String[] times = {"18:30", "19:45", "20:30", "21:15", "19:00", "20:15", "21:30"};

        for (int i = 0; i < allSlots.length; i++) {
            final int index = i;
            if (allSlots[i] != null) {
                allSlots[i].setOnClickListener(v -> {
                    selectedShowtime = times[index];
                    // Reset all to unselected
                    for (View s : allSlots) {
                        if (s != null && s.getVisibility() == View.VISIBLE) {
                            s.setBackgroundResource(R.drawable.bg_option_card_unselected);
                        }
                    }
                    // Highlight selected
                    allSlots[index].setBackgroundResource(R.drawable.bg_option_card_selected);
                    updateSelectedInfoUI();
                });
            }
        }
    }

    private void updateSelectedInfoUI() {
        if (tvSelectedInfo != null) {
            tvSelectedInfo.setText(selectedCinemaName + " • " + selectedShowtime);
        }
    }

    private void setupEvents() {
        // 1. Nút Quay lại (<)
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> {
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            });
        }

        // 2. Nút Tiếp tục chọn ghế: Mở SelectSeatActivity
        if (btnContinue != null) {
            btnContinue.setOnClickListener(v -> {
                Intent intent = new Intent(SelectCinemaActivity.this, SelectSeatActivity.class);
                intent.putExtra("EXTRA_MOVIE_TITLE", movieTitle);
                intent.putExtra("EXTRA_SHOWTIME", selectedShowtime);
                intent.putExtra("EXTRA_CINEMA_NAME", selectedCinemaName);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            });
        }
    }
}
