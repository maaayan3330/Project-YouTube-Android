package com.example.youtube.model.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.youtube.model.Comment;


import java.util.List;

@Dao
public interface CommentDao {

    // Query to get all comments from the table
    @Query("SELECT * FROM comment")
    List<Comment> index();

    // Query to get a comment by its ID
    @Query("SELECT * FROM comment WHERE id = :id")
    Comment get(int id);

    // Insert one or more comments
    @Insert
    void insert(Comment... comments);

    // Update one or more comments
    @Update
    void update(Comment... comments);

    // Delete one or more comments
    @Delete
    void delete(Comment... comments);

    // Delete all entries from the comment table
    @Query("DELETE FROM comment")
    void clear();

    // Insert a list of comments
    @Insert
    void insertList(List<Comment> commentList);
}
