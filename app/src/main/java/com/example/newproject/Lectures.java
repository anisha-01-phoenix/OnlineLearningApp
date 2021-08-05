package com.example.newproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.newproject.Upload.NewVideo;
import com.example.newproject.Videos.VideoAdapter;
import com.example.newproject.databinding.FragmentLecturesBinding;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class Lectures extends Fragment {

    VideoAdapter adapter;
    FragmentLecturesBinding lecturesBinding;
    LinearLayoutManager layoutManager;
    DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        lecturesBinding=FragmentLecturesBinding.inflate(getLayoutInflater());

        View view=lecturesBinding.getRoot();
        lecturesBinding.fabVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getActivity(), NewVideo.class);
                startActivity(intent);
            }
        });
        databaseReference=FirebaseDatabase.getInstance().getReference("videos");
        layoutManager=new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        lecturesBinding.rvVideo.setLayoutManager(layoutManager);
        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<VideoModel> options=new FirebaseRecyclerOptions.Builder<VideoModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("videos"),VideoModel.class).build();

        adapter=new VideoAdapter(options);
        adapter.startListening();
        lecturesBinding.rvVideo.setAdapter(adapter);

    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}