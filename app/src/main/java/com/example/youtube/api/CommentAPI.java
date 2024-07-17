package com.example.youtube.api;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.youtube.R;
import com.example.youtube.api.response.CommentResponse;
import com.example.youtube.api.response.CommentsResponse;
import com.example.youtube.model.Comment;
import com.example.youtube.model.UserManager;
import com.example.youtube.model.daos.CommentDao;
import com.example.youtube.utils.MyApplication;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CommentAPI {
    private final CommentDao commentDao;
    private final Retrofit retrofit;
    private final CommentWebServiceAPI commentWebServiceAPI;
    private UserManager userManager = UserManager.getInstance();

    public CommentAPI( CommentDao commentDao) {

        this.commentDao = commentDao;

        // Initialize Retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.context.getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create an instance of the web service API
        commentWebServiceAPI = retrofit.create(CommentWebServiceAPI.class);
    }

    // Fetch comments for a specific video by video ID
    public void fetchCommentsByVideoId(String videoId) {
        Call<CommentsResponse> call = commentWebServiceAPI.getCommentsByVideoId(videoId);
        call.enqueue(new Callback<CommentsResponse>() {
            @Override
            public void onResponse(Call<CommentsResponse> call, Response<CommentsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    new Thread(() -> {
                        Log.e("apiComment", response.message());
                        commentDao.clear();
                        commentDao.insertList(response.body().getComments());
                    }).start();
                }
            }

            @Override
            public void onFailure(Call<CommentsResponse> call, Throwable t) {
                Log.e("apiComment", t.getMessage());
            }
        });
    }

    // Add a new comment
    public void add(Comment comment) {
        Call<CommentResponse> call = commentWebServiceAPI.add(comment.getVideoId(), comment.getUserId(), comment,"Bearer " + userManager.getToken());
        call.enqueue(new Callback<CommentResponse>() {
            @Override
            public void onResponse(Call<CommentResponse> call, Response<CommentResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    new Thread(() -> {
                        Log.e("apiComment", response.message());
                        commentDao.insert(response.body().getComment());
                    }).start();
                }
            }

            @Override
            public void onFailure(Call<CommentResponse> call, Throwable t) {
                Log.e("apiComment", t.getMessage());
            }
        });
    }

    // Edit a comment
    public void update(Comment comment) {
        Call<Comment> call = commentWebServiceAPI.update(comment);
        call.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                if (response.isSuccessful() && response.body() != null) {
                    new Thread(() -> {
                        commentDao.update(response.body());
                    }).start();
                }
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                // Handle the failure (e.g., log the error, notify the user)
            }
        });
    }

    // Delete a comment
    public void delete(Comment comment) {
        Call<Void> call = commentWebServiceAPI.delete(comment);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    new Thread(() -> {
                        commentDao.delete(comment);
                    }).start();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle the failure (e.g., log the error, notify the user)
            }
        });
    }
}
