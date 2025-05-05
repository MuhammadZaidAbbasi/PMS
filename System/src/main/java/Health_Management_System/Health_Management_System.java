package Health_Management_System;
import management.*;
import AppointmentScheduling.*;
import EmergencyAlertSystem.*;
import ChatAndVideo.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;
import static Database.DataBaseConnection.getConnection;
import static HealthDataHandling.VitalsDatabase.DataBase;

public class Health_Management_System {
    public static void main(String[] args) {
    //    Hospital H1=new Hospital("AlShifa ","152");
//         Date d=new Date(105,9,25); 
// User zaid=new User("U135","Zaid Abbasi","mabbasi.bsds","015");
@SuppressWarnings("unused")
Scanner scanner = new Scanner(System.in);
// VitalSign v1=new VitalSign(9.1, 12.5,37.5,75.1,1.5,10);
// VitalSign v2=new VitalSign(9.1, 2.5,38.5,55.0,15,100);
// VitalsDatabase db=new VitalsDatabase();
// db.UploadVitals(v1);
// db.UploadVitals(v2);
// db.seeVitals();

//       //  zaid.setEmail("mabbasi.bsds@seecs.com");
//         zaid.setPassword("517866");
//       zaid.setRegistrationDate(d);
        
//         umer.setEmail("mnizam.bece@seecs.com");
//         umer.setPassword("517855");
        // umer.setRegistrationDate(d);








// if(     !User.addPatients(Zaid))
//     System.out.println("Patient Not Added");
// else
//     System.out.println("Patient Added");

// pat.setName("Umer Nizam ud din");
// if(     !User.addPatients(pat))
//     System.out.println("Patient Not Added");
// else
//     System.out.println("Patient Added");


        try (Connection conn = getConnection()) {
            System.out.println("Connected to MySQL!");
        } catch (SQLException e) {
            e.printStackTrace();
        }


//
//Patient Zaid=new Patient("Zaid Abbasi","mail","12-3456","A+");
//
//        Patient pat=new Patient();
//
//Doctor john=new Doctor("john","ENT","5 years","B-515");
//Doctor mickel=new Doctor("mickel","Cardiologist","15 months","B-52475");
////
////Feedback feed=new Feedback();
//AppointmentManager am=new AppointmentManager();
//Administrator.addDoctor(mickel);
//Administrator.addDoctor(john);
//Administrator.addPatient(pat);
//Administrator.addPatient(Zaid);


//ChatClient newchat = new ChatClient(new ChatServer("923312707515"));
//newchat.startChat("");
//Zaid.uploadVitals();
//System.out.println(Zaid);
//Zaid.displayVitals();
//
//EmergencyAlert alert = new EmergencyAlert();
//alert.checkVitalsAndSendAlert(Zaid.getVitals());

        //        PanicButton.Press(Zaid);
//        EmailNotification e = new EmailNotification();
//        e.sendMail("hfghfhtgdcghfjjh","mabbasi.bsds24seecs@seecs.edu.pk","TESTing");
//



// Administrator.addPatient();
//System.out.println(User.getAll_IDs());
//AppointmentManager.getAllAppointmentID_list();
//
//am.requestAppointment(john.getUser_id(),Zaid.getUser_id());
//AppointmentManager.getAllAppointmentID_list();
//am.approveAppointment();
//
//am.requestAppointment(mickel.getUser_id(),Zaid.getUser_id());
//AppointmentManager.getAllAppointmentID_list();
//am.approveAppointment();
//
//AppointmentManager.getAllAppointmentID_list();
//
//am.CancelAppointment();
//AppointmentManager.getAllAppointmentID_list();
//am.CancelAppointment();
//AppointmentManager.getAllAppointmentID_list();
//AppointmentManager.getAllAppointment_list();




// AppointmentManager.getAllAppointmentID_list();

// AppointmentManager.getAppointmentDetailsByID();

// AppointmentManager.getAppointmentDetailsByID(am.getAppointment_id());




// System.out.println(User.total_patients); 
// Administrator.removePatient(Zaid.getUser_id()); 
// System.out.println(User.total_patients); 
// System.out.println(User.total_docters); 
// Administrator.removePatient("P-1234"); 
// Administrator.removePatient(pat.user_id); 
// System.out.println(User.total_patients); 
// john.giveFeedbackToPatient();
// mickel.giveFeedbackToPatient(Zaid.getUser_id());
//System.out.println(User.getAll_IDs());           
// System.out.println(Zaid.getDoctorFeedback());
//feed.allFeedbacks();
//john.seePatientsData();
//Zaid.uploadVitals();           
//System.out.println("Enter id to see feedback"); 
//Administrator.getPatientById(scanner.nextLine()).displayVitals();          
//john.seePatientsData();
// for (Patient  pate: User.getListOfPatients()) {
//     System.err.println(pate);
// }
// Zaid.setPassword("pass");
// Zaid.login("mail","pss");
// john.giveFeedback(Zaid,"Need Panadol");
// System.out.println(Zaid.getDoctorFeedback());
    //     if(zaid.login("mabbasi.bsds@seecs.com","517866"))
    //         System.out.println("ZAID");
    //     if(umer.login("mnizam.bece@seecs.com","517855"))
         //   System.out.println(umer);
            // System.out.println(Zaid);
            // System.out.println(john);
            // System.out.println(pat);
// System.out.println(User.getListOfPatients());           
// // System.out.println(User.total_docters);
// // System.out.println(User.total_patients);
// AppointmentManager manager=new AppointmentManager();
// // AppointmentManager abd=new AppointmentManager("abd");
// manager.bookAppointment(john.getUser_id(),pat.getUser_id());
// manager.bookAppointment(mickel.getUser_id(),Zaid.getUser_id());

// // abd.bookAppointment(mickel.getName(),Zaid.getName());
// // Appointment.addAppointment(shan.appointment_id);
// // Appointment.addAppointment(abd.appointment_id);
// // ///shan.bookAppointmentWith(john.getName(),Zaid.getName());
// // System.out.println(shan);
// System.out.println(Appointment.getAllAppointment_list());         
// System.out.println(Appointment.getAllAppointmentID_list());
// System.out.println(manager.CancelAppointment(Appointment.appointment_list.get(0).getAppointment_id()));         
// System.out.println(Appointment.getAllAppointmentID_list());         
// System.out.println(Appointment.getAllAppointment_list());         

//System.out.print(zaid.login("mabbasi.bsds@sehhs.com","517866"));
// String Temp[]=H1.getDoctorsList();
// for(int i=0;i<=H1.getNumberOfDoctors();i++)
//         System.out.print(Temp[i] + " ");
//     // System.out.print(H1.getHospitalName());
    // System.out.print(H1.getEmergencyContact());


    }
}
