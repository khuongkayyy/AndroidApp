package com.example.cinema;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

public class WatchedMovieList extends AppCompatActivity {
    private Button home,movieList;
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    WatchedMovieAdapter watchedMovieAdapter;
    ArrayList<Ticket> ticketArrayList,uniqueTickets;
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME ="userpref";
    private static final String KEY_ID = "userID";
    private static final String KEY_NAME = "userName";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watched_movie_list);
        initVariable();
        homeRedirect();
        watchedMovie();
        movieListRedirect();
        ticketArrayList.clear();
        uniqueTickets.clear();
    }

    private void movieListRedirect() {
        movieList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WatchedMovieList.this,MovieList.class);
                startActivity(intent);
            }
        });
    }

    private void watchedMovie() {
        ticketArrayList.clear();
        uniqueTickets.clear();
        String userID = sharedPreferences.getString(KEY_ID,null);
        String userName = sharedPreferences.getString(KEY_NAME,null);
        HashSet<String> uniqueFilms = new HashSet<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Ticket ticket = dataSnapshot.getValue(Ticket.class);
                    if (userID != null){
                        if (ticket.getUser().equals(userID) && !checkTicketDate(ticket.getBookdate())){
                            ticketArrayList.add(ticket);
                        }
                    }
                }
                for (Ticket ticket : ticketArrayList) {
                    if (!uniqueFilms.contains(ticket.getFilm())) {
                        uniqueFilms.add(ticket.getFilm());
                        uniqueTickets.add(ticket);
                    }
                }
                watchedMovieAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private boolean checkTicketDate(String ticketDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        Date date = null;
        try {
            date = dateFormat.parse(ticketDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Compare the date to today
        Calendar today = Calendar.getInstance();
        Calendar movieDate = Calendar.getInstance();
        movieDate.setTime(date);
        if (movieDate.before(today)) {
            // The movie date is in the past
            return  false;
        } else {
            // The movie date is in the future
            return  true;
        }
    }

    private void homeRedirect() {
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WatchedMovieList.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initVariable() {
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        //button
        home = findViewById(R.id.btnAccount_Home);
        movieList = findViewById(R.id.btnWatchedMovie_Movie);
        //recycler view
        recyclerView = findViewById(R.id.watchedMovie);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        ticketArrayList = new ArrayList<>();
        uniqueTickets = new ArrayList<>();
        watchedMovieAdapter = new WatchedMovieAdapter(this,uniqueTickets);
        recyclerView.setAdapter(watchedMovieAdapter);
        //db connection
        databaseReference = FirebaseDatabase.getInstance().getReference("tickets");
    }
}