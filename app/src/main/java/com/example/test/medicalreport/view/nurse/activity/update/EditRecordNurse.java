package com.example.test.medicalreport.view.nurse.activity.update;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test.medicalreport.R;
import com.example.test.medicalreport.model.Patient;
import com.example.test.medicalreport.view.doctor.activity.AddRecordDoctorActivity;
import com.example.test.medicalreport.view.doctor.activity.MainActivity;
import com.example.test.medicalreport.view.nurse.activity.MainActivityNurse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditRecordNurse extends AppCompatActivity {

    private final int GALLERY_XRAY_REQUEST_PERMISSION = 100;
    private final int GALLERY_SCAN_REQUEST_PERMISSION = 101;
    private final int REQUEST_CODE = 101;


    EditText patientNameInput, patientAgeInput, patientHeightInput, patientWeightInput, patientSSNInput, patientAddressInput;
    EditText patientPressureInput, patientDiabetesInput, patientBloodInput, patientVirusInput, patientPhoneInput;
    TextView xRay, scan;
    Spinner doctorSpinner;


    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference, mDatabaseRef;
    private StorageReference mStorageRef;


    private String doctorId, doctorName;
    private String currentDate, currentTime, itemRandomKey;
    private String downloadXRayImageUrl = "", downloadScanImageUrl = "";


    private Uri xRayUri, scanUri;


    private Boolean nurseImage1 = false, nurseImage2 = false;
    private boolean xRaySelected = false, scanSelected = false;
    private int pickImage;
    Toolbar toolbar;

    ProgressDialog progressDialog;
    Button nextButton, deleteBtn;
    String nurseId;
    Patient patient1;
    public static Patient patient = new Patient();
    public static String patientId;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    public static  List<String> doctorKeys = new ArrayList<>();
    public static Boolean imageUpdated = false;
    String gender = "Male";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nurse);
        setSupportActionBar(toolbar);
        toolbar = findViewById(R.id.toolbar);

        initToolbar();


        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference().child("Patient Images");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Patients");
        progressDialog = new ProgressDialog(this);


        SharedPreferences prefs = this.getSharedPreferences("pref", MODE_PRIVATE);
        nurseId = prefs.getString("nurseId", "0");

        initViews();
        initButtonClicks();


    }

    private void initToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    private void getIntentData() {
        if (getIntent().getParcelableExtra("patientData") != null) {
            patient1 = getIntent().getParcelableExtra("patientData");
            patientId = patient1.getPatientId();


            patientNameInput.setText(patient1.getPatientName());
            patientAgeInput.setText(patient1.getPatientAge());
            patientHeightInput.setText(patient1.getPatientHeight());
            patientWeightInput.setText(patient1.getPatientWeight());
            patientPressureInput.setText(patient1.getPatientPressure());
            patientDiabetesInput.setText(patient1.getPatientDiabetes());
            patientBloodInput.setText(patient1.getPatientBlood());
            patientSSNInput.setText(patient1.getPatientSSN());
            patientVirusInput.setText(patient1.getPatientVirus());
            patientAddressInput.setText(patient1.getPatientAddress());
            patientPhoneInput.setText(patient1.getPatientPhone());

            if (!patient1.getPatientXRayImage().equals("") || patient1.getPatientXRayImage() != null)
                nurseImage1 = true;


            if (!patient1.getPatientScanImage().equals("") || patient1.getPatientScanImage() != null)
                nurseImage2 = true;

        }
    }

    private void initViews() {
        doctorSpinner = findViewById(R.id.doctors_spinner);
        patientNameInput = findViewById(R.id.patient_name_input);
        patientAgeInput = findViewById(R.id.patient_age_input);
        patientHeightInput = findViewById(R.id.patient_height_input);
        patientWeightInput = findViewById(R.id.patient_weight_input);
        patientPressureInput = findViewById(R.id.patient_pressure_input);
        patientDiabetesInput = findViewById(R.id.patient_diabetes_input);
        patientBloodInput = findViewById(R.id.patient_blood_input);
        patientSSNInput = findViewById(R.id.patient_ssn_input);
        patientVirusInput = findViewById(R.id.patient_virus_input);
        patientAddressInput = findViewById(R.id.patient_address_input);
        patientPhoneInput = findViewById(R.id.patient_phone_input);
        nextButton = findViewById(R.id.upload_btn);
        deleteBtn = findViewById(R.id.delete_btn);
        xRay = findViewById(R.id.upload_x_rays);
        scan = findViewById(R.id.upload_Scans);
        setSpinnerData();

    }


    private void setSpinnerData() {
        ArrayList<String> items = new ArrayList<>();

        items.add("Male");
        items.add("Female");

        ArrayAdapter<String> itemsAdapter = new ArrayAdapter(EditRecordNurse.this,
                android.R.layout.simple_spinner_dropdown_item, items);

        doctorSpinner.setAdapter(itemsAdapter);

        doctorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    gender = "Male";
                } else {
                    gender = "Female";

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    private void initButtonClicks() {

        xRay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(EditRecordNurse.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermission();
                } else {
                    pickXRayImage();
                }
            }
        });

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(EditRecordNurse.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermission();
                } else {
                    pickScanImage();
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    setDataToObject();

                    Intent intent = new Intent(EditRecordNurse.this, NurseUpdateSearchDoctorActivity.class);

                    if (nurseImage1 && nurseImage2) {
                        patient.setPatientXRayImage(patient1.getPatientXRayImage());
                        patient.setPatientScanImage(patient1.getPatientScanImage());
                        startActivity(intent);

                    } else if (xRaySelected && scanSelected) {
                        patient.setPatientScanImage(scanUri.toString());
                        patient.setPatientXRayImage(xRayUri.toString());
                        startActivity(intent);
                    } else {
                        if (!scanSelected)
                            patient.setPatientScanImage("");
                        if (!xRaySelected)
                            patient.setPatientXRayImage("");

                        startActivity(intent);

                    }


                }


            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setTitle("Delete Data");
                progressDialog.setMessage("Please Wait...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();


                firebaseFirestore.collection("PatientDoctors").document(patientId)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                firebaseFirestore.collection("Patients").document(patientId)
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {


                                                Intent intent = new Intent(EditRecordNurse.this, MainActivityNurse.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                                finish();
                                                Toast.makeText(EditRecordNurse.this, "Patient Deleted Successfully", Toast.LENGTH_SHORT).show();

                                                progressDialog.dismiss();
                                            }
                                        });
                            }
                        });


            }
        });


    }

    private void setDataToObject() {
        patient.setPatientName(patientNameInput.getText().toString());
        patient.setPatientAge(patientAgeInput.getText().toString());
        patient.setPatientAddress(patientAddressInput.getText().toString());
        patient.setPatientHeight(patientHeightInput.getText().toString());
        patient.setPatientWeight(patientWeightInput.getText().toString());
        patient.setPatientVirus(patientVirusInput.getText().toString());
        patient.setPatientBlood(patientBloodInput.getText().toString());
        patient.setPatientDiabetes(patientDiabetesInput.getText().toString());
        patient.setPatientPhone(patientPhoneInput.getText().toString());
        patient.setPatientPressure(patientPressureInput.getText().toString());
        patient.setPatientSSN(patientSSNInput.getText().toString());
        patient.setPatientGender(gender);

    }


    private void getDoctorIds() {
        getIntentData();

        firebaseFirestore.collection("PatientDoctors")
                .document(patientId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.getResult() != null) {
                            for (Map.Entry<String, Object> entry : task.getResult().getData().entrySet()) {
                                if (!entry.getKey().equals(nurseId)) {
                                    doctorKeys.add(entry.getKey());
                                }


                            }
                        }
                    }
                });


    }

    @Override
    protected void onResume() {
        super.onResume();
        doctorKeys=new ArrayList<>();
        getDoctorIds();
    }

    private boolean validateInputs() {
        if (patientNameInput.getText().toString().equals("") || patientNameInput.getText().toString().isEmpty()) {
            Toast.makeText(this, "Fill All Fields!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (patientAgeInput.getText().toString().equals("") || patientAgeInput.getText().toString().isEmpty()) {
            Toast.makeText(this, "Fill All Fields!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (patientHeightInput.getText().toString().equals("") || patientHeightInput.getText().toString().isEmpty()) {
            Toast.makeText(this, "Fill All Fields!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (patientWeightInput.getText().toString().equals("") || patientWeightInput.getText().toString().isEmpty()) {
            Toast.makeText(this, "Fill All Fields!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (patientAddressInput.getText().toString().equals("") || patientAddressInput.getText().toString().isEmpty()) {
            Toast.makeText(this, "Fill All Fields!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (patientSSNInput.getText().toString().equals("") || patientSSNInput.getText().toString().isEmpty()) {
            Toast.makeText(this, "Fill All Fields!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (patientPhoneInput.getText().toString().equals("") || patientPhoneInput.getText().toString().isEmpty()) {
            Toast.makeText(this, "Fill All Fields!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (patientPressureInput.getText().toString().equals("") || patientPressureInput.getText().toString().isEmpty()) {
            Toast.makeText(this, "Fill All Fields!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (patientBloodInput.getText().toString().equals("") || patientBloodInput.getText().toString().isEmpty()) {
            Toast.makeText(this, "Fill All Fields!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (patientVirusInput.getText().toString().equals("") || patientVirusInput.getText().toString().isEmpty()) {
            Toast.makeText(this, "Fill All Fields!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (patientDiabetesInput.getText().toString().equals("") || patientDiabetesInput.getText().toString().isEmpty()) {
            Toast.makeText(this, "Fill All Fields!", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
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
            if (ContextCompat.checkSelfPermission(EditRecordNurse.this,
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
                xRaySelected = true;
            } else if (requestCode == GALLERY_SCAN_REQUEST_PERMISSION) {
                scanUri = data.getData();
                scanSelected = true;
            }

        } else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "Choose Image", Toast.LENGTH_SHORT).show();
        }

    }


}
