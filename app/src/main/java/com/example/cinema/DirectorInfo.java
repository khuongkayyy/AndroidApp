package com.example.cinema;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.annotation.NonNullApi;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DirectorInfo extends AppCompatActivity {
    private Button home;
    private TextView fullName,name,date,info,titleName;
    private ImageView directorImg;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_director_info);
        init();
        homeRedirect();
        getInfo();

    }
    private void init(){
        home = findViewById(R.id.btnDetail_Home);
        fullName = findViewById(R.id.fullname);
        name = findViewById(R.id.name);
        date = findViewById(R.id.date);
        info = findViewById(R.id.info);
        titleName = findViewById(R.id.nameTitle);
        directorImg = findViewById(R.id.image);
    }
    private void initData(String a){
        databaseReference = FirebaseDatabase.getInstance().getReference(a);
    }
    private void homeRedirect() {
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DirectorInfo.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
    private void getInfo(){
        initData("directors");
        Intent intent = getIntent();
        String nameReceive = intent.getStringExtra("name");
        name.setText("Nghệ danh: " + nameReceive);
        titleName.setText(nameReceive);
        databaseReference = FirebaseDatabase.getInstance().getReference("directors");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Director director = dataSnapshot.getValue(Director.class);
                    Log.d("TAG",director.getName());
                    if (director.getName().equals(nameReceive.toString())){
                        Log.d("Tag","Bang nhau");
                        titleName.setText(director.getName().toUpperCase());
                        fullName.setText("Họ tên: "+director.getFullName());
                        date.setText("Ngày sinh: "+director.getDate());
                        info.setText(director.getInfo().replace("\\n","\n"));

                        String imageName = "tg_" + director.getId();
                        Resources resources = getResources();
                        int imageResourceId = resources.getIdentifier(imageName, "drawable", getPackageName());
                        if (imageResourceId == 0) {
                            imageResourceId = getResources().getIdentifier("unknown", "drawable", getPackageName());
                        }
                        directorImg.setImageResource(imageResourceId);

                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}