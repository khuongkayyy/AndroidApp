package com.example.cinema;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MovieList extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    MovieListAdapter movieListAdapter;
    ArrayList<Film> filmArrayList;
    ImageView hotFilm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_movie_show);

        recyclerView = findViewById(R.id.movieList);
        hotFilm = findViewById(R.id.hotFilm);
        hotFilm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MovieList.this,MovieDetail.class);
                intent.putExtra("filmName","Lật Mặt 6");
                startActivity(intent);
            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference("film");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        filmArrayList = new ArrayList<>();
        movieListAdapter = new MovieListAdapter(this,filmArrayList);
        recyclerView.setAdapter(movieListAdapter);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Film film = dataSnapshot.getValue(Film.class);
                    filmArrayList.add(film);
                }
                movieListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}