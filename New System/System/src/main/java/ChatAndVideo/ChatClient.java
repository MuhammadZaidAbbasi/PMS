package ChatAndVideo;
import java.awt.*;
public class ChatClient {

    private final ChatServer chatServer;

    public ChatClient(ChatServer chatServer) {
        this.chatServer = chatServer;
    }

    // Opens the WhatsApp link in the user's browser
    public void startChat(String message) {
        String chatLink = chatServer.generateWhatsAppLink(message);
        System.out.println("🔗 Click the link to start chat: " + chatLink);

        try {
            // Opens the link in the default browser (works on most systems)
            java.awt.Desktop.getDesktop().browse(new java.net.URI(chatLink));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Unable to open browser. Please copy and paste the link manually.");
        }
    }
}

