package com.example.youtube.model.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.example.youtube.model.AppDB;
import com.example.youtube.model.Comment;
import com.example.youtube.model.daos.CommentDao;
import com.example.youtube.utils.MyApplication;

import java.util.List;

public class CommentsRepository {
    private CommentDao commentDao;
    private MutableLiveData<List<Comment>> commentsByVideoId;

    public CommentsRepository() {
        AppDB db = Room.databaseBuilder(MyApplication.context, AppDB.class, "CommentsDB")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
        commentDao = db.commentDao();
        commentsByVideoId = new MutableLiveData<>();
    }

    public LiveData<List<Comment>> getCommentsByVideoId(int videoId) {
        new Thread(() -> commentsByVideoId.postValue(commentDao.getCommentsByVideoId(videoId))).start();
        return commentsByVideoId;
    }

    public void add(Comment comment) {
        new Thread(() -> commentDao.insert(comment)).start();
    }

    public void delete(Comment comment) {
        new Thread(() -> commentDao.delete(comment)).start();
    }

    public void update(Comment comment) {
        new Thread(() -> commentDao.update(comment)).start();
    }
}
