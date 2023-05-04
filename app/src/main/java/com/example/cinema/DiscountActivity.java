package com.example.cinema;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DiscountActivity extends AppCompatActivity {
    private Button home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discount);
        home = findViewById(R.id.btnGift_Home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DiscountActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
}