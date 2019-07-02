package com.example.test.medicalreport.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.test.medicalreport.model.doctor.Doctor;
import com.example.test.medicalreport.model.nurse.PatientDoctor;

import java.util.ArrayList;
import java.util.Map;

public class Patient implements Parcelable {

    private String patientId;
    private String patientName;
    private String patientAge;
    private String patientHeight;
    private String patientWeight;
    private String patientAddress;
    private String patientSSN;
    private String patientPhone;
    private String patientGender;
    private String patientPressure;
    private String patientBlood;
    private String patientVirus;
    private String patientDiabetes;
    private String patientXRayImage;
    private boolean doctorAddedData;
    private String timeStamp;
    private String patientScanImage;
    private String patientDiagnosises;
    private String patientDoctorImage1;
    private String patientDoctorImage2;
    private String patientPassword;
    private String patientPhoto;
    private String patientEmail;
    private String patientUncleName;
    private String patientDaughterName;


    public String getPatientPassword() {
        return patientPassword;
    }

    public void setPatientPassword(String patientPassword) {
        this.patientPassword = patientPassword;
    }

    public String getPatientPhoto() {
        return patientPhoto;
    }

    public void setPatientPhoto(String patientPhoto) {
        this.patientPhoto = patientPhoto;
    }

    public String getPatientEmail() {
        return patientEmail;
    }

    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }

    public String getPatientUncleName() {
        return patientUncleName;
    }

    public void setPatientUncleName(String patientUncleName) {
        this.patientUncleName = patientUncleName;
    }

    public String getPatientDaughterName() {
        return patientDaughterName;
    }

    public void setPatientDaughterName(String patientDaughterName) {
        this.patientDaughterName = patientDaughterName;
    }

    public Patient() {
    }

    protected Patient(Parcel in) {
        patientId = in.readString();
        patientName = in.readString();
        patientAge = in.readString();
        patientHeight = in.readString();
        patientWeight = in.readString();
        patientAddress = in.readString();
        patientSSN = in.readString();
        patientPhone = in.readString();
        patientGender = in.readString();
        patientPressure = in.readString();
        patientBlood = in.readString();
        patientVirus = in.readString();
        patientDiabetes = in.readString();
        patientXRayImage = in.readString();
        doctorAddedData = in.readByte() != 0;
        timeStamp = in.readString();
        patientScanImage = in.readString();
        patientDiagnosises = in.readString();
        patientDoctorImage1 = in.readString();
        patientDoctorImage2 = in.readString();
        patientPassword = in.readString();
        patientPhoto = in.readString();
        patientEmail = in.readString();
        patientUncleName = in.readString();
        patientDaughterName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(patientId);
        dest.writeString(patientName);
        dest.writeString(patientAge);
        dest.writeString(patientHeight);
        dest.writeString(patientWeight);
        dest.writeString(patientAddress);
        dest.writeString(patientSSN);
        dest.writeString(patientPhone);
        dest.writeString(patientGender);
        dest.writeString(patientPressure);
        dest.writeString(patientBlood);
        dest.writeString(patientVirus);
        dest.writeString(patientDiabetes);
        dest.writeString(patientXRayImage);
        dest.writeByte((byte) (doctorAddedData ? 1 : 0));
        dest.writeString(timeStamp);
        dest.writeString(patientScanImage);
        dest.writeString(patientDiagnosises);
        dest.writeString(patientDoctorImage1);
        dest.writeString(patientDoctorImage2);
        dest.writeString(patientPassword);
        dest.writeString(patientPhoto);
        dest.writeString(patientEmail);
        dest.writeString(patientUncleName);
        dest.writeString(patientDaughterName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Patient> CREATOR = new Creator<Patient>() {
        @Override
        public Patient createFromParcel(Parcel in) {
            return new Patient(in);
        }

        @Override
        public Patient[] newArray(int size) {
            return new Patient[size];
        }
    };

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientAge() {
        return patientAge;
    }

    public void setPatientAge(String patientAge) {
        this.patientAge = patientAge;
    }

    public String getPatientHeight() {
        return patientHeight;
    }

    public void setPatientHeight(String patientHeight) {
        this.patientHeight = patientHeight;
    }

    public String getPatientWeight() {
        return patientWeight;
    }

    public void setPatientWeight(String patientWeight) {
        this.patientWeight = patientWeight;
    }

    public String getPatientAddress() {
        return patientAddress;
    }

    public void setPatientAddress(String patientAddress) {
        this.patientAddress = patientAddress;
    }

    public String getPatientSSN() {
        return patientSSN;
    }

    public void setPatientSSN(String patientSSN) {
        this.patientSSN = patientSSN;
    }

    public String getPatientPhone() {
        return patientPhone;
    }

    public void setPatientPhone(String patientPhone) {
        this.patientPhone = patientPhone;
    }

    public String getPatientGender() {
        return patientGender;
    }

    public void setPatientGender(String patientGender) {
        this.patientGender = patientGender;
    }

    public String getPatientPressure() {
        return patientPressure;
    }

    public void setPatientPressure(String patientPressure) {
        this.patientPressure = patientPressure;
    }

    public String getPatientBlood() {
        return patientBlood;
    }

    public void setPatientBlood(String patientBlood) {
        this.patientBlood = patientBlood;
    }

    public String getPatientVirus() {
        return patientVirus;
    }

    public void setPatientVirus(String patientVirus) {
        this.patientVirus = patientVirus;
    }

    public String getPatientDiabetes() {
        return patientDiabetes;
    }

    public void setPatientDiabetes(String patientDiabetes) {
        this.patientDiabetes = patientDiabetes;
    }

    public String getPatientXRayImage() {
        return patientXRayImage;
    }

    public void setPatientXRayImage(String patientXRayImage) {
        this.patientXRayImage = patientXRayImage;
    }

    public boolean isDoctorAddedData() {
        return doctorAddedData;
    }

    public void setDoctorAddedData(boolean doctorAddedData) {
        this.doctorAddedData = doctorAddedData;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getPatientScanImage() {
        return patientScanImage;
    }

    public void setPatientScanImage(String patientScanImage) {
        this.patientScanImage = patientScanImage;
    }

    public String getPatientDiagnosises() {
        return patientDiagnosises;
    }

    public void setPatientDiagnosises(String patientDiagnosises) {
        this.patientDiagnosises = patientDiagnosises;
    }

    public String getPatientDoctorImage1() {
        return patientDoctorImage1;
    }

    public void setPatientDoctorImage1(String patientDoctorImage1) {
        this.patientDoctorImage1 = patientDoctorImage1;
    }

    public String getPatientDoctorImage2() {
        return patientDoctorImage2;
    }

    public void setPatientDoctorImage2(String patientDoctorImage2) {
        this.patientDoctorImage2 = patientDoctorImage2;
    }



    public Patient(String patientName, String patientPassword, String patientEmail, String patientUncleName, String patientDaughterName) {
        this.patientName = patientName;
        this.patientPassword = patientPassword;
        this.patientEmail = patientEmail;
        this.patientUncleName = patientUncleName;
        this.patientDaughterName = patientDaughterName;
    }






    public Patient(boolean doctorAddedData, String patientName, String patientAge, String patientHeight, String patientWeight, String patientAddress, String patientSSN, String patientPhone, String patientGender, String patientPressure, String patientBlood, String patientVirus, String patientDiabetes, String patientXRayImage, String patientScanImage, String timeStamp) {
        this.doctorAddedData=doctorAddedData;
        this.patientName = patientName;
        this.patientAge = patientAge;
        this.patientHeight = patientHeight;
        this.patientWeight = patientWeight;
        this.patientAddress = patientAddress;
        this.patientSSN = patientSSN;
        this.patientPhone = patientPhone;
        this.patientGender = patientGender;
        this.patientPressure = patientPressure;
        this.patientBlood = patientBlood;
        this.patientVirus = patientVirus;
        this.patientDiabetes = patientDiabetes;
        this.patientXRayImage = patientXRayImage;
        this.patientScanImage = patientScanImage;
        this.timeStamp = timeStamp;
    }


}
