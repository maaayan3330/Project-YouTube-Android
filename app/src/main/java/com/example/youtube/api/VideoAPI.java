package com.example.youtube.api;

import androidx.lifecycle.MutableLiveData;

import com.example.youtube.R;
import com.example.youtube.model.Video;
import com.example.youtube.model.daos.VideoDao;
import com.example.youtube.utils.MyApplication;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * VideoAPI class responsible for handling network operations related to videos.
 * It uses Retrofit to fetch video data from a remote server and Room for local database operations.
 */
public class VideoAPI {
    private MutableLiveData<List<Video>> videoListData;
    private VideoDao videoDao;
    private Retrofit retrofit;
    private VideoWebServiceAPI videoWebServiceAPI;

    /**
     * Constructor for VideoAPI.
     *
     * @param videoListData MutableLiveData object to post the video list data.
     * @param videoDao      Data Access Object for video database operations.
     */
    public VideoAPI(MutableLiveData<List<Video>> videoListData, VideoDao videoDao) {
        this.videoListData = videoListData;
        this.videoDao = videoDao;

        // Initialize Retrofit with base URL and Gson converter
        retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.context.getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create an instance of the web service API
        videoWebServiceAPI = retrofit.create(VideoWebServiceAPI.class);
    }

    /**
     * Method to fetch video data from the remote server.
     * Clears the existing video data in the local database and inserts the fetched data.
     * Updates the MutableLiveData with the new video list.
     */
    public void get() {
        // Make a network call to fetch videos
        Call<List<Video>> call = videoWebServiceAPI.getVideos();
        call.enqueue(new Callback<List<Video>>() {
            @Override
            public void onResponse(Call<List<Video>> call, Response<List<Video>> response) {
                // Run database operations on a separate thread
                new Thread(() -> {
                    // Clear existing video data
                    videoDao.clear();
                    // Insert the fetched videos data
                    videoDao.insertList(response.body());
                    // Post the updated video list to LiveData
                    videoListData.postValue(videoDao.index());
                }).start();
            }

            @Override
            public void onFailure(Call<List<Video>> call, Throwable t) {
                // Handle the failure (e.g., log the error, notify the user)
            }
        });
    }

    public void add(Video video){

    }
    public void delete(Video video){

    }
}
