package com.example.newproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.newproject.databinding.ActivityUpdateProfileBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Update_profile extends AppCompatActivity {

    ActivityUpdateProfileBinding profileBinding;
    DatabaseReference reference;
    StorageReference storageReference;
    Uri filepath;
    Bitmap bitmap;
    String UserID="";

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        UserID=user.getUid();
        reference.child(UserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    profileBinding.profileName.setText(snapshot.child("name").getValue().toString());
                    profileBinding.profileUserName.setText(snapshot.child("username").getValue().toString());
                    Picasso.with(getApplicationContext()).load(snapshot.child("imageurl").getValue().toString()).into(profileBinding.profileImg);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Update_profile.this,MainActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profileBinding=ActivityUpdateProfileBinding.inflate(getLayoutInflater());
        setContentView(profileBinding.getRoot());
        getSupportActionBar().hide();
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        UserID=user.getUid();
        reference= FirebaseDatabase.getInstance().getReference().child("Users");
        storageReference= FirebaseStorage.getInstance().getReference();
        profileBinding.editProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileBinding.updatePic.setVisibility(View.VISIBLE);
                Dexter.withContext(getApplicationContext()).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        Intent intent=new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent,"Select image"),5);
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
        });

        profileBinding.updatePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateFirebase();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==5 && resultCode==RESULT_OK)
        {
            filepath=data.getData();
            InputStream inputStream= null;
            try {
                inputStream = getContentResolver().openInputStream(filepath);
                bitmap= BitmapFactory.decodeStream(inputStream);
                profileBinding.profileImg.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateFirebase()
    {
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("File Uploader");
        progressDialog.show();
        final StorageReference uploader=storageReference.child("profileimages/"+"img"+System.currentTimeMillis());
        uploader.putFile(filepath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        uploader.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                final Map<String,Object> map=new HashMap<>();
                                map.put("imageurl",uri.toString());

                                reference.child(UserID).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists())
                                            reference.child(UserID).updateChildren(map);
                                        else
                                            reference.child(UserID).setValue(map);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                                progressDialog.dismiss();
                                profileBinding.updatePic.setVisibility(View.GONE);
                                Toast.makeText(Update_profile.this, "Successfully Updated!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnProgressListener(snapshot -> {
            float percent=(100*snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
            progressDialog.setMessage("Uploaded : "+(int)percent + "%");
    });
    }


}