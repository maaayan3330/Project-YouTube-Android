package com.example.youtube.api;

import androidx.lifecycle.MutableLiveData;

import com.example.youtube.R;
import com.example.youtube.model.User;
import com.example.youtube.model.daos.UserDao;
import com.example.youtube.utils.MyApplication;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * UserAPI class responsible for handling network operations related to users.
 * It uses Retrofit to fetch user data from a remote server and Room for local database operations.
 */
public class UserAPI {
    private MutableLiveData<List<User>> userListData;
    private UserDao userDao;
    private Retrofit retrofit;
    private UserWebServiceAPI userWebServiceAPI;

    /**
     * Constructor for UserAPI.
     *
     * @param userListData MutableLiveData object to post the user list data.
     * @param userDao      Data Access Object for user database operations.
     */
    public UserAPI(MutableLiveData<List<User>> userListData, UserDao userDao) {
        this.userListData = userListData;
        this.userDao = userDao;

        // Initialize Retrofit with base URL and Gson converter
        retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.context.getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create an instance of the web service API
        userWebServiceAPI = retrofit.create(UserWebServiceAPI.class);
    }

    /**
     * Method to fetch user data from the remote server.
     * Clears the existing user data in the local database and inserts the fetched data.
     * Updates the MutableLiveData with the new user list.
     */
    public void get() {
        // Make a network call to fetch users
        Call<List<User>> call = userWebServiceAPI.getUsers();
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                // Run database operations on a separate thread
                new Thread(() -> {
                    // Clear existing user data
                    userDao.clear();
                    // Insert the fetched users data
                    userDao.insertList(response.body());
                    // Post the updated user list to LiveData
                    userListData.postValue(userDao.index());
                }).start();
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                // Handle the failure (e.g., log the error, notify the user)
            }
        });
    }
}
