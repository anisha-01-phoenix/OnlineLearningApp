package com.example.newproject.Announcements;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newproject.ModelNotice;
import com.example.newproject.databinding.AnnounceLayoutBinding;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.ArrayList;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.NoticeViewHolder>
{
    Context context;
    ArrayList<ModelNotice> list;

    public NoticeAdapter(Context context, ArrayList<ModelNotice> list) {
        this.context=context;
        this.list = list;
    }

    @NonNull
    @Override
    public NoticeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        AnnounceLayoutBinding announceLayoutBinding=AnnounceLayoutBinding.inflate(layoutInflater,parent,false);
        return new NoticeViewHolder(announceLayoutBinding);

    }

    @Override
    public void onBindViewHolder(@NonNull NoticeViewHolder holder, int position) {
        holder.layoutBinding.setNotice(list.get(position));
        holder.layoutBinding.executePendingBindings();

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class NoticeViewHolder extends RecyclerView.ViewHolder{
        AnnounceLayoutBinding layoutBinding;
        public NoticeViewHolder(AnnounceLayoutBinding layoutBinding) {
            super(layoutBinding.getRoot());
            this.layoutBinding=layoutBinding;
        }
    }
}

