package com.example.mysocialapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mysocialapp.model.filemodel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class addimage extends AppCompatActivity {
    ImageView imageView, camera;
    Button upload;
    Uri imageuri;
    EditText imgtitle;
    TextView info;

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addimage);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.add);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.add:
                        return true;

                    case R.id.dashboard:
                        startActivity(new Intent(getApplicationContext(), dashboard.class));
                        finish();
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), update_profile.class));
                        finish();
                        overridePendingTransition(0,0);
                        return true;
                }

                return false;
            }
        });


        imgtitle = findViewById(R.id.imagetitle);
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("myimages");

        imageView = findViewById(R.id.imageView);
        upload = findViewById(R.id.upload);
        camera = findViewById(R.id.camera);
        info = findViewById(R.id.instruction);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                camera.setVisibility(View.INVISIBLE);
                info.setVisibility(View.INVISIBLE);

                Dexter.withContext(getApplicationContext())
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                Intent intent=new Intent();
                                intent.setType("image/*");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(intent,101);
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


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processimageuploading();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==101 && resultCode==RESULT_OK)
        {
            imageuri=data.getData();
            Picasso.get().load(imageuri).into(imageView);

        }

    }


    public void processimageuploading()
    {
        final ProgressDialog pd=new ProgressDialog(this);
        pd.setTitle("Image Uploading...");
        pd.show();

        final StorageReference uploader=storageReference.child("myimages/"+System.currentTimeMillis()+".jpg");
        uploader.putFile(imageuri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        uploader.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Calendar datevalue=Calendar.getInstance();

                                SimpleDateFormat dateFormat=new SimpleDateFormat("EEE, d MMM yyyy");
                                String cdate=dateFormat.format(datevalue.getTime());

                                SimpleDateFormat timeFormat=new SimpleDateFormat("HH:mm:ss");
                                String ctime=timeFormat.format(datevalue.getTime());

                                filemodel obj=new filemodel(imgtitle.getText().toString(),uri.toString(), cdate, ctime);
                                databaseReference.child(databaseReference.push().getKey()).setValue(obj)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                pd.dismiss();
                                                Toast.makeText(getApplicationContext(),"Successfully uploaded",Toast.LENGTH_LONG).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                pd.dismiss();
                                                Toast.makeText(getApplicationContext(), "Failed to upload", Toast.LENGTH_LONG).show();
                                            }
                                        });

                            }
                        });
                    }
                })
                .addOnProgressListener(snapshot -> {
                    float per=(100*snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                    pd.setMessage("Uploaded :"+(int)per+"%");
                });

    }
}