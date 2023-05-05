package com.example.cinema;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.internal.InternalTokenProvider;

public class Login extends AppCompatActivity {
    private static final int REQUEST_CODE = 1;
    private Button login,registration;
    private EditText phoneNum, password;
    private DatabaseReference databaseReference;
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME ="userpref";
    private static final String KEY_ID = "userID";
    private static final String KEY_NAME = "userName";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        initVariable();
        registRedirect();
        loginController();
    }

    private void loginController() {
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String userMobile = phoneNum.getText().toString();
                final String userPassword = password.getText().toString();
                if (userMobile.isEmpty() || userPassword.isEmpty()) {
                    Toast.makeText(Login.this,"Vui lòng nhập đầy đủ email và mật khẩu",Toast.LENGTH_SHORT).show();
                }
                else {
                    databaseReference.child("users").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(userMobile)){
                                final String getPassword = snapshot.child(userMobile).child("password").getValue(String.class);
                                final String getName = snapshot.child(userMobile).child("fullname").getValue(String.class);
                                if (getPassword.equals(userPassword)){
                                    Toast.makeText(Login.this,"Đăng nhập thành công",Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Login.this,SettingMenu.class);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString(KEY_ID,userMobile);
                                    editor.putString(KEY_NAME,getName);
                                    editor.apply();
                                    startActivity(intent);
                                    finish();
                                }else {
                                    Toast.makeText(Login.this,"Mật khẩu nhập vào không chính xác!",Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(Login.this,"Không tìm thấy tài khoản liên kết với số điện thoại trên!",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }

    private void registRedirect() {
        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(Login.this,Registration.class);
//                startActivity(intent);
                Intent intent = new Intent(Login.this,Registration.class);
                startActivityForResult(intent,REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            String userPhone = data.getStringExtra("userPhone");
            String userPass = data.getStringExtra("userPass");
            phoneNum.setText(userPhone);
            password.setText(userPass);
        }
    }

    private void initVariable() {
        //button
        login = findViewById(R.id.btnLogin);
        registration = findViewById(R.id.btnRegisterDirect);
        //edit text
        phoneNum = findViewById(R.id.edtPhoneNum);
        password = findViewById(R.id.edtPassword);

        //db connection
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://cinema-34e7b-default-rtdb.firebaseio.com/");

        //share references
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
    }
}