package EmergencyAlertSystem;
import management.*;
import HealthDataHandling.VitalSign;
import HealthDataHandling.VitalsDatabase;

public class PanicButton {
    private static boolean triggerAlert=false;


    public static void Press(Patient pat) {
        triggerAlert = true;
        EmergencyAlert alert=new EmergencyAlert();
        alert.alertmessage.append("Urgent Treatement needed by Patient : ").append(pat.getName());
        alert.SendAlert();

        triggerAlert=false;
    }
    public static boolean getTriggerAlert(){return triggerAlert;}
}
