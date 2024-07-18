package com.example.youtube.api;

import com.example.youtube.api.response.videoResponse.VideoResponse;
import com.example.youtube.api.response.videoResponse.VideosResponse;
import com.example.youtube.model.Video;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * VideoServiceAPI interface defines the API endpoints for interacting with video-related data on the backend server.
 */
public interface VideoWebServiceAPI {

    // Fetches a list of all videos.
    @GET("videos")
    Call<VideosResponse> getVideos();


    //Fetches a list of videos for a specific user by user ID.
    @GET("videos/{userId}/videos")
    Call<VideosResponse> getVideosByUserId(@Path("userId") String userId);


    //Fetches a specific video by user ID and video ID.
    @GET("videos/{userId}/videos/{videoId}")
    Call<VideoResponse> getVideo(@Path("userId") String userId, @Path("videoId") String videoId);


    // add a new video entry in the server.
    @POST("videos/{userId}/videos")
    Call<VideoResponse> add(@Path("userId") String userId, @Body Video video,
                   @Header("Authorization") String token);


    //Deletes a video entry by its ID.
    @DELETE("videos/{id}/videos/{pid}")
    Call<Void> delete(@Path("id") String userId, @Path("pid") String videoId,
                      @Header("Authorization") String token);


    //Updates an existing video entry by its ID.
    @PUT("videos/{userId}/videos/{videoId}")
    Call<VideoResponse> update(@Path("userId") String userId, @Path("videoId") String videoId,
                               @Body Video video, @Header("Authorization") String token);
}
