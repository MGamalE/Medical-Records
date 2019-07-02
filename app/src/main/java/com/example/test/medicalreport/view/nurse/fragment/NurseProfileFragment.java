package com.example.test.medicalreport.view.nurse.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.test.medicalreport.R;
import com.example.test.medicalreport.view.LoginActivity;
import com.example.test.medicalreport.view.doctor.activity.MainActivity;
import com.example.test.medicalreport.view.nurse.activity.MainActivityNurse;
import com.example.test.medicalreport.view.patient.PatientMainActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class NurseProfileFragment extends Fragment {
    View view;
    EditText nameInput, emailInput, phoneInput, addressInput, ageInput, passwordInput, hospitalInput;
    TextView userName;
    CircleImageView profileImage;

    SharedPreferences.Editor editor;
    SharedPreferences sharedPref;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    String img;
    ProgressDialog progressDialog;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_nurse_profile, container, false);

        progressDialog = new ProgressDialog(getActivity());
        initViews();


        getData();
        controlNurseInputs(false);


        return view;
    }

    private void initViews() {
        nameInput = view.findViewById(R.id.name_input);
        emailInput = view.findViewById(R.id.email_input);
        phoneInput = view.findViewById(R.id.phone_input);
        addressInput = view.findViewById(R.id.address_input);
        ageInput = view.findViewById(R.id.age_input);
        passwordInput = view.findViewById(R.id.password_input);
        userName = view.findViewById(R.id.user_name);
        hospitalInput = view.findViewById(R.id.hospital_input);
        profileImage = view.findViewById(R.id.profile_image);


    }

    public void controlNurseInputs(Boolean status) {
        nameInput.setEnabled(status);
        emailInput.setEnabled(status);
        phoneInput.setEnabled(status);
        addressInput.setEnabled(status);
        ageInput.setEnabled(status);
        passwordInput.setEnabled(status);
        hospitalInput.setEnabled(status);


    }

    public void setImage(String url) {
        Glide.with(this)
                .load(url)
                .error(R.drawable.error)
                .into(profileImage);
    }

    private void getData() {
        sharedPref = getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);

        nameInput.setText(sharedPref.getString("nurseName", ""));
        phoneInput.setText(sharedPref.getString("nursePhone", ""));
        emailInput.setText(sharedPref.getString("nurseEmail", ""));
        addressInput.setText(sharedPref.getString("nurseAddress", ""));
        ageInput.setText(sharedPref.getString("nurseAge", ""));
        passwordInput.setText(sharedPref.getString("nursePassword", ""));
        hospitalInput.setText(sharedPref.getString("nurseHospital", ""));
        userName.setText(sharedPref.getString("nurseName", ""));


        img = sharedPref.getString("nursePhoto", "");

    }


    public void saveData(String url) {

        Map<String, Object> maps = new HashMap<>();

        maps.put("nurseName", nameInput.getText().toString());
        maps.put("nursePhone", phoneInput.getText().toString());
        maps.put("nurseEmail", emailInput.getText().toString());
        maps.put("nurseAddress", addressInput.getText().toString());
        maps.put("nurseAge", ageInput.getText().toString());
        maps.put("nursePassword", passwordInput.getText().toString());
        maps.put("nurseHospital", hospitalInput.getText().toString());
        maps.put("nursePhoto", "");


        firebaseFirestore.collection("Nurses").document(sharedPref.getString("nurseId", "0"))
                .update(maps)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Nurse Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}
