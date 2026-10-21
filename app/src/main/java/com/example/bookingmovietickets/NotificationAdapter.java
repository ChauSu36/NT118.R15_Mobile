package com.example.bookingmovietickets;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private final Context context;
    private final List<Notification> list;

    public NotificationAdapter(Context context, List<Notification> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notification item = list.get(position);

        holder.tvTitle.setText(item.getTitle());
        holder.tvSubtitle.setText(item.getSubtitle());

        if (item.getDetail() != null && !item.getDetail().isEmpty()) {
            holder.tvDetail.setVisibility(View.VISIBLE);
            holder.tvDetail.setText(item.getDetail());
        } else {
            holder.tvDetail.setVisibility(View.GONE);
        }

        holder.tvTime.setText(item.getTime());

        // Phân loại hiển thị theo Type
        if (item.getType() == Notification.TYPE_REMINDER) {
            holder.imgIcon.setImageResource(R.drawable.ic_notification);
            holder.imgIcon.setBackgroundResource(R.drawable.bg_icon_circle_pink);
            holder.imgIcon.setColorFilter(Color.parseColor("#D32F2F"));

            holder.tvTitle.setTextColor(Color.parseColor("#D32F2F"));
            holder.tvTime.setTextColor(Color.parseColor("#D32F2F"));

            holder.btnQr.setVisibility(View.VISIBLE);
            holder.btnQr.setText(item.getActionText());
            holder.tvActionLink.setVisibility(View.GONE);

        } else if (item.getType() == Notification.TYPE_SUCCESS) {
            holder.imgIcon.setImageResource(R.drawable.ic_check);

            // 2. Đổi nền sang xanh nhạt bg_icon_green_light
            holder.imgIcon.setBackgroundResource(R.drawable.bg_icon_green_light);

            // 3. Đổi màu dấu tick thành xanh lá đậm
            holder.imgIcon.setColorFilter(Color.parseColor("#2E7D32"));

            holder.tvTitle.setTextColor(Color.parseColor("#1A1A1A"));
            holder.tvTime.setTextColor(Color.parseColor("#888888"));

            holder.btnQr.setVisibility(View.GONE);
            holder.tvActionLink.setVisibility(View.VISIBLE);
            holder.tvActionLink.setText(item.getActionText());

        } else if (item.getType() == Notification.TYPE_VOUCHER) {
            holder.imgIcon.setImageResource(R.drawable.ic_discount_tag);
            holder.imgIcon.setBackgroundResource(R.drawable.bg_icon_circle_pink);
            holder.imgIcon.setColorFilter(Color.parseColor("#E65100"));

            holder.tvTitle.setTextColor(Color.parseColor("#1A1A1A"));
            holder.tvTime.setTextColor(Color.parseColor("#888888"));

            holder.btnQr.setVisibility(View.GONE);
            holder.tvActionLink.setVisibility(View.VISIBLE);
            holder.tvActionLink.setText(item.getActionText());
        }

        // Sự kiện click nút QR -> Mở TicketDetailActivity
        holder.btnQr.setOnClickListener(v -> {
            Intent intent = new Intent(context, TicketDetailActivity.class);
            context.startActivity(intent);
            if (context instanceof AppCompatActivity) {
                ((AppCompatActivity) context).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        // Sự kiện click "Xem chi tiết hóa đơn" hoặc "Áp dụng ngay"
        holder.tvActionLink.setOnClickListener(v -> {
            Intent intent;
            if (item.getType() == Notification.TYPE_SUCCESS) {
                intent = new Intent(context, BookingSuccessActivity.class);
            } else {
                intent = new Intent(context, CinemaActivity.class);
                intent.putExtra("EXTRA_VOUCHER_CODE", "CQS30K");
                intent.putExtra("EXTRA_DISCOUNT_AMOUNT", 30000);
            }
            context.startActivity(intent);
            if (context instanceof AppCompatActivity) {
                ((AppCompatActivity) context).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        holder.itemView.setOnClickListener(v ->
                Toast.makeText(context, "Thông báo: " + item.getTitle(), Toast.LENGTH_SHORT).show());
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgIcon;
        TextView tvTitle, tvSubtitle, tvDetail, tvTime, tvActionLink;
        Button btnQr;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgIcon = itemView.findViewById(R.id.imgNotificationIcon);
            tvTitle = itemView.findViewById(R.id.tvNotificationTitle);
            tvSubtitle = itemView.findViewById(R.id.tvNotificationSubtitle);
            tvDetail = itemView.findViewById(R.id.tvNotificationDetail);
            tvTime = itemView.findViewById(R.id.tvNotificationTime);
            tvActionLink = itemView.findViewById(R.id.tvNotificationActionLink);
            btnQr = itemView.findViewById(R.id.btnNotificationQr);
        }
    }
}
