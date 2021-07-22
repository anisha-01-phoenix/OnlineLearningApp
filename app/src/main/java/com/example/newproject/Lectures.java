package com.example.newproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.newproject.Videos.VideoAdapter;
import com.example.newproject.Videos.VideoModel;
import com.example.newproject.Videos.VideoViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;


public class Lectures extends Fragment {

    RecyclerView recyclerView;
    VideoAdapter adapter;
    VideoViewModel videoViewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_lectures, container, false);
        FloatingActionButton fab=view.findViewById(R.id.fab_Video);
        recyclerView=view.findViewById(R.id.rv_video);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new VideoAdapter();
        recyclerView.setAdapter(adapter);
        videoViewModel= ViewModelProviders.of(getActivity()).get(VideoViewModel.class);
        videoViewModel.getAllVideos().observe(getActivity(), new Observer<List<VideoModel>>() {
            @Override
            public void onChanged(List<VideoModel> videoModels) {
                adapter.setVideos(videoModels);
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //upload video
            }
        });

        return view;


    }
}