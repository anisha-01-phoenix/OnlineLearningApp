package com.example.newproject.Videos;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newproject.Upload.PlayVideo;
import com.example.newproject.VideoModel;
import com.example.newproject.databinding.VideoLayoutBinding;

import java.util.ArrayList;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    List<VideoModel> list=new ArrayList<>();
    private VideoModel videoModel;

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        VideoLayoutBinding videoLayoutBinding =VideoLayoutBinding.inflate(layoutInflater,parent,false);
        return new VideoViewHolder(videoLayoutBinding);

    }

    public void setVideos(List<VideoModel> list)
    {
        this.list=list;
        notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {

        videoModel=list.get(position);
        holder.videoLayoutBinding.setVideomodel(videoModel);
        holder.videoLayoutBinding.executePendingBindings();
        holder.videoLayoutBinding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(),PlayVideo.class);
                intent.putExtra("VideoUrl",videoModel.getVideourl());
                v.getContext().startActivity(intent);
            }
        });
        /*  holder.videoView.setVideoURI(Uri.parse(videoModel.getVideourl()));
        holder.videoView.requestFocus();*/

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder {

        VideoLayoutBinding videoLayoutBinding;
        public VideoViewHolder(@NonNull VideoLayoutBinding videoLayoutBinding) {
            super(videoLayoutBinding.getRoot());
            this.videoLayoutBinding=videoLayoutBinding;

        }
    }
}
