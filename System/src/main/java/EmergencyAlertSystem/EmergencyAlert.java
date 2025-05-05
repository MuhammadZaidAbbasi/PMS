package EmergencyAlertSystem;
import HealthDataHandling.*;
import NotificationAndRemainder.EmailNotification;
public class EmergencyAlert {
    EmailNotification Alert = new EmailNotification();
    StringBuilder alertmessage = new StringBuilder();

    public void SendAlert() {
        if (PanicButton.getTriggerAlert() ) {
            Alert.sendMail(alertmessage.toString(), "mabbasi.bsds24seecs@seecs.edu.pk", "🚨 Panic Button Pressed!");
        }
    }
    public void checkVitalsAndSendAlert(VitalSign vital){
        if( areVitalsExceedingLimit(vital))
            Alert.sendMail(alertmessage.toString(), "mabbasi.bsds24seecs@seecs.edu.pk", "🚨 Vitals Alert!");
    }

    public boolean  areVitalsExceedingLimit(VitalSign vital) {

        if (isHeartRateAbnormal(vital.getHeartRate()))
            alertmessage.append("🚨 Abnormal Heart Rate : ").append(vital.getHeartRate()).append(" bpm\n");

        if (isOxygenLevelAbnormal(vital.getOxygenLevel()))
            alertmessage.append("🚨 Low Oxygen Level : ").append(vital.getOxygenLevel()).append("%\n");

        if (isBloodPressureAbnormal(vital.getSystolicBloodPressure(), vital.getDiastolicBloodPressure())) {
            alertmessage.append("🚨 Abnormal Blood Pressure : ").
                    append(vital.getSystolicBloodPressure()).append("/").
                    append(vital.getDiastolicBloodPressure()).append(" mmHg\n");
        }

        if (isBreathingRateAbnormal(vital.getBreathingRate()))
            alertmessage.append("️🚨 Abnormal Breathing Rate : ").append(vital.getBreathingRate()).append(" breaths/min\n");

        if (isBodyTempAbnormal(vital.getBodyTemperature()))
            alertmessage.append("🚨 Abnormal Body Temperature : ").append(vital.getBodyTemperature()).append(" °F\n");

        if (alertmessage.length() > 0 ) {
           return true;
        } else {
            System.out.println("All vitals are within normal range.");
            return false;
        }
    }





    public static boolean isHeartRateAbnormal(int heartRate) {
        return heartRate < 60 || heartRate > 100;
    }
    public static boolean isOxygenLevelAbnormal(int oxygenLevel) {
        return oxygenLevel < 90;
    }
    public static boolean isBloodPressureAbnormal(int upper, int lower) {
        return upper < 90 || upper > 120 || lower < 60 || lower > 80;
    }
    public static boolean isBreathingRateAbnormal(int breathingRate) {
        return breathingRate < 12 || breathingRate > 20;
    }
    public static boolean isBodyTempAbnormal(double temperature) {
        return temperature < 97.0 || temperature > 99.0;
    }

}
