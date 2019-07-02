package com.example.test.medicalreport.view.nurse.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.test.medicalreport.R;
import com.example.test.medicalreport.model.Patient;
import com.example.test.medicalreport.view.FullScreenImageViewer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class NursePatientDetailsActivity extends AppCompatActivity {

    Toolbar toolbar;
    EditText patientNameInput, patientAgeInput, patientHeightInput, patientWeightInput, patientSSNInput, patientAddressInput;
    EditText patientPressureInput, patientDiabetesInput, patientGenderInput, patientBloodInput, patientVirusInput, patientPhoneInput;
    Button downloadButton;
    Patient patient;
    ImageView image1, image2;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nurse_patient_details);
        setSupportActionBar(toolbar);

        progressDialog = new ProgressDialog(this);


        initViews();
        initToolbar();
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


    private void initViews() {
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
        patientGenderInput = findViewById(R.id.patient_gender_input);
        patientPhoneInput = findViewById(R.id.patient_phone_input);
        image1 = findViewById(R.id.image_1);
        image2 = findViewById(R.id.image_2);


        downloadButton = findViewById(R.id.down_btn);
        toolbar = findViewById(R.id.toolbar);


        ControlInputs(false);
        setIntentData();


    }

    private void setIntentData() {
        if (getIntent().hasExtra("patientData")) {
            patient = getIntent().getParcelableExtra("patientData");

            patientNameInput.setText(patient.getPatientName());
            patientAgeInput.setText(patient.getPatientAge());
            patientHeightInput.setText(patient.getPatientHeight());
            patientWeightInput.setText(patient.getPatientWeight());
            patientPressureInput.setText(patient.getPatientPressure());
            patientDiabetesInput.setText(patient.getPatientDiabetes());
            patientBloodInput.setText(patient.getPatientBlood());
            patientSSNInput.setText(patient.getPatientSSN());
            patientVirusInput.setText(patient.getPatientVirus());
            patientAddressInput.setText(patient.getPatientAddress());
            patientGenderInput.setText(patient.getPatientGender());
            patientPhoneInput.setText(patient.getPatientPhone());

            setImages(patient);


        }
    }

    private void setImages(final Patient patient) {
        Glide.with(this)
                .load(patient.getPatientScanImage())
                .error(R.drawable.error)
                .into(image1);

        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(NursePatientDetailsActivity.this, FullScreenImageViewer.class);
                intent.putExtra("imagefull",patient.getPatientScanImage());
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
                Intent intent=new Intent(NursePatientDetailsActivity.this, FullScreenImageViewer.class);
                intent.putExtra("imagefull",patient.getPatientXRayImage());
                startActivity(intent);
            }
        });



    }


    private void ControlInputs(boolean b) {
        patientNameInput.setEnabled(b);
        patientAgeInput.setEnabled(b);
        patientHeightInput.setEnabled(b);
        patientWeightInput.setEnabled(b);
        patientPressureInput.setEnabled(b);
        patientDiabetesInput.setEnabled(b);
        patientBloodInput.setEnabled(b);
        patientSSNInput.setEnabled(b);
        patientVirusInput.setEnabled(b);
        patientAddressInput.setEnabled(b);
        patientGenderInput.setEnabled(b);
        patientPhoneInput.setEnabled(b);
    }

    public void saveFileToStorage(Patient data) {

        String root = Environment.getExternalStorageDirectory().toString();
        FileOutputStream out = null;
        if (isStoragePermissionGranted()) { // check or ask permission
            File myDir = new File(root, "/MedicalApp");
            if (!myDir.exists()) {
                myDir.mkdirs();
            }
            String fname = data.getPatientName()+ ".txt";
            File file = new File(myDir, fname);
            if (file.exists()) {
                file.delete();
            }
            try {
                file.createNewFile(); // if file already exists will do nothing
                String s=data.getPatientName()+"\n"+data.getPatientAge()+"\n"+
                        data.getPatientPhone()+"\n"+data.getPatientPressure()+"\n"+
                        data.getPatientAddress()+"\n"+data.getPatientBlood()+"\n"+
                        data.getPatientVirus()+"\n"+data.getPatientGender()+"\n"+
                        data.getPatientDiabetes()+"\n"+data.getPatientDiagnosises();

                out = new FileOutputStream(file);
                out.write(s.getBytes());
                out.close();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            MediaScannerConnection.scanFile(this, new String[]{file.toString()}, new String[]{file.getName()}, null);
            Toast.makeText(this, "File Downloaded Successfully", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }

    }


    public boolean isStoragePermissionGranted() {
        String TAG = "Storage Permission";
        if (Build.VERSION.SDK_INT >= 23) {
            if (this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                return true;
            } else {
                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
            return true;
        }
    }


    public void download(View view) {
        progressDialog.setMessage("Downloading...");
        progressDialog.setTitle("Save File");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        saveFileToStorage(patient);
    }
}
