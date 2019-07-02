package com.example.test.medicalreport.model.nurse;

public class PatientDoctor {
    private String patientDoctorId;

    public String getPatientDoctorId() {
        return patientDoctorId;
    }

    public void setPatientDoctorId(String patientDoctorId) {
        this.patientDoctorId = patientDoctorId;
    }

    public String getPatientDoctorName() {
        return patientDoctorName;
    }

    public void setPatientDoctorName(String patientDoctorName) {
        this.patientDoctorName = patientDoctorName;
    }

    private String patientDoctorName;

    public PatientDoctor(String patientDoctorId, String patientDoctorName) {
        this.patientDoctorId = patientDoctorId;
        this.patientDoctorName = patientDoctorName;
    }
}
