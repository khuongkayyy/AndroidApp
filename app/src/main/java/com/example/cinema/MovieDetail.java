package com.example.cinema;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

public class MovieDetail extends AppCompatActivity {
    private Button home;
    private Button bookTicket;
    private TextView filmName,filmDate,filmType,filmTime,filmDirector,filmComment,filmBrief;
    private ImageView movieImage;
    private ImageView moviePic1,moviePic2,moviePic3,moviePic4,moviePic5;
    private DatabaseReference databaseReference;
    RecyclerView recyclerView;
    ArrayList<Comment> commentArrayList;
    CommentListAdapter commentListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);
        initVariable();
        updateData();
        homeRedirect();
        bookTicket();
        showCommentList();
    }

    private void showCommentList() {
        databaseReference = FirebaseDatabase.getInstance().getReference("comments");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Comment comment = dataSnapshot.getValue(Comment.class);
                    if (comment.getFilm().equals(filmName.getText())){
                        commentArrayList.add(comment);
                    }
                }
                commentListAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void bookTicket() {
        bookTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MovieDetail.this,MovieBook.class);
                intent.putExtra("filmName",filmName.getText().toString());
                startActivity(intent);
            }
        });
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
        //textview
        filmName = findViewById(R.id.txtName_one);
        filmDate = findViewById(R.id.txtDate_one);
        filmTime = findViewById(R.id.txtTime_one);
        filmType = findViewById(R.id.txtType_one);
        filmDirector = findViewById(R.id.txtDirector);
        filmComment = findViewById(R.id.txtReview);
        filmBrief = findViewById(R.id.txtFilmBrief);

        //button
        home = findViewById(R.id.btnDetail_Home);
        bookTicket = findViewById(R.id.btnBook);

        //image
        movieImage = findViewById(R.id.movieImg_one);
        moviePic1 = findViewById(R.id.picNum1);
        moviePic2 = findViewById(R.id.picNum2);
        moviePic3 = findViewById(R.id.picNum3);
        moviePic4 = findViewById(R.id.picNum4);
        moviePic5 = findViewById(R.id.picNum5);

        //recycler view
        recyclerView = findViewById(R.id.commentList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //arraylist
        commentArrayList = new ArrayList<>();
        //adapter
        commentListAdapter = new CommentListAdapter(this,commentArrayList);
        recyclerView.setAdapter(commentListAdapter);
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
                        if (film.getStatus().equalsIgnoreCase("new")){
                            bookTicket.setEnabled(false);
                            bookTicket.setText("Phim sắp lên lịch");
                        }
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
    //update movie image
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
            case "Khắc Tinh Của Quỷ":
                movieImage.setImageResource(R.drawable.quy1);
                moviePic1.setImageResource(R.drawable.quy2);
                moviePic2.setImageResource(R.drawable.quy3);
                moviePic3.setImageResource(R.drawable.quy4);
                moviePic4.setImageResource(R.drawable.quy5);
                moviePic5.setImageResource(R.drawable.quy6);
                break;
            case "Tình Người Duyên Ma 2":
                movieImage.setImageResource(R.drawable.duyenma1);
                moviePic1.setImageResource(R.drawable.duyenma2);
                moviePic2.setImageResource(R.drawable.duyenma3);
                moviePic3.setImageResource(R.drawable.duyenma4);
                moviePic4.setImageResource(R.drawable.duyenma5);
                moviePic5.setImageResource(R.drawable.duyenma6);
                break;
            case "Đầu Gấu Đụng Đầu Đất":
                movieImage.setImageResource(R.drawable.daugau1);
                moviePic1.setImageResource(R.drawable.daugau2);
                moviePic2.setImageResource(R.drawable.daugau3);
                moviePic3.setImageResource(R.drawable.daugau4);
                moviePic4.setImageResource(R.drawable.daugau5);
                moviePic5.setImageResource(R.drawable.daugau6);
                break;
            case "Yêu Như Lần Đầu":
                movieImage.setImageResource(R.drawable.yeu1);
                moviePic1.setImageResource(R.drawable.yeu2);
                moviePic2.setImageResource(R.drawable.yeu3);
                moviePic3.setImageResource(R.drawable.yeu4);
                moviePic4.setImageResource(R.drawable.yeu5);
                moviePic5.setImageResource(R.drawable.yeu2);
                break;
            case "Sisu - Già Gân Báo Thù":
                movieImage.setImageResource(R.drawable.sisu6);
                moviePic1.setImageResource(R.drawable.sisu2);
                moviePic2.setImageResource(R.drawable.sisu1);
                moviePic3.setImageResource(R.drawable.sisu3);
                moviePic4.setImageResource(R.drawable.sisu2);
                moviePic5.setImageResource(R.drawable.sisu4);
                break;
            case "Fast And Furious X":
                movieImage.setImageResource(R.drawable.fast6);
                moviePic1.setImageResource(R.drawable.fast1);
                moviePic2.setImageResource(R.drawable.fast2);
                moviePic3.setImageResource(R.drawable.fast3);
                moviePic4.setImageResource(R.drawable.fast4);
                moviePic5.setImageResource(R.drawable.fast2);
                break;
            case "Lời Nguyền Hoa Máu":
                movieImage.setImageResource(R.drawable.hoamau1);
                moviePic1.setImageResource(R.drawable.hoamau1);
                moviePic2.setImageResource(R.drawable.hoamau1);
                moviePic3.setImageResource(R.drawable.hoamau1);
                moviePic4.setImageResource(R.drawable.hoamau1);
                moviePic5.setImageResource(R.drawable.hoamau1);
                break;
            default:
                return;
        }
    }
}