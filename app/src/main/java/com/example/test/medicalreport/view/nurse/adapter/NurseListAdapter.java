package com.example.test.medicalreport.view.nurse.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.test.medicalreport.R;
import com.example.test.medicalreport.model.Patient;
import com.example.test.medicalreport.view.doctor.activity.PatientDetailsActivity;
import com.example.test.medicalreport.view.nurse.activity.NursePatientDetailsActivity;

import java.util.List;

public class NurseListAdapter extends RecyclerView.Adapter<NurseListAdapter.MyViewHolderNurse> {
    private List<Patient> list;
    private Context context;


    public NurseListAdapter(Context context,List<Patient> List) {
        this.context=context;
        this.list =List;

    }

    @NonNull
    @Override
    public NurseListAdapter.MyViewHolderNurse onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.item_home_patient, viewGroup, false);
        return new MyViewHolderNurse(view);
    }
    public void onBindViewHolder(NurseListAdapter.MyViewHolderNurse holder, final int position) {

        holder.patientName.setText(list.get(position).getPatientName());
        holder.patientAge.setText("Age: "+list.get(position).getPatientAge());

        holder.arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, NursePatientDetailsActivity.class);
                intent.putExtra("patientData",list.get(position));
                context.startActivity(intent);

            }
        });

        holder.callNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + list.get(position).getPatientPhone()));
                context.startActivity(intent);
            }
        });

        if(!list.get(position).getPatientPhoto().equals("")){
            Glide.with(context)
                    .load(list.get(position).getPatientPhoto())
                    .error(R.drawable.patientuser)
                    .into(holder.patientImage);
        }
    }


    @Override
    public int getItemCount() {

        return list.size();
    }


    public class MyViewHolderNurse extends RecyclerView.ViewHolder{

        TextView patientName,patientAge;
        ImageView arrow,patientImage;
        private  TextView callNow;

        public MyViewHolderNurse(final View itemView) {
            super(itemView);
            patientAge=itemView.findViewById(R.id.patient_age);
            patientName=itemView.findViewById(R.id.patient_name);
            patientImage=itemView.findViewById(R.id.patient_image);
            arrow=itemView.findViewById(R.id.arrow);
            callNow = ((TextView) itemView.findViewById(R.id.call));



        }
    }
}
