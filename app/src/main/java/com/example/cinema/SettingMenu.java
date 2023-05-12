package com.example.cinema;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
        accountSetting();
    }
    //redirection to account setting activity
    private void accountSetting() {
        accountSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingMenu.this,AccountSetting.class);
                startActivity(intent);
            }
        });
    }
    //redirection to transaction history
    private void ticketHistory() {
        ticketHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingMenu.this,TicketList.class);
                startActivity(intent);
            }
        });
    }
    //handle sign out request
    private void signOut() {
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSignOutDialog(Gravity.CENTER);
            }
        });
    }

    private void openSignOutDialog(int gravity) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.logout_dialog);
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        if (window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttribute = window.getAttributes();
        windowAttribute.gravity = gravity;
        window.setAttributes(windowAttribute);

        Button no = dialog.findViewById(R.id.btnNo);
        Button yes = dialog.findViewById(R.id.btnYes);

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                Toast.makeText(SettingMenu.this,"Đăng xuất thành công",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                finish();
            }
        });
        dialog.show();
    }
    //auto update data when open activity
    private void autoUpdate() {
        String userID = sharedPreferences.getString(KEY_ID,null);
        String userName = sharedPreferences.getString(KEY_NAME,null);
        if (userName != null){
            signIn.setText("Xin chào:\n"+userName);
        }else {
            signIn.setText("Xin chào,\nnhấn vào đây để đăng nhập!");
        }
        if (userID != null){
            signIn.setEnabled(false);
        }else {
            signOut.setVisibility(View.GONE);
            ticketHistory.setVisibility(View.GONE);
            accountSetting.setVisibility(View.GONE);
        }
    }
    //redirection to other activity
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