package com.example.cinema;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CommentListAdapter  extends RecyclerView.Adapter<CommentListAdapter.CommentListHolder> {
    Context context;
    ArrayList<Comment> commentArrayList;

    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME ="userpref";
    private static final String KEY_ID = "userID";
    private static final String KEY_NAME = "userName";

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
    public void onBindViewHolder(@NonNull CommentListAdapter.CommentListHolder holder, int position) {
        Comment comment = commentArrayList.get(position);
        holder.commentUser.setText(comment.getUserName());
        holder.commentDate.setText(comment.getDate());
        holder.commentMessage.setText(comment.getMessage());
        if (!checkUser(position)){
            holder.btnEditComment.setVisibility(View.GONE);
        }
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
        public Button btnEditComment;
        public CommentListHolder(@NonNull View commentView) {
            super(commentView);
            //textView
            commentUser = commentView.findViewById(R.id.txtCommentUserName);
            commentDate = commentView.findViewById(R.id.txtCommentDate);
            commentMessage = commentView.findViewById(R.id.txtCommentMessage);
            //button
            btnEditComment = commentView.findViewById(R.id.btnEditComment);
        }
    }
}
