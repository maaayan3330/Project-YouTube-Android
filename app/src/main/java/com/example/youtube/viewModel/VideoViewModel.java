package com.example.youtube.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.youtube.model.Video;
import com.example.youtube.repository.VideosRepository;

import java.util.List;

public class VideoViewModel extends ViewModel {
    private VideosRepository repository;

    private LiveData<List<Video>> videos;

    public VideoViewModel() {
        repository = new VideosRepository();
        videos = repository.getAll();
    }

    public LiveData<List<Video>> get() {
        return videos;
    }

    public void add(Video video) {
        repository.add(video);
    }

    public void delete(Video video) {
        repository.delete(video);
    }

    public void update(Video video) {
        repository.update(video);
    }

    public void reload() {
        repository.reload();
    }

}
