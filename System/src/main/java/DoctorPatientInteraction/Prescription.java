package DoctorPatientInteraction;
import management.User;

import java.time.LocalDate;
public class Prescription {
    private String prescriptionID="Med-";
    private String medicineName;
    private String dosage;
    private String schedule;
    private String patientId;
    private String doctorId;
    private LocalDate dateIssued;


    @SuppressWarnings("unused")
    Prescription(){this.prescriptionID+= User.generate_id();}
    Prescription(String patientId,String doctorId,String dosage, String medicineName, String schedule) {
        this();
        this.dateIssued=LocalDate.now();
        this.doctorId=doctorId;
        this.patientId=patientId;
        this.dosage = dosage;
        this.medicineName = medicineName;
        this.schedule = schedule;
    }

    public String getSchedule() { return schedule;}
    public String getMedicineName() {return medicineName;}
    public String getDosage() { return dosage;}
    public LocalDate getDateIssued() {return dateIssued;}
    public String getPrescriptionID() {return prescriptionID;}
    public String getPatientId() {return patientId;}
    public String getDoctorId() { return doctorId;}
    
    
    public void setSchedule(String schedule) {this.schedule = schedule;}
    public void setDosage(String dosage) {this.dosage = dosage;}
    public void setMedicineNmae(String medicineName) {this.medicineName = medicineName;}
    public void setDoctorId(String doctorId) {this.doctorId = doctorId;}
    public void setDateIssued(LocalDate dateIssued) {this.dateIssued = dateIssued;}
    public void setPatientId(String patientId) {this.patientId = patientId;}

    
    @Override
    public String toString(){
        return 
        "PrescriptionID : " + getPatientId()+ "\nPatientID : " + getPatientId()+"\tDoctorID : "+getDoctorId()+
        "\nMedicine : " + getMedicineName() + "( "+getDosage()+" , "+getSchedule()+" )";
    }

}
