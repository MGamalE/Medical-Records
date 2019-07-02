package com.example.test.medicalreport.view.nurse.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.test.medicalreport.R;
import com.example.test.medicalreport.view.nurse.activity.update.EditRecordNurse;

public class MyAdapterNurse extends RecyclerView.Adapter<MyAdapterNurse.MyViewHolderNurse> {
    private java.util.List<String> List;
    private Context context;



    public  MyAdapterNurse(Context context) {
        this.context=context;
    }
    @Override
    public int getItemCount() {

        return 10;
    }
    @NonNull
    @Override
    public MyAdapterNurse.MyViewHolderNurse onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.recycler_view_item_2, viewGroup, false);
        return new MyViewHolderNurse(view);
    }
    public void onBindViewHolder(MyAdapterNurse.MyViewHolderNurse holder, int position) {


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, EditRecordNurse.class));
            }
        });
    }
    public class MyViewHolderNurse extends RecyclerView.ViewHolder{

        TextView patientName,nurseName;
        ImageView nurseImage;
        Button editBtn,deleteBtn;

        public MyViewHolderNurse(final View itemView) {
            super(itemView);



        }
    }
}
