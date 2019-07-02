package com.example.test.medicalreport.view.doctor.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test.medicalreport.R;
import com.example.test.medicalreport.model.Patient;
import com.example.test.medicalreport.view.doctor.adapter.MyAdapter;
import com.example.test.medicalreport.view.nurse.adapter.NurseListAdapter;
import com.example.test.medicalreport.view.nurse.fragment.NurseHomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class DoctorHomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private MyAdapter myAdapter;
    private String doctorId;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<String> patientRef = new ArrayList<>();
    private List<Patient> patients;
    private ProgressBar progressBar;
    private TextView noData;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.activity_home_dotor, container, false);


        getFileStoreData(new MyCallback() {
            @Override
            public void onCallback(final List<String> patientRefs) {

                initRecycler(patientRefs, new MyCallback2() {
                    @Override
                    public void adapterCallback(List<Patient> patients) {
                        if (patients.size() == 0) {
                            setLoading(false, true, false);
                        } else {
                            setupRecycler(patients);
                        }
                    }
                });


            }
        });

        return view;
    }

    private void getFileStoreData(final MyCallback myCallback) {
        SharedPreferences prefs = getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
        doctorId = prefs.getString("doctorId", null);

        db.collection("PatientDoctors")
                .whereEqualTo(doctorId, true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                patientRef.add(documentSnapshot.getId());
                            }

                            myCallback.onCallback(patientRef);

                        }

                    }
                });


    }

    private void setupRecycler(List<Patient> patients) {
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        myAdapter = new MyAdapter(getActivity(), patients);
        recyclerView.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();
        setLoading(false, false, true);

    }


    private void initRecycler(final List<String> patientRef, final MyCallback2 myCallback2) {
        patients = new ArrayList<>();

        if (patientRef.size() == 0) {
            setLoading(false, true, false);
        } else {
            for (int i = 0; i < patientRef.size(); i++) {
                final int finalI = i;
                db.collection("Patients")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                        if (documentSnapshot.getId().equals(patientRef.get(finalI))) {
                                            Patient patient = documentSnapshot.toObject(Patient.class);
                                            patients.add(patient);
                                        }
                                        myCallback2.adapterCallback(patients);

                                    }
                                }
                            }
                        });
            }

        }
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView);
        progressBar = view.findViewById(R.id.progress);
        noData = view.findViewById(R.id.noData);

        setLoading(true, false, false);

    }


    private void setLoading(Boolean progress, Boolean data, Boolean recycler) {
        if (progress)
            progressBar.setVisibility(View.VISIBLE);
        else
            progressBar.setVisibility(View.GONE);

        if (data)
            noData.setVisibility(View.VISIBLE);
        else
            noData.setVisibility(View.GONE);


        if (recycler)
            recyclerView.setVisibility(View.VISIBLE);
        else
            recyclerView.setVisibility(View.GONE);


    }

    interface MyCallback {
        void onCallback(List<String> patientRefs);
    }

    interface MyCallback2 {
        void adapterCallback(List<Patient> patients);

    }


}