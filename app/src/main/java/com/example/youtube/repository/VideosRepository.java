package com.example.youtube.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.example.youtube.model.AppDB;
import com.example.youtube.model.Video;
import com.example.youtube.model.VideoDao;
import com.example.youtube.utils.MyApplication;

import java.util.LinkedList;
import java.util.List;

public class VideosRepository {
    private VideoDao videoDao;
    private VideoListData videoListData;

    public VideosRepository() {
        AppDB db = Room.databaseBuilder(MyApplication.context, AppDB.class, "VideosDB").fallbackToDestructiveMigration()
                .allowMainThreadQueries().build();
        videoDao = db.videoDao();
        videoListData = new VideoListData();
    }

    class VideoListData extends MutableLiveData<List<Video>> {
        public VideoListData() {
            super();
            setValue(new LinkedList<Video>());
        }

        @Override
        protected void onActive() {
            super.onActive();
            new Thread(() -> {
                VideoListData.postValue(videoDao.index());
            }).start();
        }
    }

    public LiveData<List<Video>> getAll() {
        return videoListData;
    }

    public void add(Video video) {
        videoDao.insert(video);
    }

    public void delete(Video video) {
        videoDao.delete(video);
    }

    public void reload() {

    }
}


