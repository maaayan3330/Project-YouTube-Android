package com.example.youtube.videoDisplay;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtube.R;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private List<Comment> commentList;

    public CommentAdapter(List<Comment> commentList) {
        this.commentList = commentList;
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        public TextView authorTextView, commentTextView;

        public CommentViewHolder(View itemView) {
            super(itemView);
            authorTextView = itemView.findViewById(R.id.authorTextView);
            commentTextView = itemView.findViewById(R.id.commentTextView);
        }
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_list_item, parent, false);
        return new CommentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        Comment comment = commentList.get(position);
        holder.authorTextView.setText(comment.getAuthor());
        holder.commentTextView.setText(comment.getComment());
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }


    public void addComment(Comment comment) {
        commentList.add(comment);
        notifyItemInserted(commentList.size() - 1);
    }

}


