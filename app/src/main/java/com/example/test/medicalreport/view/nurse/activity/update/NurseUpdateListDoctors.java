package com.example.test.medicalreport.view.nurse.activity.update;

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
import com.example.test.medicalreport.view.nurse.activity.MainActivityNurse;
import com.example.test.medicalreport.view.nurse.adapter.SearchDoctorsListAdapter;
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

public class NurseUpdateListDoctors extends AppCompatActivity {

    RecyclerView recyclerView;
    SearchDoctorsListAdapter searchDoctorsListAdapter;
    ArrayList<Doctor> doctors = new ArrayList<>();
    ArrayList<Doctor> finalList = new ArrayList<>();

    private DatabaseReference  mDatabaseRef;
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
        xRayUri = Uri.parse(EditRecordNurse.patient.getPatientXRayImage());

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
                Toast.makeText(NurseUpdateListDoctors.this, "Data uploaded successfully", Toast.LENGTH_SHORT).show();

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
                            Toast.makeText(NurseUpdateListDoctors.this, "You picked image url successfully", Toast.LENGTH_SHORT).show();
                            downloadXRayImageUrl = task.getResult().toString();
                            EditRecordNurse.patient.setPatientXRayImage(downloadXRayImageUrl);

                            if (!EditRecordNurse.patient.getPatientScanImage().equals("") && EditRecordNurse.patient.getPatientScanImage() != null)
                                saveDataToDB();
                            else
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
                Toast.makeText(NurseUpdateListDoctors.this, "Data Scan uploaded successfully", Toast.LENGTH_SHORT).show();

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
                            Toast.makeText(NurseUpdateListDoctors.this, "You picked scan url successfully", Toast.LENGTH_SHORT).show();
                            downloadScanImageUrl = task.getResult().toString();
                            EditRecordNurse.patient.setPatientScanImage(downloadScanImageUrl);

                            saveDataToDB();

                        }
                    }
                });
            }
        });

    }

    private void saveDataToDB() {
        Log.e("ssn",EditRecordNurse.patient.getPatientSSN());
        Map<String, Object> map = new HashMap<>();
        map.put("patientName", EditRecordNurse.patient.getPatientName());
        map.put("patientAge", EditRecordNurse.patient.getPatientAge());
        map.put("patientHeight", EditRecordNurse.patient.getPatientHeight());
        map.put("patientWeight", EditRecordNurse.patient.getPatientWeight());
        map.put("patientAddress", EditRecordNurse.patient.getPatientAddress());
        map.put("patientSSN", EditRecordNurse.patient.getPatientSSN());
        map.put("patientGender", EditRecordNurse.patient.getPatientGender());
        map.put("patientPressure", EditRecordNurse.patient.getPatientPressure());
        map.put("patientVirus", EditRecordNurse.patient.getPatientVirus());
        map.put("patientDiabetes", EditRecordNurse.patient.getPatientDiabetes());
        map.put("patientXRayImage", EditRecordNurse.patient.getPatientXRayImage());
        map.put("patientScanImage", EditRecordNurse.patient.getPatientScanImage());
        map.put("patientBlood", EditRecordNurse.patient.getPatientBlood());
        map.put("timeStamp", currentTime);
        map.put("doctorAddedData", false);


        firebaseFirestore.collection("Patients")
                .document(EditRecordNurse.patientId)
                .update(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        SharedPreferences prefs = getSharedPreferences("pref", MODE_PRIVATE);
                        String nurseId = prefs.getString("nurseId", "0");

                        for (int i = 0; i < finalList.size(); i++) {
                            Map<String, Object> map = new HashMap<>();
                            map.put(finalList.get(i).getDoctorId(), true);
                            map.put(nurseId, true);

                            firebaseFirestore.collection("PatientDoctors").document(EditRecordNurse.patientId).set(map, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Intent intent=new Intent(NurseUpdateListDoctors.this, MainActivityNurse.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                    EditRecordNurse.patient=new Patient();
                                    Toast.makeText(NurseUpdateListDoctors.this, "Patient Updated Successfully", Toast.LENGTH_SHORT).show();
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


        if (!EditRecordNurse.patient.getPatientXRayImage().equals("") && EditRecordNurse.patient.getPatientXRayImage() != null) {
            if (!EditRecordNurse.patient.getPatientScanImage().equals("") && EditRecordNurse.patient.getPatientScanImage() != null)
                saveDataToDB();
        } else {
            uploadXRay();
        }


    }
}
