package com.example.youtube.videoList;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtube.R;
import com.example.youtube.UserManager.UserManager;
import com.example.youtube.design.CustomToast;
import com.example.youtube.videoDisplay.VideoDisplayActivity;
import com.example.youtube.videoManager.Video;

import java.util.List;

/**
 * Adapter class for managing the list of videos in a RecyclerView.
 */
public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {
    private List<Video> videoList; // List of videos to display
    private Context context; // Context in which the adapter is used


    /**
     * Constructor for the VideoAdapter.
     *
     * @param videoList The list of videos to display.
     * @param context   The context in which the adapter is being used.
     */
    public VideoAdapter(List<Video> videoList, Context context) {
        this.videoList = videoList;
        this.context = context;
    }

    /**
     * ViewHolder class to hold the views for each video item.
     */
    public static class VideoViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTitle, tvDescription,tvViews,tvLikes,tvAuthor; // TextViews for title, description, views, likes
        public VideoView vvVideo; // VideoView for displaying the video
        public ImageButton ib_collapse;
        public LinearLayout llCollapse;
        public TextView tv_edit, tv_delete;

        /**
         * ViewHolder constructor to initialize the views.
         *
         * @param itemView The view of the individual item.
         */
        public VideoViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            vvVideo = itemView.findViewById(R.id.vvVideo);
            tvViews = itemView.findViewById(R.id.tv_view);
            tvLikes = itemView.findViewById(R.id.tv_like);
            tvAuthor = itemView.findViewById(R.id.tv_author);

            ib_collapse = itemView.findViewById(R.id.ib_collapse);
            llCollapse = itemView.findViewById(R.id.ll_collapse);
            tv_edit = itemView.findViewById(R.id.tv_edit);
            tv_delete = itemView.findViewById(R.id.tv_delete);
        }
    }

    /**
     * Method to create new ViewHolder instances.
     *
     * @param parent   The parent ViewGroup.
     * @param viewType The view type of the new View.
     * @return A new instance of VideoViewHolder.
     */
    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.video_list_item, parent, false);
        return new VideoViewHolder(itemView);
    }

    /**
     * Method to bind data to the views in each ViewHolder.
     *
     * @param holder   The VideoViewHolder to bind data to.
     * @param position The position of the item in the data set.
     */
    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        Video video = videoList.get(position);
        holder.tvTitle.setText(video.getTitle());
        holder.tvDescription.setText("Description: "+ video.getDescription());
        holder.tvViews.setText("Views: " + video.getViews());
        holder.tvLikes.setText("Likes: " + video.getLikes());
        holder.tvAuthor.setText("Author: " + video.getAuthor());

        // Set the video URI
        holder.vvVideo.setVideoURI(Uri.parse(video.getVideoUri()));
        holder.vvVideo.seekTo(50000);

        // Set click listener to play video on click
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, VideoDisplayActivity.class);
            intent.putExtra("extra_video", video);
            context.startActivity(intent);
        });

        //collapse function
        holder.ib_collapse.setOnClickListener(v -> {
            if (holder.llCollapse.getVisibility() == View.GONE) {
                holder.llCollapse.setVisibility(View.VISIBLE);
            } else {
                holder.llCollapse.setVisibility(View.GONE);
            }
        });
            //edit function
        holder.tv_edit.setOnClickListener(v -> {
            if (UserManager.getInstance().getCurrentUser()!= null){
            // Show dialog to edit comment
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setTitle("Edit Title and Description");

            // Create a LinearLayout to hold the EditTexts
            LinearLayout layout = new LinearLayout(v.getContext());
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setPadding(50, 40, 50, 10); // Optional: Add padding for better UI

            // Create EditTexts for title and description
            final EditText inputTitle = new EditText(v.getContext());
            inputTitle.setHint("Title");
            inputTitle.setText(video.getTitle());
            layout.addView(inputTitle);

            final EditText inputDescription = new EditText(v.getContext());
            inputDescription.setHint("Description");
            inputDescription.setText(video.getDescription());
            layout.addView(inputDescription);

            builder.setView(layout);

            builder.setPositiveButton("OK", (dialog, which) -> {
                String newTitle = inputTitle.getText().toString();
                String newDescription = inputDescription.getText().toString();
                video.setTitle(newTitle);
                video.setDescription(newDescription);
                notifyItemChanged(position);
                holder.llCollapse.setVisibility(View.GONE); // Collapse after editing
            });

            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

            builder.show();
        }else {   CustomToast.showToast(context, "Option available just for register users");}
        });
        //delete function
        holder.tv_delete.setOnClickListener(v -> {
            if (UserManager.getInstance().getCurrentUser()!= null) {
            // Remove comment from the list
            videoList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, videoList.size());
        }else {CustomToast.showToast(context, "Option available just for register users");} });
    }



    /**
     * Method to return the total number of items.
     *
     * @return The total number of video items.
     */
    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public void setFilterList(List<Video> filterList){
        this.videoList=filterList;
        notifyDataSetChanged();
    }

}
