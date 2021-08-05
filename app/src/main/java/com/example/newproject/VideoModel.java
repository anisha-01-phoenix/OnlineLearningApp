package com.example.newproject;

import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;


public class VideoModel {

    private String title;
    private String description;

    private String date;
    private String videourl;


    public VideoModel(String title, String description, String date, String videourl) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.videourl = videourl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getVideourl() {
        return videourl;
    }

    public void setVideourl(String videourl) {
        this.videourl = videourl;
    }


    @BindingAdapter("android:loadThumbnail")
    public static void loadThumbnail(ImageView view, String videourl) {
        Glide.with(view.getContext()).asBitmap().load(videourl).into(view);
    }
}
