package com.example.youtube.api;

import androidx.lifecycle.MutableLiveData;

import com.example.youtube.R;
import com.example.youtube.model.Comment;
import com.example.youtube.model.daos.CommentDao;
import com.example.youtube.utils.MyApplication;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CommentAPI {
    private MutableLiveData<List<Comment>> commentsLiveData;
    private CommentDao commentDao;
    private Retrofit retrofit;
    private CommentWebServiceAPI commentWebServiceAPI;

    public CommentAPI(MutableLiveData<List<Comment>> commentsLiveData, CommentDao commentDao) {
        this.commentsLiveData = commentsLiveData;
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
    public void fetchCommentsByVideoId(int videoId) {
        Call<List<Comment>> call = commentWebServiceAPI.getCommentsByVideoId(videoId);
        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    new Thread(() -> {
                        commentDao.clear();
                        commentDao.insertList(response.body());
                        commentsLiveData.postValue(commentDao.getCommentsByVideoId(videoId));
                    }).start();
                }
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                // Handle the failure (e.g., log the error, notify the user)
            }
        });
    }

    // Add a new comment
    public void add(Comment comment) {
        Call<Comment> call = commentWebServiceAPI.add(comment);
        call.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                if (response.isSuccessful() && response.body() != null) {
                    new Thread(() -> {
                        commentDao.insert(response.body());
                        commentsLiveData.postValue(commentDao.getCommentsByVideoId(comment.getVideoId()));
                    }).start();
                }
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                // Handle the failure (e.g., log the error, notify the user)
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
                        commentsLiveData.postValue(commentDao.getCommentsByVideoId(comment.getVideoId()));
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
                        commentsLiveData.postValue(commentDao.getCommentsByVideoId(comment.getVideoId()));
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
