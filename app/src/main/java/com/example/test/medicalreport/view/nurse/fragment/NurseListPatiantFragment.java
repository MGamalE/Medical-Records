package com.example.test.medicalreport.view.nurse.fragment;

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
import com.example.test.medicalreport.view.nurse.adapter.NurseEditPatientAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class NurseListPatiantFragment extends Fragment {
    List<String> list;
    NurseEditPatientAdapter nurseEditPatientAdapter;

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
     List<String> patientRefs=new ArrayList<>();
    private List<Patient> patients;
    private List<Patient> finalList=new ArrayList<>();
    private ProgressBar progressBar;
    private TextView noData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.activity_show_patiant_nurse,container,false);

        getFileStoreData(new MyCallback() {
            @Override
            public void onCallback(List<String> patientRefs) {

                initRecycler(patientRefs, new MyCallback2() {
                    @Override
                    public void adapterCallback(List<Patient> patients) {
                        finalList.addAll(patients);
                        if (patients.size() == 0) {
                            setLoading(false, true, false);
                        } else {
                            setupRecycler(finalList);
                        }
                    }
                });

            }
        });



        return  view;
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
    private void getFileStoreData(final MyCallback myCallback) {
        SharedPreferences prefs = getActivity().getSharedPreferences("pref", MODE_PRIVATE);
        String  nurseId= prefs.getString("nurseId", "0");

        db.collection("PatientDoctors")
                .whereEqualTo(nurseId,true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for(QueryDocumentSnapshot documentSnapshot:task.getResult()){
                                patientRefs.add(documentSnapshot.getId());
                            }

                            myCallback.onCallback(patientRefs);

                        }

                    }
                });


    }

    private void setupRecycler(List<Patient> patients){
        linearLayoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        nurseEditPatientAdapter=new NurseEditPatientAdapter(getActivity(),patients);
        recyclerView.setAdapter(nurseEditPatientAdapter);
        nurseEditPatientAdapter.notifyDataSetChanged();
        setLoading(false, false, true);

    }


    private void initRecycler(final List<String> patientRef, final MyCallback2 myCallback2) {
        patients=new ArrayList<>();

        if (patientRef.size() == 0) {
            setLoading(false, true, false);
        } else {
            db.collection("Patients")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    for (int i = 0; i < patientRef.size(); i++) {
                                        if (patientRef.get(i).equals(documentSnapshot.getId())) {

                                            Patient patient = documentSnapshot.toObject(Patient.class);
                                            Log.e("iood", "PatientSize::" + patient.getPatientId());

                                            patients.add(patient);
                                        }
                                    }
                                }
                                myCallback2.adapterCallback(patients);


                            }
                        }
                    });
        }



    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView=view.findViewById(R.id.recyclerViewnurse);
        progressBar = view.findViewById(R.id.progress);
        noData = view.findViewById(R.id.noData);

        setLoading(true, false, false);

    }




    interface MyCallback {
        void onCallback(List<String> patientRefs);
    }

    interface MyCallback2{
        void adapterCallback(List<Patient> patients);

    }




}
