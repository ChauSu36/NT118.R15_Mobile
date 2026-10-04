package com.example.bookingmovietickets;
// LƯU Ý: đổi dòng package ở trên cho khớp với package thật trong project của bạn.

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    /** Interface để HomeActivity biết khi nào người dùng bấm "Chi tiết phim". */
    public interface OnMovieClickListener {
        void onMovieClick(Movie movie);
    }

    private final List<Movie> movieList;
    private final OnMovieClickListener listener;

    public MovieAdapter(List<Movie> movieList, OnMovieClickListener listener) {
        this.movieList = movieList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movieList.get(position);

        holder.tvTitle.setText(movie.getTitle());
        holder.tvSubtitle.setText(movie.getGenreAndDuration());
        holder.tvRating.setText("★ " + movie.getRating());
        holder.tvAge.setText(movie.getAgeRating());

        // Đổi màu badge độ tuổi theo giá trị: C16 -> đỏ, P -> xanh lá
        if ("C16".equals(movie.getAgeRating())) {
            holder.tvAge.setBackgroundResource(R.drawable.bg_badge_age_c16);
        } else {
            holder.tvAge.setBackgroundResource(R.drawable.bg_badge_age_p);
        }

        holder.btnDetail.setOnClickListener(v -> {
            if (listener != null) {
                listener.onMovieClick(movie);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvSubtitle, tvAge, tvRating, btnDetail;

        MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvSubtitle = itemView.findViewById(R.id.tv_subtitle);
            tvAge = itemView.findViewById(R.id.tv_age);
            tvRating = itemView.findViewById(R.id.tv_rating);
            btnDetail = itemView.findViewById(R.id.btn_detail);
        }
    }
}
