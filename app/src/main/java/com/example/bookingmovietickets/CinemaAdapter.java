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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CinemaAdapter extends RecyclerView.Adapter<CinemaAdapter.CinemaViewHolder> {

    private final Context context;
    private final List<Cinema> cinemaList;
    private int selectedPosition = -1; // -1 nghĩa là chưa rạp nào được chọn
    private String selectedShowtimeText = "";

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

        // Hiển thị huy hiệu Gần bạn nếu có
        holder.tvBadgeNear.setVisibility(item.isNear() ? View.VISIBLE : View.GONE);

        // Nút xem tất cả lịch chiếu rạp này: Nền đỏ, chữ trắng
        holder.btnViewSchedule.setBackgroundResource(R.drawable.bg_button_primary);
        holder.btnViewSchedule.setTextColor(Color.WHITE);

        // Viền khung rạp: Chỉ rạp nào đang được click (selectedPosition == position) mới có VIỀN ĐỎ, còn lại VIỀN XÁM
        GradientDrawable borderDrawable = new GradientDrawable();
        borderDrawable.setColor(Color.WHITE);
        borderDrawable.setCornerRadius(dpToPx(16));
        if (selectedPosition == position) {
            borderDrawable.setStroke(dpToPx(1.5f), Color.parseColor("#D32F2F"));
        } else {
            borderDrawable.setStroke(dpToPx(1), Color.parseColor("#E0E0E0"));
        }
        holder.cardContainer.setBackground(borderDrawable);

        // Tạo danh sách giờ chiếu động
        holder.layoutShowtimes.removeAllViews();
        List<String> showtimes = item.getShowtimes();
        for (int index = 0; index < showtimes.size(); index++) {
            final String timeText = showtimes.get(index);
            TextView timeTv = new TextView(context);
            timeTv.setText(timeText);
            timeTv.setTextSize(12);
            timeTv.setGravity(Gravity.CENTER);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    dpToPx(60), dpToPx(32));
            params.setMargins(0, 0, dpToPx(8), 0);
            timeTv.setLayoutParams(params);

            boolean isSelectedPill = timeText.equals(item.getSelectedShowtime());

            if (isSelectedPill) {
                GradientDrawable activePill = new GradientDrawable();
                activePill.setColor(Color.WHITE);
                activePill.setCornerRadius(dpToPx(6));
                activePill.setStroke(dpToPx(1.5f), Color.parseColor("#D32F2F"));
                timeTv.setBackground(activePill);
                timeTv.setTextColor(Color.parseColor("#D32F2F"));
                timeTv.setTypeface(null, android.graphics.Typeface.BOLD);
            } else {
                GradientDrawable inactivePill = new GradientDrawable();
                inactivePill.setColor(Color.WHITE);
                inactivePill.setCornerRadius(dpToPx(6));
                inactivePill.setStroke(dpToPx(1), Color.parseColor("#E0E0E0"));
                timeTv.setBackground(inactivePill);
                timeTv.setTextColor(Color.parseColor("#333333"));
                timeTv.setTypeface(null, android.graphics.Typeface.NORMAL);
            }

            timeTv.setOnClickListener(v -> {
                selectedPosition = holder.getAdapterPosition();
                item.setSelectedShowtime(timeText);
                selectedShowtimeText = timeText;
                notifyDataSetChanged();

                Intent intent = new Intent(context, SelectCinemaActivity.class);
                intent.putExtra("EXTRA_CINEMA_NAME", item.getName());
                intent.putExtra("EXTRA_SHOWTIME", timeText);
                context.startActivity(intent);
            });

            holder.layoutShowtimes.addView(timeTv);
        }

        // Bấm vào card hoặc nút xem lịch chiếu -> Đổi khung rạp sang viền đỏ và mở SelectCinemaActivity
        View.OnClickListener selectAndOpenAction = v -> {
            selectedPosition = holder.getAdapterPosition();
            notifyDataSetChanged();

            Intent intent = new Intent(context, SelectCinemaActivity.class);
            intent.putExtra("EXTRA_CINEMA_NAME", item.getName());
            if (!selectedShowtimeText.isEmpty()) {
                intent.putExtra("EXTRA_SHOWTIME", selectedShowtimeText);
                selectedShowtimeText = ""; // reset sau khi truyền
            }
            context.startActivity(intent);
        };

        holder.btnViewSchedule.setOnClickListener(selectAndOpenAction);
        holder.cardContainer.setOnClickListener(selectAndOpenAction);
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
