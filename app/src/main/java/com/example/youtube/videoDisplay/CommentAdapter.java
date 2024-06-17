package com.example.youtube.videoDisplay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtube.R;
import com.example.youtube.UserManager.UserManager;
import com.example.youtube.design.CustomToast;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private List<Comment> commentList;
    private Context context;

    public CommentAdapter(List<Comment> commentList, Context context) {
        this.commentList = commentList;
        this.context = context;
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        public TextView authorTextView, commentTextView;
        public ImageButton ib_collapse;
        public LinearLayout llCollapse;
        public TextView textViewEdit, textViewDelete;

        public CommentViewHolder(View itemView) {
            super(itemView);
            authorTextView = itemView.findViewById(R.id.authorTextView);
            commentTextView = itemView.findViewById(R.id.commentTextView);
            ib_collapse = itemView.findViewById(R.id.ib_collapse);
            llCollapse = itemView.findViewById(R.id.ll_collapse);
            textViewEdit = itemView.findViewById(R.id.tv_edit);
            textViewDelete = itemView.findViewById(R.id.tv_delete);
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
        holder.commentTextView.setText(comment.getCommentText());

        holder.ib_collapse.setOnClickListener(v -> {
            if (holder.llCollapse.getVisibility() == View.GONE) {
                holder.llCollapse.setVisibility(View.VISIBLE);
            } else {
                holder.llCollapse.setVisibility(View.GONE);
            }
        });

        holder.textViewEdit.setOnClickListener(v -> {
            if (UserManager.getInstance().getCurrentUser() != null) {
                // Show dialog to edit comment
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Edit Comment");

                final EditText input = new EditText(v.getContext());
                input.setText(comment.getCommentText());
                builder.setView(input);

                builder.setPositiveButton("OK", (dialog, which) -> {
                    String newCommentText = input.getText().toString();
                    comment.setCommentText(newCommentText);
                    notifyItemChanged(position);
                    holder.llCollapse.setVisibility(View.GONE); // Collapse after editing
                });

                builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

                builder.show();
            } else {
                CustomToast.showToast(context, "Option available just for register users");
            }
        });


        holder.textViewDelete.setOnClickListener(v -> {
            if (UserManager.getInstance().getCurrentUser() != null) {

                // Remove comment from the list
                commentList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, commentList.size());
            } else {
                CustomToast.showToast(context, "Option available just for register users");
            }
        });
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


