package ChatAndVideo;

public class ChatServer {

    // The WhatsApp number with country code, without "+" (ex: 19706603801)
    private final String whatsappNumber;

    public ChatServer(String whatsappNumber) {
        this.whatsappNumber = whatsappNumber;
    }

    // Returns the WhatsApp link with optional pre-filled message
    public String generateWhatsAppLink(String message) {
        // Encode spaces as %20 for the URL
        String encodedMessage = message.replace(" ", "%20");
        return "https://wa.me/" + whatsappNumber + "?text=" + encodedMessage;
    }
}


