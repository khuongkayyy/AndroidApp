package com.example.cinema;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class CommentListAdapter  extends RecyclerView.Adapter<CommentListAdapter.CommentListHolder> {
    Context context;
    ArrayList<Comment> commentArrayList;

    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME ="userpref";
    private static final String KEY_ID = "userID";
    private static final String KEY_NAME = "userName";

    DatabaseReference databaseReference;
    public CommentListAdapter(Context context, ArrayList<Comment> commentArrayList) {
        this.context = context;
        this.commentArrayList = commentArrayList;
    }

    @NonNull
    @Override
    public CommentListAdapter.CommentListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.one_comment_show,parent,false);
        return new CommentListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentListAdapter.CommentListHolder holder, @SuppressLint("RecyclerView") int position) {
        Comment comment = commentArrayList.get(position);
        holder.commentUser.setText(comment.getUserName());
        holder.commentDate.setText(comment.getDate());
        holder.commentMessage.setText(comment.getMessage());
        if (!checkUser(position)){
            holder.btnEditComment.setVisibility(View.GONE);
            holder.btnDeleteComment.setVisibility(View.GONE);
        }
        holder.btnDeleteComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteComment(Gravity.CENTER,holder.commentMessage.getText().toString(),position);
            }
        });
        holder.btnEditComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditCommentDialog(Gravity.CENTER,comment.getMessage(),comment.getId());
            }
        });
    }

    private void showEditCommentDialog(int gravity,String commentMessage,String commentID) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.cancel_ticket_dialog);
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
        //declare
        TextView editDialogLabel = dialog.findViewById(R.id.txtCancelLabel);
        TextView editDialogMessage = dialog.findViewById(R.id.txtCancelMessage);
        EditText editContent = dialog.findViewById(R.id.edtCancelTicketDialog);
        Button no = dialog.findViewById(R.id.btnCancelNo);
        Button yes = dialog.findViewById(R.id.btnCancelYes);
        //update dialog data
        editDialogLabel.setText("Cập nhật nội dung bình luận");
        editDialogMessage.setText("Nội dung bình luận");
        editContent.setText(commentMessage);
        //event listener
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference = FirebaseDatabase.getInstance().getReference("comments");
                databaseReference.child("comments").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String newComment = editContent.getText().toString();
                        databaseReference.child(commentID).child("message").setValue(newComment);
                        databaseReference.child(commentID).child("date").setValue(getCommentDate());
                        Toast.makeText(context, "Đã cập nhật bình luận thành công!", Toast.LENGTH_SHORT).show();
                        commentArrayList.clear();
                        notifyDataSetChanged();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    public String getCommentDate(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy"); // format pattern
        String currentDate = dateFormat.format(calendar.getTime()); // convert to string
        return  currentDate;
    }

    private void deleteComment(int gravity,String commentMessage,int position) {
        final Dialog dialog = new Dialog(context);
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

        //declare
        TextView dialogLabel = dialog.findViewById(R.id.dialogTittle);
        TextView dialogMessage = dialog.findViewById(R.id.txtDialogMessage);
        Button no = dialog.findViewById(R.id.btnRequestNo);
        Button yes =dialog.findViewById(R.id.btnRequestYes);
        yes.setBackgroundColor(0xFFFF0000);
        //update dialog data
        dialogLabel.setText("Xác nhận xóa bình luận");
        dialogMessage.setText("Nội dung bình luận: "+commentMessage);
        //event listener
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String commentID = commentArrayList.get(position).getId();
                databaseReference = FirebaseDatabase.getInstance().getReference("comments");
                databaseReference.child(commentID).setValue(null);
                Toast.makeText(context, "Đã xóa binh luận thành công!", Toast.LENGTH_SHORT).show();
                commentArrayList.clear();
                notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return commentArrayList.size();
    }
    private boolean checkUser(int position){
        //init share references
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        String userID = sharedPreferences.getString(KEY_ID,null);
        String userName = sharedPreferences.getString(KEY_NAME,null);
        if (userID == null){
            return false;
        }else if (userID.equals(commentArrayList.get(position).getUserID())){
            return true;
        }
        return false;
    }

    public class CommentListHolder extends RecyclerView.ViewHolder {
        public TextView commentUser, commentDate, commentMessage;
        public Button btnEditComment,btnDeleteComment;
        public CommentListHolder(@NonNull View commentView) {
            super(commentView);
            //textView
            commentUser = commentView.findViewById(R.id.txtCommentUserName);
            commentDate = commentView.findViewById(R.id.txtCommentDate);
            commentMessage = commentView.findViewById(R.id.txtCommentMessage);
            //button
            btnEditComment = commentView.findViewById(R.id.btnEditComment);
            btnDeleteComment = commentView.findViewById(R.id.btnDeleteComment);
        }
    }
}
