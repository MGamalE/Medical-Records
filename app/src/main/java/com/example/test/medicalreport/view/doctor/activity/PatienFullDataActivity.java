package com.example.test.medicalreport.view.doctor.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.test.medicalreport.R;
import com.example.test.medicalreport.model.Patient;

public class PatienFullDataActivity extends AppCompatActivity {


    TextView patientName,patientAge,patientAddress,patientSSN,patientPhone,patientBlood;

    Patient patient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_complete_record);


        patientName=findViewById(R.id.patient_name);
        patientAge=findViewById(R.id.patient_age);
        patientAddress=findViewById(R.id.patient_address);
        patientSSN=findViewById(R.id.patient_ssn);
        patientPhone=findViewById(R.id.patient_phone);
        patientBlood=findViewById(R.id.patient_blood);


        if(getIntent().getParcelableExtra("patientData") != null){
            patient=getIntent().getParcelableExtra("patientData");

            patientName.setText(patient.getPatientName());
            patientAge.setText(patient.getPatientAge());
            patientBlood.setText(patient.getPatientBlood());
            patientSSN.setText(patient.getPatientSSN());
            patientPhone.setText(patient.getPatientPhone());
            patientAddress.setText(patient.getPatientAddress());
        }
    }
}
