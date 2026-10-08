package com.example.bookingmovietickets;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class SelectSeatActivity extends AppCompatActivity {

    private ImageView btnBack;
    private RelativeLayout layoutVoucherBar;
    private Button btnCombo, btnBookNow;
    private TextView tvMovieTitle;
    private TextView tvTicketLabel, tvTicketPrice;
    private TextView tvComboPrice, tvDiscountPrice, tvTotalPrice;
    private TextView tvAppliedVoucher, tvVoucherStatus;
    private LinearLayout layoutSeatMatrix;

    // Giá tiền vé
    private static final int PRICE_REGULAR = 100000;
    private static final int PRICE_VIP = 110000;

    // Quản lý danh sách ghế đã chọn
    private final List<SeatInfo> selectedSeats = new ArrayList<>();

    // Các biến giá trị tổng tiền
    private int ticketTotal = 0;
    private int comboTotal = 89000; // Mặc định Combo 2
    private int discountTotal = 30000; // Mặc định Voucher 30k
    private String selectedVoucherCode = "CQS30K";

    private final DecimalFormat formatter = new DecimalFormat("#,###");

    private static class SeatInfo {
        String name; // Ví dụ: "F5"
        boolean isVip;
        int price;
        TextView view;

        SeatInfo(String name, boolean isVip, int price, TextView view) {
            this.name = name;
            this.isVip = isVip;
            this.price = price;
            this.view = view;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_select_seat);

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
        setupSeatMatrix();
        setupEvents();
        recalculateAndRefreshUI();
    }

    private void bindViews() {
        btnBack = findViewById(R.id.btn_back);
        layoutVoucherBar = findViewById(R.id.layout_voucher_bar);
        btnCombo = findViewById(R.id.btn_combo);
        btnBookNow = findViewById(R.id.btn_book_now);

        tvMovieTitle = findViewById(R.id.tv_movie_title);

        tvTicketLabel = findViewById(R.id.tv_ticket_label);
        tvTicketPrice = findViewById(R.id.tv_ticket_price);
        tvComboPrice = findViewById(R.id.tv_combo_price);
        tvDiscountPrice = findViewById(R.id.tv_discount_price);
        tvTotalPrice = findViewById(R.id.tv_total_price);

        tvAppliedVoucher = findViewById(R.id.tv_applied_voucher);
        tvVoucherStatus = findViewById(R.id.tv_voucher_status);

        layoutSeatMatrix = findViewById(R.id.layout_seat_matrix);
    }

    private void displayIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            String title = intent.getStringExtra("EXTRA_MOVIE_TITLE");
            if (title != null && !title.isEmpty() && tvMovieTitle != null) {
                tvMovieTitle.setText(title);
            }
        }
    }

    /**
     * Tự động duyệt qua tất cả các ô ghế trong sơ đồ ma trận
     * và gán sự kiện click để chọn / hủy chọn ghế.
     */
    private void setupSeatMatrix() {
        if (layoutSeatMatrix == null) return;

        for (int i = 0; i < layoutSeatMatrix.getChildCount(); i++) {
            View rowView = layoutSeatMatrix.getChildAt(i);
            if (!(rowView instanceof LinearLayout)) continue;

            LinearLayout rowLayout = (LinearLayout) rowView;
            if (rowLayout.getChildCount() < 2) continue;

            View firstChild = rowLayout.getChildAt(0);
            if (!(firstChild instanceof TextView)) continue;
            String rowLetter = ((TextView) firstChild).getText().toString().trim();

            for (int j = 1; j < rowLayout.getChildCount(); j++) {
                View child = rowLayout.getChildAt(j);
                if (!(child instanceof TextView)) continue;

                TextView tvSeat = (TextView) child;
                String seatNum = tvSeat.getText().toString().trim();
                String seatName = rowLetter + seatNum;

                int color = getBackgroundColor(tvSeat);

                boolean isSold = (color == 0xFFE0E0E0 || color == Color.GRAY || color == Color.LTGRAY);
                boolean isSelected = (color == 0xFF4CAF50 || color == Color.GREEN);
                boolean isVip = (color == 0xFFFF9800 || "C".equals(rowLetter) || "D".equals(rowLetter) || "F".equals(rowLetter));

                int price = isVip ? PRICE_VIP : PRICE_REGULAR;

                if (isSelected) {
                    selectedSeats.add(new SeatInfo(seatName, isVip, price, tvSeat));
                }

                tvSeat.setOnClickListener(v -> {
                    if (isSold) {
                        Toast.makeText(SelectSeatActivity.this, "Ghế " + seatName + " đã có người đặt!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    SeatInfo existing = null;
                    for (SeatInfo seat : selectedSeats) {
                        if (seat.name.equals(seatName)) {
                            existing = seat;
                            break;
                        }
                    }

                    if (existing != null) {
                        selectedSeats.remove(existing);
                        if (isVip) {
                            tvSeat.setBackgroundColor(0xFFFF9800);
                            tvSeat.setTextColor(Color.WHITE);
                        } else {
                            tvSeat.setBackgroundColor(Color.WHITE);
                            tvSeat.setTextColor(Color.BLACK);
                        }
                    } else {
                        selectedSeats.add(new SeatInfo(seatName, isVip, price, tvSeat));
                        tvSeat.setBackgroundColor(0xFF4CAF50);
                        tvSeat.setTextColor(Color.WHITE);
                    }

                    recalculateAndRefreshUI();
                });
            }
        }
    }

    private int getBackgroundColor(View view) {
        if (view.getBackground() instanceof ColorDrawable) {
            return ((ColorDrawable) view.getBackground()).getColor();
        }
        return 0;
    }

    private void setupEvents() {
        // 1. Nút quay lại (<): Đóng màn hình SelectSeatActivity để quay lại SelectCinemaActivity
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        // 2. Bấm chọn Combo Bắp Nước
        if (btnCombo != null) {
            btnCombo.setOnClickListener(v -> showComboDialog());
        }

        // 3. Bấm mở danh sách Mã Giảm Giá
        if (layoutVoucherBar != null) {
            layoutVoucherBar.setOnClickListener(v -> showVoucherDialog());
        }

        // 4. Nút Đặt Vé Ngay
        if (btnBookNow != null) {
            btnBookNow.setOnClickListener(v -> {
                if (selectedSeats.isEmpty()) {
                    Toast.makeText(this, "Vui lòng chọn ít nhất 1 ghế!", Toast.LENGTH_SHORT).show();
                    return;
                }
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < selectedSeats.size(); i++) {
                    if (i > 0) sb.append(", ");
                    sb.append(selectedSeats.get(i).name);
                }
                String seatsText = sb.toString();
                int finalTotal = ticketTotal + comboTotal - discountTotal;
                if (finalTotal < 0) finalTotal = 0;

                Toast.makeText(this, "Đặt vé thành công ghế [" + seatsText + "]. Tổng: " + formatter.format(finalTotal) + " đ", Toast.LENGTH_LONG).show();
            });
        }
    }

    /** Hiển thị BottomSheet chọn Combo Bắp Nước với khung viền đẹp mắt. */
    private void showComboDialog() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_select_combo, null);
        bottomSheetDialog.setContentView(dialogView);

        View cardCombo1 = dialogView.findViewById(R.id.card_combo_1);
        View cardCombo2 = dialogView.findViewById(R.id.card_combo_2);
        View cardCombo3 = dialogView.findViewById(R.id.card_combo_3);
        View btnClose = dialogView.findViewById(R.id.btn_close_combo_dialog);

        // Highlight combo đang chọn
        if (comboTotal == 69000 && cardCombo1 != null) {
            cardCombo1.setBackgroundResource(R.drawable.bg_option_card_selected);
        } else if (comboTotal == 89000 && cardCombo2 != null) {
            cardCombo2.setBackgroundResource(R.drawable.bg_option_card_selected);
        } else if (comboTotal == 129000 && cardCombo3 != null) {
            cardCombo3.setBackgroundResource(R.drawable.bg_option_card_selected);
        }

        if (cardCombo1 != null) {
            cardCombo1.setOnClickListener(v -> {
                comboTotal = 69000;
                recalculateAndRefreshUI();
                bottomSheetDialog.dismiss();
            });
        }

        if (cardCombo2 != null) {
            cardCombo2.setOnClickListener(v -> {
                comboTotal = 89000;
                recalculateAndRefreshUI();
                bottomSheetDialog.dismiss();
            });
        }

        if (cardCombo3 != null) {
            cardCombo3.setOnClickListener(v -> {
                comboTotal = 129000;
                recalculateAndRefreshUI();
                bottomSheetDialog.dismiss();
            });
        }

        if (btnClose != null) {
            btnClose.setOnClickListener(v -> bottomSheetDialog.dismiss());
        }

        bottomSheetDialog.show();
    }

    /** Hiển thị BottomSheet chọn Mã Giảm Giá với khung viền đẹp mắt. */
    private void showVoucherDialog() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_select_voucher, null);
        bottomSheetDialog.setContentView(dialogView);

        View card30k = dialogView.findViewById(R.id.card_voucher_30k);
        View card50k = dialogView.findViewById(R.id.card_voucher_50k);
        View card100k = dialogView.findViewById(R.id.card_voucher_100k);
        View btnClose = dialogView.findViewById(R.id.btn_close_voucher_dialog);

        // Highlight voucher đang chọn
        if (discountTotal == 30000 && card30k != null) {
            card30k.setBackgroundResource(R.drawable.bg_option_card_selected);
        } else if (discountTotal == 50000 && card50k != null) {
            card50k.setBackgroundResource(R.drawable.bg_option_card_selected);
        } else if (discountTotal == 100000 && card100k != null) {
            card100k.setBackgroundResource(R.drawable.bg_option_card_selected);
        }

        if (card30k != null) {
            card30k.setOnClickListener(v -> {
                discountTotal = 30000;
                selectedVoucherCode = "CQS30K";
                recalculateAndRefreshUI();
                bottomSheetDialog.dismiss();
            });
        }

        if (card50k != null) {
            card50k.setOnClickListener(v -> {
                discountTotal = 50000;
                selectedVoucherCode = "CQS50K";
                recalculateAndRefreshUI();
                bottomSheetDialog.dismiss();
            });
        }

        if (card100k != null) {
            card100k.setOnClickListener(v -> {
                discountTotal = 100000;
                selectedVoucherCode = "MOVIE100K";
                recalculateAndRefreshUI();
                bottomSheetDialog.dismiss();
            });
        }

        if (btnClose != null) {
            btnClose.setOnClickListener(v -> bottomSheetDialog.dismiss());
        }

        bottomSheetDialog.show();
    }

    /** Cập nhật tính toán tiền vé, combo, giảm giá và hiển thị lại toàn bộ UI. */
    private void recalculateAndRefreshUI() {
        // 1. Tính tổng tiền vé từ danh sách ghế đã chọn
        ticketTotal = 0;
        StringBuilder seatNamesSb = new StringBuilder();
        int vipCount = 0;
        int regCount = 0;

        for (int i = 0; i < selectedSeats.size(); i++) {
            SeatInfo seat = selectedSeats.get(i);
            ticketTotal += seat.price;

            if (seat.isVip) vipCount++;
            else regCount++;

            if (seatNamesSb.length() > 0) seatNamesSb.append(", ");
            seatNamesSb.append(seat.name);
        }

        if (tvTicketLabel != null) {
            if (selectedSeats.isEmpty()) {
                tvTicketLabel.setText("Tiền vé (0 ghế đã chọn):");
            } else {
                String typeStr = (vipCount > 0 && regCount > 0) ? "Ghế " : (vipCount > 0 ? "Ghế VIP " : "Ghế ");
                tvTicketLabel.setText("Tiền vé (" + selectedSeats.size() + "x " + typeStr + seatNamesSb + "):");
            }
        }
        if (tvTicketPrice != null) {
            tvTicketPrice.setText(formatter.format(ticketTotal) + " đ");
        }

        // 2. Cập nhật Combo UI
        if (tvComboPrice != null) {
            if (comboTotal > 0) {
                tvComboPrice.setText("+ " + formatter.format(comboTotal) + " đ");
            } else {
                tvComboPrice.setText("0 đ");
            }
        }
        if (btnCombo != null) {
            if (comboTotal > 0) {
                btnCombo.setText("🍿 COMBO (+" + (comboTotal / 1000) + "k)");
            } else {
                btnCombo.setText("🍿 CHỌN COMBO");
            }
        }

        // 3. Cập nhật Voucher UI
        if (discountTotal > 0 && !selectedVoucherCode.isEmpty()) {
            if (tvAppliedVoucher != null) tvAppliedVoucher.setText("Đã áp dụng mã " + selectedVoucherCode);
            if (tvVoucherStatus != null) tvVoucherStatus.setText("Giảm -" + formatter.format(discountTotal) + " đ");
            if (tvDiscountPrice != null) tvDiscountPrice.setText("- " + formatter.format(discountTotal) + " đ");
        } else {
            if (tvAppliedVoucher != null) tvAppliedVoucher.setText("Chưa áp dụng mã giảm giá");
            if (tvVoucherStatus != null) tvVoucherStatus.setText("0 đ");
            if (tvDiscountPrice != null) tvDiscountPrice.setText("0 đ");
        }

        // 4. Tính Thành Tiền cuối cùng
        int finalTotal = ticketTotal + comboTotal - discountTotal;
        if (finalTotal < 0) finalTotal = 0;

        String formattedTotal = formatter.format(finalTotal) + " đ";
        if (tvTotalPrice != null) {
            tvTotalPrice.setText(formattedTotal);
        }
        if (btnBookNow != null) {
            if (finalTotal > 0) {
                btnBookNow.setText("ĐẶT VÉ NGAY (" + (finalTotal / 1000) + "K)");
            } else {
                btnBookNow.setText("ĐẶT VÉ NGAY");
            }
        }
    }
}
