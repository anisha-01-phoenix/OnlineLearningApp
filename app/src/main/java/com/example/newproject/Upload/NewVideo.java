package com.example.newproject.Upload;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BindingAdapter;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.newproject.R;
import com.example.newproject.VideoModel;
import com.example.newproject.databinding.ActivityNewVideoBinding;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NewVideo extends AppCompatActivity {

    public static final String EXTRA_TITLE= "EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION= "EXTRA_DESCRIPTION";
    public static final String EXTRA_DATE= "EXTRA_DATE";

    Uri uri;
    MediaController mediaController;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    VideoModel model;
    ActivityNewVideoBinding activityNewVideoBinding;
    UploadTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityNewVideoBinding=ActivityNewVideoBinding.inflate(getLayoutInflater());
        setContentView(activityNewVideoBinding.getRoot());
        storageReference= FirebaseStorage.getInstance().getReference("Video");
        databaseReference= FirebaseDatabase.getInstance().getReference("videos");
        model=new VideoModel();

        activityNewVideoBinding.gotoVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activityNewVideoBinding.uploadVideoOption.isChecked())
                {
                    activityNewVideoBinding.uploadvideoview.setVisibility(View.VISIBLE);
                    UploadVideo();
                }
                else
                    if (activityNewVideoBinding.goLiveOption.isChecked()) {
                    }
            }
        });


        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        setTitle("Add Video");
    }

    private void UploadVideo()
    {
        activityNewVideoBinding.chooseVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseVideo();
            }
        });

        mediaController=new MediaController(this);
        activityNewVideoBinding.videoview.setMediaController(mediaController);
        activityNewVideoBinding.uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uri!=null)
                {
                    activityNewVideoBinding.progressBarUpload.setVisibility(View.VISIBLE);
                    final StorageReference reference=storageReference.child(System.currentTimeMillis() + "." +getExt(uri));
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
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful())
                            {
                                Uri downloadUrl=task.getResult();
                                activityNewVideoBinding.progressBarUpload.setVisibility(View.INVISIBLE);
                                model.setVideourl(downloadUrl.toString());
                                String key=databaseReference.push().getKey();
                                databaseReference.child(key).setValue(model);
                                Toast.makeText(NewVideo.this, "Uploaded!", Toast.LENGTH_SHORT).show();
                                activityNewVideoBinding.uploadvideoview.setVisibility(View.INVISIBLE);

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
        Intent intent=new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==2 && resultCode==RESULT_OK)
        {
            if(data!=null || data.getData()!=null)
            {
                uri=data.getData();
                activityNewVideoBinding.videoview.setVideoURI(uri);
            }

        }
    }

    private String getExt(Uri uri)
    {
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.add_video,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.save :
                save();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void save() {

        String title=activityNewVideoBinding.inputTitle.getEditText().getText().toString().trim();
        String description=activityNewVideoBinding.inputDescription.getEditText().getText().toString().trim();

        if (title.isEmpty())
        {
            activityNewVideoBinding.inputTitle.setError("Enter Title");
            return;
        }
        else if (title.length()>20)
        {
            activityNewVideoBinding.inputTitle.setError("Maximum 20 characters!");
            return;
        }

        if (description.isEmpty())
        {
            activityNewVideoBinding.inputDescription.setError("Enter Description");
            return;
        }

        if (activityNewVideoBinding.options.getCheckedRadioButtonId()==-1)
        {
            Toast.makeText(this, "Select the Video Type", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent=new Intent();
        intent.putExtra(EXTRA_TITLE,title);
        intent.putExtra(EXTRA_DESCRIPTION,description);
        Calendar calendar= Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyy");
        String date=dateFormat.format(calendar.getTime());
        intent.putExtra(EXTRA_DATE,date);

        setResult(RESULT_OK,intent);
        finish();
    }
}
