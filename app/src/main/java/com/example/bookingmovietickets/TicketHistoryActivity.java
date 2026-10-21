package com.example.bookingmovietickets;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TicketHistoryActivity extends AppCompatActivity {

    private ImageView btnBack;
    private TextView tabUpcoming, tabWatched, tabCancelled;
    private RecyclerView rvTicketHistory;
    private TicketHistoryAdapter adapter;

    private List<TicketHistory> allTickets;
    private List<TicketHistory> filteredTickets;

    private static final int TAB_UPCOMING = 0;
    private static final int TAB_WATCHED = 1;
    private static final int TAB_CANCELLED = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ticket_history);

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
        setupEvents();

        // Mặc định chọn Tab Sắp xem
        switchTab(TAB_UPCOMING);
    }

    private void bindViews() {
        btnBack = findViewById(R.id.btn_back);
        tabUpcoming = findViewById(R.id.tab_upcoming);
        tabWatched = findViewById(R.id.tab_watched);
        tabCancelled = findViewById(R.id.tab_cancelled);
        rvTicketHistory = findViewById(R.id.rv_ticket_history);
    }

    private void initDummyData() {
        allTickets = new ArrayList<>();

        allTickets.add(new TicketHistory(
                "#CQS88921",
                "LẬT MẶT 7: ONE WISH",
                "CQS Cinema Nguyễn Trãi",
                "18:30 • 20/10/2026",
                "RẠP 03",
                "F05, F06",
                "190.000 đ",
                "SẮP XEM",
                "2D Phụ đề"
        ));

        allTickets.add(new TicketHistory(
                "#CQS88920",
                "MAI (2024)",
                "CQS Cinema Bến Thành",
                "19:00 • 15/09/2026",
                "RẠP 01",
                "D08",
                "100.000 đ",
                "ĐÃ XEM",
                "2D Phụ đề"
        ));

        allTickets.add(new TicketHistory(
                "#CQS88918",
                "KUNG FU PANDA 4",
                "CQS Cinema Nguyễn Trãi",
                "20:15 • 10/08/2026",
                "RẠP 02",
                "E04, E05",
                "200.000 đ",
                "ĐÃ HỦY",
                "2D Lồng tiếng"
        ));
    }

    private void setupRecyclerView() {
        filteredTickets = new ArrayList<>();
        adapter = new TicketHistoryAdapter(this, filteredTickets);
        if (rvTicketHistory != null) {
            rvTicketHistory.setLayoutManager(new LinearLayoutManager(this));
            rvTicketHistory.setAdapter(adapter);
        }
    }

    private void setupTabs() {
        if (tabUpcoming != null) {
            tabUpcoming.setOnClickListener(v -> switchTab(TAB_UPCOMING));
        }
        if (tabWatched != null) {
            tabWatched.setOnClickListener(v -> switchTab(TAB_WATCHED));
        }
        if (tabCancelled != null) {
            tabCancelled.setOnClickListener(v -> switchTab(TAB_CANCELLED));
        }
    }

    private void switchTab(int tabIndex) {
        setTabStyle(tabUpcoming, tabIndex == TAB_UPCOMING);
        setTabStyle(tabWatched, tabIndex == TAB_WATCHED);
        setTabStyle(tabCancelled, tabIndex == TAB_CANCELLED);

        filteredTickets.clear();
        String targetStatus = (tabIndex == TAB_UPCOMING) ? "SẮP XEM" : (tabIndex == TAB_WATCHED) ? "ĐÃ XEM" : "ĐÃ HỦY";

        for (TicketHistory ticket : allTickets) {
            if (targetStatus.equalsIgnoreCase(ticket.getStatus())) {
                filteredTickets.add(ticket);
            }
        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    private void setTabStyle(TextView tab, boolean active) {
        if (tab == null) return;
        if (active) {
            tab.setBackgroundResource(R.drawable.bg_button_primary);
            tab.setTextColor(Color.WHITE);
        } else {
            tab.setBackgroundResource(R.drawable.bg_button_outline);
            tab.setTextColor(Color.parseColor("#424242"));
        }
    }

    private void setupEvents() {
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> {
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            });
        }
    }
}
