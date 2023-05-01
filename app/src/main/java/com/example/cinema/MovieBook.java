package com.example.cinema;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MovieBook extends AppCompatActivity {
    private Button home;
    private TextView filmName,cinemaName,filmDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookticket);
        initVariable();
        homeRedirect();
    }
    private void initVariable() {
        home = findViewById(R.id.btnBook_Home);
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
}