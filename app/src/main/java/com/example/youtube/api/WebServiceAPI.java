package com.example.youtube.api;

import com.example.youtube.model.Video;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface WebServiceAPI {
    @GET("videos")
    Call<List<Video>> getVideos();

    @POST("videos")
    Call<Void> createVideo(@Body Video post);

    @DELETE("videos/{id}")
    Call<Void> deleteVideo(@Path("id") int id);
}
