package com.example.test.medicalreport.view.nurse.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test.medicalreport.R;
import com.example.test.medicalreport.model.doctor.Doctor;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class NurseSearchDoctorActivity extends AppCompatActivity {

    EditText searchDoctorInput;
    LinearLayout searchDoctorLayout;
    TextView doctorName;
    ImageButton searchDoctorButton;
    DatabaseReference databaseReference;
    ArrayList<Doctor> doctors = new ArrayList<>();

    Doctor doctor;

    private Uri xRayUri, scanUri;

    FirebaseFirestore firebaseFirestore;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_search);
        searchDoctorInput = findViewById(R.id.doctor_search_name_input);
        searchDoctorLayout = findViewById(R.id.doctor_item_search_layout);
        doctorName = findViewById(R.id.doctor_search_name);
        searchDoctorButton = findViewById(R.id.doctor_search_button);
        firebaseFirestore = FirebaseFirestore.getInstance();

        searchDoctorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchDoctorLayout.setVisibility(View.GONE);

                FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
                rootRef.collection("Doctors")
                        .whereEqualTo("doctorName", searchDoctorInput.getText().toString().trim())
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                List<Doctor> doctors = queryDocumentSnapshots.toObjects(Doctor.class);
                                if (doctors.size() != 0 || doctors.size()>1) {
                                    doctor = doctors.get(0);
                                    searchDoctorLayout.setVisibility(View.VISIBLE);
                                    doctorName.setText(doctor.getDoctorName());

                                }{
                                }
                            }
                        });

            }
        });


        searchDoctorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NurseSearchDoctorActivity.this, "Doctor selected", Toast.LENGTH_SHORT).show();
                searchDoctorInput.setText("");
                searchDoctorLayout.setVisibility(View.GONE);
                doctors.add(doctor);
            }
        });

    }


    public void openList(View view) {
        if (doctors.size() == 0) {
            Toast.makeText(this, "Select Doctor", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(this, NurseListDoctors.class);
            intent.putParcelableArrayListExtra("doctorList", doctors);
            intent.putExtra("scanImage", getIntent().getStringExtra("scanImage"));
            intent.putExtra("xRayImage", getIntent().getStringExtra("xRayImage"));

            startActivity(intent);
            doctors = new ArrayList<>();
        }
    }
}
