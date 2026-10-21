package com.example.bookingmovietickets;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TicketHistoryAdapter extends RecyclerView.Adapter<TicketHistoryAdapter.TicketViewHolder> {

    private final Context context;
    private final List<TicketHistory> ticketList;

    public TicketHistoryAdapter(Context context, List<TicketHistory> ticketList) {
        this.context = context;
        this.ticketList = ticketList;
    }

    @NonNull
    @Override
    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ticket_history, parent, false);
        return new TicketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketViewHolder holder, int position) {
        TicketHistory item = ticketList.get(position);

        holder.tvOrderCode.setText("Mã đơn: " + item.getOrderCode());
        holder.tvTicketStatus.setText(item.getStatus());
        holder.tvSubType.setText(item.getSubType());
        holder.tvMovieTitle.setText(item.getMovieTitle());
        holder.tvCinemaName.setText("Rạp: " + item.getCinemaName());
        holder.tvShowtime.setText("Suất: " + item.getShowtime());
        holder.tvHallName.setText(item.getHallName());
        holder.tvSeats.setText(item.getSeats());
        holder.tvTotalPrice.setText(item.getTotalPrice());

        // Đổi màu badge trạng thái
        if ("SẮP XEM".equalsIgnoreCase(item.getStatus())) {
            holder.tvTicketStatus.setBackgroundResource(R.drawable.bg_badge_hot);
            holder.tvTicketStatus.setTextColor(Color.WHITE);
            holder.btnCancelTicket.setVisibility(View.VISIBLE);
        } else if ("ĐÃ XEM".equalsIgnoreCase(item.getStatus())) {
            holder.tvTicketStatus.setBackgroundResource(R.drawable.bg_badge_rating);
            holder.tvTicketStatus.setTextColor(Color.WHITE);
            holder.btnCancelTicket.setVisibility(View.GONE);
        } else {
            // ĐÃ HỦY
            holder.tvTicketStatus.setBackgroundResource(R.drawable.bg_button_outline);
            holder.tvTicketStatus.setTextColor(Color.GRAY);
            holder.btnCancelTicket.setVisibility(View.GONE);
        }

        // Bấm Chi tiết vé & mã QR
        holder.btnDetailQr.setOnClickListener(v ->
                Toast.makeText(context, "Chi tiết vé " + item.getOrderCode() + " & Mã QR", Toast.LENGTH_SHORT).show());

        // Bấm Hủy vé
        holder.btnCancelTicket.setOnClickListener(v -> new AlertDialog.Builder(context)
                .setTitle("Hủy vé xem phim")
                .setMessage("Bạn có chắc muốn hủy vé đơn " + item.getOrderCode() + " không?")
                .setNegativeButton("Không", null)
                .setPositiveButton("Hủy vé", (dialog, which) -> {
                    Toast.makeText(context, "Đã gửi yêu cầu hủy vé " + item.getOrderCode(), Toast.LENGTH_SHORT).show();
                })
                .show());
    }

    @Override
    public int getItemCount() {
        return ticketList != null ? ticketList.size() : 0;
    }

    public static class TicketViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderCode, tvTicketStatus, tvSubType, tvMovieTitle;
        TextView tvCinemaName, tvShowtime, tvHallName, tvSeats, tvTotalPrice;
        TextView btnDetailQr, btnCancelTicket;

        public TicketViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderCode = itemView.findViewById(R.id.tv_order_code);
            tvTicketStatus = itemView.findViewById(R.id.tv_ticket_status);
            tvSubType = itemView.findViewById(R.id.tv_sub_type);
            tvMovieTitle = itemView.findViewById(R.id.tv_movie_title);
            tvCinemaName = itemView.findViewById(R.id.tv_cinema_name);
            tvShowtime = itemView.findViewById(R.id.tv_showtime);
            tvHallName = itemView.findViewById(R.id.tv_hall_name);
            tvSeats = itemView.findViewById(R.id.tv_seats);
            tvTotalPrice = itemView.findViewById(R.id.tv_total_price);
            btnDetailQr = itemView.findViewById(R.id.btn_detail_qr);
            btnCancelTicket = itemView.findViewById(R.id.btn_cancel_ticket);
        }
    }
}
