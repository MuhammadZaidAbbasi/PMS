package  HealthDataHandling;
import javafx.scene.input.InputEvent;
import management.Administrator;

import java.util.InputMismatchException;

public class VitalSign{
    private int heartRate;//in beats per minutes 
    private int oxygenLevel;//in% like 85% etc
    private int upper,lower;//upper(systolic)  ,  lower(diastolic)
    private String bloodPressure;//like 120/80(upper/lower)mmHg
    private int breathingRate;//in breaths per minutes 
    private double bodyTemperature;//stores in fahrenheit
    private String timestamp;  // date and time when vitals were recorded

    public VitalSign(){}
    public VitalSign(int heart, int upper,int lower, double bodyTemp,int oxygen, int breathing) {
      setDiastolicBloodPressure(lower);
      setSystolicBloodPressure(upper);
      setBloodPressure(upper,lower);
      setBodyTemperature(bodyTemp);
      setBreathingRate(breathing);
      setHeartRate(heart);
      setOxygenLevel(oxygen);
      timestamp=Administrator.currentTimestamp();
    }

    public int getHeartRate() {return heartRate ;}
    public String getBloodPressure() {return bloodPressure;}
    public int getSystolicBloodPressure() {return upper;}
    public int getDiastolicBloodPressure() {return lower;}
    public double getBodyTemperature() {return bodyTemperature;}
    public int getOxygenLevel() { return oxygenLevel;}
    public int getBreathingRate() { return breathingRate;}
    public String getTimestamp(){return timestamp;}
    
    public void setHeartRate(int heartRate) {this.heartRate = heartRate;}
    public void setSystolicBloodPressure(int upper) { this.upper = upper;}
    public void setDiastolicBloodPressure(int lower) { this.lower = lower;}
    private void setBloodPressure(int upper,int lower) {this.bloodPressure = upper+"/"+lower;}
    public void setBodyTemperature(double bodyTemperature) {  this.bodyTemperature = bodyTemperature;}
    public void setOxygenLevel(int oxygenLevel) {this.oxygenLevel = oxygenLevel;}
    public void setBreathingRate(int breathingRate) { this.breathingRate = breathingRate;}


    
    @Override
    public String toString() {
        return "Vitals { " +
                "heartRate = " + heartRate +"BPM"+
                ", bloodPressure = " + bloodPressure +"mmHg"+
                ", bodyTemperature = " + bodyTemperature +"°F"+
                ", oxygenLevel = " + oxygenLevel +"%"+
                ", breathingRate = " + breathingRate +"breathes/min"+
                " }"+"\nTime :"+timestamp;
    }
    

}