package com.example.newproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.newproject.Announcements.Announcement;
import com.example.newproject.Announcements.NoticeAdapter;
import com.example.newproject.databinding.FragmentHomeBinding;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.common.net.InternetDomainName;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class Home extends Fragment {

    FragmentHomeBinding homeBinding;
    NoticeAdapter adapter;
    GridLayoutManager layoutManager;
    DatabaseReference reference;
    ArrayList<ModelNotice> list;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        homeBinding= FragmentHomeBinding.inflate(getLayoutInflater());

        View view=homeBinding.getRoot();
        homeBinding.fabAnnounce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Announcement.class));
            }
        });

       layoutManager=new GridLayoutManager(getActivity(),2);
       reference=FirebaseDatabase.getInstance().getReference("Announcements");
       layoutManager.setReverseLayout(true);

        homeBinding.rvAnnouncements.setLayoutManager(layoutManager);
        list=new ArrayList<>();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    ModelNotice modelNotice=dataSnapshot.getValue(ModelNotice.class);
                    assert  modelNotice!=null;
                    list.add(modelNotice);
                }

                adapter=new NoticeAdapter(getContext(),list);
                homeBinding.rvAnnouncements.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Users");
        ref.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    String title="Welcome "+snapshot.child("name").getValue().toString()+" !";
                    homeBinding.welcome.setText(title);
                    Picasso.with(homeBinding.circleImageView.getContext()).load(snapshot.child("imageurl").getValue().toString()).into(homeBinding.circleImageView);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}