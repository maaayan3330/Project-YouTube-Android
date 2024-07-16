package com.example.youtube.api;

import com.example.youtube.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * UserServiceAPI interface defines the API endpoints for interacting with user-related data on the backend server.
 */
public interface UserWebServiceAPI {

    /**
     * Fetches a list of all users.
     *
     * @return A Call object to make the network request.
     */
    @GET("users/fetchUsers")
    Call<UsersResponse> getUsers();

    @GET("api/users/{id}")
    Call<SingleUserResponse> getUser(@Path("id") String id);

    /**
     * Creates a new user entry in the server.
     *
     * @param user The user object to be created.
     * @return A Call object to make the network request.
     */
    @POST("api/users")
    Call<Void> createUser(@Body User user);

    /**
     * Deletes a user entry by its ID.
     *
     * @param id The ID of the user to be deleted.
     * @return A Call object to make the network request.
     */
    @DELETE("users/{id}")
    Call<Void> deleteUser(@Path("id") int id);

    /**
     * Updates an existing user entry by its ID.
     *
     * @param id The ID of the user to be updated.
     * @param user The updated user object.
     * @return A Call object to make the network request.
     */
    @PUT("users/{id}")
    Call<Void> updateVideo(@Path("id") int id, @Body User user);
}
