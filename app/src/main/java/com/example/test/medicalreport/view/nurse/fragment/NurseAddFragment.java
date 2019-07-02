package com.example.test.medicalreport.view.nurse.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.test.medicalreport.model.PatientBody;
import com.example.test.medicalreport.model.doctor.Doctor;
import com.example.test.medicalreport.view.LoginActivity;
import com.example.test.medicalreport.view.nurse.activity.NurseSearchDoctorActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import io.paperdb.Paper;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class NurseAddFragment extends Fragment {

    private final int GALLERY_XRAY_REQUEST_PERMISSION = 100;
    private final int GALLERY_SCAN_REQUEST_PERMISSION = 101;
    private final int REQUEST_CODE = 101;


    View view;
    EditText patientNameInput, patientAgeInput, patientHeightInput, patientWeightInput, patientSSNInput, patientAddressInput;
    EditText patientPressureInput, patientDiabetesInput, patientBloodInput, patientVirusInput, patientPhoneInput;
    TextView xRay, scan;
    Spinner doctorSpinner;



   public static Patient patient=new Patient();



    private Uri xRayUri, scanUri;

    private boolean xRaySelected = false, scanSelected = false;
    private int pickImage;
    String gender="Male";

    ProgressDialog progressDialog;
    Button nextButton,deleteButton;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_nurse_add_patient, container, false);

        initViews();


        progressDialog = new ProgressDialog(getActivity());

        initButtonClicks();

        return view;
    }


    private void initViews() {
        doctorSpinner = view.findViewById(R.id.doctors_spinner);
        patientNameInput = view.findViewById(R.id.patient_name_input);
        patientAgeInput = view.findViewById(R.id.patient_age_input);
        patientHeightInput = view.findViewById(R.id.patient_height_input);
        patientWeightInput = view.findViewById(R.id.patient_weight_input);
        patientPressureInput = view.findViewById(R.id.patient_pressure_input);
        patientDiabetesInput = view.findViewById(R.id.patient_diabetes_input);
        patientBloodInput = view.findViewById(R.id.patient_blood_input);
        patientSSNInput = view.findViewById(R.id.patient_ssn_input);
        patientVirusInput = view.findViewById(R.id.patient_virus_input);
        patientAddressInput = view.findViewById(R.id.patient_address_input);
        patientPhoneInput = view.findViewById(R.id.patient_phone_input);
        nextButton = view.findViewById(R.id.upload_btn);
        deleteButton = view.findViewById(R.id.delete_btn);
        deleteButton.setVisibility(View.GONE);
        xRay = view.findViewById(R.id.upload_x_rays);
        scan = view.findViewById(R.id.upload_Scans);
        setSpinnerData();

    }

    private void setSpinnerData() {
        ArrayList<String> items = new ArrayList<>();

        items.add("Male");
        items.add("Female");

        ArrayAdapter<String> itemsAdapter = new ArrayAdapter(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, items);

        doctorSpinner.setAdapter(itemsAdapter);

        doctorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    gender="Male";
                }else{
                    gender="Female";

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
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
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
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
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
                    setInputDataToObject();
                    Intent intent = new Intent(getActivity(), NurseSearchDoctorActivity.class);
                    intent.putExtra("scanImage", scanUri.toString());
                    intent.putExtra("xRayImage", xRayUri.toString());
                    startActivity(intent);

                }
            }
        });


    }

    private void setInputDataToObject(){
        patient.setPatientName(patientNameInput.getText().toString());
        patient.setPatientAddress(patientAddressInput.getText().toString());
        patient.setPatientAge(patientAgeInput.getText().toString());
        patient.setDoctorAddedData(false);
        patient.setPatientSSN(patientSSNInput.getText().toString());
        patient.setPatientBlood(patientBloodInput.getText().toString());
        patient.setPatientVirus(patientVirusInput.getText().toString());
        patient.setPatientDiabetes(patientDiabetesInput.getText().toString());
        patient.setPatientPhone(patientPhoneInput.getText().toString());
        patient.setPatientHeight(patientHeightInput.getText().toString());
        patient.setPatientWeight(patientWeightInput.getText().toString());
        patient.setPatientPressure(patientPressureInput.getText().toString());
        patient.setPatientGender(gender);


    }

    private boolean validateInputs() {
        if (patientNameInput.getText().toString().equals("") || patientNameInput.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Fill All Fields!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (patientAgeInput.getText().toString().equals("") || patientAgeInput.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Fill All Fields!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (patientHeightInput.getText().toString().equals("") || patientHeightInput.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Fill All Fields!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (patientWeightInput.getText().toString().equals("") || patientWeightInput.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Fill All Fields!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (patientAddressInput.getText().toString().equals("") || patientAddressInput.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Fill All Fields!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (patientSSNInput.getText().toString().equals("") || patientSSNInput.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Fill All Fields!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (patientPhoneInput.getText().toString().equals("") || patientPhoneInput.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Fill All Fields!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (patientPressureInput.getText().toString().equals("") || patientPressureInput.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Fill All Fields!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (patientBloodInput.getText().toString().equals("") || patientBloodInput.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Fill All Fields!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (patientVirusInput.getText().toString().equals("") || patientVirusInput.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Fill All Fields!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (patientDiabetesInput.getText().toString().equals("") || patientDiabetesInput.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Fill All Fields!", Toast.LENGTH_SHORT).show();
            return false;
        } else if(!xRaySelected && !scanSelected) {
            Toast.makeText(getActivity(), "Select Images!", Toast.LENGTH_SHORT).show();
            return false;
        }else{
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
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        (Activity) getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {

                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_CODE);

                } else {
                    ActivityCompat
                            .requestPermissions(
                                    (Activity) getActivity(),
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
                Toast.makeText(getActivity(), "Allow Storage Permissionً", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getActivity(), "Choose Image", Toast.LENGTH_SHORT).show();
        }

    }

}
