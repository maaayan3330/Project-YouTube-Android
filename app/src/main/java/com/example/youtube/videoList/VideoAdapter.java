package com.example.youtube.videoList;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtube.R;
import com.example.youtube.videoDisplay.VideoDisplayActivity;

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

        }
    }

    /**
     * Method to create new ViewHolder instances.
     *
     * @param parent   The parent ViewGroup.
     * @param viewType The view type of the new View.
     * @return A new instance of VideoViewHolder.
     */
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
        holder.tvDescription.setText(video.getDescription());
        holder.tvViews.setText("Views: " + video.getViews());
        holder.tvLikes.setText("Likes: " + video.getLikes());
        holder.tvAuthor.setText("Author: " + video.getAuthor());

        // Get the video resource ID
        int videoResId = ((VideoListActivity) context).getRawResIdByName(video.getVideoResId());
        String videoPath = "android.resource://" + context.getPackageName() + "/" + videoResId;

        // Set the video path and seek to 1 ms to show the first frame as a preview
        holder.vvVideo.setVideoPath(videoPath);
        holder.vvVideo.seekTo(50000);

        // Set click listener to play video on click
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, VideoDisplayActivity.class);
            intent.putExtra("extra_video", video);
            context.startActivity(intent);
        });
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
