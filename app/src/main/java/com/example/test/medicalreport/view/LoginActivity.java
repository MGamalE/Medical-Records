package com.example.test.medicalreport.view;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test.medicalreport.R;
import com.example.test.medicalreport.model.Patient;
import com.example.test.medicalreport.model.doctor.Doctor;
import com.example.test.medicalreport.model.nurse.Nurse;
import com.example.test.medicalreport.view.doctor.activity.MainActivity;
import com.example.test.medicalreport.view.nurse.activity.MainActivityNurse;
import com.example.test.medicalreport.view.patient.PatientMainActivity;
import com.example.test.medicalreport.view.patient.PatientSignUpActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    EditText passwordInput, nameInput;
    CardView loginBtn;
    TextView signUpBtn;
    Spinner spinner;
    int userType;
    FirebaseDatabase firebaseDatabase;
    ProgressDialog progressDialog;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    Patient patient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        passwordInput = findViewById(R.id.password_input);
        nameInput = findViewById(R.id.name_input);
        loginBtn = findViewById(R.id.login_btn);
        signUpBtn = findViewById(R.id.btn_signUp);
        spinner = findViewById(R.id.userType_spinner);

        firebaseDatabase = FirebaseDatabase.getInstance();
        progressDialog = new ProgressDialog(this);
        sharedPref = LoginActivity.this.getSharedPreferences("pref", Context.MODE_PRIVATE);
        editor = sharedPref.edit();


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateInputs();

            }
        });
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));

            }
        });

        setSpinnerData();

    }


    private void checkUserType(int userType) {
        switch (userType) {
            case 0:
                loginDoctorUser();
                break;
            case 1:
                loginNurseUser();
                break;
            case 2:
                loginPatientUser();
            default:
                break;
        }
    }

    private void loginPatientUser() {
        progressDialog.setMessage("Loading...");
        progressDialog.setTitle("Login");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        firebaseFirestore.collection("Patients")
                .whereEqualTo("patientName", nameInput.getText().toString())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().toObjects(Nurse.class).size() != 0) {
                                List<Patient> patients = task.getResult().toObjects(Patient.class);
                                patient=new Patient();
                                for (int i = 0; i < patients.size(); i++) {
                                    patient=patients.get(i);
                                }
                                editor.putString("patientId", patient.getPatientId());
                                editor.putString("patientName",patient.getPatientName());
                                editor.putString("patientAge", patient.getPatientAge());
                                editor.putString("patientPhone", patient.getPatientPhone());
                                editor.putString("patientHeight", patient.getPatientHeight());
                                editor.putString("patientWeight",patient.getPatientWeight());
                                editor.putString("patientAddress", patient.getPatientWeight());
                                editor.putString("patientSSN", patient.getPatientSSN());
                                editor.putString("patientGender", patient.getPatientGender());
                                editor.putString("patientPressure", patient.getPatientPressure());
                                editor.putString("patientBlood", patient.getPatientBlood());
                                editor.putString("patientVirus", patient.getPatientVirus());
                                editor.putString("patientDiabetes", patient.getPatientDiabetes());
                                editor.putString("patientDiagnosises", patient.getPatientDiagnosises());
                                editor.putString("patientPassword", patient.getPatientPassword());
                                editor.putString("patientEmail", patient.getPatientEmail());
                                editor.putString("patientUncleName",patient.getPatientUncleName());
                                editor.putString("patientDaughterName", patient.getPatientDaughterName());
                                editor.putString("patientDoctorImage1", patient.getPatientDoctorImage1());
                                editor.putString("patientDoctorImage2", patient.getPatientDoctorImage2());
                                editor.putString("patientXRayImage",patient.getPatientXRayImage());
                                editor.putString("patientScanImage",patient.getPatientScanImage());
                                editor.putString("patientPhoto",patient.getPatientPhoto());
                                editor.commit();

                                if (patient.isDoctorAddedData()) {
                                    if (patient.getPatientName().equals(nameInput.getText().toString())) {
                                        if (patient.getPatientPassword().equals(passwordInput.getText().toString())) {
                                            Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(LoginActivity.this, PatientMainActivity.class);

                                            startActivity(intent);

                                        } else {
                                            Toast.makeText(LoginActivity.this, "Register", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(LoginActivity.this, PatientSignUpActivity.class);

                                            startActivity(intent);

                                        }
                                    }
                                } else {
                                    Toast.makeText(LoginActivity.this, "Doctor should complete patient profile!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, "User Invalid!", Toast.LENGTH_SHORT).show();
                            }

                            progressDialog.dismiss();

                        } else {
                            progressDialog.dismiss();
                            Log.e("error", "Error getting documents.", task.getException());
                        }
                    }
                });


    }

    private void loginNurseUser() {
        progressDialog.setMessage("Loading...");
        progressDialog.setTitle("Login");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        firebaseFirestore.collection("Nurses")
                .whereEqualTo("nurseName", nameInput.getText().toString())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().toObjects(Nurse.class).size() != 0) {
                                List<Nurse> nurse = task.getResult().toObjects(Nurse.class);
                                if (nurse.get(0).getNurseName().equals(nameInput.getText().toString())
                                        && nurse.get(0).getNursePassword().equals(passwordInput.getText().toString())) {
                                    Intent intent = new Intent(LoginActivity.this, MainActivityNurse.class);
                                    editor.putString("nurseId", nurse.get(0).getNurseId());
                                    editor.putString("nurseName", nurse.get(0).getNurseName());
                                    editor.putString("nurseEmail", nurse.get(0).getNurseName());
                                    editor.putString("nursePassword", nurse.get(0).getNursePassword());
                                    editor.putString("nurseHospital", nurse.get(0).getNurseHospital());
                                    editor.putString("nursePhone", nurse.get(0).getNursePhone());
                                    editor.putString("nurseAddress", nurse.get(0).getNurseAddress());
                                    editor.putString("nurseAge", nurse.get(0).getNurseAge());
                                    editor.putString("nursePhoto", nurse.get(0).getNursePhoto());
                                    editor.commit();
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    Toast.makeText(LoginActivity.this, "Hello " + nurse.get(0).getNurseName(), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, "User Invalid!", Toast.LENGTH_SHORT).show();
                            }

                            progressDialog.dismiss();

                        } else {
                            progressDialog.dismiss();
                            Log.e("error", "Error getting documents.", task.getException());
                        }
                    }
                });


    }

    private void loginDoctorUser() {


        progressDialog.setMessage("Loading...");
        progressDialog.setTitle("Login");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        firebaseFirestore.collection("Doctors")
                .whereEqualTo("doctorName", nameInput.getText().toString())
                .whereEqualTo("doctorPassword", passwordInput.getText().toString())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().toObjects(Doctor.class).size() != 0) {
                                List<Doctor> doctors = task.getResult().toObjects(Doctor.class);
                                if (doctors.get(0).getDoctorName().equals(nameInput.getText().toString())
                                        && doctors.get(0).getDoctorPassword().equals(passwordInput.getText().toString())) {
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    editor.putString("doctorId", doctors.get(0).getDoctorId());
                                    editor.putString("doctorName", doctors.get(0).getDoctorName());
                                    editor.putString("doctorEmail", doctors.get(0).getDoctorEmail());
                                    editor.putString("doctorPassword", doctors.get(0).getDoctorPassword());
                                    editor.putString("doctorSpec", doctors.get(0).getDoctorSpec());
                                    editor.putString("doctorHospital", doctors.get(0).getDoctorHospital());
                                    editor.putString("doctorPhone", doctors.get(0).getDoctorPhone());
                                    editor.putString("doctorAddress", doctors.get(0).getDoctorAddress());
                                    editor.putString("doctorAge", doctors.get(0).getDoctorAge());
                                    editor.putString("doctorProfile", doctors.get(0).getDoctorProfile());

                                    editor.commit();
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    Toast.makeText(LoginActivity.this, "Hello " + doctors.get(0).getDoctorName(), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, "User Invalid!", Toast.LENGTH_SHORT).show();
                            }

                            progressDialog.dismiss();

                        } else {
                            progressDialog.dismiss();
                            Log.e("error", "Error getting documents.", task.getException());
                        }
                    }
                });

    }

    private void validateInputs() {
        if (nameInput.getText().toString().equals("") || nameInput.getText().toString().isEmpty()) {
            Toast.makeText(this, "Set User Name!", Toast.LENGTH_SHORT).show();
        } else if (passwordInput.getText().toString().equals("") || passwordInput.getText().toString().isEmpty()) {
            Toast.makeText(this, "Set Password!", Toast.LENGTH_SHORT).show();
        } else {
            checkUserType(userType);
        }

    }

    private void setSpinnerData() {
        ArrayList<String> items = new ArrayList<>();

        items.add("Doctor");
        items.add("Nurse");
        items.add("Patient");


        ArrayAdapter<String> itemsAdapter = new ArrayAdapter(LoginActivity.this,
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
