package com.example.cinema;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.service.autofill.DateTransformation;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TicketList extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME ="userpref";
    private static final String KEY_ID = "userID";
    private static final String KEY_NAME = "userName";
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    TicketListAdapter ticketListAdapter;
    ArrayList<Ticket> ticketArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_list);
        initVariable();
        ticketHistory();
    }

    private void ticketHistory() {
        //init variable
        String userID = sharedPreferences.getString(KEY_ID,null);
        String userName = sharedPreferences.getString(KEY_NAME,null);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Ticket ticket = dataSnapshot.getValue(Ticket.class);
                    if (userID != null){
                        if (ticket.getUser().equals(userID)){
                            ticketArrayList.add(ticket);
                        }
                    }
                }
                ticketListAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initVariable() {
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        recyclerView = findViewById(R.id.ticketList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ticketArrayList = new ArrayList<>();
        ticketListAdapter = new TicketListAdapter(this,ticketArrayList);
        recyclerView.setAdapter(ticketListAdapter);
        //db connection
        databaseReference = FirebaseDatabase.getInstance().getReference("tickets");
    }
}