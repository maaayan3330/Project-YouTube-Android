package com.example.youtube.api;
import static android.content.ContentValues.TAG;

import android.util.Log;
import androidx.lifecycle.MutableLiveData;

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
                .baseUrl("http://10.0.2.2:80/users/") // הכתובת הבסיסית של השרת שלך
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        userWebServiceAPI = retrofit.create(UserWebServiceAPI.class);
    }

    public void get() {
        Call<UserResponse> call = userWebServiceAPI.getUsers();
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
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
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.e(TAG, "Failed to fetch users", t);
            }
        });
    }

    public void add(User user) {
        Call<Void> call = userWebServiceAPI.createUser(user);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                new Thread(() -> {
                    userDao.insert(user);
                    userListData.postValue(userDao.index());
                }).start();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle the failure
            }
        });
    }

    public void delete(User user) {
        Call<Void> call = userWebServiceAPI.deleteUser(user.getId());
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
        Call<Void> call = userWebServiceAPI.updateVideo(user.getId(), user);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                new Thread(() -> {
                    userDao.update(user);
                    userListData.postValue(userDao.index());
                }).start();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle the failure
            }
        });
    }
}
