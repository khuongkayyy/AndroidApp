package com.example.cinema;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class WatchedMovieAdapter extends RecyclerView.Adapter<WatchedMovieAdapter.WatchedMovieListHolder> {
    Context context;
    ArrayList<Ticket> ticketArrayList;

    public WatchedMovieAdapter(Context context, ArrayList<Ticket> ticketArrayList) {
        this.context = context;
        this.ticketArrayList = ticketArrayList;
    }

    @NonNull
    @Override
    public WatchedMovieAdapter.WatchedMovieListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.one_watchedmovie_show,parent,false);
        return new WatchedMovieListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WatchedMovieAdapter.WatchedMovieListHolder holder, int position) {
        Ticket ticket = ticketArrayList.get(position);

        //set watched movie information:
        holder.watchedMovieName.setText(ticket.getFilm());
        holder.watchedMovieDate.setText("Ngày xem: "+ ticket.getBookdate());

        //set watched movie image
        String filmName = holder.watchedMovieName.getText().toString();
        switch (filmName){
            case "Lật Mặt 6":
                holder.watchedMovieImage.setImageResource(R.drawable.latmat1);
                break;
            case "Con Nhót Mót Chồng":
                holder.watchedMovieImage.setImageResource(R.drawable.cnmc1);
                break;
            case "Anh Em Mario":
                holder.watchedMovieImage.setImageResource(R.drawable.mario1);
                break;
            case "Ma Lai Rút Ruột":
                holder.watchedMovieImage.setImageResource(R.drawable.malai1);
                break;
            case "Mèo Siêu Quậy":
                holder.watchedMovieImage.setImageResource(R.drawable.meo1);
                break;
            case "Trạm Tàu Ma":
                holder.watchedMovieImage.setImageResource(R.drawable.ma1);
                break;
            case "Vệ Binh Dãy Ngân Hà":
                holder.watchedMovieImage.setImageResource(R.drawable.vebinh1);
                break;
            case "Tình Người Duyên Ma 2":
                holder.watchedMovieImage.setImageResource(R.drawable.duyenma1);
                break;
            case "Đầu Gấu Đụng Đầu Đất":
                holder.watchedMovieImage.setImageResource(R.drawable.daugau1);
                break;
            case "Khắc Tinh Của Quỷ":
                holder.watchedMovieImage.setImageResource(R.drawable.quy1);
                break;
            default:
                return;
        }
    }

    @Override
    public int getItemCount() {
        return ticketArrayList.size();
    }

    public class WatchedMovieListHolder extends RecyclerView.ViewHolder {
        public TextView watchedMovieName,watchedMovieDate;
        public ImageView watchedMovieImage;
        public WatchedMovieListHolder(@NonNull View watchedMovieView) {
            super(watchedMovieView);
            //textview
            watchedMovieName =watchedMovieView.findViewById(R.id.txtWatchedFilmName);
            watchedMovieDate =watchedMovieView.findViewById(R.id.txtWatchedFilmDate);
            //image view
            watchedMovieImage = watchedMovieView.findViewById(R.id.watchedMovieImg);
        }
    }
}
