package com.example.umesh.uploadtofirebase;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private StorageReference mstorage;
    private static final int one = 2;
    private ProgressDialog mporgress;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button)findViewById(R.id.btn);
        mstorage = FirebaseStorage.getInstance().getReference();
        mporgress = new ProgressDialog(this);
        imageView = (ImageView)findViewById(R.id.imageView);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent intent = new Intent(Intent.ACTION_PICK);     ( pick inages form storage.... )

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); /*intent to fire camera*/

//                intent.setType("image/*");                         (only image type can be picked....)

                startActivityForResult(intent,one);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == one && resultCode == RESULT_OK){

            mporgress.setMessage("uploding.....");
            mporgress.show();

            Uri uri = data.getData();
            StorageReference filePath = mstorage.child("Photos").child(uri.getLastPathSegment());
            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri uri1 = taskSnapshot.getDownloadUrl();
                    Picasso.with(MainActivity.this).load(uri1).into(imageView);
                    Toast.makeText(MainActivity.this,"Upload completed",Toast.LENGTH_LONG).show();
                    mporgress.dismiss();
                }
            });
        }
    }
}
