package com.example.youtube.api;

import com.example.youtube.api.response.VideoResponse;
import com.example.youtube.model.Video;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * VideoServiceAPI interface defines the API endpoints for interacting with video-related data on the backend server.
 */
public interface VideoWebServiceAPI {

    /**
     * Fetches a list of all videos.
     *
     * @return A Call object to make the network request.
     */
    @GET("videos")
    Call<VideoResponse> getVideos();

    /**
     * Creates a new video entry in the server.
     *
     * @param video The video object to be created.
     * @return A Call object to make the network request.
     */
    @POST("videos")
    Call<Void> createVideo(@Body Video video);

    /**
     * Deletes a video entry by its ID.
     *
     * @param id The ID of the video to be deleted.
     * @return A Call object to make the network request.
     */
    @DELETE("videos/{id}")
    Call<Void> deleteVideo(@Path("id") int id);

    /**
     * Updates an existing video entry by its ID.
     *
     * @param id The ID of the video to be updated.
     * @param video The updated video object.
     * @return A Call object to make the network request.
     */
    @PUT("videos/{id}")
    Call<Void> updateVideo(@Path("id") int id, @Body Video video);
}
