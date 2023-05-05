package com.example.cinema;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountSetting extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    DatabaseReference databaseReference;
    private static final String SHARED_PREF_NAME ="userpref";
    private static final String KEY_ID = "userID";
    private static final String KEY_NAME = "userName";
    TextView accountName;
    EditText name, password, passwordConfirmed;
    Button home,newInforSubmit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setting);
        intiVariable();
        updateData();
//        editTextClicked();
        homeRedirect();
        confirmedUpdateData();
    }

    private void confirmedUpdateData() {
        String userID = sharedPreferences.getString(KEY_ID,null);
        newInforSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newUserName = name.getText().toString();
                String newUserPassword = password.getText().toString();
                String newUserPassconfirmed = passwordConfirmed.getText().toString();
                databaseReference.child("users").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (newUserName.isEmpty() || newUserPassword.isEmpty() || newUserPassconfirmed.isEmpty()){
                            Toast.makeText(AccountSetting.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                        } else if (!isValidPassword(newUserPassword)) {
                            Toast.makeText(AccountSetting.this, "Mật khẩu phải dài từ 8 ký tự trở lên, có ít nhất một ký tự in hoa, không chứa ký tự đặt biệt!", Toast.LENGTH_SHORT).show();
                        } else if (!newUserPassword.equals(newUserPassconfirmed)) {
                            Toast.makeText(AccountSetting.this, "Mật khẩu không trùng lắp!", Toast.LENGTH_SHORT).show();
                        }else {
                            databaseReference.child(userID).child("fullname").setValue(newUserName);
                            databaseReference.child(userID).child("password").setValue(newUserPassword);
                            Toast.makeText(AccountSetting.this, "Đã cập nhật thông tin tài khoản thành công!", Toast.LENGTH_SHORT).show();
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.clear();
                            editor.commit();
                            editor.putString(KEY_ID,userID);
                            editor.putString(KEY_NAME,newUserName);
                            accountName.setText("Xin chào, "+newUserName);
                            name.setText(newUserName);
                            editor.apply();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });
    }
    public boolean isValidPassword(String password) {
        // Check if password is at least 8 characters long, has at least 1 uppercase character, and doesn't contain special characters
        String pattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
        return password.matches(pattern);
    }

    private void homeRedirect() {
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountSetting.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
    private void updateData() {
        String userName = sharedPreferences.getString(KEY_NAME,null);
        accountName.setText("Xin chào, "+userName);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    if (user.getFullname().equals(userName)){
                        name.setText(userName);
                        password.setText(user.getPassword());
                        passwordConfirmed.setText(user.getPassword());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void intiVariable() {
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        accountName = findViewById(R.id.txtAccountName);
        //edit text
        name = findViewById(R.id.accountNameEdt);
        password = findViewById(R.id.accountPasswordEdt);
        passwordConfirmed = findViewById(R.id.accountPasswordConfirmedEdt);
        //db connection
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        //button
        home = findViewById(R.id.btnAccount_Home);
        newInforSubmit = findViewById(R.id.btnNewInforSubmit);
    }
}