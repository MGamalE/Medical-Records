package com.example.test.medicalreport.util;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.test.medicalreport.R;

public class PatientHolder extends RecyclerView.ViewHolder {
   public TextView patientName,patientAge,patientHeight;
    public ImageView patientImage;


    public PatientHolder(@NonNull View itemView) {
        super(itemView);

        patientName=itemView.findViewById(R.id.patient_name);
        patientAge=itemView.findViewById(R.id.patient_age);
        patientHeight=itemView.findViewById(R.id.patient_height);
        patientImage=itemView.findViewById(R.id.patient_image);

    }
}
