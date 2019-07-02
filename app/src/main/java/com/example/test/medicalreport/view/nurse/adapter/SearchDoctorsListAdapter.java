package com.example.test.medicalreport.view.nurse.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.test.medicalreport.R;
import com.example.test.medicalreport.model.doctor.Doctor;

import java.util.ArrayList;

public class SearchDoctorsListAdapter extends RecyclerView.Adapter<SearchDoctorsListAdapter.MyViewHolderSearch> {

    Context context;
    ArrayList<Doctor> doctors;

    public SearchDoctorsListAdapter(Context context,ArrayList<Doctor> doctors) {
        this.context = context;
        this.doctors=doctors;
    }


    @NonNull
    @Override
    public SearchDoctorsListAdapter.MyViewHolderSearch onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.item_search_doctor, viewGroup, false);
        return new MyViewHolderSearch(view);

    }

    @Override
    public void onBindViewHolder(@NonNull SearchDoctorsListAdapter.MyViewHolderSearch myViewHolderSearch, final int i) {

        myViewHolderSearch.doctorName.setText(doctors.get(i).getDoctorName());


        myViewHolderSearch.deleteDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doctors.remove(i);
                notifyItemRemoved(i);
                notifyItemRangeChanged(i, doctors.size());

            }
        });
    }
    public ArrayList<Doctor> getArrayList(){
        return doctors;
    }


    @Override
    public int getItemCount() {
        return doctors.size();
    }

    public class MyViewHolderSearch extends RecyclerView.ViewHolder {
        TextView doctorName;
        ImageView deleteDoctor;

        public MyViewHolderSearch(@NonNull View itemView) {
            super(itemView);

            doctorName=itemView.findViewById(R.id.doctor_name);
            deleteDoctor=itemView.findViewById(R.id.delete_doctor);
        }
    }
}
