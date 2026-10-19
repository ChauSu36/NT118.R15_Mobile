package com.example.bookingmovietickets;

import android.os.Bundle;
import android.os.CountDownTimer;
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

public class PaymentActivity extends AppCompatActivity {

    private static final long COUNTDOWN_MILLIS = 10 * 60 * 1000;

    // ====== View ======
    private TextView tvCountdown;
    private LinearLayout rowBank, rowAtm, rowCash;
    private ImageView ivRadioBank, ivRadioAtm, ivRadioCash;
    private TextView tvLabelBank, tvLabelAtm, tvLabelCash;
    private TextView btnCancelPayment;

    private CountDownTimer countDownTimer;

    private static final int METHOD_BANK = 0;
    private static final int METHOD_ATM = 1;
    private static final int METHOD_CASH = 2;
    private int selectedMethod = METHOD_BANK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment);

        View mainView = findViewById(R.id.main);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        bindViews();
        setupPaymentMethods();
        setupCancelButton();
        startCountdown();
    }

    private void bindViews() {
        tvCountdown = findViewById(R.id.tv_countdown);

        rowBank = findViewById(R.id.row_bank);
        rowAtm = findViewById(R.id.row_atm);
        rowCash = findViewById(R.id.row_cash);

        ivRadioBank = findViewById(R.id.iv_radio_bank);
        ivRadioAtm = findViewById(R.id.iv_radio_atm);
        ivRadioCash = findViewById(R.id.iv_radio_cash);

        tvLabelBank = findViewById(R.id.tv_label_bank);
        tvLabelAtm = findViewById(R.id.tv_label_atm);
        tvLabelCash = findViewById(R.id.tv_label_cash);

        btnCancelPayment = findViewById(R.id.btn_cancel_payment);
    }

    private void setupPaymentMethods() {
        rowBank.setOnClickListener(v -> selectMethod(METHOD_BANK));
        rowAtm.setOnClickListener(v -> selectMethod(METHOD_ATM));
        rowCash.setOnClickListener(v -> selectMethod(METHOD_CASH));
    }

    private void selectMethod(int method) {
        selectedMethod = method;

        boolean isBank = method == METHOD_BANK;
        boolean isAtm = method == METHOD_ATM;
        boolean isCash = method == METHOD_CASH;

        applyCardState(rowBank, ivRadioBank, tvLabelBank, isBank);
        applyCardState(rowAtm, ivRadioAtm, tvLabelAtm, isAtm);
        applyCardState(rowCash, ivRadioCash, tvLabelCash, isCash);

        View qrSection = findViewById(R.id.layout_qr_section);
        qrSection.setVisibility(isCash ? View.GONE : View.VISIBLE);
    }

    private void applyCardState(LinearLayout row, ImageView radioIcon, TextView label, boolean selected) {
        if (selected) {
            row.setBackgroundResource(R.drawable.bg_option_card_selected);
            radioIcon.setImageResource(R.drawable.ic_radio_selected);
            label.setTextColor(getColor(R.color.color_primary));
        } else {
            row.setBackgroundResource(R.drawable.bg_option_card_unselected);
            radioIcon.setImageResource(R.drawable.ic_radio_unselected);
            label.setTextColor(getColor(android.R.color.darker_gray));
        }
    }

    private void setupCancelButton() {
        btnCancelPayment.setOnClickListener(v -> {
            Toast.makeText(this, "Đã hủy thanh toán", Toast.LENGTH_SHORT).show();
            finish();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });
    }

    private void startCountdown() {
        countDownTimer = new CountDownTimer(COUNTDOWN_MILLIS, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long totalSeconds = millisUntilFinished / 1000;
                long minutes = totalSeconds / 60;
                long seconds = totalSeconds % 60;
                tvCountdown.setText(String.format("%02d:%02d", minutes, seconds));
            }

            @Override
            public void onFinish() {
                tvCountdown.setText("00:00");
                Toast.makeText(PaymentActivity.this,
                        "Hết thời gian giữ vé, đơn hàng đã bị hủy",
                        Toast.LENGTH_LONG).show();
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        };
        countDownTimer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
