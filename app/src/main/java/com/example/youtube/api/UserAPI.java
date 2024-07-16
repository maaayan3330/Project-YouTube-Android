package com.example.youtube.api;

import android.util.Log;
import androidx.lifecycle.MutableLiveData;

import com.example.youtube.api.response.UserResponse;
import com.example.youtube.model.User;
import com.example.youtube.model.daos.UserDao;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserAPI {
    private MutableLiveData<List<User>> userListData;
    private UserDao userDao;
    private Retrofit retrofit;
    private UserWebServiceAPI userWebServiceAPI;
    private static final String TAG = "UserAPI";

    public UserAPI(MutableLiveData<List<User>> userListData, UserDao userDao) {
        this.userListData = userListData;
        this.userDao = userDao;

        retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:80/") // base url
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        userWebServiceAPI = retrofit.create(UserWebServiceAPI.class);
    }

    public void getAllUsers() {
        Call<UsersResponse> call = userWebServiceAPI.getUsers();
        call.enqueue(new Callback<UsersResponse>() {
            @Override
            public void onResponse(Call<UsersResponse> call, Response<UsersResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "Response: " + response.body().toString());
                    List<User> users = response.body().getUsers();
                    new Thread(() -> {
                        userDao.clear();
                        userDao.insertList(users);
                        userListData.postValue(userDao.index());
                    }).start();
                } else {
                    Log.e(TAG, "Response body is null or not successful");
                }
            }

            @Override
            public void onFailure(Call<UsersResponse> call, Throwable t) {
                Log.e(TAG, "Failed to fetch users", t);
            }
        });
    }

    public void getUserById(String id) {
        Call<SingleUserResponse> call = userWebServiceAPI.getUser(id);
        call.enqueue(new Callback<SingleUserResponse>() {
            @Override
            public void onResponse(Call<SingleUserResponse> call, Response<SingleUserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "Response: " + response.body().toString());
                    User user = response.body().getUser();
                    new Thread(() -> {
                        userDao.insert(user);
                    }).start();
                } else {
                    Log.e(TAG, "Response body is null or not successful");
                }
            }

            @Override
            public void onFailure(Call<SingleUserResponse> call, Throwable t) {
                Log.e(TAG, "Failed to fetch one user", t);
            }
        });
    }


    public void delete(User user) {
        Call<Void> call = userWebServiceAPI.deleteUser(user.getRoomId());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                new Thread(() -> {
                    userDao.delete(user);
                    userListData.postValue(userDao.index());
                }).start();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle the failure
            }
        });
    }

    public void update(User user) {
        Call<Void> call = userWebServiceAPI.updateVideo(user.getRoomId(), user);
    }

    // we asked from server throw the retrofit - Call<Void> createUser(@Body User user)
    // - to sent an object to the server
    public void add(User user) {
        Call<Void> call = userWebServiceAPI.createUser(user);
        // we use call back to get the respond from the server
        // good respond - we save the user in room
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    new Thread(() -> {
                        userDao.insert(user);
                        userListData.postValue(userDao.index());
                    }).start();
                } else {
                    Log.e(TAG, "Response body is null or not successful");
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle the failure
                Log.e(TAG, "Failed to add a new user", t);
            }
        });
    }
}
