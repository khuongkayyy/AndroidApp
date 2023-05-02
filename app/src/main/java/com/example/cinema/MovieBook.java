package com.example.cinema;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MovieBook extends AppCompatActivity {
    private Button home;
    private Button cinemaChoice, filmChoice;
    private TextView filmName,cinemaName,filmDate;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookticket);
        initVariable();
        homeRedirect();
        updateData();
        cinemaChoice();
        filmChoice();
    }

    private void filmChoice() {
        filmChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MovieBook.this,MovieList.class);
                startActivity(intent);
            }
        });
    }

    private void cinemaChoice() {
        cinemaChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCinemaList(cinemaChoice);
            }
        });
    }
    private void showCinemaList(Button button) {
        final  String[] cinemaList = {"Nam Sài Gòn","Gò Vấp","Cộng Hòa","GoldView","Cantavil","Thủ Đức","Moonlight","NowZone","Phú Thọ"};
        final int[] selectedItem = {0};
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MovieBook.this);
                builder.setTitle("Hãy chọn một rạp phim: ");
                builder.setSingleChoiceItems(cinemaList, selectedItem[0], new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedItem[0] = which;
                    }
                });
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Button button = findViewById(R.id.btnCinemaChoice);
                        button.setText(cinemaList[selectedItem[0]]);
                        cinemaName.setText(button.getText().toString());
                    }
                });
                builder.setNegativeButton("Cancel", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private void initVariable() {
        //button:
        home = findViewById(R.id.btnBook_Home);
        cinemaChoice = findViewById(R.id.btnCinemaChoice);
        filmChoice = findViewById(R.id.btnFilmChoice);
        //textName:
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
        filmChoice.setText(name);
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