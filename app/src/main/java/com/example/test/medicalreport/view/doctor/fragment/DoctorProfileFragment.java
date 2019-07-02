package com.example.test.medicalreport.view.doctor.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.test.medicalreport.R;
import com.example.test.medicalreport.model.doctor.Doctor;
import com.example.test.medicalreport.view.doctor.ReportActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class DoctorProfileFragment extends Fragment {
    View view;
    EditText nameInput,emailInput, phoneInput, addressInput, ageInput, passwordInput,hospitalInput,specIput;
    TextView userName;
    CircleImageView profileImage;
    Button showReport;
    SharedPreferences sharedPref;
    String img;
    private FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.activity_profile,container,false);

        initViews();



        getData();
        controlInputs(false);
        return view;
    }

    public void controlInputs(Boolean status){
        nameInput.setEnabled(status);
        emailInput.setEnabled(status);
        phoneInput.setEnabled(status);
        addressInput.setEnabled(status);
        ageInput.setEnabled(status);
        passwordInput.setEnabled(status);
        specIput.setEnabled(status);
        hospitalInput.setEnabled(status);

    }


    private void initViews() {
        nameInput=view.findViewById(R.id.name_input);
        emailInput=view.findViewById(R.id.email_input);
        phoneInput=view.findViewById(R.id.phone_input);
        addressInput=view.findViewById(R.id.address_input);
        ageInput=view.findViewById(R.id.age_input);
        passwordInput=view.findViewById(R.id.password_input);
        userName=view.findViewById(R.id.user_name);
        profileImage=view.findViewById(R.id.profile_image);
        hospitalInput=view.findViewById(R.id.hospital_input);
        specIput=view.findViewById(R.id.spec_input);
        showReport=view.findViewById(R.id.show_report);

        showReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), ReportActivity.class);
                startActivity(intent);
            }
        });
    }

    public void setImage(String url){
        Glide.with(this)
                .load(url)
                .error(R.drawable.error)
                .into(profileImage);
    }

    private void getData(){
         sharedPref = getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);

        nameInput.setText(sharedPref.getString("doctorName",""));
        phoneInput.setText(sharedPref.getString("doctorPhone",""));
        emailInput.setText(sharedPref.getString("doctorEmail",""));
        addressInput.setText(sharedPref.getString("doctorAddress",""));
        ageInput.setText(sharedPref.getString("doctorAge",""));
        passwordInput.setText(sharedPref.getString("doctorPassword",""));
        userName.setText(sharedPref.getString("doctorName",""));
        specIput.setText(sharedPref.getString("doctorSpec",""));
        hospitalInput.setText(sharedPref.getString("doctorHospital",""));



        img=sharedPref.getString("doctorProfile","");

    }



    public void saveData(String url){
        Map<String,Object> maps=new HashMap<>();

        maps.put("doctorName",nameInput.getText().toString());
        maps.put("doctorPhone",phoneInput.getText().toString());
        maps.put("doctorEmail",emailInput.getText().toString());
        maps.put("doctorAddress",addressInput.getText().toString());
        maps.put("doctorAge",ageInput.getText().toString());
        maps.put("doctorPassword",passwordInput.getText().toString());
        maps.put("doctorSpec",specIput.getText().toString());
        maps.put("doctorHospital",hospitalInput.getText().toString());
        maps.put("doctorProfile","");



        firebaseFirestore.collection("Doctors").document(sharedPref.getString("doctorId","0"))
                .update(maps)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getActivity(), "Doctor Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                });



    }


}
