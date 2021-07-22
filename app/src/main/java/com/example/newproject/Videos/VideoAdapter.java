package com.example.newproject.Videos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newproject.R;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    List<VideoModel> list=new ArrayList<>();
    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.video_layout,parent,false);
        return new VideoViewHolder(view);
    }

    public void setVideos(List<VideoModel> list)
    {
        this.list=list;
        notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {

        VideoModel videoModel=list.get(position);
       /* Calendar calendar=Calendar.getInstance();
        String currentdate = DateFormat.getDateInstance().format(calendar.getTime());*/
        videoModel.setDate(new Date());

        holder.date.setText(String.valueOf(videoModel.getDate()));
        holder.title.setText(videoModel.getTitle());
        holder.description.setText(videoModel.getDescription());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder {
        TextView date, title, description;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            date=itemView.findViewById(R.id.videoDate);
            title=itemView.findViewById(R.id.Title);
            description=itemView.findViewById(R.id.desc);
        }
    }
}
