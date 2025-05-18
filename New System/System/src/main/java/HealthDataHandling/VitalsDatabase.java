package  HealthDataHandling;
import java.util.ArrayList;
public class VitalsDatabase extends VitalSign {
    public static ArrayList <VitalSign> DataBase = new ArrayList<>();

    public static void storeVitals(VitalSign vitals){
        DataBase.add(vitals);
    }

    public void seeAllVitals(){
        for(VitalSign vital : DataBase) {
            System.out.println(vital);
        }
    }
    

}
