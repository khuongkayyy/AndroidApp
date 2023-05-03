package com.example.cinema;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TicketListAdapter extends RecyclerView.Adapter<TicketListAdapter.TicketListHolder> {
    Context context;
    ArrayList<Ticket> ticketArrayList;
    DatabaseReference databaseReference;

    public TicketListAdapter(Context context, ArrayList<Ticket> ticketArrayList) {
        this.context = context;
        this.ticketArrayList = ticketArrayList;
    }

    @NonNull
    @Override
    public TicketListAdapter.TicketListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.one_ticket_show,parent,false);
        return new TicketListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketListAdapter.TicketListHolder holder, int position) {
        Ticket ticket = ticketArrayList.get(position);
        //set ticket information:
        holder.ticketName.setText(ticket.getFilm());
        holder.ticketCinema.setText("Phòng chiếu: "+ticket.getCinema());
        holder.ticketTime.setText("Suất chiếu: "+ticket.getTime());
        holder.ticketBuyer.setText("Giá vé: "+ticket.getPrice());
        holder.ticketType.setText("Loại vé: "+ticket.getTickettype());
        holder.ticketDate.setText("Ngày đặt vé: "+ticket.getBookdate());

        //cancel ticket controller
        if (!checkTicketDate(ticket.getBookdate())){
            holder.cancelTicket.setText("Không thể hủy vé!");
            holder.cancelTicket.setEnabled(false);
        }else {
            holder.cancelTicket.setEnabled(true);
        }
        String ticketID = ticket.getId();
        holder.cancelTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelTicketRequest(ticketID);
            }
        });
        //set ticket image
        String filmName = holder.ticketName.getText().toString();
        switch(filmName){
            case "Lật Mặt 6":
                holder.ticketImage.setImageResource(R.drawable.latmat1);
                break;
            case "Con Nhót Mót Chồng":
                holder.ticketImage.setImageResource(R.drawable.cnmc1);
                break;
            case "Anh Em Mario":
                holder.ticketImage.setImageResource(R.drawable.mario1);
                break;
            case "Ma Lai Rút Ruột":
                holder.ticketImage.setImageResource(R.drawable.malai1);
                break;
            case "Mèo Siêu Quậy":
                holder.ticketImage.setImageResource(R.drawable.meo1);
                break;
            case "Trạm Tàu Ma":
                holder.ticketImage.setImageResource(R.drawable.ma1);
                break;
            case "Vệ Binh Dãy Ngân Hà":
                holder.ticketImage.setImageResource(R.drawable.vebinh1);
                break;
            case "Tình Người Duyên Ma 2":
                holder.ticketImage.setImageResource(R.drawable.duyenma1);
                break;
            case "Đầu Gấu Đụng Đầu Đất":
                holder.ticketImage.setImageResource(R.drawable.daugau1);
                break;
            case "Khắc Tinh Của Quỷ":
                holder.ticketImage.setImageResource(R.drawable.quy1);
                break;
            default:
                return;
        }
    }

    private void cancelTicketRequest(String ticketId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Bạn có muốn hủy vé ?");
        String text = "Nhập 'Yes' để tiến hành xóa vé";
        SpannableString spannableString = new SpannableString(text);
        StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
        spannableString.setSpan(boldSpan, 7, 14, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        builder.setMessage(text);

        final EditText input = new EditText(context);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Get the input value and check if it is valid
                String confirm = input.getText().toString().trim();
                if (confirm.equalsIgnoreCase("Yes")){
                    databaseReference = FirebaseDatabase.getInstance().getReference("tickets");
                    databaseReference.child("tickets").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            databaseReference.child(ticketId).setValue(null);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    Toast.makeText(context, "Đã hủy vé thành công!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // Set the negative button action to cancel the dialog
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Show the dialog
        builder.show();
    }

    private boolean checkTicketDate(String ticketDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        Date date = null;
        try {
            date = dateFormat.parse(ticketDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Compare the date to today
        Calendar today = Calendar.getInstance();
        Calendar movieDate = Calendar.getInstance();
        movieDate.setTime(date);
        if (movieDate.before(today)) {
            // The movie date is in the past
            return  false;
        } else {
            // The movie date is in the future
            return  true;
        }
    }

    @Override
    public int getItemCount() {
        return ticketArrayList.size();
    }

    public class TicketListHolder extends RecyclerView.ViewHolder {
        public TextView ticketName,ticketCinema,ticketTime, ticketBuyer, ticketType, ticketDate;
        public Button cancelTicket;
        public ImageView ticketImage;
        public TicketListHolder(View ticketView) {
            super(ticketView);
            //textview
            ticketName = ticketView.findViewById(R.id.txtTicketName);
            ticketCinema = ticketView.findViewById(R.id.txtTicketCinema);
            ticketTime = ticketView.findViewById(R.id.txtTicketTime);
            ticketBuyer = ticketView.findViewById(R.id.txtTicketBuyer);
            ticketType = ticketView.findViewById(R.id.txtTicketType);
            ticketDate = ticketView.findViewById(R.id.txtTicketDate);
            //button
            cancelTicket = ticketView.findViewById(R.id.btnCancelTicket);

            //image
            ticketImage = ticketView.findViewById(R.id.ticketImg);
        }
    }
}
