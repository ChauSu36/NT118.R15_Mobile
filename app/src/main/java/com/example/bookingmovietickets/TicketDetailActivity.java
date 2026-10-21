package com.example.bookingmovietickets;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TicketDetailActivity extends AppCompatActivity {

    private ImageView btnBack;
    private TextView btnSaveQr, btnDirections;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ticket_detail);

        View mainView = findViewById(R.id.main);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        bindViews();
        setupEvents();
    }

    private void bindViews() {
        btnBack = findViewById(R.id.btn_back);
        btnSaveQr = findViewById(R.id.btn_save_qr);
        btnDirections = findViewById(R.id.btn_directions);
    }

    private void setupEvents() {
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> {
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            });
        }

        if (btnSaveQr != null) {
            btnSaveQr.setOnClickListener(v ->
                    Toast.makeText(this, "Đã lưu mã QR vào thư viện ảnh của máy!", Toast.LENGTH_SHORT).show());
        }

        if (btnDirections != null) {
            btnDirections.setOnClickListener(v ->
                    Toast.makeText(this, "Mở bản đồ chỉ đường tới CQS Cinema Nguyễn Trãi", Toast.LENGTH_SHORT).show());
        }
    }
}
