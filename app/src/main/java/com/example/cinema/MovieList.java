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
    Button currentMovie, newMovie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_movie_show);
        initVariable();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Film film = dataSnapshot.getValue(Film.class);
                    if (film.getStatus() != null){
                        if (!film.getStatus().equalsIgnoreCase("new"))
                        {
                            filmArrayList.add(film);
                        }
                    }
                }
                movieListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        currentMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCurrentMovie();
            }
        });
        newMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNewMovie();
            }
        });
    }

    private void initVariable() {
        recyclerView = findViewById(R.id.movieList);
        databaseReference = FirebaseDatabase.getInstance().getReference("film");
        currentMovie = findViewById(R.id.btnCurrentMovie);
        newMovie = findViewById(R.id.btnNewMovie);
        filmArrayList = new ArrayList<>();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        filmArrayList.clear();
        movieListAdapter = new MovieListAdapter(this,filmArrayList);
        recyclerView.setAdapter(movieListAdapter);
    }

    private void showNewMovie() {
        filmArrayList.clear();
        recyclerView.setAdapter(movieListAdapter);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Film film = dataSnapshot.getValue(Film.class);
                    if (film.getStatus() != null){
                        if (film.getStatus().equalsIgnoreCase("new"))
                        {
                            filmArrayList.add(film);
                        }
                    }
                }
                movieListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void showCurrentMovie() {
        filmArrayList.clear();
        recyclerView.setAdapter(movieListAdapter);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Film film = dataSnapshot.getValue(Film.class);
                    if (film.getStatus() != null){
                        if (!film.getStatus().equalsIgnoreCase("new"))
                        {
                            filmArrayList.add(film);
                        }
                    }
                }
                movieListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}