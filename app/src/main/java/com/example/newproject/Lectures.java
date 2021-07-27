package com.example.newproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.newproject.Upload.NewVideo;
import com.example.newproject.Videos.VideoAdapter;
import com.example.newproject.Videos.VideoViewModel;
import com.example.newproject.databinding.FragmentLecturesBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import static android.app.Activity.RESULT_OK;


public class Lectures extends Fragment {

    VideoAdapter adapter;
    VideoViewModel videoViewModel;
    DatabaseReference myRef;
    String videourl;
    FragmentLecturesBinding lecturesBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        lecturesBinding=FragmentLecturesBinding.inflate(getLayoutInflater());

        View view=lecturesBinding.getRoot();
        lecturesBinding.fabVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getActivity(), NewVideo.class);
                startActivityForResult(intent,1);
            }
        });
        lecturesBinding.rvVideo.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new VideoAdapter();
        lecturesBinding.rvVideo.setAdapter(adapter);
        videoViewModel= ViewModelProviders.of(getActivity()).get(VideoViewModel.class);
        videoViewModel.getAllVideos().observe(getActivity(), new Observer<List<VideoModel>>() {
            @Override
            public void onChanged(List<VideoModel> videoModels) {
                adapter.setVideos(videoModels);
            }
        });


        return view;


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==1 && resultCode==RESULT_OK)
        {
            assert data != null;
            String title=data.getStringExtra(NewVideo.EXTRA_TITLE);
            String description=data.getStringExtra(NewVideo.EXTRA_DESCRIPTION);
            String date=data.getStringExtra(NewVideo.EXTRA_DATE);

            myRef= FirebaseDatabase.getInstance().getReference("videos");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    videourl=(String)snapshot.child("videos").getValue();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            VideoModel model=new VideoModel(title,videourl,date,description);
            videoViewModel.insert(model);
            Toast.makeText(getActivity(), "Video Saved", Toast.LENGTH_SHORT).show();
        }
    }
}