package com.example.cinema;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieListHolder> {
    Context context;
    ArrayList<Film> filmArrayList;

    public MovieListAdapter(Context context, ArrayList<Film> filmArrayList) {
        this.context = context;
        this.filmArrayList = filmArrayList;
    }

    @NonNull
    @Override
    public MovieListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.one_movie_show,parent,false);
        return new MovieListHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MovieListHolder holder, int position) {
        Film film = filmArrayList.get(position);
        holder.nameTxt.setText(film.getName());
        holder.dateTxt.setText("Ngày công chiếu: "+film.getDate());
        holder.timeTxt.setText("Thời lượng: "+film.getTime());
        holder.typeTxt.setText("Thể loại: "+film.getType());
        holder.movieImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(MovieListAdapter.this,MovieDetail.class);
            }
        });

        //set image:
        String filmName = holder.nameTxt.getText().toString();
        switch (filmName){
            case "Lật Mặt 6":
                holder.movieImage.setImageResource(R.drawable.latmat1);
                break;
            case "Con Nhót Mót Chồng":
                holder.movieImage.setImageResource(R.drawable.cnmc1);
                break;
            case "Anh Em Mario":
                holder.movieImage.setImageResource(R.drawable.mario1);
                break;
            case "Ma Lai Rút Ruột":
                holder.movieImage.setImageResource(R.drawable.malai1);
                break;
            case "Mèo Siêu Quậy":
                holder.movieImage.setImageResource(R.drawable.meo1);
                break;
            case "Trạm Tàu Ma":
                holder.movieImage.setImageResource(R.drawable.ma1);
                break;
            case"Vệ Binh Dãy Ngân Hà":
                holder.movieImage.setImageResource(R.drawable.vebinh1);
                break;
            default:
                return;
        }
    }

    @Override
    public int getItemCount() {
        return filmArrayList.size();
    }
    public class MovieListHolder extends RecyclerView.ViewHolder{
        public ImageView movieImage;
        public TextView dateTxt;
        public TextView timeTxt;
        public TextView nameTxt;
        public TextView typeTxt;
        public MovieListHolder(View movieView) {
            super(movieView);
            movieImage = movieView.findViewById(R.id.movieImg_one);
            dateTxt = movieView.findViewById(R.id.txtDate_one);
            timeTxt = movieView.findViewById(R.id.txtTime_one);
            nameTxt = movieView.findViewById(R.id.txtName_one);
            typeTxt = movieView.findViewById(R.id.txtType_one);
        }
    }
}
