package com.example.test.medicalreport.model.doctor;

import android.os.Parcel;
import android.os.Parcelable;

public class Doctor  implements Parcelable {

    private String doctorId;
    private String doctorName;
    private String doctorEmail;
    private String doctorPassword;
    private String doctorSpec;
    private String doctorAddress;
    private String doctorHospital;
    private String doctorPhone;
    private String doctorAge;
    private int userType;
    private  String doctorProfile;


    protected Doctor(Parcel in) {
        doctorId = in.readString();
        doctorName = in.readString();
        doctorEmail = in.readString();
        doctorPassword = in.readString();
        doctorSpec = in.readString();
        doctorAddress = in.readString();
        doctorHospital = in.readString();
        doctorPhone = in.readString();
        doctorAge = in.readString();
        userType = in.readInt();
        doctorProfile = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(doctorId);
        dest.writeString(doctorName);
        dest.writeString(doctorEmail);
        dest.writeString(doctorPassword);
        dest.writeString(doctorSpec);
        dest.writeString(doctorAddress);
        dest.writeString(doctorHospital);
        dest.writeString(doctorPhone);
        dest.writeString(doctorAge);
        dest.writeInt(userType);
        dest.writeString(doctorProfile);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Doctor> CREATOR = new Creator<Doctor>() {
        @Override
        public Doctor createFromParcel(Parcel in) {
            return new Doctor(in);
        }

        @Override
        public Doctor[] newArray(int size) {
            return new Doctor[size];
        }
    };

    public String getDoctorProfile() {
        return doctorProfile;
    }

    public void setDoctorProfile(String doctorProfile) {
        this.doctorProfile = doctorProfile;
    }


    public Doctor(){

    }

    public Doctor(String doctorProfile,String doctorName, String doctorEmail, String doctorPassword, String doctorSpec, String doctorAddress, String doctorHospital, String doctorPhone, String doctorAge, int userType) {
        this.doctorName = doctorName;
        this.doctorProfile=doctorProfile;
        this.doctorEmail = doctorEmail;
        this.doctorPassword = doctorPassword;
        this.doctorSpec = doctorSpec;
        this.doctorAddress = doctorAddress;
        this.doctorHospital = doctorHospital;
        this.doctorPhone = doctorPhone;
        this.doctorAge = doctorAge;
        this.userType = userType;
    }

    public String getDoctorSpec() {
        return doctorSpec;
    }

    public void setDoctorSpec(String doctorSpec) {
        this.doctorSpec = doctorSpec;
    }

    public String getDoctorAddress() {
        return doctorAddress;
    }

    public void setDoctorAddress(String doctorAddress) {
        this.doctorAddress = doctorAddress;
    }

    public String getDoctorHospital() {
        return doctorHospital;
    }

    public void setDoctorHospital(String doctorHospital) {
        this.doctorHospital = doctorHospital;
    }

    public String getDoctorAge() {
        return doctorAge;
    }

    public void setDoctorAge(String doctorAge) {
        this.doctorAge = doctorAge;
    }

    public String getDoctorPhone() {
        return doctorPhone;
    }

    public void setDoctorPhone(String doctorPhone) {
        this.doctorPhone = doctorPhone;
    }



    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }


    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDoctorEmail() {
        return doctorEmail;
    }

    public void setDoctorEmail(String doctorEmail) {
        this.doctorEmail = doctorEmail;
    }

    public String getDoctorPassword() {
        return doctorPassword;
    }

    public void setDoctorPassword(String doctorPassword) {
        this.doctorPassword = doctorPassword;
    }




}
