package com.example.test.medicalreport.view.doctor.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test.medicalreport.R;
import com.example.test.medicalreport.view.LoginActivity;
import com.example.test.medicalreport.view.doctor.fragment.RecordDoctorFragment;
import com.example.test.medicalreport.view.doctor.fragment.DoctorHomeFragment;
import com.example.test.medicalreport.view.doctor.fragment.DoctorProfileFragment;
import com.example.test.medicalreport.view.doctor.fragment.DoctorSearchFragment;
import com.example.test.medicalreport.view.nurse.activity.MainActivityNurse;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomnav;
    ImageView save, edit,exit;
    TextView toolbarTitle;
    DoctorProfileFragment doctorProfileFragment=new DoctorProfileFragment();
    public ProgressDialog progressDialog;
    String img;
    SharedPreferences sharedPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomnav = findViewById(R.id.bottom_navigation);
        save = findViewById(R.id.save);
        edit = findViewById(R.id.edit);
        exit=findViewById(R.id.logout);

        toolbarTitle=findViewById(R.id.toolbar_title);

        bottomnav.setOnNavigationItemSelectedListener(navlistener);
        bottomnav.setItemTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DoctorHomeFragment()).commit();

        setVisibility(false, false, true);
        toolbarTitle.setText("Home");
        sharedPref = this.getSharedPreferences("pref", Context.MODE_PRIVATE);

        img = sharedPref.getString("patientPhoto", "");

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    doctorProfileFragment.saveData("");
                       }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doctorProfileFragment.controlInputs(true);
            }
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navlistener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment selectedfrgment = null;
            switch (menuItem.getItemId()) {
                case R.id.nav_home:
                    selectedfrgment = new DoctorHomeFragment();
                    setVisibility(false,false,true);
                    toolbarTitle.setText("Home");

                    break;
                case R.id.nav_profile:
                    selectedfrgment = doctorProfileFragment;
                    setVisibility(true,true,true);
                    toolbarTitle.setText("Profile");
                    break;
                case R.id.nav_search:
                    selectedfrgment = new DoctorSearchFragment();
                    setVisibility(false,false,true);
                    toolbarTitle.setText("Search");
                    break;
                case R.id.nav_add:
                    selectedfrgment = new RecordDoctorFragment();
                    setVisibility(false,false,true);
                    toolbarTitle.setText("Record");
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedfrgment).commit();
            return true;
        }

    };

    private void setVisibility(Boolean saveBtn, Boolean editBtn, Boolean exitBtn) {
        if(saveBtn)
            save.setVisibility(View.VISIBLE);
        else
            save.setVisibility(View.GONE);

        if(editBtn)
            edit.setVisibility(View.VISIBLE);
        else
            edit.setVisibility(View.GONE);


        if(exitBtn)
            exit.setVisibility(View.VISIBLE);
        else
            exit.setVisibility(View.GONE);

    }




}
