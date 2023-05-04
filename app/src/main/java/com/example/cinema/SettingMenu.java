package com.example.cinema;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SettingMenu extends AppCompatActivity {
    private Button signIn,signOut,home,ticketHistory,accountSetting;
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME ="userpref";
    private static final String KEY_ID = "userID";
    private static final String KEY_NAME = "userName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_menu);
        initVariable();
        autoUpdate();
        signIn();
        signOut();
        homeRedirect();
        ticketHistory();
    }

    private void ticketHistory() {
        ticketHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingMenu.this,TicketList.class);
                startActivity(intent);
            }
        });
    }

    private void signOut() {
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                Toast.makeText(SettingMenu.this,"Đăng xuất thành công",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void autoUpdate() {
        String userID = sharedPreferences.getString(KEY_ID,null);
        String userName = sharedPreferences.getString(KEY_NAME,null);
        if (userName != null){
            signIn.setText("Xin chào: "+userName);
        }else {
            signIn.setText("Xin chào, nhấn vào đây để đăng nhập!");
        }
//        if (!signIn.getText().toString().equals("Xin chào, nhấn vào đây để đăng nhập!")){
//            signIn.setEnabled(false);
//        }else {
//            signOut.setVisibility(View.GONE);
//        }
        if (userID != null){
            signIn.setEnabled(false);
        }else {
            signOut.setVisibility(View.GONE);
            ticketHistory.setVisibility(View.GONE);
            accountSetting.setVisibility(View.GONE);
        }
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

    private void signIn() {
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingMenu.this,Login.class);
                startActivity(intent);
            }
        });
    }

    private void initVariable() {
        //button
        signIn = findViewById(R.id.btnUserName);
        signOut = findViewById(R.id.btnLogOut);
        home = findViewById(R.id.btnSetting_Home);
        ticketHistory = findViewById(R.id.btnMyTicket);
        accountSetting = findViewById(R.id.btnAccountSetting);
        //shared references
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
    }
}