package com.example.newproject.Videos;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newproject.MainActivity;
import com.example.newproject.Upload.PlayVideo;
import com.example.newproject.VideoModel;
import com.example.newproject.databinding.VideoLayoutBinding;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;

import java.util.List;

public class VideoAdapter extends FirebaseRecyclerAdapter<VideoModel,VideoAdapter.VideoViewHolder> {

    public VideoAdapter(@NonNull FirebaseRecyclerOptions<VideoModel> options) {
        super(options);
    }



    @Override
    protected void onBindViewHolder(@NonNull VideoViewHolder holder, int position, @NonNull VideoModel model) {

        holder.videoLayoutBinding.setVideomodel(model);
        holder.videoLayoutBinding.executePendingBindings();
        Context context=holder.videoLayoutBinding.getRoot().getContext();
        holder.videoLayoutBinding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, PlayVideo.class);
                intent.putExtra("VideoUrl",model.getVideourl());
                context.startActivity(intent);
            }
        });

    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        VideoLayoutBinding videoLayoutBinding =VideoLayoutBinding.inflate(layoutInflater,parent,false);
        return new VideoViewHolder(videoLayoutBinding);
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder {

        VideoLayoutBinding videoLayoutBinding;

        public VideoViewHolder(@NonNull VideoLayoutBinding videoLayoutBinding) {
            super(videoLayoutBinding.getRoot());
            this.videoLayoutBinding = videoLayoutBinding;

        }

    }
}
