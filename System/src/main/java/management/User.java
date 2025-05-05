package management;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;
public class User {
    protected String user_id="",Name,phoneNumber,email,
    recovery_email,password;
    protected static int total_docters=0,total_patients=0;
    protected boolean status;
    protected static ArrayList<Patient> listOfPatients=new ArrayList<>();
    protected static ArrayList<Doctor> listOfDoctors=new ArrayList<>();
    public LocalDate registrationDate ,dateOfBirth, lastLogin;
    public static ArrayList<String> All_IDs =new ArrayList<>();
    private static ArrayList<String> tempIDs =new ArrayList<>();
   @SuppressWarnings("unused")
    User(String id_initial){
       user_id+=id_initial+generate_id();
        setRegistrationDate();
   }
    User(String name,String mail,String contact,String id_init){
        this(id_init);
        Name=name;
        email=mail;
        phoneNumber=contact;
    }

public final boolean login(String mail,String pass){
    status=this.email.equals(mail) && this.password.equals(pass);
        return status ; 
}
public void logout(){}
public void update_profile(String id){ }
public void view_profile(String id){ }
public void changePassword(String oldPass,String newPass){
    if(this.password.equals(oldPass))
        this.password=newPass;
}
public String recoverPassword(String recovery_mail){ 
    return (this.recovery_email.equals(recovery_mail)) ?
    this.password :
    "Email not Found";
}
public void deleteAccount(String user_id){ }

public static ArrayList<Patient> getListOfPatients() {
    if (listOfPatients.isEmpty()) 
        System.out.println("No patients found.");
    return listOfPatients;
}
public static ArrayList<Doctor> getListOfDoctors() {
    if (listOfDoctors.isEmpty()) 
        System.out.println("No doctors Available.");
    return listOfDoctors;
}


public void setName(String name) {Name = name;}
public void setEmail(String mail){email=mail;}
public void setPassword(String pass){password=pass;}
public void setDate_of_birth(LocalDate dob) {dateOfBirth = dob;}
public void setPhoneNumber(String contact) {phoneNumber = contact;}
public void setRecovery_email(String recovery_email){this.recovery_email=recovery_email;}
public void setRegistrationDate(){registrationDate = LocalDate.now(); }
public void setLastLogin(LocalDate lastLogin) {this.lastLogin = lastLogin;}

public String getUserId() { return this.user_id; }
public String getName() { return this.Name; }
public String getEmail() { return this.email; }
public String getPhoneNumber() { return this.phoneNumber; } //
public LocalDate getDate_of_birth() {return this.dateOfBirth;}
public LocalDate getLastLogin() {return this.lastLogin;}
public LocalDate getRegistrationDate() {return this.registrationDate;}
public String getStatus(){
    return (this.status)?"Currently LoggedIn":"Currently LoggedOut";
}


public static ArrayList<String> getAll_IDs(String id_initials){
    for (String id:  All_IDs){
        if(id.startsWith(id_initials))
            tempIDs.add(id);     
    }
    if(tempIDs.isEmpty()){
        System.err.println("No IDs Found.");
    }
    return tempIDs;}
public static ArrayList<String> getAll_IDs(){
    if(All_IDs.isEmpty()){
        System.out.println("No IDs Found.");
    }
    return All_IDs;}

public static final String generate_id() {
    Random num=new Random();
    String id="";
    String[] set ={"9","1","5","8","6","5","2","7","3","0"}; 

    for(int i=1;i<=4;i++){
        id+=set[num.nextInt(9)];
    }
//     if(!isUniqueId(id)){
//         All_IDs.remove(id);
//         generate_id();
//     }
    return id;    
}

public static boolean isUniqueId(String ID){
     int frequency=0;
       ArrayList<String> id_list = All_IDs;
    for(String id:id_list){
        if (id.equals(ID)) {
            frequency+=1;
        if (frequency==2)
            return false;
        }

    }
    return true;
}

@Override
public String toString(){
    return
          "ID                    : "+getUserId()+
        "\nName              : "+getName()+
        "\nBirthDate         : "+getDate_of_birth()+
        "\nContact           : "+getPhoneNumber()+
        "\nE-mail             : "+getEmail();
}

}


