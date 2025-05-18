package AppointmentScheduling;
import javafx.beans.property.SimpleStringProperty;
import management.*;
import java.util.ArrayList;
public class Appointment{

    private final SimpleStringProperty appointmentId;
    private final SimpleStringProperty status;
    private final SimpleStringProperty doctor;
    private final SimpleStringProperty patient;
    private final SimpleStringProperty date;
    private final SimpleStringProperty time;
    private final SimpleStringProperty reason;


    public static ArrayList<Appointment> appointmentList = new ArrayList<>();
    public static ArrayList<String> appointmentIdList = new ArrayList<>();

    // Default constructor (if needed)
    public Appointment() {
        this("Pending", "", "", "", "","");
    }

    public Appointment(String status, String doctor, String patient, String date, String time,String reason) {
        this.appointmentId = new SimpleStringProperty("A-"+User.generate_id());
        this.status = new SimpleStringProperty(status);
        this.doctor = new SimpleStringProperty(doctor);
        this.patient = new SimpleStringProperty(patient);
        this.date = new SimpleStringProperty(date);
        this.time = new SimpleStringProperty(time);
        this.reason = new SimpleStringProperty(reason);

    }

    // === Getters for TableView ===

    public String getAppointmentId() {
        return appointmentId.get();
    }
    public String getStatus() {
        return status.get();
    }
    public String getDoctor() {
        return doctor.get();
    }
    public String getPatient() {
        return patient.get();
    }
    public String getDate() {
        return date.get();
    }
    public String getTime() {
        return time.get();
    }
    public String getReason() {return reason.get();}


    // === Setters ===

    public void setStatus(String status) {
        this.status.set(status);
    }



    @Override
    public String toString() {
        return "\n\t------ Appointment Detail ------" +
                "\nAppointment ID      : " + getAppointmentId() +
                "\t\tDate  : " + getDate() +
                "\nDoctor              : " + (getDoctor().isEmpty() ? "Not Assigned" : "Dr. " + getDoctor()) +
                "\nPatient             : " + (getPatient().isEmpty() ? "Not Assigned" : getPatient()) +
                "\nStatus              : " + getStatus();
    }
}