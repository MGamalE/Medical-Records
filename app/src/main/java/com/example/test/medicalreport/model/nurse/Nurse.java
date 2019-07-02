package com.example.test.medicalreport.model.nurse;

import android.os.Parcel;
import android.os.Parcelable;

public class Nurse implements Parcelable {

    private String nurseName;
    private String nurseId;
    private int userType;
    private String nursePassword;
    private String nurseEmail;
    private String nursePhone;
    private String nurseAddress;
    private String nurseHospital;
    private String nurseAge;
    private String nursePhoto;




    public Nurse(){

    }

    public Nurse(String nursePhoto,String nurseName, int userType, String nursePassword, String nurseEmail, String nursePhone, String nurseAddress, String nurseHospital, String nurseAge) {
        this.nurseName = nurseName;
        this.userType = userType;
        this.nursePassword = nursePassword;
        this.nurseEmail = nurseEmail;
        this.nursePhone = nursePhone;
        this.nurseAddress = nurseAddress;
        this.nurseHospital = nurseHospital;
        this.nurseAge = nurseAge;
        this.nursePhoto=nursePhoto;
    }

    protected Nurse(Parcel in) {
        nurseName = in.readString();
        nurseId = in.readString();
        userType = in.readInt();
        nursePassword = in.readString();
        nurseEmail = in.readString();
        nursePhone = in.readString();
        nurseAddress = in.readString();
        nurseHospital = in.readString();
        nurseAge = in.readString();
        nursePhoto = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nurseName);
        dest.writeString(nurseId);
        dest.writeInt(userType);
        dest.writeString(nursePassword);
        dest.writeString(nurseEmail);
        dest.writeString(nursePhone);
        dest.writeString(nurseAddress);
        dest.writeString(nurseHospital);
        dest.writeString(nurseAge);
        dest.writeString(nursePhoto);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Nurse> CREATOR = new Creator<Nurse>() {
        @Override
        public Nurse createFromParcel(Parcel in) {
            return new Nurse(in);
        }

        @Override
        public Nurse[] newArray(int size) {
            return new Nurse[size];
        }
    };

    public String getNursePhone() {
        return nursePhone;
    }

    public void setNursePhone(String nursePhone) {
        this.nursePhone = nursePhone;
    }

    public String getNurseAddress() {
        return nurseAddress;
    }

    public void setNurseAddress(String nurseAddress) {
        this.nurseAddress = nurseAddress;
    }

    public String getNurseHospital() {
        return nurseHospital;
    }

    public void setNurseHospital(String nurseHospital) {
        this.nurseHospital = nurseHospital;
    }

    public String getNurseAge() {
        return nurseAge;
    }

    public void setNurseAge(String nurseAge) {
        this.nurseAge = nurseAge;
    }

    public String getNursePhoto() {
        return nursePhoto;
    }

    public void setNursePhoto(String nursePhoto) {
        this.nursePhoto = nursePhoto;
    }



    public String getNurseName() {
        return nurseName;
    }

    public void setNurseName(String nurseName) {
        this.nurseName = nurseName;
    }

    public String getNursePassword() {
        return nursePassword;
    }

    public void setNursePassword(String nursePassword) {
        this.nursePassword = nursePassword;
    }

    public String getNurseEmail() {
        return nurseEmail;
    }

    public void setNurseEmail(String nurseEmail) {
        this.nurseEmail = nurseEmail;
    }

    public String getNurseId() {
        return nurseId;
    }

    public void setNurseId(String nurseId) {
        this.nurseId = nurseId;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }






}
