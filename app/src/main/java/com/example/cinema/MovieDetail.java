package com.example.cinema;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MovieDetail extends AppCompatActivity {
    private Button home;
    private Button bookTicket;
    private TextView filmName,filmDate,filmType,filmTime,filmDirector,filmComment,filmBrief;
    private ImageView movieImage;
    private ImageView moviePic1,moviePic2,moviePic3,moviePic4,moviePic5;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);
        initVariable();
        updateData();
        homeRedirect();
    }
    private void homeRedirect() {
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MovieDetail.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initVariable() {
        filmName = findViewById(R.id.txtName_one);
        filmDate = findViewById(R.id.txtDate_one);
        filmTime = findViewById(R.id.txtTime_one);
        filmType = findViewById(R.id.txtType_one);
        filmDirector = findViewById(R.id.txtDirector);
        filmComment = findViewById(R.id.txtReview);
        filmBrief = findViewById(R.id.txtFilmBrief);
        home = findViewById(R.id.btnDetail_Home);
        movieImage = findViewById(R.id.movieImg_one);
        moviePic1 = findViewById(R.id.picNum1);
        moviePic2 = findViewById(R.id.picNum2);
        moviePic3 = findViewById(R.id.picNum3);
        moviePic4 = findViewById(R.id.picNum4);
        moviePic5 = findViewById(R.id.picNum5);
    }
    private void updateData() {
        Intent intent = getIntent();
        String name = intent.getStringExtra("filmName");
        filmName.setText(name);
        databaseReference = FirebaseDatabase.getInstance().getReference("film");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Film film = dataSnapshot.getValue(Film.class);
                    if (film.getName().equals(filmName.getText().toString())){
                        filmDate.setText("Ngày khởi chiếu: "+film.getDate());
                        filmTime.setText("Thời lượng: "+film.getTime());
                        filmType.setText("Thể loại: "+film.getType());
                        filmDirector.setText(film.getDirector());
                        filmComment.setText(film.getComment());
                        filmBrief.setText(film.getDescription());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        setMovieImage(filmName.getText().toString());
    }

    private void setMovieImage(String filmName) {
        switch (filmName){
            case "Lật Mặt 6":
                movieImage.setImageResource(R.drawable.latmat1);
                moviePic1.setImageResource(R.drawable.latmat1);
                moviePic2.setImageResource(R.drawable.latmat2);
                moviePic3.setImageResource(R.drawable.latmat3);
                moviePic4.setImageResource(R.drawable.latmat4);
                moviePic5.setImageResource(R.drawable.latmat5);
                break;
            case "Con Nhót Mót Chồng":
                movieImage.setImageResource(R.drawable.cnmc1);
                moviePic1.setImageResource(R.drawable.cnmc2);
                moviePic2.setImageResource(R.drawable.cnmc3);
                moviePic3.setImageResource(R.drawable.cnmc4);
                moviePic4.setImageResource(R.drawable.cnmc5);
                moviePic5.setImageResource(R.drawable.cnmc6);
                break;
            case "Trạm Tàu Ma":
                movieImage.setImageResource(R.drawable.ma1);
                moviePic1.setImageResource(R.drawable.ma2);
                moviePic2.setImageResource(R.drawable.ma3);
                moviePic3.setImageResource(R.drawable.ma4);
                moviePic4.setImageResource(R.drawable.ma5);
                moviePic5.setImageResource(R.drawable.ma6);
                break;
            case "Vệ Binh Dãy Ngân Hà":
                movieImage.setImageResource(R.drawable.vebinh1);
                moviePic1.setImageResource(R.drawable.vebinh2);
                moviePic2.setImageResource(R.drawable.vebinh3);
                moviePic3.setImageResource(R.drawable.vebinh4);
                moviePic4.setImageResource(R.drawable.vebinh5);
                moviePic5.setImageResource(R.drawable.vebinh6);
                break;
            case "Mèo Siêu Quậy":
                movieImage.setImageResource(R.drawable.meo1);
                moviePic1.setImageResource(R.drawable.meo2);
                moviePic2.setImageResource(R.drawable.meo3);
                moviePic3.setImageResource(R.drawable.meo4);
                moviePic4.setImageResource(R.drawable.meo5);
                moviePic5.setImageResource(R.drawable.meo6);
                break;
            case "Ma Lai Rút Ruột":
                movieImage.setImageResource(R.drawable.malai1);
                moviePic1.setImageResource(R.drawable.malai2);
                moviePic2.setImageResource(R.drawable.malai3);
                moviePic3.setImageResource(R.drawable.malai4);
                moviePic4.setImageResource(R.drawable.malai5);
                moviePic5.setImageResource(R.drawable.malai6);
                break;
            case "Anh Em Mario":
                movieImage.setImageResource(R.drawable.mario1);
                moviePic1.setImageResource(R.drawable.mario2);
                moviePic2.setImageResource(R.drawable.mario3);
                moviePic3.setImageResource(R.drawable.mario4);
                moviePic4.setImageResource(R.drawable.mario5);
                moviePic5.setImageResource(R.drawable.mario6);
                break;
            default:
                return;
        }
    }
}