package DoctorPatientInteraction;
import management.*;
import java.util.ArrayList;
public class Feedback {
    //private ArrayList<Feedback> feedback;
    private String note;
    private String patientID;
    private String doctorID;
    //private ArrayList<Prescription> medication=new ArrayList<>();
    private ArrayList<Feedback> feedbacks=new ArrayList<>();

    public Feedback() {
    }

    public String getfeedbackNote() {
        return note;
    }

    public String getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public void setNote(String note) {
        this.note = note;
    }

    // storing feedbacks
    public void storeFeedback() {
        feedbacks.add(this);
    }

    // public String getDoctorFeedback() { 
        
    //     return feedback;}

    public void allFeedbacks() {
        System.out.println("Total Feedbacks are " + (feedbacks.size()));
        for (int i = 0; i < feedbacks.size(); i++) {
                Patient pat = Administrator.getPatientById(feedbacks.get(i).getPatientID());
                Doctor doc = Administrator.getDoctorById(feedbacks.get(i).getDoctorID());
                System.out.println("\nFeedBack " + (i+1) + " :");
                System.out.println("Patient Name   : " + ((pat.getName() != null) ? pat.getName() : "Not Found"));
                System.out.println("Doctor Name    : " + ((doc != null) ? doc.getName() : "Not Found"));
                System.out.println("Feedback Notes : " + feedbacks.get(i).getfeedbackNote() );
            }
        }
    //public Prescription getMedication() { return medication.getFirst();}

    //public void setMediocation(Prescription mediocation) {this.mediocation = mediocation;}
    
@Override
public String toString(){
    return "Patient Name   : "+Administrator.getPatientById(patientID).getName()+
         "\nDoctor Name    : " +Administrator.getDoctorById(doctorID).getName()+
         "\nFeedback Notes : "+note;
}
    
    
}
