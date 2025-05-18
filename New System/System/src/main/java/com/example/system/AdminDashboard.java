package com.example.system;

import Database.DataBaseConnection;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import management.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class AdminDashboard extends Application {
    private final List<String> doctors = new ArrayList<>();
    private final List<String> patients = new ArrayList<>();
    private final TextArea logArea = new TextArea();
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Admin Dashboard");

        VBox buttonPanel = createButtonPanel();
        logArea.setEditable(false);
        logArea.setWrapText(true);
        logArea.setPrefSize(600, 400);
        logArea.setStyle("-fx-font-family: 'Consolas'; -fx-font-size: 13px;");

        ScrollPane scrollPane = new ScrollPane(logArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background: #f0f0f0; -fx-padding: 10;");

        HBox root = new HBox(20, buttonPanel, scrollPane);
        root.setPadding(new Insets(20));

        Scene scene = new Scene(root, 850, 500);
        primaryStage.setMinHeight(500);
        primaryStage.setMinWidth(810);
        primaryStage.setScene(scene);
        primaryStage.show();

        loadDoctorsFromDatabase();
        loadPatientsFromDatabase();
    }

    private VBox createButtonPanel() {
        VBox panel = new VBox(12);
        panel.setPadding(new Insets(10));
        panel.setPrefWidth(220);

        Label title = new Label("Admin Actions");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        panel.getChildren().add(title);

        String[] buttonLabels = {
                "Add Doctor/Add Patient", "Remove Doctor",
                "Remove Patient", "View Logs", "Generate Reports",
                "Update Profile", "View Profile", "Logout"
        };

        for (String label : buttonLabels) {
            Button btn = new Button(label);
            btn.setMaxWidth(Double.MAX_VALUE);
            switch (label) {
                case "Add Doctor/Add Patient" -> btn.setOnAction(e -> addPatientOrDoctor());
                case "Remove Doctor" -> btn.setOnAction(e -> removeDoctor());
                case "Remove Patient" -> btn.setOnAction(e -> removePatient());
                case "View Logs" -> btn.setOnAction(e -> viewLogs());
                case "Generate Reports" -> btn.setOnAction(e -> generateReport());
                case "Update Profile" -> btn.setOnAction(e -> updateProfile());
                case "View Profile" -> btn.setOnAction(e -> viewProfile());
                case "Logout" -> btn.setOnAction(e -> logout());
            }
            panel.getChildren().add(btn);
        }

        return panel;
    }

    private void addPatientOrDoctor() {
        new Signup().start(new Stage());
        loadDoctorsFromDatabase();
        loadPatientsFromDatabase();
    }

    private void removeDoctor() {
        loadDoctorsFromDatabase();
        if (doctors.isEmpty()) {
            showAlert("No doctors to remove.");
            return;
        }

        ChoiceDialog<String> dialog = new ChoiceDialog<>(doctors.get(0), doctors);
        dialog.setHeaderText("Select doctor to remove:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(selection -> {
            try {
                String[] toRemove = selection.split(",");
                removeDoctorFromDatabase(toRemove[0]);
            } catch (Exception ex) {
                showAlert("Error: Invalid doctor selection.");
            }
        });
    }

    private void removeDoctorFromDatabase(String doctorId) {
        try (Connection conn = DataBaseConnection.getConnection()) {
            String deleteDoctor = "DELETE FROM doctor WHERE doctor_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteDoctor)) {
                stmt.setString(1, doctorId);
                stmt.executeUpdate();
            }
            SystemLog.addLog(new SystemLog("Removed Doctor with ID: " + doctorId));
            loadDoctorsFromDatabase();
            viewLogs();
            removeFromAuthorization(doctorId);
        } catch (SQLException e) {
            showAlert("Error removing doctor: " + e.getMessage());
        }
    }

    private void removePatient() {
        loadPatientsFromDatabase();
        if (patients.isEmpty()) {
            showAlert("No patients to remove.");
            return;
        }
        ChoiceDialog<String> dialog = new ChoiceDialog<>(patients.get(0), patients);
        dialog.setHeaderText("Select patient to remove:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(selection -> {
            try {
                String[] toRemove = selection.split(",");
                removePatientFromDatabase(toRemove[0]);
            } catch (Exception ex) {
                showAlert("Error: Invalid patient selection.");
            }
        });
    }

    private void removePatientFromDatabase(String patientId) {
        try (Connection conn = DataBaseConnection.getConnection()) {
            String deletePatient = "DELETE FROM patient WHERE patient_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deletePatient)) {
                stmt.setString(1, patientId);
                stmt.executeUpdate();
            }
            removeFromAuthorization(patientId);
            SystemLog.addLog(new SystemLog("Removed Patient with ID: " + patientId));
            loadPatientsFromDatabase();
            viewLogs();
        } catch (SQLException e) {
            showAlert("Error removing patient: " + e.getMessage());
        }
    }

    private void removeFromAuthorization(String userId) {
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM authorization WHERE user_id = ?")) {
            stmt.setString(1, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            showAlert("Error removing from authorization: " + e.getMessage());
        }
    }

    private void viewLogs() {
        logArea.clear();
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement("SELECT * FROM logs")) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                logArea.appendText(rs.getString("log") + " at " + rs.getTimestamp("timestamp") + "\n");
            }
        } catch (SQLException e) {
            showAlert("Database Error: " + e.getMessage());
        }
    }

    private void generateReport() {
        StringBuilder report = new StringBuilder("=== System Report ===\n");
        report.append("Total Doctors: ").append(User.total_docters).append("\n");
        report.append("Total Patients: ").append(User.total_patients).append("\n");

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM logs ORDER BY timestamp DESC LIMIT 10")) {
            ResultSet rs = stmt.executeQuery();
            List<String> recentLogs = new ArrayList<>();
            while (rs.next()) {
                recentLogs.add(rs.getString("log") + " at " + rs.getTimestamp("timestamp"));
            }

            report.append("Total Logs: ").append(getTotalLogCount(conn)).append("\n\n");
            report.append("Recent Logs:\n");
            recentLogs.forEach(log -> report.append(log).append("\n"));

            logArea.setText(report.toString());
        } catch (SQLException e) {
            showAlert("Error generating report: " + e.getMessage());
        }
    }

    private int getTotalLogCount(Connection conn) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM logs");
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadDoctorsFromDatabase() {
        doctors.clear();
        String query = "SELECT * FROM doctor";
        String count = "SELECT COUNT(doctor_id) FROM doctor";

        try (Connection conn = DataBaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                doctors.add(rs.getString("doctor_id") + "," + rs.getString("name"));
            }
            ResultSet r = stmt.executeQuery(count);
            if (r.next()) User.total_docters = r.getInt(1);
        } catch (SQLException e) {
            showAlert("Error loading doctors: " + e.getMessage());
        }
    }

    private void loadPatientsFromDatabase() {
        patients.clear();
        String query = "SELECT patient_id, name FROM patient";

        try (Connection conn = DataBaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                patients.add(rs.getString("patient_id") + "," + rs.getString("name"));
            }
            ResultSet r = stmt.executeQuery("SELECT COUNT(patient_id) FROM patient");
            if (r.next()) User.total_patients = r.getInt(1);
        } catch (SQLException e) {
            showAlert("Error loading patients: " + e.getMessage());
        }
    }

    private void viewProfile() {
        try (Connection conn = DataBaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM admin WHERE id = 1")) {

            if (rs.next()) {
                String profile = "=== Admin Profile ===\n";
                profile+="Name: "+rs.getString("name")+"\n";
                profile+="Email: "+rs.getString("email");
                showAlert(profile);
            } else {
                showAlert("No admin profile found.");
            }

        } catch (SQLException e) {
            showAlert("Error fetching profile: " + e.getMessage());
        }
    }
    private void updateProfile() {
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM admin WHERE id = 1");
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                String currentName = rs.getString("name");
                String currentEmail = rs.getString("email");

                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.setTitle("Update Profile");

                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(20, 150, 10, 10));

                TextField nameField = new TextField(currentName);
                nameField.setPrefWidth(250);
                TextField emailField = new TextField(currentEmail);
                emailField.setPrefWidth(250);

                grid.add(new Label("Name:"), 0, 0);
                grid.add(nameField, 1, 0);
                grid.add(new Label("Email:"), 0, 1);
                grid.add(emailField, 1, 1);

                dialog.getDialogPane().setContent(grid);
                dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

                Optional<ButtonType> result = dialog.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    String newName = nameField.getText().trim();
                    String newEmail = emailField.getText().trim();

                    if (newName.isEmpty() || newEmail.isEmpty()) {
                        showAlert("Both fields must be filled.");
                        return;
                    }

                    try (PreparedStatement updateStmt = conn.prepareStatement(
                            "UPDATE admin SET name = ?, email = ? WHERE id = 1")) {
                        updateStmt.setString(1, newName);
                        updateStmt.setString(2, newEmail);

                        int rowsAffected = updateStmt.executeUpdate();
                        if (rowsAffected > 0) {
                            SystemLog.addLog(new SystemLog("Admin profile updated."));
                            showAlert("Profile updated successfully.");
                        } else {
                            showAlert("No changes made to profile.");
                        }
                    }
                }

            } else {
                showAlert("No admin profile found.");
            }

        } catch (SQLException e) {
            showAlert("Error updating profile: " + e.getMessage());
        }
    }

    private void logout() {
        try {
            primaryStage.close();
            new LoginApp().start(new Stage());
        } catch (Exception e) {
            showAlert("Logout failed: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

class SystemLog {
    String message;
    LocalDateTime timestamp;

    SystemLog(String message) {
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public static void addLog(SystemLog log) {
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement("INSERT INTO logs(timestamp,log) VALUES(?,?)")) {
            statement.setTimestamp(1, Timestamp.valueOf(log.timestamp));
            statement.setString(2, log.message);
            statement.executeUpdate();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Log Error");
            alert.setHeaderText("Could not insert log entry");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}
