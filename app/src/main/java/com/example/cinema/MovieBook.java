package com.example.cinema;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MovieBook extends AppCompatActivity {
    private Button home;
    private TextView filmName,cinemaName,filmDate;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookticket);
        initVariable();
        homeRedirect();
        updateData();
    }
    private void initVariable() {
        home = findViewById(R.id.btnBook_Home);
        filmName = findViewById(R.id.txtBook_FilmName);
        cinemaName = findViewById(R.id.txtBook_Cinema);
        filmDate = findViewById(R.id.txtBook_Date);
    }
    private void homeRedirect() {
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MovieBook.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
    private void updateData(){
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
                        filmDate.setText(film.getDate());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}