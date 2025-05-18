package management;
import DoctorPatientInteraction.*;
import HealthDataHandling.*;
import EmergencyAlertSystem.*;
import java.util.InputMismatchException;
import java.util.Scanner;
public class Patient extends User {
   // private String medical_history,insuranceDetails,emergencyContact,doctorFeedback;
    private String bloodType;
    private String gender;
    public Feedback doctorFeedback=new Feedback();
    private VitalSign vitals;

@SuppressWarnings("unused")
public Patient(){
    super("P-");
    total_patients++;
}
public Patient(String name,String mail,String contact,String blood){
    super(name, mail, contact,"P-");
    bloodType=blood;
    total_patients++;
}


public void setGender(String gen){gender=gen;}
public void setBloodType(String BT){bloodType=BT;}

public VitalSign getVitals() {return vitals;}
public String getBloodType() { return this.bloodType; }
public String getGender() { return String.valueOf(this.gender); }
public Feedback getDoctorFeedback() {return this.doctorFeedback;}
    public void setDoctorFeedback(Feedback doctorFeedback){this.doctorFeedback = doctorFeedback;}


public void uploadVitals() {

    Scanner scanner = new Scanner(System.in);
int upper=0,lower=0,breath=0,heart=0,oxygen=0;
double temp=0.0;
boolean keepTakingInput=true;

do {
    try {
        System.out.print("Enter Heart Rate (BPM): ");
        heart = scanner.nextInt();
        keepTakingInput=false;
    } catch (InputMismatchException ime) {
        System.out.println("HeartRate must be number(Integer) !!");
        scanner.nextLine();
    }
}while(keepTakingInput);

keepTakingInput=true;

    do {
        try {
            System.out.print("Enter Blood Pressure (Systolic): ");
            upper=scanner.nextInt();
            keepTakingInput=false;
        } catch (InputMismatchException ime) {
            System.out.println("Systolic BP must be number(Integer) !! ");
            scanner.nextLine();
        }
    }while(keepTakingInput);

keepTakingInput=true;

    do {
        try {
            System.out.print("Enter Blood Pressure (Diastolic): ");
            lower=scanner.nextInt();
            keepTakingInput=false;
        } catch (InputMismatchException ime) {
            System.out.println("Diastolic BP must be number(Integer) !! ");
            scanner.nextLine();
        }
    }while(keepTakingInput);

keepTakingInput=true;

    do {
        try {
            System.out.print("Enter Body Temperature (°F): ");
            temp=scanner.nextDouble();
            keepTakingInput=false;
        } catch (InputMismatchException ime) {
            System.out.println("Body Temperature must be number(Double) !! ");
            scanner.nextLine();
        }
    }while(keepTakingInput);


keepTakingInput=true;

    do {
        try {
            System.out.print("Enter Breathing Rate (breaths per minute): ");
            breath=scanner.nextInt();
            keepTakingInput=false;
        } catch (InputMismatchException ime) {
            System.out.println("Breathing Rate must be number(Integer) !! ");
            scanner.nextLine();
        }
    }while(keepTakingInput);

keepTakingInput=true;

    do {
        try {
            System.out.print("Enter Oxygen Level ( % ): ");
            oxygen=scanner.nextInt();

            keepTakingInput=false;
        } catch (InputMismatchException ime) {
            System.out.println("Oxygen Level must be number(Integer) !! ");
            scanner.nextLine();
        }
    }while(keepTakingInput);

    vitals=new VitalSign(heart,upper,lower,temp,oxygen,breath);
    VitalsDatabase.storeVitals(vitals);
    System.out.println("\nYour Health Data uploaded Successfully\n");
}

public void displayVitals() {
    if(vitals!=null){
        System.out.println("\n--- Uploaded Vitals ---");
    System.out.println("Heart Rate: " + vitals.getHeartRate() + " BPM");
    System.out.println("Blood Pressure: " + vitals.getBloodPressure()+" mmHg");
    System.out.println("Body Temperature: " + vitals.getBodyTemperature()+ " °F");
    System.out.println("Respiratory Rate: " + vitals.getBreathingRate() + " breaths/min");
    System.out.println("Blood Oxygen Level: " +vitals.getOxygenLevel() + "%");
    System.out.println("Recorded on : " +vitals.getTimestamp());

    }
    else
        System.out.println("Vitals Not Uploaded Yet");

}


@Override
    public String toString(){
        return"\n\t-------Patient Detail ------"+
              super.toString()+
              "\nBlood Type : "+getBloodType()+"  "+
              "\tGender   : "+getGender()+" ";
    }

    public void clone(Patient pat){
        this.Name=pat.Name;
        this.user_id=pat.user_id;
        this.bloodType=pat.bloodType;
        this.gender=pat.gender;
     }

}
