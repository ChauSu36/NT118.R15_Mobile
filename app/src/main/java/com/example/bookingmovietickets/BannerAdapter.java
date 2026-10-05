package com.example.bookingmovietickets;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerViewHolder> {

    public interface OnBannerClickListener {
        void onBannerClick(Banner banner);
    }

    private final List<Banner> bannerList;
    private final OnBannerClickListener listener;

    public BannerAdapter(List<Banner> bannerList, OnBannerClickListener listener) {
        this.bannerList = bannerList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_banner, parent, false);
        return new BannerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder holder, int position) {
        Banner banner = bannerList.get(position);
        holder.tvBadge.setText(banner.getBadge());
        holder.tvTitle.setText(banner.getTitle());
        holder.tvSubtitle.setText(banner.getSubtitle());

        holder.btnDetail.setPaintFlags(
                holder.btnDetail.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        holder.btnDetail.setOnClickListener(v -> {
            if (listener != null) {
                listener.onBannerClick(banner);
            }
        });

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onBannerClick(banner);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bannerList.size();
    }

    static class BannerViewHolder extends RecyclerView.ViewHolder {
        TextView tvBadge, tvTitle, tvSubtitle, btnDetail;

        BannerViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBadge = itemView.findViewById(R.id.tv_badge);
            tvTitle = itemView.findViewById(R.id.tv_banner_title);
            tvSubtitle = itemView.findViewById(R.id.tv_banner_subtitle);
            btnDetail = itemView.findViewById(R.id.btn_banner_detail);
        }
    }
}
