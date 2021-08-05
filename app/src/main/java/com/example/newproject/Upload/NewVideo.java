package com.example.newproject.Upload;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.MediaController;
import android.widget.Toast;

import com.example.newproject.Lectures;
import com.example.newproject.R;
import com.example.newproject.VideoModel;
import com.example.newproject.databinding.ActivityNewVideoBinding;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NewVideo extends AppCompatActivity {
    Uri uri;
    MediaController mediaController;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    UploadTask uploadTask;
    ActivityNewVideoBinding activityNewVideoBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityNewVideoBinding = ActivityNewVideoBinding.inflate(getLayoutInflater());
        setContentView(activityNewVideoBinding.getRoot());
        storageReference = FirebaseStorage.getInstance().getReference("Video");
        databaseReference = FirebaseDatabase.getInstance().getReference("videos");
        setTitle("Add Video");
        if (activityNewVideoBinding.enterLink.getEditText().getText().toString().trim().isEmpty()) {

            activityNewVideoBinding.chooseVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chooseVideo();
                }
            });
        }
        else
        {
           uri=Uri.parse(activityNewVideoBinding.enterLink.getEditText().getText().toString().trim());
        }

        activityNewVideoBinding.uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uri != null) {

                        String title = activityNewVideoBinding.inputTitle.getEditText().getText().toString().trim();
                        String description = activityNewVideoBinding.inputDescription.getEditText().getText().toString().trim();
                        if (title.isEmpty()) {
                            activityNewVideoBinding.inputTitle.setError("Enter Title");
                            return;
                        }else if (title.length() > 20) {
                            activityNewVideoBinding.inputTitle.setError("Maximum 20 characters!");
                            return;
                        }

                        if (description.isEmpty()) {
                            activityNewVideoBinding.inputDescription.setError("Enter Description");
                            return;
                        }
                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyy");
                        String date = dateFormat.format(calendar.getTime());

                        activityNewVideoBinding.progressBarUpload.setVisibility(View.VISIBLE);
                        final StorageReference reference = storageReference.child(System.currentTimeMillis() + "." + getExt(uri));
                        uploadTask=reference.putFile(uri);
                        Task<Uri> urltask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful())
                                {
                                    throw task.getException();
                                }
                                return reference.getDownloadUrl();
                            }
                        }) .addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful())
                                {
                                    Uri downloadUrl=task.getResult();
                                    activityNewVideoBinding.progressBarUpload.setVisibility(View.INVISIBLE);
                                    Toast.makeText(NewVideo.this, "Uploaded Successfully!", Toast.LENGTH_SHORT).show();
                                    VideoModel model=new VideoModel(title,description,date,uri.toString());
                                    databaseReference.child(databaseReference.push().getKey()).setValue(model);
                                    startActivity(new Intent(NewVideo.this,Lectures.class));
                                }
                                else
                                    Toast.makeText(NewVideo.this, "Upload Failed!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
        });

    }

    private void chooseVideo() {

        activityNewVideoBinding.videoview.setVisibility(View.VISIBLE);
        mediaController = new MediaController(this);
        activityNewVideoBinding.videoview.setMediaController(mediaController);
        activityNewVideoBinding.videoview.start();
        Dexter.withContext(getApplicationContext())
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 2);
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                permissionToken.continuePermissionRequest();
            }
        }).check();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK) {
            if (data != null || data.getData() != null) {
                uri = data.getData();
                activityNewVideoBinding.videoview.setVideoURI(uri);
            }

        }
    }

    private String getExt(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }

}