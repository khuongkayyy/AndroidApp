package com.example.cinema;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
                //check the information inputted
                if (newUserMobile.isEmpty() || newUserName.isEmpty() || newUserEmail.isEmpty() || newUserPass.isEmpty() || newUserPassCon.isEmpty()){
                    Toast.makeText(Registration.this,"Vui lòng nhập đầy đủ thông tin!",Toast.LENGTH_SHORT).show();
                }else if (!isValidPhoneNumber(newUserMobile)){
                    Toast.makeText(Registration.this,"Số điện thoại không hợp lệ",Toast.LENGTH_SHORT).show();
                } else if (!isValidEmail(newUserEmail)) {
                    Toast.makeText(Registration.this,"Địa chỉ email không hợp lệ",Toast.LENGTH_SHORT).show();
                } else if (!isValidPassword(newUserPass)) {
                    Toast.makeText(Registration.this,"Mật khẩu phải dài từ 8 ký tự trở lên, có ít nhất một ký tự in hoa,một chữ số, không chứa ký tự đặt biệt!",Toast.LENGTH_SHORT).show();
                }else {
                    databaseReference.child("users").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            //check the information inputted
                            if (snapshot.hasChild(newUserMobile)){
                                Toast.makeText(Registration.this,"Số điện thoại trên đã được đăng ký rồi!",Toast.LENGTH_SHORT).show();
                            } else if (!newUserPass.equals(newUserPassCon)) {
                                Toast.makeText(Registration.this,"Mật khẩu không trùng lắp",Toast.LENGTH_SHORT).show();
                            }else {
                                //process to create account
                                databaseReference.child("users").child(newUserMobile).child("fullname").setValue(newUserName);
                                databaseReference.child("users").child(newUserMobile).child("email").setValue(newUserEmail);
                                databaseReference.child("users").child(newUserMobile).child("password").setValue(newUserPass);
                                databaseReference.child("users").removeEventListener(this);
                                registrationSuccessfully(Gravity.CENTER,newUserMobile,newUserPass);
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
    //inform user that account has been created successfully
    private void registrationSuccessfully(int gravity,String newUserMobile, String newUserPass){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.request_dialog);
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

        //declare:
        TextView dialogTittle = dialog.findViewById(R.id.dialogTittle);
        TextView dialogMessage = dialog.findViewById(R.id.txtDialogMessage);
        Button no = dialog.findViewById(R.id.btnRequestNo);
        Button yes = dialog.findViewById(R.id.btnRequestYes);

        //update data:
        dialogTittle.setText("Đã tạo tài khoản mới thành công!");
        dialogMessage.setText("Đăng nhập ngay?");
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("userPhone",newUserMobile);
                resultIntent.putExtra("userPass",newUserPass);
                setResult(Activity.RESULT_OK,resultIntent);
                onStop();
                finish();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
    //check the constraint
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
    //redirection to login activity
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