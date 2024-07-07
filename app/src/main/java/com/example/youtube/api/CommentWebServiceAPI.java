package com.example.youtube.api;

import com.example.youtube.model.Comment;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CommentWebServiceAPI {

    // Fetch all comments for a specific video by video ID
    @GET("videos/{pid}/comments")
    Call<List<Comment>> getCommentsByVideoId(@Path("pid") int videoId);

    // Add a new comment
    @POST("videos/{pid}/comments/{id}")
    Call<Comment> addComment(@Path("pid") int videoId, @Path("id") int userId, @Body Comment comment);

    // Edit a comment by comment ID
    @PUT("videos/{pid}/comments/{cid}")
    Call<Comment> editComment(@Path("pid") int videoId, @Path("cid") int commentId, @Body Comment comment);

    // Delete a comment by comment ID
    @DELETE("videos/{pid}/comments/{cid}")
    Call<Void> deleteComment(@Path("pid") int videoId, @Path("cid") int commentId);
}
