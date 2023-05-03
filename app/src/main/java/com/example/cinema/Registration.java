package com.example.cinema;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registration extends AppCompatActivity {
    private Button registration,login;
    private EditText mobile,name,email,password,passwordConfirmed;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);
        initVariable();
        loginRedirect();
        registrationController();
    }

    private void registrationController() {
        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //fetch data
                final String newUserMobile = mobile.getText().toString();
                final String newUserName = name.getText().toString();
                final String newUserEmail = email.getText().toString();
                final String newUserPass = password.getText().toString();
                final String newUserPassCon = passwordConfirmed.getText().toString();

                if (newUserMobile.isEmpty() || newUserName.isEmpty() || newUserEmail.isEmpty() || newUserPass.isEmpty() || newUserPassCon.isEmpty()){
                    Toast.makeText(Registration.this,"Vui lòng nhập đầy đủ thông tin!",Toast.LENGTH_SHORT).show();
                } else if (!newUserPass.equals(newUserPassCon)) {
                    Toast.makeText(Registration.this,"Mật khẩu không trùng lắp",Toast.LENGTH_SHORT).show();
                }else {

                    databaseReference.child("users").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(newUserMobile)){
                                Toast.makeText(Registration.this,"Số điện thoại trên đã được đăng ký rồi!",Toast.LENGTH_SHORT).show();
                            } else if (!isValidPhoneNumber(newUserMobile)) {
                                Toast.makeText(Registration.this,"Số điên thoại không hợp lệ!",Toast.LENGTH_SHORT).show();
                            } else {
                                if (!isValidPassword(newUserPass)){
                                    Toast.makeText(Registration.this,"Mật khẩu phải dài từ 8 ký tự trở lên, có ít nhất một ký tự in hoa, không chứa ký tự đặt biệt!",Toast.LENGTH_SHORT).show();
                                }else {
                                    if (isValidEmail(newUserEmail)){
                                        Toast.makeText(Registration.this,"Địa chỉ email không hợp lệ!",Toast.LENGTH_SHORT).show();
                                    }
                                    databaseReference.child("users").child(newUserMobile).child("fullname").setValue(newUserName);
                                    databaseReference.child("users").child(newUserMobile).child("email").setValue(newUserEmail);
                                    databaseReference.child("users").child(newUserMobile).child("password").setValue(newUserPass);
                                    Toast.makeText(Registration.this,"Đăng ký tài khoản mới thành công!",Toast.LENGTH_SHORT).show();
                                }
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
    public static boolean isValidPhoneNumber(String input) {
        String regexPattern = "^0(91|94|98|97|90|93)\\d{7}$";
        return input.matches(regexPattern);
    }
    public boolean isValidPassword(String password) {
        // Check if password is at least 8 characters long, has at least 1 uppercase character, and doesn't contain special characters
        String pattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
        return password.matches(pattern);
    }
    public static boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void loginRedirect() {
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Registration.this,Login.class);
                startActivity(intent);
            }
        });
    }

    private void initVariable() {
        //button
        registration = findViewById(R.id.btnRegister);
        login = findViewById(R.id.btnLoginDirect);

        //edit text
        mobile = findViewById(R.id.edtNewUserMobile);
        name = findViewById(R.id.edtNewUserName);
        email = findViewById(R.id.edtNewUserEmail);
        password = findViewById(R.id.edtNewUserPassword);
        passwordConfirmed = findViewById(R.id.edtNewUserPassCon);

        //db connection:
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://cinema-34e7b-default-rtdb.firebaseio.com/");
    }
}