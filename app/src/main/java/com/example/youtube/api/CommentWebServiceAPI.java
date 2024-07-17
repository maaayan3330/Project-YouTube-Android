package com.example.youtube.api;

import com.example.youtube.api.response.CommentResponse;
import com.example.youtube.api.response.CommentsResponse;
import com.example.youtube.model.Comment;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CommentWebServiceAPI {

    // Fetch all comments for a specific video by video ID
    @GET("videos/{pid}/comments")
    Call<CommentsResponse> getCommentsByVideoId(@Path("pid") String videoId);

    // Add a new comment
    @POST("videos/{pid}/comments/{id}")
    Call<CommentResponse> add(@Path("pid") String videoId, @Path("id") String userId, @Body Comment comment, @Header("Authorization") String token);

    // Edit a comment by comment ID
    @PUT("videos/{pid}/comments/{cid}")
    Call<Comment> update(@Body Comment comment);

    // Delete a comment by comment ID
    @DELETE("videos/{pid}/comments/{cid}")
    Call<Void> delete(@Body Comment comment);
}
