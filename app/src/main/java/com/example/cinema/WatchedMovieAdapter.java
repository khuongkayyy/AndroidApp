package com.example.cinema;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class WatchedMovieAdapter extends RecyclerView.Adapter<WatchedMovieAdapter.WatchedMovieListHolder> {
    Context context;
    ArrayList<Ticket> ticketArrayList;
    DatabaseReference databaseReference;
    ArrayList<Comment> commentArrayList;
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME ="userpref";
    private static final String KEY_ID = "userID";
    private static final String KEY_NAME = "userName";

    public WatchedMovieAdapter(Context context, ArrayList<Ticket> ticketArrayList) {
        this.context = context;
        this.ticketArrayList = ticketArrayList;
    }

    @NonNull
    @Override
    public WatchedMovieAdapter.WatchedMovieListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.one_watchedmovie_show,parent,false);
        return new WatchedMovieListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WatchedMovieAdapter.WatchedMovieListHolder holder, int position) {
        Ticket ticket = ticketArrayList.get(position);

        //set watched movie information:
        holder.watchedMovieName.setText(ticket.getFilm());
        holder.watchedMovieDate.setText("Ngày xem: "+ ticket.getBookdate());

        //set watched movie image
        String filmName = holder.watchedMovieName.getText().toString();
        switch (filmName){
            case "Lật Mặt 6":
                holder.watchedMovieImage.setImageResource(R.drawable.latmat1);
                break;
            case "Con Nhót Mót Chồng":
                holder.watchedMovieImage.setImageResource(R.drawable.cnmc1);
                break;
            case "Anh Em Mario":
                holder.watchedMovieImage.setImageResource(R.drawable.mario1);
                break;
            case "Ma Lai Rút Ruột":
                holder.watchedMovieImage.setImageResource(R.drawable.malai1);
                break;
            case "Mèo Siêu Quậy":
                holder.watchedMovieImage.setImageResource(R.drawable.meo1);
                break;
            case "Trạm Tàu Ma":
                holder.watchedMovieImage.setImageResource(R.drawable.ma1);
                break;
            case "Vệ Binh Dãy Ngân Hà":
                holder.watchedMovieImage.setImageResource(R.drawable.vebinh1);
                break;
            case "Tình Người Duyên Ma 2":
                holder.watchedMovieImage.setImageResource(R.drawable.duyenma1);
                break;
            case "Đầu Gấu Đụng Đầu Đất":
                holder.watchedMovieImage.setImageResource(R.drawable.daugau1);
                break;
            case "Khắc Tinh Của Quỷ":
                holder.watchedMovieImage.setImageResource(R.drawable.quy1);
                break;
            default:
                return;
        }
        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCommentDialog(Gravity.CENTER,holder.watchedMovieName.getText().toString());
            }
        });
    }

    private void showCommentDialog(int gravity, String filmName) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.comment_dialog);
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

        //textview
        TextView commentFilmName = dialog.findViewById(R.id.txtCommenFilmName);
        TextView commentLabel = dialog.findViewById(R.id.txtCommentLabel);
        EditText commentMessage = dialog.findViewById(R.id.edtCommentMessage);
        Button submitComment = dialog.findViewById(R.id.btnCommentSubmit);
        Button cancelComment = dialog.findViewById(R.id.btnCancelComment);

        //update dialog data
        commentLabel.setText("Nhập nhận xét");
        commentFilmName.setText("Tên tựa phim: "+filmName);


        //button click event listener:
        cancelComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        submitComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = commentMessage.getText().toString();
                getCommentID(new OnCommentIDFetchedListener() {
                    @Override
                    public void onCommentIDFetched(String commentID) {
//                        try{
//                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//                            imm.unregisterEditTex(editText);
//                        }catch (IllegalArgumentException e){
//
//                        }
                        addNewComment(commentID,filmName,message);
                        dialog.dismiss();
                    }
                });
            }
        });
        dialog.show();
    }

    private void addNewComment(String commentID,String filmName, String message) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
        String userID = sharedPreferences.getString(KEY_ID,null);
        String userName = sharedPreferences.getString(KEY_NAME,null);
        databaseReference = FirebaseDatabase.getInstance().getReference("comments");
        databaseReference.child(commentID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                databaseReference.child(commentID).child("date").setValue(getCommentDate());
                databaseReference.child(commentID).child("film").setValue(filmName);
                databaseReference.child(commentID).child("message").setValue(message);
                databaseReference.child(commentID).child("userID").setValue(userID);
                databaseReference.child(commentID).child("userName").setValue(userName);
                databaseReference.child(commentID).child("id").setValue(commentID);
                Toast.makeText(context, "Cám ơn bạn đã bình luận!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public String getCommentDate(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy"); // format pattern
        String currentDate = dateFormat.format(calendar.getTime()); // convert to string
        return  currentDate;
    }
    public interface OnCommentIDFetchedListener {
        void onCommentIDFetched(String commentID);
    }
    private String getCommentID(OnCommentIDFetchedListener listener) {
        commentArrayList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("comments");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Comment comment = dataSnapshot.getValue(Comment.class);
                    commentArrayList.add(comment);
                }
                int lastCommentId = commentArrayList.size() > 0 ? Integer.parseInt(commentArrayList.get(commentArrayList.size()-1).getId()) : 0;
                listener.onCommentIDFetched(String.valueOf(lastCommentId + 1));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return  "";
    }

    @Override
    public int getItemCount() {
        return ticketArrayList.size();
    }

    public class WatchedMovieListHolder extends RecyclerView.ViewHolder {
        public TextView watchedMovieName,watchedMovieDate;
        public ImageView watchedMovieImage;
        Button comment;
        public WatchedMovieListHolder(@NonNull View watchedMovieView) {
            super(watchedMovieView);
            //textview
            watchedMovieName =watchedMovieView.findViewById(R.id.txtWatchedFilmName);
            watchedMovieDate =watchedMovieView.findViewById(R.id.txtWatchedFilmDate);
            //image view
            watchedMovieImage = watchedMovieView.findViewById(R.id.watchedMovieImg);
            //button
            comment = watchedMovieView.findViewById(R.id.btnComment);
        }
    }
}
