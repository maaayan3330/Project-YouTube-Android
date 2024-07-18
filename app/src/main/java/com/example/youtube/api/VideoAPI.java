package com.example.youtube.api;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.youtube.R;
import com.example.youtube.api.response.videoResponse.VideoResponse;
import com.example.youtube.api.response.videoResponse.VideosResponse;
import com.example.youtube.model.UserManager;
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
    private final VideoDao videoDao;
    private final Retrofit retrofit;
    private final VideoWebServiceAPI videoWebServiceAPI;
    private final UserManager userManager = UserManager.getInstance();

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


    //Method to fetch all videos data from the server.
    public void fetchAllVideos() {
        // Make a network call to fetch videos
        Call<VideosResponse> call = videoWebServiceAPI.getVideos();
        call.enqueue(new Callback<VideosResponse>() {
            @Override
            public void onResponse(Call<VideosResponse> call, Response<VideosResponse> response) {
                List<Video> videos = response.body() != null ? response.body().getVideos() : null;
                adjustVideoUrls(videos); // Adjust the URLs here
                // Run database operations on a separate thread
                new Thread(() -> {
                    // Clear existing video data
                    videoDao.clear();
                    // Insert the fetched videos data
                    videoDao.insertList(videos);
                    // Post the updated video list to LiveData
                    videoListData.postValue(videoDao.index());
                }).start();
            }

            @Override
            public void onFailure(Call<VideosResponse> call, Throwable t) {
                // Handle the failure (e.g., log the error, notify the user)
                Log.e("api", t.getMessage());
            }
        });
    }


    // Fetch videos by user ID
    public LiveData<List<Video>> fetchVideosByUserId(String userId) {
        final MutableLiveData<List<Video>> userVideosliveData = new MutableLiveData<>();
        Call<VideosResponse> call = videoWebServiceAPI.getVideosByUserId(userId);
        call.enqueue(new Callback<VideosResponse>() {
            @Override
            public void onResponse(Call<VideosResponse> call, Response<VideosResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.e("apiVideo", response.message());
                    userVideosliveData.setValue(response.body().getVideos());
                }
            }

            @Override
            public void onFailure(Call<VideosResponse> call, Throwable t) {
                Log.e("apiVideo", t.getMessage());
                userVideosliveData.setValue(null);
            }
        });
        return userVideosliveData;
    }


    // Fetch a specific video by user ID and video ID
    public void getVideo(String userId, String videoId) {
        Call<VideosResponse> call = videoWebServiceAPI.getVideo(userId, videoId);
        call.enqueue(new Callback<VideosResponse>() {
            @Override
            public void onResponse(Call<VideosResponse> call, Response<VideosResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.e("apiVideo", response.message());
//                    videoDao.insert(response.body().getVideo());
                }
            }

            @Override
            public void onFailure(Call<VideosResponse> call, Throwable t) {
                Log.e("apiVideo", t.getMessage());
            }
        });
    }


    // Add a new video
    public void add(Video video) {
        String token = "Bearer " + userManager.getToken();
        Call<VideoResponse> call = videoWebServiceAPI.add(video.getUserId(), video, token);
        call.enqueue(new Callback<VideoResponse>() {
            @Override
            public void onResponse(Call<VideoResponse> call, Response<VideoResponse> response) {
                if (response.isSuccessful()) {
                    Log.e("apiVideo", response.message());
                    Video newVideo= response.body().getVideo();
                    videoDao.insert(video);
                }else {
                    Log.e("apiVideoAdd", "Server error: " + response.code() + " " + response.message());
                }
            }

            @Override
            public void onFailure(Call<VideoResponse> call, Throwable t) {
                Log.e("apiVideo", t.getMessage());
            }
        });
    }


    // Edit a video
    public void update(Video video) {
        String token = "Bearer " + userManager.getToken();
        Call<VideoResponse> call = videoWebServiceAPI.update(video.getUserId(), video.getApiId(), video, token);
        call.enqueue(new Callback<VideoResponse>() {
            @Override
            public void onResponse(Call<VideoResponse> call, Response<VideoResponse> response) {
                if (response.isSuccessful()) {
                    Log.e("apiVideo", response.message());
                    videoDao.update(video);
                }
            }
            @Override
            public void onFailure(Call<VideoResponse> call, Throwable t) {
                Log.e("apiVideo", t.getMessage());
            }
        });
    }


    // Delete a video
    public void delete(Video video) {
        Log.e("apiVideo", video.getUserId()+"/videos/"+video.getApiId());

        Call<Void> call = videoWebServiceAPI.delete(video.getUserId(), video.getApiId(),
                "Bearer " + userManager.getToken());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {

//                    CommentsRepository commentsRepository =new CommentsRepository();
//                    List<Comment> comments = (List<Comment>) commentsRepository.getCommentsByVideoId(video.getApiId());
//                    for (Comment comment :comments){
//                        commentsRepository.delete(comment);
//                    }

                    videoDao.delete(video);
                } else {
                    Log.e("apiVideo", "Server error: " + response.code() + " " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("apiVideo", t.getMessage());
            }
        });
    }

    //adjust the url to fit the localhost
    private void adjustVideoUrls(List<Video> videos) {
        for (Video video : videos) {
            String adjustedVideoUrl = video.getVideoUrl().replace("http://localhost", "http://10.0.2.2");
            video.setVideoUrl(adjustedVideoUrl);
            String adjustedAvatarUrl = video.getAvatar().replace("/localPhotos/", "http://10.0.2.2/localPhotos/");
            video.setAvatar(adjustedAvatarUrl);
        }
    }
}
