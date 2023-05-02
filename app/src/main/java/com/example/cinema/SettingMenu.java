package com.example.cinema;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SettingMenu extends AppCompatActivity {
    private Button signIn,home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String userName;
        setContentView(R.layout.activity_setting_menu);
        initVariable();
        signIn();
        homeRedirect();
    }

    private void homeRedirect() {
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingMenu.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int LAUNCH_LOGIN_ACTIVITY = 1;
        if (requestCode == LAUNCH_LOGIN_ACTIVITY) {
            if(resultCode == Activity.RESULT_OK){
                String name = data.getStringExtra("user");
                userName
                signIn.setText(name);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                // Write your code if there's no result
            }
        }
    }

    private void signIn() {
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int LAUNCH_LOGIN_ACTIVITY = 1;
                Intent intent = new Intent(SettingMenu.this,Login.class);
                startActivityForResult(intent,LAUNCH_LOGIN_ACTIVITY);
            }
        });
    }

    private void initVariable() {
        signIn = findViewById(R.id.btnUserName);
        home = findViewById(R.id.btnSetting_Home);
    }
}