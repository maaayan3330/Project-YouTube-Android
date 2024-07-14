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

    public LiveData<List<Comment>> getCommentsByVideoId(String videoId) {
        new Thread(() -> {
            commentAPI.fetchCommentsByVideoId(videoId);
        }).start();
        return commentsByVideoId;
    }

    public void add(Comment comment) {

    }

    public void delete(Comment comment) {
        new Thread(() -> {
            commentDao.delete(comment);
        }).start();
    }

    public void update(Comment comment) {
        new Thread(() -> {
            commentDao.update(comment);
        }).start();
    }
}
