package com.example.system;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.sql.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static Database.DataBaseConnection.getConnection;

public class ChatClient extends Application {

    private static String staticUserId;
    private static String staticRole;

    private String userId;
    private String role;

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    private VBox chatBox;
    private TextField messageField;
    private String receiverUserId;
    private String receiverName;
    private Stage primaryStage;

    public ChatClient(String userId, String role) {
        ChatClient.staticUserId = userId;
        ChatClient.staticRole = role;
    }

    public ChatClient() {
        // Required default constructor for JavaFX
    }

    @Override
    public void start(Stage primaryStage) {
        this.userId = staticUserId;
        this.role = staticRole;
        this.primaryStage = primaryStage;

        if (userId.isBlank()|| role == null || role.isBlank()) {
            showErrorAndExit("User ID or role missing. Cannot open chat.");
            return;
        }

        showContactSelectionScreen();
    }

    private void showContactSelectionScreen() {
        Label title = new Label("Select a contact to chat with:");
        title.setStyle("-fx-font-size: 18px; -fx-text-fill: #e0f0ff; -fx-font-weight: bold;");

        ListView<String> contactsList = new ListView<>();
        contactsList.setStyle(
                "-fx-control-inner-background: #e8f5e9;" +
                        "-fx-text-fill: #1b5e20;" +
                        "-fx-border-color: #66bb6a;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;"
        );


        List<String> contacts = fetchContacts();
        if (contacts.isEmpty()) {
            DoctorDashboard.showAlert(Alert.AlertType.ERROR,"Nothing Found","No contacts found for this user.");
            return;
        }

        contactsList.getItems().addAll(contacts);

        Button startChatButton = new Button("Start Chat");
        startChatButton.setStyle(
                "-fx-background-color: linear-gradient(#ff6f00, #d84315);" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 10;" +
                        "-fx-padding: 8 16;" +
                        "-fx-font-weight: bold;"
        );


        startChatButton.setOnAction(e -> {
            String selected = contactsList.getSelectionModel().getSelectedItem();
            if (selected != null && selected.contains(":")) {
                String[] parts = selected.split(":", 2);
                receiverUserId = parts[0];
                receiverName = parts[1];

                if (connectToServer()) {
                    showChatInterface();
                } else {
                    showError("Unable to connect to chat server.");
                }
            } else {
                showError("Please select a contact.");
            }
        });

        VBox layout = new VBox(20, title, contactsList, startChatButton);
        layout.setStyle("-fx-background-color: #0d1b2a;");
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(30));

