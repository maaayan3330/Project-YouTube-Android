package com.example.youtube.videoManager;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface VideoDao {

    @Query("SELECT * FROM video")
    List<Video> index();
    @Query("SELECT * FROM video WHERE id = :id")
    Video get(int id);

    @Insert
    void insert(Video... Videos);

    @Update
    void update(Video... Videos);
    @Delete
    void delete(Video... Videos);
}
