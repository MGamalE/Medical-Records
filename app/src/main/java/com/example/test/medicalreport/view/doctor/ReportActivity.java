package com.example.test.medicalreport.view.doctor;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.example.test.medicalreport.R;
import com.example.test.medicalreport.model.Patient;
import com.example.test.medicalreport.view.doctor.adapter.MyAdapter;
import com.example.test.medicalreport.view.doctor.fragment.DoctorHomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ReportActivity extends AppCompatActivity {

    Toolbar toolbar;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<String> patientRef = new ArrayList<>();
    private List<Patient> patients;
    private String doctorId;
    List<DataEntry> data;
    Cartesian cartesian;
    Column column;
    AnyChartView anyChartView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        setSupportActionBar(toolbar);
        toolbar = findViewById(R.id.toolbar);
        initToolbar();

        anyChartView = findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(findViewById(R.id.progress_bar));

         cartesian = AnyChart.column();

        data = new ArrayList<>();


        data.add(new ValueDataEntry("Pressure", 90));
        data.add(new ValueDataEntry("Diabetes", 55));
        data.add(new ValueDataEntry("Virus", 20));
        data.add(new ValueDataEntry("OverWeight", 74));

        column = cartesian.column(data);
        column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_TOP)
                .anchor(Anchor.CENTER_TOP)
                .offsetX(0d)
                .offsetY(5d)
                .format("%{%Value}{groupsSeparator: }");

        cartesian.animation(true);
        cartesian.title("The Most Chronic Diseases");


        cartesian.yScale().minimum(0d);

        cartesian.yAxis(0).labels().format("%{%Value}{groupsSeparator: }");

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);

        cartesian.xAxis(0).title("Disease");
        cartesian.yAxis(0).title("Rate");

        anyChartView.setChart(cartesian);





    }

    private void getFileStoreData(final ReportActivity.MyCallback myCallback) {
        SharedPreferences prefs = this.getSharedPreferences("pref", Context.MODE_PRIVATE);
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
        int totalPressure=0,totoalDiabetes=0,totalOverWeight=0;

        try {
            for (int i = 0; i < patients.size(); i++) {
                int p = Integer.parseInt(patients.get(i).getPatientPressure());
                int d = Integer.parseInt(patients.get(i).getPatientDiabetes());
                int w = Integer.parseInt(patients.get(i).getPatientWeight());
                totalPressure = totalPressure + p;
                totoalDiabetes=totoalDiabetes+d;
                totalOverWeight=totalOverWeight+w;

            }
        }catch (Exception e){
            Log.e("exception",e.getMessage());
        }

        int v1=(totalPressure/patients.size())*100;
        int v2=(totoalDiabetes/patients.size())*100;
        int v3=(totalOverWeight/patients.size())*100;

        data.add(new ValueDataEntry("Pressure", v1));
        data.add(new ValueDataEntry("Diabetes", v2));
        data.add(new ValueDataEntry("Virus", patients.size()));
        data.add(new ValueDataEntry("OverWeight", v3));

        column = cartesian.column(data);


        column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_TOP)
                .anchor(Anchor.CENTER_TOP)
                .offsetX(0d)
                .offsetY(5d)
                .format("%{%Value}{groupsSeparator: }");

        cartesian.animation(true);
        cartesian.title("The Most Chronic Diseases");


        cartesian.yScale().minimum(0d);

        cartesian.yAxis(0).labels().format("%{%Value}{groupsSeparator: }");

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);

        cartesian.xAxis(0).title("Disease");
        cartesian.yAxis(0).title("Rate");

        anyChartView.setChart(cartesian);

    }


    private void initRecycler(final List<String> patientRef, final ReportActivity.MyCallback2 myCallback2) {
        patients = new ArrayList<>();

        if (patientRef.size() == 0) {
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




    interface MyCallback {
        void onCallback(List<String> patientRefs);
    }

    interface MyCallback2 {
        void adapterCallback(List<Patient> patients);

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




}
