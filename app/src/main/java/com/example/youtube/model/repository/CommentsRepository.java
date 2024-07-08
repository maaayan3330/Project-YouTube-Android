package com.example.youtube.model.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.example.youtube.api.CommentAPI;
import com.example.youtube.model.AppDB;
import com.example.youtube.model.Comment;
import com.example.youtube.model.daos.CommentDao;
import com.example.youtube.utils.MyApplication;

import java.util.List;

public class CommentsRepository {
    private CommentDao commentDao;
    private MutableLiveData<List<Comment>> commentsByVideoId;
    private CommentAPI commentAPI;

    public CommentsRepository() {
        AppDB db = Room.databaseBuilder(MyApplication.context, AppDB.class, "CommentsDB")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
        commentDao = db.commentDao();
        commentsByVideoId = new MutableLiveData<>();
        commentAPI = new CommentAPI(commentsByVideoId, commentDao);
    }

    public LiveData<List<Comment>> getCommentsByVideoId(int videoId) {
        commentAPI.fetchCommentsByVideoId(videoId);
        return commentsByVideoId;
    }

    public void add(Comment comment) {
        commentAPI.add(comment);
    }

    public void delete(Comment comment) {
        commentAPI.delete(comment);
    }

    public void update(Comment comment) {
        commentAPI.update(comment);
    }
}
