package com.example.test.medicalreport.view.nurse.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.test.medicalreport.R;
import com.example.test.medicalreport.model.Patient;
import com.example.test.medicalreport.view.nurse.activity.update.EditRecordNurse;

import java.util.List;

public class NurseEditPatientAdapter extends RecyclerView.Adapter<NurseEditPatientAdapter.ViewHolder> {
    private Context context;
    private List<Patient> patients;



    public NurseEditPatientAdapter(Context context, List<Patient> patients) {
        this.context = context;
        this.patients = patients;
    }

    @NonNull
    @Override
    public NurseEditPatientAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.item_edit_patient, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NurseEditPatientAdapter.ViewHolder viewHolder, final int i) {



        viewHolder.patientAge.setText("Age: " + patients.get(i).getPatientAge());
        viewHolder.patientName.setText(patients.get(i).getPatientName());

        Log.e("id","PatientId::"+patients.get(i).getPatientId());
        viewHolder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, EditRecordNurse.class);
                intent.putExtra("patientData", patients.get(i));
                context.startActivity(intent);

            }
        });


        if(!patients.get(i).getPatientPhoto().equals("")){
            Glide.with(context)
                    .load(patients.get(i).getPatientPhoto())
                    .error(R.drawable.patientuser)
                    .into(viewHolder.patientImage);
        }

    }

    @Override
    public int getItemCount() {
        Log.e("id","PatientSize::"+patients.size());

        return patients.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView patientName, patientAge;
        ImageView patientImage;
        ImageView editBtn;

        public ViewHolder(final View itemView) {
            super(itemView);

            patientName = itemView.findViewById(R.id.patient_name);
            patientAge = itemView.findViewById(R.id.patient_age);
            patientImage = itemView.findViewById(R.id.patient_image);
            editBtn = itemView.findViewById(R.id.patient_edit);
        }
    }
}
