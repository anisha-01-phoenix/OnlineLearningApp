package com.example.newproject.Videos;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class VideoViewModel extends AndroidViewModel {

    private Repository repository;
    private LiveData<List<VideoModel>> allVideos;


    public VideoViewModel(@NonNull Application application) {
        super(application);

        repository=new Repository(application);
        allVideos=repository.getAllVideos();
    }

    public void insert(VideoModel video){repository.insert(video);}
    public void update(VideoModel video){repository.update(video); }
    public void delete(VideoModel video){repository.delete(video); }


    public LiveData<List<VideoModel>> getAllVideos()
    {
        return allVideos;
    }
}
