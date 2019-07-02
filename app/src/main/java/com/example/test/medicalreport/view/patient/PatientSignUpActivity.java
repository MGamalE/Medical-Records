package com.example.test.medicalreport.view.patient;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test.medicalreport.R;
import com.example.test.medicalreport.view.LoginActivity;
import com.example.test.medicalreport.view.doctor.activity.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class PatientSignUpActivity extends AppCompatActivity {

    CardView signUpBtn;
    TextView loginBtn;
    EditText emailInput, unlceInput,daughterInput, passwordInput;
    FirebaseDatabase firebaseDatabase;

    FirebaseFirestore firebaseFirestore;
    DocumentReference documentReference;
    ProgressDialog progressDialog;

    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_signup);

        signUpBtn = findViewById(R.id.register_btn);
        emailInput = findViewById(R.id.email_input);
        unlceInput = findViewById(R.id.uncle_name_input);
        daughterInput = findViewById(R.id.daughter_name_input);
        passwordInput = findViewById(R.id.password_input);

        firebaseDatabase = FirebaseDatabase.getInstance();
        progressDialog = new ProgressDialog(this);

        firebaseFirestore = FirebaseFirestore.getInstance();

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateInputs();
            }
        });

        sharedPref = this.getSharedPreferences("pref", Context.MODE_PRIVATE);

    }




    private void registerNewPatient() {
        String id=sharedPref.getString("patientId","0");
        String name=sharedPref.getString("patientName","name");

        progressDialog.setMessage("Loading...");
        progressDialog.setTitle("Register");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        Map<String,Object> map=new HashMap<>();
        map.put("patientName",name);
        map.put("patientUncleName",unlceInput.getText().toString());
        map.put("patientPassword",passwordInput.getText().toString());
        map.put("patientDaughterName",daughterInput.getText().toString());
        map.put("patientEmail",emailInput.getText().toString());


        firebaseFirestore.collection("Patients")
                .document(id)
                .update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                startActivity(new Intent(PatientSignUpActivity.this, LoginActivity.class));
                Toast.makeText(PatientSignUpActivity.this, "Successfully", Toast.LENGTH_SHORT).show();

                progressDialog.dismiss();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();

            }
        });


    }



    private void validateInputs() {
        if (unlceInput.getText().toString().equals("") || unlceInput.getText().toString().isEmpty()) {
            Toast.makeText(this, "Set User Name!", Toast.LENGTH_SHORT).show();
            return;
        } else if (passwordInput.getText().toString().equals("") || passwordInput.getText().toString().isEmpty()) {
            Toast.makeText(this, "Set Password!", Toast.LENGTH_SHORT).show();
            return;
        } else if (emailInput.getText().toString().equals("") || emailInput.getText().toString().isEmpty()) {
            Toast.makeText(this, "Set Email!", Toast.LENGTH_SHORT).show();
            return;
        } else {
            registerNewPatient();
        }

    }



}
