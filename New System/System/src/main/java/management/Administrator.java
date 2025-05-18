package management;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
public class Administrator extends User {
    //private int totalUsers=total_docters+total_patients ;
    private static ArrayList<String> logs=new ArrayList<>();

    public Administrator() {
        super("Ad-");
    }

    public static ArrayList<String> getLogs() {
        return logs;
    }

    public static Doctor getDoctorById(String doctorID) {
        for (Doctor doc : listOfDoctors) {
            if (doc.getUserId().equalsIgnoreCase(doctorID)) {
                return doc;
            }
        }
        return null;
    }
    public static Patient getPatientById(String patientID) {
        for (Patient pat : listOfPatients) {
            if (pat.getUserId().equalsIgnoreCase(patientID)) {
                return pat;
            }
        }
        return null;
    }

    public static void setLogs(ArrayList<String> logs) {
        Administrator.logs = logs;
    }

    public static void  addPatient(Patient pat) {
        listOfPatients.add(pat);
        All_IDs.add(pat.user_id);    
    }
    public static void  addPatient() {
        Patient pat=new Patient();
        Scanner scanner=new Scanner(System.in);
        System.out.print("Enter Patient's Name : ");
        pat.setName(scanner.nextLine());
        System.out.print("Enter Patient's Birth Date (YYYY/MM/DD) : ");
        pat.setDate_of_birth(LocalDate.parse(scanner.nextLine()));
        System.out.print("Enter Patient's Blood Type : "); 
        pat.setBloodType(scanner.nextLine());
        System.out.print("Enter Patient's e-mail  : "); 
        pat.setEmail(scanner.nextLine());
        System.out.print("Enter Patient's Contact : "); 
        pat.setPhoneNumber(scanner.nextLine());
        pat.setRegistrationDate();
        System.out.print("Enter Patient's Gender : "); 
        pat.setGender(scanner.nextLine());
        listOfPatients.add(pat);    
    }




    public static void removePatient(String patID){
        Patient patient=getPatientById(patID);
        if(patient!=null){
        listOfPatients.remove(patient);
        All_IDs.remove(patID);
        total_patients--;
        System.out.println("Patient removed wuith id "+patID);
        }
        else 
            System.out.println("No Patient Found With Id "+patID);
    }



    public static void addDoctor(Doctor doctor) {
        listOfDoctors.add(doctor);    
        All_IDs.add(doctor.user_id);
    }
    public static void  addDoctor() {
        Doctor doc=new Doctor();
        Scanner scanner=new Scanner(System.in);
        System.out.print("Enter Doctor's Name : ");
        doc.setName(scanner.nextLine());
        System.out.print("Enter Doctor's Birth Date (YYYY/MM/DD) : "); 
        doc.setDate_of_birth(LocalDate.parse(scanner.nextLine()));
        System.out.print("Enter Doctor's Experience : "); 
        doc.setExperience(scanner.nextLine());
        System.out.print("Enter Doctor's e-mail  : "); 
        doc.setEmail(scanner.nextLine());
        System.out.print("Enter Doctor's Contact : "); 
        doc.setPhoneNumber(scanner.nextLine());
        doc.setRegistrationDate();
        System.out.print("Enter Doctor's Specilization : "); 
        doc.setSpecialization(scanner.nextLine());
        listOfDoctors.add(doc);    
    }
    public static void removeDoctor(String docID){
        Doctor doctor=getDoctorById(docID);
        if(doctor!=null){
        listOfDoctors.remove(doctor);
        All_IDs.remove(docID);
        System.out.println("Doctor removed With Id "+docID);
        total_docters--;
        }
        else 
            System.out.println("No Doctor Found With Id "+docID);
    }
    
    public void displayLogs() {
        System.out.println("Total Logs in System are " + logs.size() + "\n");
        if (logs.isEmpty()) {
            System.out.println("No logs available!");
            return;
        }
        int counter = 1;
        for (String log: logs) {
            System.out.println("\t" + (counter++) +") " + log);
        }
    }

    public void deleteLog(int position) {
        position -= 1;
        if (position >= 0 && position < logs.size()) {
            logs.remove(position);
            System.out.println("Log Deleted Successfully");
        }else {
            System.out.println("Invalid Position entered. Plz enter correct one");
        }
    }

    public static String currentTimestamp() {
        return "["+LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss "))+"]";
    }


}
