package com.example.test.medicalreport.view.patient;

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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.test.medicalreport.R;
import com.example.test.medicalreport.model.Patient;
import com.example.test.medicalreport.view.FullScreenImageViewer;
import com.example.test.medicalreport.view.LoginActivity;
import com.example.test.medicalreport.view.doctor.activity.AddRecordDoctorActivity;
import com.example.test.medicalreport.view.doctor.activity.MainActivity;
import com.example.test.medicalreport.view.doctor.activity.PatientDetailsActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class PatientMainActivity extends AppCompatActivity {

    Toolbar toolbar;
    EditText nameInput, emailInput, phoneInput, addressInput, ageInput, passwordInput;
    TextView userName;
    CircleImageView profileImage;
    EditText patientNameInput, patientUncleName, patientDaughterName, patientHeightInput, patientWeightInput, patientDiagnosis, patientSSNInput, patientAddressInput;
    EditText patientPressureInput, patientDiabetesInput, patientGenderInput, patientBloodInput, patientVirusInput, patientPhoneInput;
    ImageView image1, image2, image3, image4;
    ImageView save, edit, exit;

    private final int REQUEST_CODE = 101;
    private final int GALLERY_SCAN_REQUEST_PERMISSION = 101;
    private Uri scanUri;
    private StorageReference mStorageRef;
    private String downloadScanImageUrl = "";
    String img;
    Boolean imageSelected = false;


    SharedPreferences sharedPref;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_main);
        setSupportActionBar(toolbar);
        sharedPref = this.getSharedPreferences("pref", Context.MODE_PRIVATE);
        mStorageRef = FirebaseStorage.getInstance().getReference().child("Patient Images");
        progressDialog = new ProgressDialog(this);


        initViews();
        setVisibility(false, true, true);
        ControlInputs(false);
        ControlInputs2(false);
    }


    private void initViews() {
        nameInput = findViewById(R.id.name_input);
        emailInput = findViewById(R.id.email_input);
        phoneInput = findViewById(R.id.phone_input);
        addressInput = findViewById(R.id.address_input);
        ageInput = findViewById(R.id.age_input);
        passwordInput = findViewById(R.id.password_input);
        userName = findViewById(R.id.user_name);
        profileImage = findViewById(R.id.profile_image);

        patientDaughterName = findViewById(R.id.daughter_name_input);
        patientUncleName = findViewById(R.id.uncle_name_input);
        patientHeightInput = findViewById(R.id.patient_height_input);
        patientWeightInput = findViewById(R.id.patient_weight_input);
        patientPressureInput = findViewById(R.id.patient_pressure_input);
        patientDiabetesInput = findViewById(R.id.patient_diabetes_input);
        patientBloodInput = findViewById(R.id.patient_blood_input);
        patientSSNInput = findViewById(R.id.patient_ssn_input);
        patientVirusInput = findViewById(R.id.patient_virus_input);
        patientGenderInput = findViewById(R.id.patient_gender_input);
        patientDiagnosis = findViewById(R.id.doctor_diag);

        image1 = findViewById(R.id.image_1);
        image2 = findViewById(R.id.image_2);
        image3 = findViewById(R.id.image_3);
        image4 = findViewById(R.id.image_4);

        save = findViewById(R.id.save);
        edit = findViewById(R.id.edit);
        exit = findViewById(R.id.logout);
        toolbar = findViewById(R.id.toolbar);


        setIntentData();


        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientMainActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setVisibility(false, true, true);

                progressDialog.setMessage("Waiting...");
                progressDialog.setTitle("Update Profile");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                if (img.equals("")) {
                    Toast.makeText(PatientMainActivity.this, "Select Profile Photo", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                } else if (imageSelected) {
                    uploadScan();
                } else {
                    saveToDb();
                }

            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setVisibility(true, false, true);
                ControlInputs2(true);
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(PatientMainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermission();
                } else {
                    pickScanImage();
                }
            }
        });

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

    public void pickScanImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select File"), GALLERY_SCAN_REQUEST_PERMISSION);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickScanImage();

            } else {
                Toast.makeText(this, "Allow Storage Permissionً", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == GALLERY_SCAN_REQUEST_PERMISSION) {
                scanUri = data.getData();
                imageSelected = true;
                img = "data";
                Glide.with(this)
                        .load(scanUri.toString())
                        .error(R.drawable.error)
                        .into(profileImage);
            }

        } else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "Choose Image", Toast.LENGTH_SHORT).show();
        }

    }

    private void uploadScan() {
        final StorageReference filePath = mStorageRef.child(scanUri.getLastPathSegment());
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
                Toast.makeText(PatientMainActivity.this, "profile uploaded successfully", Toast.LENGTH_SHORT).show();

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
                            downloadScanImageUrl = task.getResult().toString();
                            saveToDb();
                            imageSelected=false;

                        }
                    }
                });
            }
        });

    }

    private void saveToDb() {
        Map<String, Object> map = new HashMap<>();


        map.put("patientName", nameInput.getText().toString());
        map.put("patientAge", ageInput.getText().toString());
//        map.put("patientHeight",patientHeightInput.getText().toString());
//        map.put("patientWeight",patientWeightInput.getText().toString());
        map.put("patientAddress", addressInput.getText().toString());
        map.put("patientSSN", patientSSNInput.getText().toString());
//        map.put("patientGender",patientGenderInput.getText().toString());
//        map.put("patientPressure",patientPressureInput.getText().toString());
//        map.put("patientBlood",patientBloodInput.getText().toString());
//        map.put("patientVirus",patientVirusInput.getText().toString());
//        map.put("patientDiabetes",patientDiabetesInput.getText().toString());
//        map.put("patientDiagnosises",patientDiagnosis.getText().toString());
//        map.put("patientPassword",patientPressureInput.getText().toString());
        map.put("patientEmail", emailInput.getText().toString());
        map.put("patientUncleName", patientUncleName.getText().toString());
        map.put("patientDaughterName", patientDaughterName.getText().toString());
        map.put("patientPhone", phoneInput.getText().toString());

        if (imageSelected)
            map.put("patientPhoto", downloadScanImageUrl);


        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        String id = sharedPref.getString("patientId", "0");

        firebaseFirestore.collection("Patients")
                .document(id)
                .update(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        ControlInputs2(false);
                        progressDialog.dismiss();
                        Toast.makeText(PatientMainActivity.this, "Patient Profile Updated Successfully!", Toast.LENGTH_SHORT).show();
                    }
                });


    }

    private void setIntentData() {
        userName.setText(sharedPref.getString("patientName", ""));
        nameInput.setText(sharedPref.getString("patientName", ""));
        ageInput.setText(sharedPref.getString("patientAge", ""));
        phoneInput.setText(sharedPref.getString("patientPhone", ""));
        patientHeightInput.setText(sharedPref.getString("patientHeight", ""));
        patientWeightInput.setText(sharedPref.getString("patientWeight", ""));
        addressInput.setText(sharedPref.getString("patientAddress", ""));
        patientSSNInput.setText(sharedPref.getString("patientSSN", ""));
        patientGenderInput.setText(sharedPref.getString("patientGender", ""));
        patientPressureInput.setText(sharedPref.getString("patientPressure", ""));
        patientBloodInput.setText(sharedPref.getString("patientBlood", ""));
        patientVirusInput.setText(sharedPref.getString("patientVirus", ""));
        patientDiabetesInput.setText(sharedPref.getString("patientDiabetes", ""));
        patientDiagnosis.setText(sharedPref.getString("patientDiagnosises", ""));
        passwordInput.setText(sharedPref.getString("patientPassword", ""));
        emailInput.setText(sharedPref.getString("patientEmail", ""));
        patientUncleName.setText(sharedPref.getString("patientUncleName", ""));
        patientDaughterName.setText(sharedPref.getString("patientDaughterName", ""));
        img = sharedPref.getString("patientPhoto", "");


        Patient patient = new Patient();
        patient.setPatientDoctorImage1(sharedPref.getString("patientDoctorImage1", ""));
        patient.setPatientDoctorImage2(sharedPref.getString("patientDoctorImage2", ""));
        patient.setPatientXRayImage(sharedPref.getString("patientXRayImage", ""));
        patient.setPatientScanImage(sharedPref.getString("patientScanImage", ""));


        setImages(patient);
    }

    private void setImages(final Patient patient) {
        Glide.with(this)
                .load(patient.getPatientScanImage())
                .error(R.drawable.error)
                .into(image1);

        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientMainActivity.this, FullScreenImageViewer.class);
                intent.putExtra("imagefull", patient.getPatientScanImage());
                startActivity(intent);
            }
        });

        Glide.with(this)
                .load(patient.getPatientXRayImage())
                .error(R.drawable.error)
                .into(image2);

        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientMainActivity.this, FullScreenImageViewer.class);
                intent.putExtra("imagefull", patient.getPatientXRayImage());
                startActivity(intent);
            }
        });

        Glide.with(this)
                .load(patient.getPatientDoctorImage1())
                .error(R.drawable.error)
                .into(image3);

        image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientMainActivity.this, FullScreenImageViewer.class);
                intent.putExtra("imagefull", patient.getPatientDoctorImage1());
                startActivity(intent);
            }
        });

        Glide.with(this)
                .load(patient.getPatientDoctorImage2())
                .error(R.drawable.error)
                .into(image4);

        image4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientMainActivity.this, FullScreenImageViewer.class);
                intent.putExtra("imagefull", patient.getPatientDoctorImage2());
                startActivity(intent);
            }
        });

        if (!img.equals("")) {
            Glide.with(this)
                    .load(img)
                    .error(R.drawable.error)
                    .into(profileImage);
        }

    }

    private void setVisibility(Boolean saveBtn, Boolean editBtn, Boolean exitBtn) {
        if (saveBtn)
            save.setVisibility(View.VISIBLE);
        else
            save.setVisibility(View.GONE);

        if (editBtn)
            edit.setVisibility(View.VISIBLE);
        else
            edit.setVisibility(View.GONE);


        if (exitBtn)
            exit.setVisibility(View.VISIBLE);
        else
            exit.setVisibility(View.GONE);

    }


    private void ControlInputs(boolean b) {
        patientHeightInput.setEnabled(b);
        patientWeightInput.setEnabled(b);
        patientPressureInput.setEnabled(b);
        patientDiabetesInput.setEnabled(b);
        patientBloodInput.setEnabled(b);
        patientVirusInput.setEnabled(b);
        patientGenderInput.setEnabled(b);
        patientDiagnosis.setEnabled(b);
    }


    private void ControlInputs2(boolean b) {
        patientDaughterName.setEnabled(b);
        patientUncleName.setEnabled(b);
        patientSSNInput.setEnabled(b);
        nameInput.setEnabled(b);
        emailInput.setEnabled(b);
        addressInput.setEnabled(b);
        phoneInput.setEnabled(b);
        ageInput.setEnabled(b);

    }


}
