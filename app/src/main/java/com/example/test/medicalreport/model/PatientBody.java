package com.example.test.medicalreport.model;

public class PatientBody {
    private String patientId;
    private String patientName;
    private String patientAge;
    private String patientHeight;
    private String patientWeight;
    private String patientAddress;
    private String patientSSN;
    private String patientPhone;

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

    public PatientBody(String patientId, String patientName, String patientAge, String patientHeight, String patientWeight, String patientAddress, String patientSSN, String patientPhone, String patientGender, String patientPressure, String patientBlood, String patientVirus, String patientDiabetes, String patientXRayImage, boolean doctorAddedData, String timeStamp, String patientScanImage) {
        this.patientId = patientId;
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
        this.doctorAddedData = doctorAddedData;
        this.timeStamp = timeStamp;
        this.patientScanImage = patientScanImage;
    }
}
