package com.example.bookingmovietickets;

import android.app.AlertDialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Calendar;
import java.util.Locale;

public class DashboardActivity extends AppCompatActivity {

    private ImageView btnBack;
    private TextView tabDay, tabMonth, tabYear;
    private TextView btnSelectCinema, btnSelectDate;
    private TextView tvRevenueLabel, tvTotalRevenueAmount, tvGrowthRate;
    private TextView tvTicketRevenue, tvTicketPercent;
    private TextView tvFnbRevenue, tvFnbPercent;
    private TextView tvTicketsSold, tvTicketsGrowth;
    private TextView tvOccupancyRate, tvOccupancyGrowth;

    private static final int MODE_DAY = 0;
    private static final int MODE_MONTH = 1;
    private static final int MODE_YEAR = 2;
    private int currentMode = MODE_DAY;

    private String selectedCinema = "CQS Nguyễn Trãi";
    private String selectedDate = "30/09/2026";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);

        View mainView = findViewById(R.id.main);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        bindViews();
        setupTabs();
        setupFilters();
        setupEvents();

        switchMode(MODE_DAY);
    }

    private void bindViews() {
        btnBack = findViewById(R.id.btn_back);

        tabDay = findViewById(R.id.tab_day);
        tabMonth = findViewById(R.id.tab_month);
        tabYear = findViewById(R.id.tab_year);

        btnSelectCinema = findViewById(R.id.btn_select_cinema);
        btnSelectDate = findViewById(R.id.btn_select_date);

        tvRevenueLabel = findViewById(R.id.tv_revenue_label);
        tvTotalRevenueAmount = findViewById(R.id.tv_total_revenue_amount);
        tvGrowthRate = findViewById(R.id.tv_growth_rate);

        tvTicketRevenue = findViewById(R.id.tv_ticket_revenue);
        tvTicketPercent = findViewById(R.id.tv_ticket_percent);

        tvFnbRevenue = findViewById(R.id.tv_fnb_revenue);
        tvFnbPercent = findViewById(R.id.tv_fnb_percent);

        tvTicketsSold = findViewById(R.id.tv_tickets_sold);
        tvTicketsGrowth = findViewById(R.id.tv_tickets_growth);

        tvOccupancyRate = findViewById(R.id.tv_occupancy_rate);
        tvOccupancyGrowth = findViewById(R.id.tv_occupancy_growth);
    }

    private void setupTabs() {
        if (tabDay != null) tabDay.setOnClickListener(v -> switchMode(MODE_DAY));
        if (tabMonth != null) tabMonth.setOnClickListener(v -> switchMode(MODE_MONTH));
        if (tabYear != null) tabYear.setOnClickListener(v -> switchMode(MODE_YEAR));
    }

    private void switchMode(int mode) {
        currentMode = mode;

        setTabActive(tabDay, mode == MODE_DAY);
        setTabActive(tabMonth, mode == MODE_MONTH);
        setTabActive(tabYear, mode == MODE_YEAR);

        recalculateDataAndRefreshUI();
    }

    private void setTabActive(TextView tab, boolean active) {
        if (tab == null) return;
        if (active) {
            tab.setBackgroundResource(R.drawable.bg_card_rounded);
            tab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#1A1A1A")));
            tab.setTextColor(Color.WHITE);
        } else {
            tab.setBackground(null);
            tab.setTextColor(Color.parseColor("#757575"));
        }
    }

    private void setupFilters() {
        // Nút Chọn Rạp
        if (btnSelectCinema != null) {
            btnSelectCinema.setOnClickListener(v -> showCinemaSelectionDialog());
        }

        // Nút Chọn Mốc Thời Gian
        if (btnSelectDate != null) {
            btnSelectDate.setOnClickListener(v -> showDatePickerBottomSheet());
        }
    }

    private void showCinemaSelectionDialog() {
        String[] cinemas = {"Tất cả các rạp", "CQS Nguyễn Trãi", "CQS Hai Bà Trưng", "CQS Tân Bình"};
        new AlertDialog.Builder(this)
                .setTitle("Chọn rạp chi nhánh")
                .setItems(cinemas, (dialog, which) -> {
                    selectedCinema = cinemas[which];
                    if (btnSelectCinema != null) {
                        btnSelectCinema.setText("📍 " + selectedCinema + "  ▾");
                    }
                    recalculateDataAndRefreshUI();
                    Toast.makeText(this, "Đã lọc báo cáo cho: " + selectedCinema, Toast.LENGTH_SHORT).show();
                })
                .show();
    }

    private void showDatePickerBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_select_date, null);
        bottomSheetDialog.setContentView(dialogView);

        DatePicker datePicker = dialogView.findViewById(R.id.datePicker);
        Button btnConfirm = dialogView.findViewById(R.id.btnConfirmDate);

        if (datePicker != null) {
            Calendar cal = Calendar.getInstance();
            cal.set(2026, Calendar.SEPTEMBER, 30);
            datePicker.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        }

        if (btnConfirm != null) {
            btnConfirm.setOnClickListener(v -> {
                if (datePicker != null) {
                    int day = datePicker.getDayOfMonth();
                    int month = datePicker.getMonth() + 1;
                    int year = datePicker.getYear();

                    selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%d", day, month, year);
                    if (btnSelectDate != null) {
                        btnSelectDate.setText("📅 " + selectedDate + "  ▾");
                    }
                    recalculateDataAndRefreshUI();
                }
                bottomSheetDialog.dismiss();
            });
        }

        bottomSheetDialog.show();
    }

    private void recalculateDataAndRefreshUI() {
        if (currentMode == MODE_DAY) {
            if (tvRevenueLabel != null) tvRevenueLabel.setText("TỔNG DOANH THU NGÀY " + selectedDate);
            if (tvTotalRevenueAmount != null) tvTotalRevenueAmount.setText("1.450.000.000 đ");
            if (tvGrowthRate != null) tvGrowthRate.setText("▲ +12.5% so với hôm qua");

            if (tvTicketRevenue != null) tvTicketRevenue.setText("980 Triệu đ");
            if (tvTicketPercent != null) tvTicketPercent.setText("67.6%");

            if (tvFnbRevenue != null) tvFnbRevenue.setText("470 Triệu đ");
            if (tvFnbPercent != null) tvFnbPercent.setText("32.4%");

            if (tvTicketsSold != null) tvTicketsSold.setText("12.250 Vé");
            if (tvTicketsGrowth != null) tvTicketsGrowth.setText("+8.4%");

            if (tvOccupancyRate != null) tvOccupancyRate.setText("68.5%");
            if (tvOccupancyGrowth != null) tvOccupancyGrowth.setText("+3.1%");

        } else if (currentMode == MODE_MONTH) {
            if (tvRevenueLabel != null) tvRevenueLabel.setText("TỔNG DOANH THU THÁNG 09/2026");
            if (tvTotalRevenueAmount != null) tvTotalRevenueAmount.setText("38.500.000.000 đ");
            if (tvGrowthRate != null) tvGrowthRate.setText("▲ +18.2% so với tháng trước");

            if (tvTicketRevenue != null) tvTicketRevenue.setText("26.2 Triệu đ");
            if (tvTicketPercent != null) tvTicketPercent.setText("68.1%");

            if (tvFnbRevenue != null) tvFnbRevenue.setText("12.3 Triệu đ");
            if (tvFnbPercent != null) tvFnbPercent.setText("31.9%");

            if (tvTicketsSold != null) tvTicketsSold.setText("325.000 Vé");
            if (tvTicketsGrowth != null) tvTicketsGrowth.setText("+14.0%");

            if (tvOccupancyRate != null) tvOccupancyRate.setText("74.2%");
            if (tvOccupancyGrowth != null) tvOccupancyGrowth.setText("+5.2%");

        } else {
            // MODE_YEAR
            if (tvRevenueLabel != null) tvRevenueLabel.setText("TỔNG DOANH THU NĂM 2026");
            if (tvTotalRevenueAmount != null) tvTotalRevenueAmount.setText("412.000.000.000 đ");
            if (tvGrowthRate != null) tvGrowthRate.setText("▲ +24.6% so với năm 2025");

            if (tvTicketRevenue != null) tvTicketRevenue.setText("280 Tỷ đ");
            if (tvTicketPercent != null) tvTicketPercent.setText("68.0%");

            if (tvFnbRevenue != null) tvFnbRevenue.setText("132 Tỷ đ");
            if (tvFnbPercent != null) tvFnbPercent.setText("32.0%");

            if (tvTicketsSold != null) tvTicketsSold.setText("3.850.000 Vé");
            if (tvTicketsGrowth != null) tvTicketsGrowth.setText("+21.5%");

            if (tvOccupancyRate != null) tvOccupancyRate.setText("78.0%");
            if (tvOccupancyGrowth != null) tvOccupancyGrowth.setText("+8.0%");
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
