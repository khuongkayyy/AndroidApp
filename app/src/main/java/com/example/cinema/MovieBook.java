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

    private String showTimeFormat(String time, String duration) {
        String timeString = time +" "+" "+duration;
//        String durationString = duration.substring(0, duration.indexOf(" "));
//        int duration = Integer.parseInt(durationString);
//        int duration = Integer.parseInt(durationString.substring(0, durationString.indexOf(" ")));
        String[] parts = timeString.split("h");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1].substring(0, 2));
        int totalMinutes = minutes + 120;
        int newHours = hours + totalMinutes / 60;
        int newMinutes = totalMinutes % 60;
        String formattedTime = String.format("%02dh%02d", newHours, newMinutes);
        return time+" ~ "+ formattedTime;
//        String[] parts = start.split("h");
//        int hours = Integer.parseInt(parts[0]);
//        int minutes = Integer.parseInt(parts[1]);
//        int durationMinutes = Integer.parseInt(duration.split(" ")[0]);
//        minutes += durationMinutes;
//        if (minutes >= 60) {
//            hours += 1;
//            minutes -= 60;
//        }
//        String end = hours + "h" + minutes;
//        return start +"~" +end;
    }

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

                    dialogCinema.setText("Rạp chiếu: "+ cinemaName.getText().toString());
                    dialogDate.setText("Ngày xem: "+filmDate.getText().toString());
                    dialogTime.setText("Suất chiếu: "+finalShowTime);
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

    private void openTicketDialog(int gravity, String cinemaName, String ticketDate, String ticketTime, String ticketBuyer) {
        final Dialog dialog = new Dialog(this);
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

        //update data to textview:
        dialogCinema.setText("Rạp chiếu hiện tại là: "+cinemaName);
        dialogDate.setText("Ngày chiếu: "+ticketDate);
        dialogTime.setText("Suất chiếu: "+ticketTime);
        dialogUser.setText("Người đặt vé: "+ticketBuyer);
        //button event handle:
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        dialog.show();
    }
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
        //uopdate data:
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
        //uopdate data:
        dialogTittle.setText("Đã đạt vé thành công");
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

    private void filmChoice() {
        filmChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MovieBook.this,MovieList.class);
                startActivity(intent);
            }
        });
    }

    private void cinemaChoice() {
        cinemaChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCinemaList(cinemaChoice);
            }
        });
    }
    private void showCinemaList(Button button) {
        final  String[] cinemaList = {"Nam Sài Gòn","Gò Vấp","Cộng Hòa","GoldView","Cantavil","Thủ Đức","Moonlight","NowZone","Phú Thọ"};
        final int[] selectedItem = {0};
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MovieBook.this);
                builder.setTitle("Hãy chọn một rạp phim: ");
                builder.setSingleChoiceItems(cinemaList, selectedItem[0], new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedItem[0] = which;
                    }
                });
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Button button = findViewById(R.id.btnCinemaChoice);
                        button.setText(cinemaList[selectedItem[0]]);
                        cinemaName.setText(button.getText().toString());
                    }
                });
                builder.setNegativeButton("Cancel", null);
                AlertDialog dialog = builder.create();
                dialog.show();
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
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        show1.setText(showTimeFormat(show1.getText().toString(),filmTime));
        show2.setText(showTimeFormat(show2.getText().toString(),filmTime));
        show3.setText(showTimeFormat(show3.getText().toString(),filmTime));
        show4.setText(showTimeFormat(show4.getText().toString(),filmTime));
        show5.setText(showTimeFormat(show5.getText().toString(),filmTime));
        show6.setText(showTimeFormat(show6.getText().toString(),filmTime));

    }
}