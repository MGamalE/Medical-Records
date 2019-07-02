package com.example.test.medicalreport.view.doctor.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.test.medicalreport.R;
import com.example.test.medicalreport.model.Patient;
import com.example.test.medicalreport.view.nurse.activity.MainActivityNurse;
import com.example.test.medicalreport.view.nurse.activity.update.NurseUpdateListDoctors;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddRecordDoctorActivity extends AppCompatActivity {

    private final int GALLERY_XRAY_REQUEST_PERMISSION = 100;
    private final int GALLERY_SCAN_REQUEST_PERMISSION = 101;
    private final int REQUEST_CODE = 101;


    private Uri xRayUri, scanUri;
    private String downloadXRayImageUrl = "", downloadScanImageUrl = "";
    private StorageReference mStorageRef;
    ProgressDialog progressDialog;
    private String currentDate, currentTime, itemRandomKey;
    private String doctorId;

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();


    private boolean image1Selected = false, image2Selected = false;
    private boolean doctorImage1 = false, doctorImage2 = false;
    private int pickImage;

    private String patientId;
    Patient patient;

    EditText editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record_doctor);


        editText = findViewById(R.id.doctor_diag);

        progressDialog = new ProgressDialog(this);

        mStorageRef = FirebaseStorage.getInstance().getReference().child("Patient Images");


        if (getIntent().getParcelableExtra("patientData") != null) {
            patient = getIntent().getParcelableExtra("patientData");
            setData(patient);
            patientId = patient.getPatientId();

            String img1 = patient.getPatientDoctorImage1();
            String img2 = patient.getPatientDoctorImage2();


            if (!img1.equals("")) {
                doctorImage1 = true;

                if (!img2.equals(""))
                    doctorImage2 = true;

            }


        }

        SharedPreferences prefs = this.getSharedPreferences("pref", Context.MODE_PRIVATE);
        doctorId = prefs.getString("doctorId", null);


    }

    private void setData(Patient patient) {

        if (patient.isDoctorAddedData()) {
            editText.setText(patient.getPatientDiagnosises());
        } else {
            editText.setText("Diagnosises");
        }
    }

    private void uploadXRay() {
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
                Toast.makeText(AddRecordDoctorActivity.this, "Data uploaded successfully", Toast.LENGTH_SHORT).show();

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
                            Toast.makeText(AddRecordDoctorActivity.this, "You picked image url successfully", Toast.LENGTH_SHORT).show();
                            downloadXRayImageUrl = task.getResult().toString();

                            uploadScan();

                        }
                    }
                });
            }
        });

    }

    private void uploadScan() {
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
                Toast.makeText(AddRecordDoctorActivity.this, "Data Scan uploaded successfully", Toast.LENGTH_SHORT).show();

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
                            Toast.makeText(AddRecordDoctorActivity.this, "You picked scan url successfully", Toast.LENGTH_SHORT).show();
                            downloadScanImageUrl = task.getResult().toString();
                            saveDataToDB();

                        }
                    }
                });
            }
        });

    }

    private void saveDataToDB() {


        Map<String, Object> map = new HashMap<>();
        map.put("patientDiagnosises", editText.getText().toString());
        map.put("patientDoctorImage1", downloadScanImageUrl);
        map.put("patientDoctorImage2", downloadXRayImageUrl);
        map.put("doctorAddedData", true);


        firebaseFirestore.collection("Patients").document(patientId).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                startActivity(new Intent(AddRecordDoctorActivity.this, MainActivity.class));
                Toast.makeText(AddRecordDoctorActivity.this, "Patient Updated Successfully", Toast.LENGTH_SHORT).show();

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

    private void saveDataToDB2() {
        Map<String, Object> map = new HashMap<>();
        map.put("patientDiagnosises", editText.getText().toString());
        map.put("patientDoctorImage1", patient.getPatientDoctorImage1());
        map.put("patientDoctorImage2", patient.getPatientDoctorImage2());
        map.put("doctorAddedData", true);


        firebaseFirestore.collection("Patients").document(patientId).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Intent intent = new Intent(AddRecordDoctorActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                Toast.makeText(AddRecordDoctorActivity.this, "Patient Updated Successfully", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();

            }
        });


    }

    public void deletePatient(View view) {
        progressDialog.setTitle("Delete Data");
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        Map<String, Object> map = new HashMap<>();
        map.put(doctorId, false);
        firebaseFirestore.collection("PatientDoctors")
                .document(patientId)
                .update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Intent intent = new Intent(AddRecordDoctorActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                Toast.makeText(AddRecordDoctorActivity.this, "Patient Deleted Successfully", Toast.LENGTH_SHORT).show();

                progressDialog.dismiss();
            }
        });

    }

    public void uploadData(View view) {

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


        if (doctorImage1 && doctorImage2) {
            saveDataToDB2();
        } else if (image1Selected && image2Selected) {
            uploadXRay();
        } else {
            Toast.makeText(this, "Select Images", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }


    }


    public void uploadImage2(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
        } else {
            pickXRayImage();
        }
    }

    public void uploadImage1(View view) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
        } else {
            pickScanImage();
        }
    }

    public void pickXRayImage() {
        pickImage = 1;
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select File"), GALLERY_XRAY_REQUEST_PERMISSION);

    }

    public void pickScanImage() {
        pickImage = 0;
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select File"), GALLERY_SCAN_REQUEST_PERMISSION);

    }


    private boolean requestPermission() {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        (Activity) this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_CODE);

                } else {
                    ActivityCompat
                            .requestPermissions(
                                    (Activity) this,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    REQUEST_CODE);
                }
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (pickImage == 1)
                    pickXRayImage();
                else if (pickImage == 0)
                    pickScanImage();

            } else {
                Toast.makeText(this, "Allow Storage Permissionً", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == GALLERY_XRAY_REQUEST_PERMISSION) {

                xRayUri = data.getData();
                image1Selected = true;
            } else if (requestCode == GALLERY_SCAN_REQUEST_PERMISSION) {
                scanUri = data.getData();
                image2Selected = true;
            }

        } else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "Choose Image", Toast.LENGTH_SHORT).show();
        }

    }


}
