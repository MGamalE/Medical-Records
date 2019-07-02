package com.example.test.medicalreport.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test.medicalreport.R;
import com.example.test.medicalreport.model.doctor.Doctor;
import com.example.test.medicalreport.model.nurse.Nurse;
import com.example.test.medicalreport.view.doctor.activity.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SignUpActivity extends AppCompatActivity {

    CardView signUpBtn;
    TextView loginBtn;
    EditText emailInput, nameInput, passwordInput;
    Spinner spinner;
    int userType;

    FirebaseDatabase firebaseDatabase;

    FirebaseFirestore firebaseFirestore;
    DocumentReference documentReference;
    ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signUpBtn = findViewById(R.id.register_btn);
        loginBtn = findViewById(R.id.btn_loginUp);
        emailInput = findViewById(R.id.email_input);
        nameInput = findViewById(R.id.name_input);
        passwordInput = findViewById(R.id.password_input);
        spinner = findViewById(R.id.userType_spinner);

        firebaseDatabase = FirebaseDatabase.getInstance();
        progressDialog = new ProgressDialog(this);

        firebaseFirestore = FirebaseFirestore.getInstance();

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateInputs();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                finish();
            }
        });

        setSpinnerData();
    }


    private void registerNewDoctor() {
        final Doctor doctor = new Doctor("",nameInput.getText().toString(),
                emailInput.getText().toString(), passwordInput.getText().toString(), "","",
                "","","",1);
        progressDialog.setMessage("Loading...");
        progressDialog.setTitle("Register");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        CollectionReference usersRef = firebaseFirestore.collection("Doctors");
        Query query = usersRef.whereEqualTo("doctorName", nameInput.getText().toString());
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot documentSnapshot : task.getResult()){
                        String user = documentSnapshot.getString("doctorName");
                        if(user.equals(nameInput.getText().toString())){
                            Toast.makeText(SignUpActivity.this, "Username already exists. Please try other username.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    progressDialog.dismiss();

                }
                if(task.getResult().size() == 0 ){
                    Log.d("error", "User not Exists");
                    progressDialog.dismiss();
                    String doctorId = firebaseFirestore.collection("Doctors").document().getId();
                    doctor.setDoctorId(doctorId);
                    firebaseFirestore.collection("Doctors").document(doctorId).set(doctor);
                            onBackPressed();
                }
            }
        });




    }


    private void registerNewNurse() {
        final Nurse nurse = new Nurse("",nameInput.getText().toString(),0, passwordInput.getText().toString(),
                emailInput.getText().toString(), "","","","");


        progressDialog.setMessage("Loading...");
        progressDialog.setTitle("Register");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();



        CollectionReference usersRef = firebaseFirestore.collection("Nurses");
        Query query = usersRef.whereEqualTo("nurseName", nameInput.getText().toString());
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot documentSnapshot : task.getResult()){
                        String user = documentSnapshot.getString("nurseName");
                        if(user.equals(nameInput.getText().toString())){
                            Toast.makeText(SignUpActivity.this, "Username already exists. Please try other username.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    progressDialog.dismiss();

                }
                if(task.getResult().size() == 0 ){
                    Log.d("error", "User not Exists");
                    progressDialog.dismiss();
                    String nurseId = firebaseFirestore.collection("Nurses").document().getId();
                    nurse.setNurseId(nurseId);
                    firebaseFirestore.collection("Nurses").document(nurseId).set(nurse);
                    onBackPressed();
                }
            }
        });








    }


    private void checkUserType(int userType) {
        switch (userType) {
            case 0:
                registerNewDoctor();
                break;

            case 1:
                registerNewNurse();
                break;
            default:
                break;
        }
    }

    private void validateInputs() {
        if (nameInput.getText().toString().equals("") || nameInput.getText().toString().isEmpty()) {
            Toast.makeText(this, "Set User Name!", Toast.LENGTH_SHORT).show();
            return;
        } else if (passwordInput.getText().toString().equals("") || passwordInput.getText().toString().isEmpty()) {
            Toast.makeText(this, "Set Password!", Toast.LENGTH_SHORT).show();
            return;
        } else if (emailInput.getText().toString().equals("") || emailInput.getText().toString().isEmpty()) {
            Toast.makeText(this, "Set Email!", Toast.LENGTH_SHORT).show();
            return;
        } else {
            checkUserType(userType);
        }

    }

    private void setSpinnerData() {
        ArrayList<String> items = new ArrayList<>();

        items.add("Doctor");
        items.add("Nurse");


        ArrayAdapter<String> itemsAdapter = new ArrayAdapter(SignUpActivity.this,
                android.R.layout.simple_spinner_dropdown_item, items);

        spinner.setAdapter(itemsAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                userType = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


}
