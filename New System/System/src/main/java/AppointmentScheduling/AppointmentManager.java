package AppointmentScheduling;
import management.Administrator;
import java.time.LocalDate;
import java.util.Scanner;
public class AppointmentManager extends Appointment{
    Scanner scanner = new Scanner(System.in);

    public AppointmentManager(){}

    public static void getAllAppointmentIdList() {
        if (appointmentIdList.isEmpty()) 
            System.out.println("No Appointment Available.");
       System.out.println(appointmentIdList);
    }
    public static void getAllAppointment_list() {
        if (appointmentList.isEmpty())

            System.out.println("No Appointment Available.");
        System.out.println(appointmentList);
    }
    public static Appointment getAppointmentByID(String appointmentID) {
        for (Appointment appointment : appointmentList) {
            if(appointment.getAppointmentId().equals(appointmentID))
                return appointment;
        }
        return null;
    }

    public static void getAppointmentDetailsByID(String appointmentID){
        if (appointmentList.isEmpty())
            System.out.println("No Appointment With Id "+appointmentID);
        else{
            Appointment app = getAppointmentByID(appointmentID);
                    System.out.println(app);
        }
    }


    public void requestAppointment(String doctorID,String patientID){
        String app_date ;
            if(Administrator.getDoctorById(doctorID)==null) {
                System.out.println("No Doctor Found With ID "+ doctorID);
                return ;
            }
            else if(Administrator.getPatientById(patientID) == null ){
                System.out.println("No Patient Found With ID "+ patientID);
                return ;
            }
            else{
                System.out.println("Appointment of "+Administrator.getPatientById(patientID).getName()+" in process with Dr."+ Administrator.getDoctorById(doctorID).getName());
                System.out.println("Enter Appointment Date In Format (YYYY-MM-DD) :");
                app_date =selectDate();
                appointmentList.add(new Appointment("Pending",doctorID,patientID,
                        app_date,"",""));
                appointmentIdList.add(appointmentList.get(appointmentList.size()-1).getAppointmentId());
                System.out.println("Appointment Requested ");

            }            
    }

    public void approveAppointment() {
        System.out.println("Enter Appointment id for Approval : ");
        String appointmentID = scanner.nextLine();
        Appointment app=getAppointmentByID(appointmentID);
        if(app!=null){
            if (app.getStatus().equalsIgnoreCase("pending")) {
            app.setStatus("Approved");
            System.out.println("Appointment with \"id="+appointmentID+"\" approved successfully!");
            return;
            }
            System.out.println("No Pending Appointment Found with id --> "+appointmentID);
            return;
        }
        System.out.println("No appointment found with id --> "+appointmentID);
    }


    public void CancelAppointment(){
        System.out.println("Enter Appointment id to Cancle : ");
        String appointment_ID = scanner.nextLine();
        Appointment app= getAppointmentByID(appointment_ID);
            if(app!=null){
                appointmentIdList.remove(app.getAppointmentId());
                appointmentList.remove(app);
                System.out.println("Appointment with \"id="+appointment_ID+"\" is Cancled");
            }
            System.out.println( "No Appointment Found with id --> " + appointment_ID);
    }


    private String selectDate(){
            return (scanner.nextLine());
        }


    
    @Override
    public String toString(){
        return   super.toString();
    }

}