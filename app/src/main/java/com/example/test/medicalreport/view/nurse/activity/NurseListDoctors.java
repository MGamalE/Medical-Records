package com.example.test.medicalreport.view.nurse.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.test.medicalreport.R;
import com.example.test.medicalreport.model.Patient;
import com.example.test.medicalreport.model.doctor.Doctor;
import com.example.test.medicalreport.view.nurse.activity.update.NurseUpdateListDoctors;
import com.example.test.medicalreport.view.nurse.adapter.SearchDoctorsListAdapter;
import com.example.test.medicalreport.view.nurse.fragment.NurseAddFragment;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;

public class NurseListDoctors extends AppCompatActivity {

    RecyclerView recyclerView;
    SearchDoctorsListAdapter searchDoctorsListAdapter;
    ArrayList<Doctor> doctors = new ArrayList<>();
    ArrayList<Doctor> finalList = new ArrayList<>();

    private DatabaseReference mDatabaseReference, mDatabaseRef;
    private Uri xRayUri, scanUri;
    private String downloadXRayImageUrl = "", downloadScanImageUrl = "";
    private StorageReference mStorageRef;
    ProgressDialog progressDialog;
    private String currentDate, currentTime, itemRandomKey;
    FirebaseFirestore firebaseFirestore;
    DocumentReference documentReference;
    String patientId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_doctors);
        recyclerView = findViewById(R.id.recyclerView);
        progressDialog = new ProgressDialog(this);


        doctors = getIntent().getParcelableArrayListExtra("doctorList");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        searchDoctorsListAdapter = new SearchDoctorsListAdapter(this, doctors);
        recyclerView.setAdapter(searchDoctorsListAdapter);
        searchDoctorsListAdapter.notifyDataSetChanged();


        mStorageRef = FirebaseStorage.getInstance().getReference().child("Patient Images");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Patients");
        firebaseFirestore = FirebaseFirestore.getInstance();
    }


    private void uploadXRay() {
        xRayUri = Uri.parse(getIntent().getStringExtra("xRayImage"));

        final StorageReference filePath = mStorageRef.child(xRayUri.getLastPathSegment() + itemRandomKey);
        final UploadTask uploadTask = filePath.putFile(xRayUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Exception", e.getMessage());
                progressDialog.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(NurseListDoctors.this, "Data uploaded successfully", Toast.LENGTH_SHORT).show();

                Task<Uri> task = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            Log.e("Exception", task.getException().toString());
                            progressDialog.dismiss();
                        }

                        downloadXRayImageUrl = filePath.getDownloadUrl().toString();

                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(NurseListDoctors.this, "You picked image url successfully", Toast.LENGTH_SHORT).show();
                            downloadXRayImageUrl = task.getResult().toString();

                            uploadScan();

                        }
                    }
                });
            }
        });

    }

    private void uploadScan() {
        scanUri = Uri.parse(getIntent().getStringExtra("scanImage"));

        final StorageReference filePath = mStorageRef.child(scanUri.getLastPathSegment() + itemRandomKey);
        final UploadTask uploadTask = filePath.putFile(scanUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Exception", e.getMessage());
                progressDialog.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(NurseListDoctors.this, "Data Scan uploaded successfully", Toast.LENGTH_SHORT).show();

                Task<Uri> task = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            Log.e("Exception", task.getException().toString());
                            progressDialog.dismiss();
                        }

                        downloadScanImageUrl = filePath.getDownloadUrl().toString();

                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(NurseListDoctors.this, "You picked scan url successfully", Toast.LENGTH_SHORT).show();
                            downloadScanImageUrl = task.getResult().toString();
                            saveDataToDB();

                        }
                    }
                });
            }
        });

    }

    private void saveDataToDB() {


        NurseAddFragment.patient.setTimeStamp(currentTime);
        NurseAddFragment.patient.setPatientXRayImage(downloadXRayImageUrl);
        NurseAddFragment.patient.setPatientScanImage(downloadScanImageUrl);
        NurseAddFragment.patient.setPatientDiagnosises("");
        NurseAddFragment.patient.setPatientDoctorImage1("");
        NurseAddFragment.patient.setPatientDoctorImage2("");
        NurseAddFragment.patient.setPatientPhoto("");
        NurseAddFragment.patient.setPatientPassword("");

        patientId = firebaseFirestore.collection("Patients").document().getId();
        NurseAddFragment.patient.setPatientId(patientId);

        firebaseFirestore.collection("Patients").document(patientId).set(NurseAddFragment.patient)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        SharedPreferences prefs = getSharedPreferences("pref", MODE_PRIVATE);
                        String nurseId = prefs.getString("nurseId", "0");

                        for (int i = 0; i < finalList.size(); i++) {
                            Map<String, Object> map = new HashMap<>();
                            map.put(finalList.get(i).getDoctorId(), true);
                            map.put(nurseId, true);

                            firebaseFirestore.collection("PatientDoctors").document(patientId).set(map, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    Intent intent=new Intent(NurseListDoctors.this, MainActivityNurse.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                    NurseAddFragment.patient=new Patient();
                                    Toast.makeText(NurseListDoctors.this, "Patient Added Successfully", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        progressDialog.dismiss();


                    }
                });


    }

    public void uploadData(View view) {

        finalList = searchDoctorsListAdapter.getArrayList();
        for (int i = 0; i < finalList.size(); i++) {
            Log.e("list:", finalList.get(i).getDoctorName() + "::" + finalList.get(i).getDoctorId());
        }


        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyy");
        currentDate = simpleDateFormat.format(calendar.getTime());

        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("HH:mm:ss a");
        currentTime = simpleTimeFormat.format(calendar.getTime());

        itemRandomKey = currentDate + currentTime;


        progressDialog.setTitle("Uploading Data");
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        uploadXRay();


    }
}
