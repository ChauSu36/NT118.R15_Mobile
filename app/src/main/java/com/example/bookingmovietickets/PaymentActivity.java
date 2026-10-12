package com.example.bookingmovietickets;
// LƯU Ý: đổi dòng package ở trên cho khớp với package thật trong project của bạn.

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PaymentActivity extends AppCompatActivity {

    // Thời gian đếm ngược trước khi tự hủy đơn — đơn vị mili-giây.
    // Ảnh thiết kế hiện khoảng 9 phút 58 giây, nên để tròn 10 phút cho dễ hiểu;
    // con số thật nên lấy từ server (thời điểm tạo đơn + 10 phút) khi nối API.
    private static final long COUNTDOWN_MILLIS = 10 * 60 * 1000;

    // ====== View ======
    private TextView tvCountdown;
    private LinearLayout rowBank, rowAtm, rowCash;
    private ImageView ivRadioBank, ivRadioAtm, ivRadioCash;
    private TextView tvLabelBank, tvLabelAtm, tvLabelCash;
    private TextView btnCancelPayment;

    private CountDownTimer countDownTimer;

    // 3 phương thức thanh toán, đánh số để dễ so sánh trong switch
    private static final int METHOD_BANK = 0;
    private static final int METHOD_ATM = 1;
    private static final int METHOD_CASH = 2;
    private int selectedMethod = METHOD_BANK; // mặc định chọn sẵn "Chuyển khoản ngân hàng" giống ảnh

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        bindViews();
        setupPaymentMethods();
        setupCancelButton();
        startCountdown();

        // TODO: nối dữ liệu thật — nhận booking_id, số tiền, nội dung chuyển khoản
        // từ màn SelectSeatActivity truyền qua Intent (putExtra/getExtras), thay vì
        // dùng số "279.000 đ" và mã "CQS279001" hard-code trong layout.
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

    /** Gắn sự kiện bấm cho cả 3 thẻ phương thức thanh toán. */
    private void setupPaymentMethods() {
        rowBank.setOnClickListener(v -> selectMethod(METHOD_BANK));
        rowAtm.setOnClickListener(v -> selectMethod(METHOD_ATM));
        rowCash.setOnClickListener(v -> selectMethod(METHOD_CASH));
    }

    /**
     * Đổi trạng thái 3 thẻ: thẻ được chọn tô viền đỏ + nền hồng + chữ đỏ,
     * 2 thẻ còn lại trở về viền xám + chữ đen — đúng hiệu ứng "các ô có thể
     * đổi khi bấm vào" bạn mô tả.
     */
    private void selectMethod(int method) {
        selectedMethod = method;

        boolean isBank = method == METHOD_BANK;
        boolean isAtm = method == METHOD_ATM;
        boolean isCash = method == METHOD_CASH;

        applyCardState(rowBank, ivRadioBank, tvLabelBank, isBank);
        applyCardState(rowAtm, ivRadioAtm, tvLabelAtm, isAtm);
        applyCardState(rowCash, ivRadioCash, tvLabelCash, isCash);

        // Khi chọn "Tiền mặt tại rạp" thì không cần hiện QR nữa.
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
            // Không cần startActivity gì cả — chỉ cần đóng màn Thanh toán lại.
            // Vì SelectSeatActivity mở PaymentActivity bằng startActivity() (không
            // gọi finish()), nó vẫn nằm sẵn trong back stack, nên finish() ở đây
            // sẽ tự động quay về đúng màn Chọn ghế.
            Toast.makeText(this, "Đã hủy thanh toán", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    /** Đếm ngược thời gian giữ đơn, định dạng mm:ss. */
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
            }
        };
        countDownTimer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Hủy CountDownTimer khi rời màn hình, tránh rò rỉ bộ nhớ (memory leak)
        // vì CountDownTimer vẫn chạy ngầm nếu không được cancel() thủ công.
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
