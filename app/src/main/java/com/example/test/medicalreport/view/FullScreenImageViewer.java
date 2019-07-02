package com.example.test.medicalreport.view;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.test.medicalreport.R;
import com.github.chrisbanes.photoview.PhotoView;

public class FullScreenImageViewer extends AppCompatActivity {
    PhotoView image;
    String uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image_viewer);

        image = findViewById(R.id.photo_view);

        Intent intent = getIntent();
        uri = intent.getStringExtra("imagefull");


        Glide.with(this)
                .load(uri)
                .error(R.drawable.error)
                .into(image);



    }


}
