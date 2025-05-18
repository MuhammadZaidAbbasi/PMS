package NotificationAndRemainder;

public class RemainderService extends EmailNotification{

    public  void sendAppointmentRemainder(String messageText, String receiver) {
        super.sendMail(messageText, receiver,"Appointment Remainder");
    }
    public void sendMedicationRemainder(String messageText, String receiver) {
        super.sendMail(messageText, receiver,"Medication Remainder");
    }
}
