package com.example.newproject;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;

import java.util.Date;

@Entity(tableName = "video_table")
public class VideoModel {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String videourl;
    private String date;
    private String description;

    public VideoModel()
    {}

    public VideoModel(String title, String videourl, String date, String description) {
        this.title = title;
        this.videourl = videourl;
        this.date = date;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideourl() {
        return videourl;
    }

    public void setVideourl(String videourl) {
        this.videourl = videourl;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

  /*  @BindingAdapter("android:loadVideo")
    public static void loadVideo(ImageView imageView, String videourl)
    {
            Bitmap thumb = ThumbnailUtils.createVideoThumbnail(videourl, MediaStore.Video.Thumbnails.MICRO_KIND);
            imageView.setImageBitmap(thumb);
    } */
}
