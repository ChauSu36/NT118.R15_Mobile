package com.example.bookingmovietickets;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    /** Interface để HomeActivity biết khi nào người dùng bấm "Chi tiết phim". */
    public interface OnMovieClickListener {
        void onMovieClick(Movie movie);
    }

    private List<Movie> movieList;
    private List<Movie> originalList;
    private final OnMovieClickListener listener;

    public MovieAdapter(List<Movie> movieList, OnMovieClickListener listener) {
        this.movieList = movieList;
        this.originalList = new ArrayList<>(movieList);
        this.listener = listener;
    }

    /** Cập nhật dữ liệu mới từ API khi chuyển tab hoặc tải lại */
    public void updateData(List<Movie> newList) {
        this.movieList = newList;
        this.originalList = new ArrayList<>(newList);
        notifyDataSetChanged();
    }

    /** Lọc danh sách phim theo từ khóa tên hoặc thể loại */
    public void filterList(String query) {
        List<Movie> filteredList = new ArrayList<>();

        if (query == null || query.trim().isEmpty()) {
            filteredList.addAll(originalList);
        } else {
            String pattern = query.toLowerCase().trim();
            for (Movie movie : originalList) {
                boolean matchTitle = movie.getTitle() != null && movie.getTitle().toLowerCase().contains(pattern);
                boolean matchGenre = movie.getGenre() != null && movie.getGenre().toLowerCase().contains(pattern);

                if (matchTitle || matchGenre) {
                    filteredList.add(movie);
                }
            }
        }

        this.movieList = filteredList;
        notifyDataSetChanged();
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

        if (movie.getAgeRating() != null && !movie.getAgeRating().isEmpty()) {
            holder.tvAge.setText(movie.getAgeRating());
            holder.tvAge.setVisibility(View.VISIBLE);
            if ("C16".equals(movie.getAgeRating()) || "T16".equals(movie.getAgeRating()) || "T18".equals(movie.getAgeRating())) {
                holder.tvAge.setBackgroundResource(R.drawable.bg_badge_age_c16);
            } else {
                holder.tvAge.setBackgroundResource(R.drawable.bg_badge_age_p);
            }
        } else {
            holder.tvAge.setVisibility(View.GONE);
        }

        // Tải ảnh poster từ Database MySQL qua Glide
        if (movie.getPosterUrl() != null && !movie.getPosterUrl().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(movie.getPosterUrl())
                    .placeholder(R.color.color_poster_placeholder)
                    .error(R.color.color_poster_placeholder)
                    .centerCrop()
                    .into(holder.imgPoster);
        } else {
            holder.imgPoster.setImageResource(R.color.color_poster_placeholder);
        }

        holder.btnDetail.setOnClickListener(v -> {
            if (listener != null) {
                listener.onMovieClick(movie);
            }
        });

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onMovieClick(movie);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieList != null ? movieList.size() : 0;
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPoster;
        TextView tvTitle, tvSubtitle, tvAge, tvRating, btnDetail;

        MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPoster = itemView.findViewById(R.id.img_poster);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvSubtitle = itemView.findViewById(R.id.tv_subtitle);
            tvAge = itemView.findViewById(R.id.tv_age);
            tvRating = itemView.findViewById(R.id.tv_rating);
            btnDetail = itemView.findViewById(R.id.btn_detail);
        }
    }
}
