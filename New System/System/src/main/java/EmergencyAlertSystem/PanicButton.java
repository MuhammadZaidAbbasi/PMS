package EmergencyAlertSystem;
import management.*;
import HealthDataHandling.VitalSign;
import HealthDataHandling.VitalsDatabase;

public class PanicButton {
    private static boolean triggerAlert=false;


    public static void Press(String patName,String To) {
        triggerAlert = true;
        EmergencyAlert alert=new EmergencyAlert();
        alert.alertmessage.append("Urgent Treatment needed by Patient : ").append(patName);
        alert.SendAlert(To,"🚨 Emergency Alert for !"+patName);

        triggerAlert=false;
    }
    public static boolean getTriggerAlert(){return triggerAlert;}
}
