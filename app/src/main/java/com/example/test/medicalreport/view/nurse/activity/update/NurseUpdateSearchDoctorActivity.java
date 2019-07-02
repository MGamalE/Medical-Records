package com.example.test.medicalreport.view.nurse.activity.update;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test.medicalreport.R;
import com.example.test.medicalreport.model.doctor.Doctor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class NurseUpdateSearchDoctorActivity extends AppCompatActivity {

    EditText searchDoctorInput;
    LinearLayout searchDoctorLayout;
    TextView doctorName;
    ImageButton searchDoctorButton;
    ArrayList<Doctor> doctors = new ArrayList<>();

    Doctor doctor;
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
                                    for(int i=0;i<EditRecordNurse.doctorKeys.size();i++){
                                        if(EditRecordNurse.doctorKeys.get(i).equals(doctor.getDoctorId())){
                                            Toast.makeText(NurseUpdateSearchDoctorActivity.this, "Doctor Name Already Exist", Toast.LENGTH_SHORT).show();
                                           return;
                                        }else {
                                            continue;
                                        }

                                    }
                                    searchDoctorLayout.setVisibility(View.VISIBLE);
                                    doctorName.setText(doctor.getDoctorName());
                                }
                            }
                        });

            }
        });



        searchDoctorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NurseUpdateSearchDoctorActivity.this, "Doctor selected", Toast.LENGTH_SHORT).show();
                searchDoctorInput.setText("");
                searchDoctorLayout.setVisibility(View.GONE);
                doctors.add(doctor);
            }
        });

    }


    private void getDoctorsFromServer() {
        doctors=new ArrayList<>();
        for(int i=0;i<EditRecordNurse.doctorKeys.size();i++){
            firebaseFirestore.collection("Doctors")
                    .document(EditRecordNurse.doctorKeys.get(i))
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.getResult()!=null){
                                Doctor doctor=task.getResult().toObject(Doctor.class);
                                doctors.add(doctor);
                            }

                        }
                    });
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        getDoctorsFromServer();

    }

    public void openList(View view) {
        if (doctors.size() == 0) {
            Toast.makeText(this, "Select Doctor", Toast.LENGTH_SHORT).show();
        } else {
            Log.e("doctors", String.valueOf(doctors.size()));
            Intent intent = new Intent(this, NurseUpdateListDoctors.class);
            intent.putParcelableArrayListExtra("doctorList", doctors);
            startActivity(intent);
        }
    }
}
