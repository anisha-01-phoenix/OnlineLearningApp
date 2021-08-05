package com.example.newproject.Announcements;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.newproject.MainActivity;
import com.example.newproject.ModelNotice;
import com.example.newproject.databinding.ActivityAnnouncementBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;

public class Announcement extends AppCompatActivity {
    DatabaseReference reference,ref;
    FirebaseUser firebaseUser;
    ActivityAnnouncementBinding announcementBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        announcementBinding=ActivityAnnouncementBinding.inflate(getLayoutInflater());
        setContentView(announcementBinding.getRoot());

        setTitle("New Announcement");
        announcementBinding.newNotice.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String announce=announcementBinding.announce.getText().toString();
                if (announce.isEmpty())
                {
                    announcementBinding.announce.setError("Enter the relevant announcements!");
                    announcementBinding.announce.requestFocus();
                    return;
                }
                else
                {


                    reference=FirebaseDatabase.getInstance().getReference("Announcements");
                    HashMap<String, String> map=new HashMap<>();
                    map.put("announce",announce);
                    reference.child(String.valueOf(System.currentTimeMillis())).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onSuccess(Void aVoid) {
                            ModelNotice notice=new ModelNotice(announce);
                            reference.child(reference.push().getKey()).setValue(notice).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    Toast.makeText(Announcement.this, "Announcement Saved!", Toast.LENGTH_SHORT).show();
                                    notice();
                                    startActivity(new Intent(Announcement.this,MainActivity.class));

                                }
                            });

                        }
                    });

                }
            }
        });
    }

    private void notice() {

        FirebaseMessaging.getInstance().subscribeToTopic("allUsers").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }
}