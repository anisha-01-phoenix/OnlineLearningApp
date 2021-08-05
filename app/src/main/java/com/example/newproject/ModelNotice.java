package com.example.newproject;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;

public class ModelNotice {

    String announce;

    public ModelNotice() {
    }

    public ModelNotice(String announce) {
        this.announce = announce;
    }

    public String getAnnounce() {
        return announce;
    }

    public void setAnnounce(String announce) {
        this.announce = announce;
    }
}
