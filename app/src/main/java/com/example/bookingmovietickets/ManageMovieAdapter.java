package com.example.bookingmovietickets;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ManageMovieAdapter extends RecyclerView.Adapter<ManageMovieAdapter.ManageViewHolder> {

    private final Context context;
    private final List<ManageMovie> movieMinis;

    public ManageMovieAdapter(Context context, List<ManageMovie> movieMinis) {
        this.context = context;
        this.movieMinis = movieMinis;
    }

    @NonNull
    @Override
    public ManageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_manage_movie, parent, false);
        return new ManageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ManageViewHolder holder, int position) {
        ManageMovie item = movieMinis.get(position);

        holder.tvPosterTitle.setText(item.getTitle().toUpperCase());
        holder.tvTitle.setText(item.getTitle());
        holder.tvAgeDuration.setText("Độ tuổi: " + item.getAgeRating() + " • Thời lượng: " + item.getDuration());
        holder.tvGenre.setText("Thể loại: " + item.getGenre());
        holder.tvRelease.setText("Khởi chiếu: " + item.getReleaseDate());

        // Cập nhật giao diện trạng thái TẮT / BẬT
        boolean isDisabled = "TẮT".equalsIgnoreCase(item.getStatus());
        if (isDisabled) {
            holder.tvStatus.setText("ĐÃ TẮT");
            holder.tvStatus.setBackgroundResource(R.drawable.bg_button_outline);
            holder.tvStatus.setTextColor(Color.GRAY);
            holder.btnToggleVisibility.setText("BẬT");
            holder.btnToggleVisibility.setBackgroundResource(R.drawable.bg_button_outline);
            holder.btnToggleVisibility.setTextColor(Color.parseColor("#2E7D32"));
        } else {
            holder.tvStatus.setText(item.getStatus());
            holder.tvStatus.setBackgroundResource(R.drawable.bg_button_light_red);
            holder.tvStatus.setTextColor(Color.parseColor("#D32F2F"));
            holder.btnToggleVisibility.setText("TẮT");
            holder.btnToggleVisibility.setBackgroundResource(R.drawable.bg_button_light_red);
            holder.btnToggleVisibility.setTextColor(Color.parseColor("#D32F2F"));
        }

        // Cập nhật ngày suất chiếu: dayOffset (0 = Hôm nay 30/09, 1 = Ngày tiếp theo 01/10, -1 = Ngày trước đó 29/09)
        int dayOffset = item.getDayOffset();

        if (dayOffset == 1) {
            // Ngày tiếp theo (01/10) -> Nút > NỀN ĐỎ, Nút < NỀN BÌNH THƯỜNG
            holder.btnPrevDay.setBackgroundResource(R.drawable.bg_button_outline);
            holder.btnPrevDay.setTextColor(Color.parseColor("#333333"));
            holder.btnNextDay.setBackgroundResource(R.drawable.bg_button_primary);
            holder.btnNextDay.setTextColor(Color.WHITE);

            holder.tvShowtimeHeader.setText("SUẤT CHIẾU NGÀY TIẾP THEO (01/10) - " + item.getShowtimeHall() + ":");
            holder.tvSlot1Time.setText("10:15");
            holder.tvSlot1Status.setText("Còn 42/50 ghế");
            holder.tvSlot1Status.setTextColor(Color.parseColor("#2E7D32"));
            holder.slot1Bg.setBackgroundResource(R.drawable.bg_info_box_border);

            holder.tvSlot2Time.setText("14:30");
            holder.tvSlot2Status.setText("Còn 20/50 ghế");
            holder.tvSlot2Status.setTextColor(Color.parseColor("#2E7D32"));
            holder.slot2Bg.setBackgroundResource(R.drawable.bg_info_box_border);

            holder.tvSlot3Time.setText("18:00");
            holder.tvSlot3Status.setText("ĐÃ KÍN CHỖ");
            holder.tvSlot3Status.setTextColor(Color.parseColor("#D32F2F"));
            holder.slot3Bg.setBackgroundResource(R.drawable.bg_button_light_red);

        } else if (dayOffset == -1) {
            // Ngày trước đó (29/09) -> Nút < NỀN ĐỎ, Nút > NỀN BÌNH THƯỜNG
            holder.btnPrevDay.setBackgroundResource(R.drawable.bg_button_primary);
            holder.btnPrevDay.setTextColor(Color.WHITE);
            holder.btnNextDay.setBackgroundResource(R.drawable.bg_button_outline);
            holder.btnNextDay.setTextColor(Color.parseColor("#333333"));

            holder.tvShowtimeHeader.setText("SUẤT CHIẾU NGÀY TRƯỚC ĐÓ (29/09) - " + item.getShowtimeHall() + ":");
            holder.tvSlot1Time.setText("09:00");
            holder.tvSlot1Status.setText("Còn 15/50 ghế");
            holder.tvSlot1Status.setTextColor(Color.parseColor("#2E7D32"));
            holder.slot1Bg.setBackgroundResource(R.drawable.bg_info_box_border);

            holder.tvSlot2Time.setText("13:15");
            holder.tvSlot2Status.setText("ĐÃ KÍN CHỖ");
            holder.tvSlot2Status.setTextColor(Color.parseColor("#D32F2F"));
            holder.slot2Bg.setBackgroundResource(R.drawable.bg_button_light_red);

            holder.tvSlot3Time.setText("17:45");
            holder.tvSlot3Status.setText("Còn 30/50 ghế");
            holder.tvSlot3Status.setTextColor(Color.parseColor("#2E7D32"));
            holder.slot3Bg.setBackgroundResource(R.drawable.bg_info_box_border);

        } else {
            // Ngày hiện tại (Hôm nay 30/09) -> CẢ 2 NÚT < > NỀN BÌNH THƯỜNG (KHÔNG ĐỎ)
            holder.btnPrevDay.setBackgroundResource(R.drawable.bg_button_outline);
            holder.btnPrevDay.setTextColor(Color.parseColor("#333333"));
            holder.btnNextDay.setBackgroundResource(R.drawable.bg_button_outline);
            holder.btnNextDay.setTextColor(Color.parseColor("#333333"));

            holder.tvShowtimeHeader.setText("SUẤT CHIẾU HÔM NAY (" + item.getShowtimeDate() + ") - " + item.getShowtimeHall() + ":");
            holder.tvSlot1Time.setText("18:00");
            holder.tvSlot1Status.setText("Còn 35/50 ghế");
            holder.tvSlot1Status.setTextColor(Color.parseColor("#2E7D32"));
            holder.slot1Bg.setBackgroundResource(R.drawable.bg_info_box_border);

            holder.tvSlot2Time.setText("20:00");
            holder.tvSlot2Status.setText("ĐÃ KÍN CHỖ");
            holder.tvSlot2Status.setTextColor(Color.parseColor("#D32F2F"));
            holder.slot2Bg.setBackgroundResource(R.drawable.bg_button_light_red);

            holder.tvSlot3Time.setText("22:15");
            holder.tvSlot3Status.setText("Còn 48/50 ghế");
            holder.tvSlot3Status.setTextColor(Color.parseColor("#2E7D32"));
            holder.slot3Bg.setBackgroundResource(R.drawable.bg_info_box_border);
        }

        // 1. Nút "ĐIỀU CHỈNH SUẤT" (Nền đen)
        holder.btnAdjustShowtimes.setOnClickListener(v -> new AlertDialog.Builder(context)
                .setTitle("Điều chỉnh suất chiếu")
                .setMessage("Quản lý lịch chiếu phim \"" + item.getTitle() + "\":\n- Thay đổi khung giờ chiếu\n- Đổi phòng / rạp chiếu\n- Cập nhật lại giá vé từng suất\n- Xóa suất chiếu đã tạo")
                .setPositiveButton("Đồng ý", null)
                .show());

        // 2. Nút "SỬA PHIM" (Nền trắng viền xám)
        holder.btnEditMovie.setOnClickListener(v -> new AlertDialog.Builder(context)
                .setTitle("Sửa thông tin phim")
                .setMessage("Chỉnh sửa mô tả phim \"" + item.getTitle() + "\":\n- Cập nhật Tên phim, Poster\n- Quy định Độ tuổi, Thể loại, Thời lượng\n- Ngày khởi chiếu, Tóm tắt nội dung & Link Trailer")
                .setPositiveButton("Lưu thay đổi", (dialog, which) ->
                        Toast.makeText(context, "Đã cập nhật thông tin phim!", Toast.LENGTH_SHORT).show())
                .setNegativeButton("Hủy", null)
                .show());

        // 3. Nút "TẮT / BẬT" (Nền đỏ nhạt, chữ đỏ)
        holder.btnToggleVisibility.setOnClickListener(v -> {
            if (isDisabled) {
                item.setStatus("ĐANG CHIẾU");
                Toast.makeText(context, "Đã BẬT hiển thị phim \"" + item.getTitle() + "\" trên ứng dụng", Toast.LENGTH_SHORT).show();
            } else {
                item.setStatus("TẮT");
                Toast.makeText(context, "Đã TẮT (Ẩn) phim \"" + item.getTitle() + "\" khỏi ứng dụng", Toast.LENGTH_SHORT).show();
            }
            notifyItemChanged(position);
        });

        // 4. Nút Mũi tên < (Ngày trước đó)
        holder.btnPrevDay.setOnClickListener(v -> {
            if (dayOffset > -1) {
                int newOffset = dayOffset - 1;
                item.setDayOffset(newOffset);
                if (newOffset == -1) {
                    Toast.makeText(context, "Chuyển sang suất chiếu ngày trước đó (29/09)", Toast.LENGTH_SHORT).show();
                } else if (newOffset == 0) {
                    Toast.makeText(context, "Quay về suất chiếu hôm nay (30/09)", Toast.LENGTH_SHORT).show();
                }
                notifyItemChanged(position);
            }
        });

        // 5. Nút Mũi tên > (Ngày tiếp theo)
        holder.btnNextDay.setOnClickListener(v -> {
            if (dayOffset < 1) {
                int newOffset = dayOffset + 1;
                item.setDayOffset(newOffset);
                if (newOffset == 1) {
                    Toast.makeText(context, "Chuyển sang suất chiếu ngày tiếp theo (01/10)", Toast.LENGTH_SHORT).show();
                } else if (newOffset == 0) {
                    Toast.makeText(context, "Quay về suất chiếu hôm nay (30/09)", Toast.LENGTH_SHORT).show();
                }
                notifyItemChanged(position);
            }
        });

        // 6. Nút "+ SUẤT MỚI"
        holder.btnAddShowtime.setOnClickListener(v ->
                Toast.makeText(context, "Thêm suất chiếu mới cho " + item.getTitle(), Toast.LENGTH_SHORT).show());
    }

    @Override
    public int getItemCount() {
        return movieMinis != null ? movieMinis.size() : 0;
    }

    public static class ManageViewHolder extends RecyclerView.ViewHolder {
        TextView tvPosterTitle, tvStatus, tvTitle, tvAgeDuration, tvGenre, tvRelease, tvShowtimeHeader;
        LinearLayout slot1Bg, slot2Bg, slot3Bg;
        TextView tvSlot1Time, tvSlot1Status, tvSlot2Time, tvSlot2Status, tvSlot3Time, tvSlot3Status;
        TextView btnAddShowtime, btnAdjustShowtimes, btnEditMovie, btnToggleVisibility, btnPrevDay, btnNextDay;

        public ManageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPosterTitle = itemView.findViewById(R.id.tv_poster_title);
            tvStatus = itemView.findViewById(R.id.tv_manage_status);
            tvTitle = itemView.findViewById(R.id.tv_manage_title);
            tvAgeDuration = itemView.findViewById(R.id.tv_manage_age_duration);
            tvGenre = itemView.findViewById(R.id.tv_manage_genre);
            tvRelease = itemView.findViewById(R.id.tv_manage_release);
            tvShowtimeHeader = itemView.findViewById(R.id.tv_showtime_header);

            slot1Bg = itemView.findViewById(R.id.slot1_bg);
            slot2Bg = itemView.findViewById(R.id.slot2_bg);
            slot3Bg = itemView.findViewById(R.id.slot3_bg);

            tvSlot1Time = itemView.findViewById(R.id.tv_slot1_time);
            tvSlot1Status = itemView.findViewById(R.id.tv_slot1_status);
            tvSlot2Time = itemView.findViewById(R.id.tv_slot2_time);
            tvSlot2Status = itemView.findViewById(R.id.tv_slot2_status);
            tvSlot3Time = itemView.findViewById(R.id.tv_slot3_time);
            tvSlot3Status = itemView.findViewById(R.id.tv_slot3_status);

            btnAddShowtime = itemView.findViewById(R.id.btn_add_showtime);
            btnAdjustShowtimes = itemView.findViewById(R.id.btn_adjust_showtimes);
            btnEditMovie = itemView.findViewById(R.id.btn_edit_movie);
            btnToggleVisibility = itemView.findViewById(R.id.btn_toggle_visibility);
            btnPrevDay = itemView.findViewById(R.id.btn_prev_day);
            btnNextDay = itemView.findViewById(R.id.btn_next_day);
        }
    }
}
