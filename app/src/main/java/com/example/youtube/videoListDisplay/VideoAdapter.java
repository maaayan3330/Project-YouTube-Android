package com.example.youtube.videoListDisplay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.youtube.R;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {
    private List<Video> videoList;
    private Context context;

    public VideoAdapter(List<Video> videoList, Context context) {
        this.videoList = videoList;
        this.context = context;
    }


    // ViewHolder class to hold the views for each movie item
    public static class VideoViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTitle, tvDescription;
        public VideoView vvVideo;

        // ViewHolder constructor to initialize the views
        public VideoViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            vvVideo = itemView.findViewById(R.id.vvVideo);
        }
    }

    // Method to create new ViewHolder instances
    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.video_list_item, parent, false);
        return new VideoViewHolder(itemView);
    }

    // Method to bind data to the views in each ViewHolder
    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        Video video = videoList.get(position);
        holder.tvTitle.setText(video.getTitle());
        holder.tvDescription.setText(video.getDescription());

        // Get the video resource ID
        int videoResId = ((MainActivity) context).getRawResIdByName(video.getVideoResId());
        String videoPath = "android.resource://" + context.getPackageName() + "/" + videoResId;

        holder.vvVideo.setVideoPath(videoPath);
        holder.vvVideo.seekTo(1); // Seek to 1 ms to show the first frame as a preview


    }

    // Method to return the total number of items
    @Override
    public int getItemCount() {
        return videoList.size();
    }

}