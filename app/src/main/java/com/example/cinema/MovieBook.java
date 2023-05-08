package com.example.cinema;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class MovieBook extends AppCompatActivity {
    private Button home;
    private Button cinemaChoice, filmChoice,dateChoice;
    private Button show1,show2,show3,show4,show5,show6;
    private TextView filmName,cinemaName,filmDate;
    private String filmTime;
    private DatabaseReference databaseReference;
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME ="userpref";
    private static final String KEY_ID = "userID";
    private static final String KEY_NAME = "userName";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookticket);
        initVariable();
        homeRedirect();
        updateData();
        cinemaChoice();
        filmChoice();
        dateChoice();
        showChoice();
    }
    //format movie show time
    private String showTimeFormat(String time, String duration) {
        String timeString = time +" "+" "+duration;
        int durationNum = Integer.parseInt(duration.substring(0, duration.indexOf(" ")));
        String[] parts = timeString.split("h");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1].substring(0, 2));
        int totalMinutes = minutes + durationNum;
        int newHours = hours + totalMinutes / 60;
        int newMinutes = totalMinutes % 60;
        String formattedTime = String.format("%02dh%02d", newHours, newMinutes);
        return time+" ~ "+ formattedTime;
    }
    //handle show time choose event
    private void showChoice() {
        singleShowChoice(show1,1,Gravity.CENTER);
        singleShowChoice(show2,2,Gravity.CENTER);
        singleShowChoice(show3,3,Gravity.CENTER);
        singleShowChoice(show4,4,Gravity.CENTER);
        singleShowChoice(show5,5,Gravity.CENTER);
        singleShowChoice(show6,6,Gravity.CENTER);
    }
    private void singleShowChoice(Button button, int showID,int gravity) {
        //user information:
        String userID = sharedPreferences.getString(KEY_ID,null);
        String userName = sharedPreferences.getString(KEY_NAME,null);
        String showTime = null;
        String ticketType = null;
        String ticketPrice = null;
        //find which show time to get time, type and price of ticket
        switch (showID){
            case 1:
                showTime = show1.getText().toString();
                ticketType = "2D - Phụ Đề";
                ticketPrice = "100.000 đồng";
                break;
            case 2:
                showTime = show2.getText().toString();
                ticketType = "2D - Phụ Đề";
                ticketPrice = "100.000 đồng";
                break;
            case 3:
                showTime = show3.getText().toString();
                ticketType = "2D - Thuyết Minh";
                ticketPrice = "150.000 đồng";
                break;
            case 4:
                showTime = show4.getText().toString();
                ticketType = "2D - Thuyết Minh";
                ticketPrice = "150.000 đồng";
                break;
            case 5:
                showTime = show5.getText().toString();
                ticketType = "3D - Phụ Đề";
                ticketPrice = "185.000 đồng";
                break;
            case 6:
                showTime = show6.getText().toString();
                ticketType = "3D - Phụ Đề";
                ticketPrice = "185.000 đồng";
                break;
            default:
                return;
        }
        String finalShowTime = showTime;
        String finalTicketPrice = ticketPrice;
        String finalTicketType = ticketType;

        //count the current number of ticket to get the ticketID
        ArrayList<Ticket> ticketArrayList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("tickets");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Ticket ticket = dataSnapshot.getValue(Ticket.class);
                    ticketArrayList.add(ticket);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //check the ticket information:
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MovieBook.this);
                if (filmName.getText().toString().isEmpty() || cinemaName.getText().toString().isEmpty() || filmDate.getText().toString().isEmpty()){
                    builder.setTitle("Vui lòng chọn đầy đủ thông tin của vé!");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                } else if (userID == null) {
                    requestToLogin(Gravity.CENTER);
                } else {
                    //show up dialog
                    final Dialog dialog = new Dialog(MovieBook.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.ticket_dialog);
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
                    TextView dialogCinema = dialog.findViewById(R.id.txtDialogCinema);
                    TextView dialogDate = dialog.findViewById(R.id.txtDialogDate);
                    TextView dialogTime = dialog.findViewById(R.id.txtDialogTime);
                    TextView dialogUser = dialog.findViewById(R.id.txtDialogUser);
                    Button no = dialog.findViewById(R.id.btnTicketDialogNo);
                    Button yes = dialog.findViewById(R.id.btnTicketDialogYes);
                    //update dialog information
                    dialogCinema.setText("Rạp chiếu: "+ cinemaName.getText().toString());
                    dialogDate.setText("Ngày xem: "+filmDate.getText().toString());
                    dialogTime.setText("Suất chiếu: "+showTimeFormat(finalShowTime,filmTime));
                    dialogUser.setText("Người đặt vé: "+userName);
                    no.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //process to book movie ticket
                            databaseReference.child("tickets").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String ticketID = String.valueOf(ticketArrayList.size());
                                    databaseReference.child(ticketID).child("bookdate").setValue(filmDate.getText().toString());
                                    databaseReference.child(ticketID).child("cinema").setValue(cinemaName.getText().toString());
                                    databaseReference.child(ticketID).child("film").setValue(filmName.getText().toString());
                                    databaseReference.child(ticketID).child("price").setValue(finalTicketPrice);
                                    databaseReference.child(ticketID).child("tickettype").setValue(finalTicketType);
                                    databaseReference.child(ticketID).child("time").setValue(finalShowTime);
                                    databaseReference.child(ticketID).child("user").setValue(userID);
                                    databaseReference.child(ticketID).child("id").setValue(ticketID);
                                    dialog.dismiss();
                                    bookSuccessfully(Gravity.CENTER);
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });
                        }
                    });
                    dialog.show();
                }
                AlertDialog dialogMain = builder.create();
                dialogMain.show();
            }
        });
    }
    //request to log in before booking ticket
    private void requestToLogin(int gravity) {
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
        dialogTittle.setText("Không thể thực hiện");
        dialogMessage.setText("Bạn phải tiến hành đăng nhập để mua vé\n Đăng nhập ngay ?");
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MovieBook.this,Login.class);
                startActivity(intent);
            }
        });
        dialog.show();
    }
    //show up booking ticket result successfully
    private void bookSuccessfully(int gravity){
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
        dialogTittle.setText("Đã đặt vé thành công");
        dialogMessage.setText("Bạn có muốn xem danh sách vé đã đặt!");
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MovieBook.this,TicketList.class);
                startActivity(intent);
            }
        });
        dialog.show();
    }
    //update data when change book date
    private void dateChoice() {
        dateChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(MovieBook.this);
                datePickerDialog.setCanceledOnTouchOutside(true);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
                datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        String date = String.valueOf(i2)+"/"+String.valueOf(i1+1)+"/"+String.valueOf(i);
                        filmDate.setText(date);
                        dateChoice.setText(date);
                    }
                });
                datePickerDialog.show();
            }
        });
    }
    //update data when change film
    private void filmChoice() {
        filmChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MovieBook.this,MovieList.class);
                startActivity(intent);
            }
        });
    }
    //update data when change cinema
    private void cinemaChoice() {
        cinemaChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCinemaList(cinemaChoice,Gravity.CENTER);
            }
        });
    }
    private void showCinemaList(Button button,int gravity) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.cinema_choose_dialog);
        dialog.setCancelable(true);
        Window window = dialog.getWindow();
        if (window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttribute = window.getAttributes();
        windowAttribute.gravity = gravity;
        window.setAttributes(windowAttribute);
        //declare variable:
        Button cinema1 = dialog.findViewById(R.id.btnDialogCine1);
        Button cinema2 = dialog.findViewById(R.id.btnDialogCine2);
        Button cinema3 = dialog.findViewById(R.id.btnDialogCine3);
        Button cinema4 = dialog.findViewById(R.id.btnDialogCine4);
        Button cinema5 = dialog.findViewById(R.id.btnDialogCine5);
        Button cinema6 = dialog.findViewById(R.id.btnDialogCine6);
        Button cinema7 = dialog.findViewById(R.id.btnDialogCine7);
        Button cinema8 = dialog.findViewById(R.id.btnDialogCine8);
        Button cinema9 = dialog.findViewById(R.id.btnDialogCine9);

        setCinemaName(dialog,cinema1);
        setCinemaName(dialog,cinema2);
        setCinemaName(dialog,cinema3);
        setCinemaName(dialog,cinema4);
        setCinemaName(dialog,cinema5);
        setCinemaName(dialog,cinema6);
        setCinemaName(dialog,cinema7);
        setCinemaName(dialog,cinema8);
        setCinemaName(dialog,cinema9);

        dialog.show();
    }
    //update data when change cinema
    private void setCinemaName(Dialog dialog,Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button button1 = findViewById(R.id.btnCinemaChoice);
                button1.setText(button.getText().toString());
                cinemaName.setText(button.getText().toString());
                dialog.dismiss();
            }
        });
    }

    private void initVariable() {
        //button:
        home = findViewById(R.id.btnBook_Home);
        cinemaChoice = findViewById(R.id.btnCinemaChoice);
        filmChoice = findViewById(R.id.btnFilmChoice);
        dateChoice = findViewById(R.id.btnDateChoice);

        show1 = findViewById(R.id.btnShowTime1);
        show2 = findViewById(R.id.btnShowTime2);
        show3 = findViewById(R.id.btnShowTime3);
        show4 = findViewById(R.id.btnShowTime4);
        show5 = findViewById(R.id.btnShowTime5);
        show6 = findViewById(R.id.btnShowTime6);

        //textName:
        filmName = findViewById(R.id.txtBook_FilmName);
        cinemaName = findViewById(R.id.txtBook_Cinema);
        filmDate = findViewById(R.id.txtBook_Date);

        //db connection
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://cinema-34e7b-default-rtdb.firebaseio.com/");
        //share references
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        //arraylist
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
    private void updateData(){
        Intent intent = getIntent();
        String name = intent.getStringExtra("filmName");
        filmName.setText(name);
        filmChoice.setText(name);
        //error here, plank text when no movie is choose
        databaseReference = FirebaseDatabase.getInstance().getReference("film");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Film film = dataSnapshot.getValue(Film.class);
                    if (film.getName().equals(filmName.getText().toString())){
                        filmTime = film.getTime();
//                        filmName.setText(filmTime);
                        show1.setText(showTimeFormat(show1.getText().toString(),filmTime));
                        show2.setText(showTimeFormat(show2.getText().toString(),filmTime));
                        show3.setText(showTimeFormat(show3.getText().toString(),filmTime));
                        show4.setText(showTimeFormat(show4.getText().toString(),filmTime));
                        show5.setText(showTimeFormat(show5.getText().toString(),filmTime));
                        show6.setText(showTimeFormat(show6.getText().toString(),filmTime));
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}