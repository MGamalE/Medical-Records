package com.example.test.medicalreport.view.doctor.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.test.medicalreport.R;
import com.example.test.medicalreport.model.Patient;
import com.example.test.medicalreport.view.doctor.activity.PatienFullDataActivity;
import com.example.test.medicalreport.view.doctor.activity.PatientDetailsActivity;

import java.util.List;
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

     List<Patient> patients;
    private Context context;


    public  MyAdapter(Context context, List<Patient> list) {
        this.patients = list;
        this.context=context;
    }
    @Override
    public int getItemCount() {

        return patients.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_home_patient, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.patientAge.setText("Age: "+patients.get(position).getPatientAge());
        holder.patiantName.setText(patients.get(position).getPatientName());


        holder.arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, PatientDetailsActivity.class);
                intent.putExtra("patientData",patients.get(position));
                context.startActivity(intent);
            }
        });


        holder.callNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + patients.get(position).getPatientPhone()));
                context.startActivity(intent);
            }
        });


        if(!patients.get(position).getPatientPhoto().equals("")){
            Glide.with(context)
                    .load(patients.get(position).getPatientPhoto())
                    .error(R.drawable.patientuser)
                    .into(holder.patientImage);
        }



    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private  TextView patientAge;
        private  TextView patiantName;
        private ImageView patientImage,arrow;
        private  TextView callNow;

        public MyViewHolder(final View itemView) {
            super(itemView);

            patiantName = ((TextView) itemView.findViewById(R.id.patient_name));
            patientAge = ((TextView) itemView.findViewById(R.id.patient_age));
            callNow = ((TextView) itemView.findViewById(R.id.call));
            patientImage = ((ImageView) itemView.findViewById(R.id.patient_image));
            arrow = ((ImageView) itemView.findViewById(R.id.arrow));

    }
        }
}
