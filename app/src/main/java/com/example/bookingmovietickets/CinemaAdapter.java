package com.example.bookingmovietickets;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CinemaAdapter extends RecyclerView.Adapter<CinemaAdapter.CinemaViewHolder> {

    private final Context context;
    private final List<Cinema> cinemaList;

    public CinemaAdapter(Context context, List<Cinema> cinemaList) {
        this.context = context;
        this.cinemaList = cinemaList;
    }

    @NonNull
    @Override
    public CinemaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cinema, parent, false);
        return new CinemaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CinemaViewHolder holder, int position) {
        Cinema item = cinemaList.get(position);

        holder.tvName.setText(item.getName());
        holder.tvAddress.setText(item.getAddress());
        holder.tvDistance.setText(item.getDistance());

        // Phân biệt Rạp Gần Bạn và Rạp thường
        if (item.isNear()) {
            holder.tvBadgeNear.setVisibility(View.VISIBLE);

            GradientDrawable borderDrawable = new GradientDrawable();
            borderDrawable.setColor(Color.WHITE);
            borderDrawable.setCornerRadius(dpToPx(16));
            borderDrawable.setStroke(dpToPx(1.5f), Color.parseColor("#D32F2F"));
            holder.cardContainer.setBackground(borderDrawable);

            holder.btnViewSchedule.setBackgroundResource(R.drawable.bg_button_primary);
            holder.btnViewSchedule.setTextColor(Color.WHITE);
        } else {
            holder.tvBadgeNear.setVisibility(View.GONE);

            GradientDrawable borderDrawable = new GradientDrawable();
            borderDrawable.setColor(Color.WHITE);
            borderDrawable.setCornerRadius(dpToPx(16));
            borderDrawable.setStroke(dpToPx(1), Color.parseColor("#E0E0E0"));
            holder.cardContainer.setBackground(borderDrawable);

            holder.btnViewSchedule.setBackgroundResource(R.drawable.bg_button_outline_red);
            holder.btnViewSchedule.setTextColor(Color.parseColor("#D32F2F"));
        }

        // Tạo danh sách giờ chiếu động
        holder.layoutShowtimes.removeAllViews();
        List<String> showtimes = item.getShowtimes();
        for (int i = 0; i < showtimes.size(); i++) {
            final String timeText = showtimes.get(i);
            TextView timeTv = new TextView(context);
            timeTv.setText(timeText);
            timeTv.setTextSize(12);
            timeTv.setGravity(Gravity.CENTER);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    dpToPx(60), dpToPx(32));
            params.setMargins(0, 0, dpToPx(8), 0);
            timeTv.setLayoutParams(params);

            if (item.isNear() && i == 0) {
                GradientDrawable activePill = new GradientDrawable();
                activePill.setColor(Color.WHITE);
                activePill.setCornerRadius(dpToPx(6));
                activePill.setStroke(dpToPx(1), Color.parseColor("#D32F2F"));
                timeTv.setBackground(activePill);
                timeTv.setTextColor(Color.parseColor("#D32F2F"));
                timeTv.setTypeface(null, android.graphics.Typeface.BOLD);
            } else {
                GradientDrawable inactivePill = new GradientDrawable();
                inactivePill.setColor(Color.parseColor("#F5F5F5"));
                inactivePill.setCornerRadius(dpToPx(6));
                timeTv.setBackground(inactivePill);
                timeTv.setTextColor(Color.parseColor("#333333"));
            }

            timeTv.setOnClickListener(v -> {
                Intent intent = new Intent(context, SelectSeatActivity.class);
                intent.putExtra("EXTRA_SHOWTIME", timeText);
                context.startActivity(intent);
            });

            holder.layoutShowtimes.addView(timeTv);
        }

        // Bấm xem tất cả lịch chiếu rạp này -> Mở SelectCinemaActivity
        holder.btnViewSchedule.setOnClickListener(v -> {
            Intent intent = new Intent(context, SelectCinemaActivity.class);
            intent.putExtra("EXTRA_CINEMA_NAME", item.getName());
            context.startActivity(intent);
        });

        holder.cardContainer.setOnClickListener(v -> {
            Intent intent = new Intent(context, SelectCinemaActivity.class);
            intent.putExtra("EXTRA_CINEMA_NAME", item.getName());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return cinemaList != null ? cinemaList.size() : 0;
    }

    private int dpToPx(float dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }

    public static class CinemaViewHolder extends RecyclerView.ViewHolder {
        LinearLayout cardContainer, layoutShowtimes;
        TextView tvBadgeNear, tvName, tvAddress, tvDistance;
        Button btnViewSchedule;

        public CinemaViewHolder(@NonNull View itemView) {
            super(itemView);
            cardContainer = itemView.findViewById(R.id.cardContainer);
            layoutShowtimes = itemView.findViewById(R.id.layoutShowtimes);
            tvBadgeNear = itemView.findViewById(R.id.tvBadgeNear);
            tvName = itemView.findViewById(R.id.tvCinemaName);
            tvAddress = itemView.findViewById(R.id.tvCinemaAddress);
            tvDistance = itemView.findViewById(R.id.tvDistance);
            btnViewSchedule = itemView.findViewById(R.id.btnViewSchedule);
        }
    }
}