        primaryStage.setScene(new Scene(layout, 380, 450));
        primaryStage.setTitle("Select Contact");
        primaryStage.show();
    }

    private boolean connectToServer() {
        try {
            socket = new Socket("localhost", 12345);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            out.println(userId);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showChatInterface() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #0d1b2a;");

        chatBox = new VBox(10);
        chatBox.setPadding(new Insets(10));

        ScrollPane scrollPane = new ScrollPane(chatBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #102841; -fx-border-color: #3498db;");

        List<ChatMessage> previousMessages = fetchMessages(userId, receiverUserId);
        for (ChatMessage msg : previousMessages) {
            if (msg.getSenderId().equals(userId)) {
                displayOwnMessage(msg.getMessageText());
            } else {
                displayIncomingMessage("User " + msg.getSenderId(), msg.getMessageText());
            }
        }

        messageField = new TextField();
        messageField.setPromptText("Type your message...");
        messageField.setStyle(
                "-fx-background-color: #e8f5e9;" +
                        "-fx-text-fill: black;" +
                        "-fx-background-radius: 8;" +
                        "-fx-border-color: #66bb6a;" +
                        "-fx-border-radius: 8;"
        );
        messageField.setPrefHeight(20);
        messageField.setPrefWidth(350);
        messageField.setFont(Font.font("Arial",FontWeight.BOLD,16));
        Button sendButton = new Button("Send");
        sendButton.setStyle(
                "-fx-background-color: #43a047;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bolder;" +
                        "-fx-background-radius: 10;" +
                        "-fx-padding: 6 16;"
        );
        sendButton.setPrefWidth(90);
        sendButton.setPrefHeight(25);

        sendButton.setOnAction(e -> sendMessage());

        HBox inputArea = new HBox(10, messageField, sendButton);
        inputArea.setPadding(new Insets(10));
        inputArea.setAlignment(Pos.CENTER);

        root.setCenter(scrollPane);
        root.setBottom(inputArea);

        primaryStage.setScene(new Scene(root, 550, 620));
        primaryStage.setTitle("Chat with: " + receiverName);
        primaryStage.show();

        new Thread(this::receiveMessages).start();
    }


    private void sendMessage() {
        String text = messageField.getText().trim();
        if (!text.isEmpty() && out != null && receiverUserId !=null) {
            try (Connection connection = getConnection()) {
                String query = "INSERT INTO chat_messages (sender_id, receiver_id, message_text) VALUES (?, ?, ?)";
                try (PreparedStatement stmt = connection.prepareStatement(query)) {
                    stmt.setString(1, userId);
                    stmt.setString(2, receiverUserId);
                    stmt.setString(3, text);
                    stmt.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showError("Database error while sending message.");
            }

            out.println("TO:" + receiverUserId + ":" + text);
            displayOwnMessage(text);
            messageField.clear();
        }
    }

    private void receiveMessages() {
        try {
            String incomingLine;
            while ((incomingLine = in.readLine()) != null) {
                if (incomingLine.startsWith("FROM:")) {
                    processIncomingMessage(incomingLine.substring(5));
                }
            }
        } catch (IOException e) {
            Platform.runLater(() -> showErrorAndExit("Connection lost. Please restart the app."));
        }
    }

    private void processIncomingMessage(String rawMessage) {
        String[] parts = rawMessage.split(":", 2);
        if (parts.length == 2) {
            try {
                String senderId = parts[0];
                String messageText = parts[1];

                Platform.runLater(() -> {
                    displayIncomingMessage("User " + senderId, messageText);
                    markMessageAsSeen(senderId);
                });
            } catch (NumberFormatException e) {
                System.err.println("Invalid sender ID: " + parts[0]);
            }
        }
    }

    private void displayOwnMessage(String text) {
        VBox messageContent = new VBox(5);

        Label messageLabel = new Label(text);
        messageLabel.setWrapText(true);
        messageLabel.setStyle("-fx-background-color: #00796b; -fx-text-fill: white; -fx-padding: 10px; -fx-background-radius: 10px;");

        Label timeLabel = new Label(currentTime());
        timeLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #ccc;");

        messageContent.getChildren().addAll(messageLabel, timeLabel);
        messageContent.setAlignment(Pos.CENTER_RIGHT);

        HBox messageBubble = new HBox(messageContent);
        messageBubble.setAlignment(Pos.CENTER_RIGHT);
        messageBubble.setPadding(new Insets(5, 10, 5, 50));

        chatBox.getChildren().add(messageBubble);
    }

    private void displayIncomingMessage(String sender, String text) {
        VBox messageContent = new VBox(5);

        Label messageLabel = new Label(text);
        messageLabel.setWrapText(true);
        messageLabel.setStyle("-fx-background-color: #424242; -fx-text-fill: white; -fx-padding: 10px; -fx-background-radius: 10px;");

        Label timeLabel = new Label(currentTime());
        timeLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #bbb;");

        messageContent.getChildren().addAll(messageLabel, timeLabel);
        messageContent.setAlignment(Pos.CENTER_LEFT);

        HBox messageBubble = new HBox(messageContent);
        messageBubble.setAlignment(Pos.CENTER_LEFT);
        messageBubble.setPadding(new Insets(5, 50, 5, 10));

        chatBox.getChildren().add(messageBubble);
    }

    private String currentTime() {
        return LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    private void markMessageAsSeen(String senderId) {
        String updateQuery = " UPDATE chat_messages SET seen = TRUE WHERE receiver_id = ? AND sender_id = ? AND seen = FALSE ";

        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(updateQuery)) {
            stmt.setString(1, userId);
            stmt.setString(2, senderId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private List<ChatMessage> fetchMessages(String user1, String user2) {
        List<ChatMessage> messages = new ArrayList<>();
        String query = " SELECT sender_id, message_text, sent_time FROM chat_messages "+
            " WHERE (sender_id = ? AND receiver_id = ?) OR (sender_id = ? AND receiver_id = ?) ORDER BY sent_time";


        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, user1);
            stmt.setString(2, user2);
            stmt.setString(3, user2);
            stmt.setString(4, user1);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    messages.add(new ChatMessage(
                            rs.getString("sender_id"),
                            rs.getString("message_text"),
                            rs.getTimestamp("sent_time")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Failed to fetch messages.");
        }

        return messages;
    }
    private List<String> fetchContacts() {
        List<String> contacts = new ArrayList<>();

        try (Connection connection = getConnection()) {

            String query = switch (role.toLowerCase()) {
                case "patient" -> "SELECT DISTINCT d.doctor_id, d.name " +
                        "FROM doctor d " +
                        "JOIN doctor_patient_assignment dap ON d.doctor_id = dap.doctor_id " +
                        "WHERE dap.patient_id = ?";
                case "doctor" -> "SELECT DISTINCT p.patient_id, p.name " +
                        "FROM patient p " +
                        "JOIN doctor_patient_assignment dap ON p.patient_id = dap.patient_id " +
                        "WHERE dap.doctor_id = ?";
                default -> {
                    showError("Invalid role: " + role);
                    yield null;
                }
            };

            if (query == null) return contacts;

            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, staticUserId);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        contacts.add(rs.getString(1) + ":" + rs.getString(2));  // e.g., "5:Dr. Smith"
                    }
                }
            }
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            showError("Database error while fetching contacts.");
        }

        return contacts;
    }

    private void showError(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
            alert.setTitle("Error");
            alert.showAndWait();
        });
    }

    private void showErrorAndExit(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
            alert.setTitle("Fatal Error");
            alert.showAndWait();
            Platform.exit();
        });
    }

    @Override
    public void stop() {
        try {
            if (socket != null) socket.close();
            if (in != null) in.close();
            if (out != null) out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
