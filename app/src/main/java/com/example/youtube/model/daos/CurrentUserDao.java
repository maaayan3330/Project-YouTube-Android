package com.example.youtube.model.daos;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.youtube.model.User;
import java.util.List;

@Dao
public interface CurrentUserDao {

    // Set the current user
    @Query("UPDATE user SET isCurrentUser = 1 WHERE username = :username AND password = :password")
    void setCurrentUser(String username, String password);

    // Clear the current user
    @Query("UPDATE user SET isCurrentUser = 0 WHERE isCurrentUser = 1")
    void clearCurrentUser();

    // Get the current user
    @Query("SELECT * FROM user WHERE isCurrentUser = 1 LIMIT 1")
    User getCurrentUser();
}
