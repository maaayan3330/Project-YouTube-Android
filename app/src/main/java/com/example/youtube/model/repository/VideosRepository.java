package com.example.youtube.model.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.example.youtube.api.VideoAPI;
import com.example.youtube.model.AppDB;
import com.example.youtube.model.Video;
import com.example.youtube.model.daos.VideoDao;
import com.example.youtube.utils.MyApplication;

import java.util.LinkedList;
import java.util.List;

public class VideosRepository {
    private final VideoDao videoDao;
    private VideoListData videoListData;
    private final VideoAPI videoAPI;

    public VideosRepository() {
        AppDB db = Room.databaseBuilder(MyApplication.context, AppDB.class, "VideosDB")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
        videoDao = db.videoDao();
        videoListData = new VideoListData();
        videoAPI=new VideoAPI(videoListData,videoDao);
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
                videoListData.postValue(videoDao.index());
            }).start();
        }
    }

    public LiveData<List<Video>> getAll() {
        return videoListData;
    }

    public void add(Video video) {
        new Thread(() -> {
            videoDao.insert(video);
        }).start();
    }

    public void delete(Video video) {
        new Thread(() -> {
            videoDao.delete(video);
        }).start();
    }

    public void update(Video video) {
        new Thread(() -> {
            videoDao.update(video);
        }).start();
    }


    public void reload() {
        new Thread(videoAPI::get).start();
    }
}



