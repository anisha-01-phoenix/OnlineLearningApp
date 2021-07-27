package com.example.newproject.Videos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.newproject.VideoModel;

import java.util.List;

@Dao
public interface VideoDao {

    @Insert
    void insert(VideoModel video);

    @Update
    void update(VideoModel video);

    @Delete
    void delete(VideoModel video);


    @Query("SELECT * FROM video_table")
    LiveData<List<VideoModel>> getAllVideos();
}
