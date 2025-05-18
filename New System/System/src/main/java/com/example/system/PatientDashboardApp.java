//package com.example.system;//coppied
//
//import AppointmentScheduling.Appointment;
//import javafx.application.Application;
//
//import javafx.geometry.Insets;
//import javafx.geometry.Pos;
//
//import javafx.scene.Scene;
//import javafx.scene.chart.LineChart;
//import javafx.scene.chart.NumberAxis;
//import javafx.scene.chart.XYChart;
//import javafx.scene.control.*;
//
//import javafx.scene.layout.*;
//import javafx.scene.text.Font;
//import javafx.scene.text.FontWeight;
//import javafx.scene.text.Text;
//import javafx.stage.FileChooser;
//import javafx.stage.Stage;
//
//import jakarta.mail.*;
//import jakarta.mail.internet.*;
//
//import java.io.*;
//import java.sql.*;
//
//import java.text.SimpleDateFormat;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.*;
//import static Database.DataBaseConnection.getConnection;
//
//
//public class PatientDashboardApp extends Application {
//
//    private String caller = "patient"; // default
//    private Stage primaryStage;
//    private String patientName;
//    private Scene profileScene,updateProfileScene;
//    public static  String currentPatientId;
//    private List<VitalRecord> vitalsHistory = new ArrayList<>();
//    private int emergencyClickCount = 0;
//
//
//    @Override
//    public void start(Stage primaryStage) {
//        this.primaryStage = primaryStage;
//        profileScene = buildProfileView(currentPatientId);
//        updateProfileScene=buildUpdateProfileView();
//        showDashboardScene();
//    }
//    private Scene buildProfileView(String currentPatientId) {
//        VBox view = new VBox(10);
//        view.setStyle("-fx-font-size:16px;");
//        view.setPadding(new Insets(20));
//
//        Label subtitle=new Label("Patients's Information");
//        subtitle.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 22));
//
//
//        ArrayList<Label> profileLabels= new ArrayList<> ();
//        ArrayList<TextField> profileField=new ArrayList<>();
//        ArrayList<HBox> line =new ArrayList<>();
//
//        profileLabels.add(new Label("ID"));
//        profileLabels.add(new Label("Name"));
//        profileLabels.add(new Label("Birth Date"));
//        profileLabels.add(new Label("Contact"));
//        profileLabels.add(new Label("E-mail"));
//        profileLabels.add(new Label("Blood Group"));
//        profileLabels.add(new Label("Gender"));
//        profileLabels.add(new Label("Registration"));
//
//        String readQuery = "SELECT * FROM patient WHERE patient_id = ?";
//
//        try (Connection conn = getConnection();
//             PreparedStatement stmt = conn.prepareStatement(readQuery)) {
//            stmt.setString(1,currentPatientId);
//            try (ResultSet rs = stmt.executeQuery()) {
//                if (rs.next()) {
//                    String id = rs.getString("patient_id");
//                    String name = rs.getString("name");
//                    patientName=name;
//                    java.sql.Date dob = rs.getDate("Dob");
//                    String contact = rs.getString("contact");
//                    String email = rs.getString("email");
//                    String bloodGroup = rs.getString("blood_group");
//                    String gender = rs.getString("gender");
//                    java.sql.Date regDate = rs.getDate("registration_date");
//
//                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//
//                    profileField.add(new TextField(id));
//                    profileField.add(new TextField(name));
//                    profileField.add(new TextField(formatter.format(dob)));
//                    profileField.add(new TextField(contact));
//                    profileField.add(new TextField(email));
//                    profileField.add(new TextField(bloodGroup));
//                    profileField.add(new TextField(gender));
//                    profileField.add(new TextField(formatter.format(regDate)));
//                } else {
//                    System.out.println("No patient found with that email.");
//                }
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        view.getChildren().addAll(subtitle);
//
//        Button backBtn = createStyledButton("⬅ Back", "#57606f");
//        backBtn.setOnAction(e -> showDashboardScene());
//        backBtn.setAlignment(Pos.CENTER);
//
//        for (int i = 0; i < profileField.size(); i++) {
//            if(i==0) profileField.get(i).setStyle("-fx-background-color : transparent; -fx-font-size:16;");
//            profileField.get(i).setEditable(false);
//            profileField.get(i).setPrefWidth(400);
//            line.add(DoctorDashboard.createLabeledTextField(profileLabels.get(i).getText(), profileField.get(i)));
//            view.getChildren().add(line.get(i));
//        }
//        view.getChildren().add(backBtn);
//
//        return  new Scene(view, 600, 600);
//
//    }
//    private Scene buildUpdateProfileView() {
//        VBox content = new VBox(15);
//        content.setPadding(new Insets(20));
//        content.setStyle("-fx-font-size: 16;  -fx-border-color: transparent;");
//        content.setSpacing(10);
//        content.setFillWidth(true);
//
//        Label header = new Label("Update Profile");
//        header.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 22));
//
//        ArrayList<Label> fieldNames = new ArrayList<>();
//        fieldNames.add(new Label("ID"));
//        fieldNames.add(new Label("Name"));
//        fieldNames.add(new Label("Birth Date"));
//        fieldNames.add(new Label("Contact"));
//        fieldNames.add(new Label("E-mail"));
//        fieldNames.add(new Label("Blood Group"));
//        fieldNames.add(new Label("Gender"));
//        fieldNames.add(new Label("Registration"));
//
//
//        ArrayList<TextField> profileFields = new ArrayList<>();
//        DatePicker dobPicker = null;
//
//        // Read doctor info from DB
//        String readQuery = "SELECT * FROM patient WHERE patient_id = ?";
//        try (Connection conn = getConnection();
//             PreparedStatement stmt = conn.prepareStatement(readQuery)) {
//            stmt.setString(1, currentPatientId);
//
//            try (ResultSet rs = stmt.executeQuery()) {
//                if (rs.next()) {
//                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//                    profileFields.add(new TextField(rs.getString("patient_id"))); // ID
//                    profileFields.add(new TextField(rs.getString("name")));      // Name
//                    profileFields.add(new TextField(formatter.format(rs.getDate("Dob")))); // DOB
//                    profileFields.add(new TextField(rs.getString("contact")));
//                    profileFields.add(new TextField(rs.getString("email")));
//                    profileFields.add(new TextField(rs.getString("blood_group")));
//                    profileFields.add(new TextField(rs.getString("gender")));
//                    profileFields.add(new TextField(formatter.format(rs.getDate("registration_date"))));
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        // ID and registration date are not editable
//        if (!profileFields.isEmpty()) {
//            profileFields.get(0).setStyle("-fx-background-color : transparent; -fx-font-size:16;");
//            profileFields.get(0).setEditable(false);
//            profileFields.get(7).setEditable(false);
//        }
//
//        content.getChildren().add(header);
//
//        for (int i = 0; i < fieldNames.size(); i++) {
//            if (i == 2) {
//                dobPicker = new DatePicker(LocalDate.parse(profileFields.get(i).getText()));
//                HBox dobBox = new HBox(15);
//                dobBox.setAlignment(Pos.CENTER_LEFT);
//                Label label = new Label("Birth Date");
//                label.setMinWidth(110);
//                label.setStyle("-fx-font-weight: bold;");
//                dobBox.getChildren().addAll(label, dobPicker);
//                HBox.setHgrow(dobPicker, Priority.ALWAYS);
//                content.getChildren().add(dobBox);
//            } else {
//                TextField tf = profileFields.get(i);
//                tf.setPrefWidth(400);
//                HBox fieldBox = DoctorDashboard.createLabeledTextField(fieldNames.get(i).getText(), tf);
//                content.getChildren().add(fieldBox);
//            }
//        }
//
//        Button saveBtn = new Button("Save Changes");
//        saveBtn.setAlignment(Pos.CENTER);
//        Button backBtn = createStyledButton("⬅ Back", "#57606f");
//        content.getChildren().addAll(saveBtn,backBtn);
//        backBtn.setOnAction(e->{showDashboardScene();});
//
//        // Set Save Action
//        DatePicker finalDobPicker = dobPicker;
//        saveBtn.setOnAction(e -> {
//            try {
//                String name = profileFields.get(1).getText();
//                String contact = profileFields.get(3).getText();
//                String email = profileFields.get(4).getText();
//                String bloodGroup = profileFields.get(5).getText();
//                String gender = profileFields.get(6).getText();
//                String regDate = profileFields.get(7).getText();
//
//                if (finalDobPicker == null || finalDobPicker.getValue() == null ||
//                        name.isEmpty() || contact.isEmpty() || email.isEmpty() ||
//                        bloodGroup.isEmpty() || gender.isEmpty() || regDate.isEmpty()) {
//                    DoctorDashboard.showAlert(Alert.AlertType.ERROR, "Validation Error", "All fields must be filled out.");
//                    return;
//                }
//
//                if (!DoctorDashboard.isValidEmail(email)) {
//                    DoctorDashboard.showAlert(Alert.AlertType.ERROR, "Validation Error", "Invalid email format.");
//                    return;
//                }
//
//                String updateQuery = "UPDATE patient SET name = ?, Dob = ?, contact = ?, email = ?, " +
//                        "blood_group = ?, gender = ?, registration_date = ? WHERE patient_id = ?";
//
//                try (Connection conn = getConnection();
//                     PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
//                    stmt.setString(1, name);
//                    stmt.setDate(2, java.sql.Date.valueOf(finalDobPicker.getValue()));
//                    stmt.setString(3, contact);
//                    stmt.setString(4, email);
//                    stmt.setString(5, bloodGroup);
//                    stmt.setString(6, gender);
//                    stmt.setDate(7, java.sql.Date.valueOf(regDate));
//                    stmt.setString(8, profileFields.get(0).getText());
//
//                    stmt.executeUpdate();
//                    DoctorDashboard.showAlert(Alert.AlertType.INFORMATION, "Success", "Profile updated successfully.");
//                    profileScene = buildProfileView(currentPatientId); // refresh
//                }
//                patientName=name;
//            } catch (Exception ex) {
//                DoctorDashboard.showAlert(Alert.AlertType.ERROR, "Error", "Failed to update profile.");
//            }
//
//        });
//        return new Scene(content,600,600);
//    }
//
//    private void showDashboardScene() {
//        VBox root = new VBox(20);
//        root.setAlignment(Pos.CENTER);
//        root.setStyle("-fx-background-color: linear-gradient(to bottom, #fbc2eb, #a6c1ee);");
//
//        Text title = new Text("Welcome, " + patientName + " \uD83D\uDE0A");
//        title.setFont(Font.font("Arial", 26));
//        title.setStyle("-fx-fill: #333;");
//
//        Text idText = new Text("Patient ID: " + currentPatientId);
//        idText.setStyle("-fx-font-size: 16;");
//
//        Button vitalsButton = createStyledButton("Vitals ❤", "#ffb347");
//        Button profileButton = createStyledButton("View Profile", "#70a1ff");
//        Button updateProfileButton = createStyledButton("Update Profile", "#2ed573");
//        Button feedbackButton = createStyledButton("View Feedback", "#ffa502");
//        Button emergencyButton = createStyledButton("Emergency 🚨🚨 (Press Twice)", "#ff4c4c");
//        Button chatButton = createStyledButton("Chat 💬", "#ffb447");
//        Button appointmentButton = createStyledButton("Appointment 🩺", "#9b59b6");
//        Button logoutButton = createStyledButton("LogOut ","#f6f6c6");
//
//        // Set uniform button size
//        double buttonWidth = 300;
//        vitalsButton.setPrefWidth(buttonWidth);
//        profileButton.setPrefWidth(buttonWidth);
//        updateProfileButton.setPrefWidth(buttonWidth);
//        feedbackButton.setPrefWidth(buttonWidth);
//        emergencyButton.setPrefWidth(buttonWidth);
//        chatButton.setPrefWidth(buttonWidth);
//        appointmentButton.setPrefWidth(buttonWidth);
//        logoutButton.setPrefWidth(buttonWidth/2);
//
//        // Button actions
//        vitalsButton.setOnAction(e -> showVitalsOptionsScene());
//        profileButton.setOnAction(e -> primaryStage.setScene(profileScene));
//        updateProfileButton.setOnAction(e -> primaryStage.setScene(updateProfileScene));
//        feedbackButton.setOnAction(e -> showErrorAlert("Feedback viewing coming soon!"));
//        emergencyButton.setOnAction(e -> {
//            emergencyClickCount++;
//            if (emergencyClickCount >= 2) {
//                showEmergencyAlert("Manual Emergency Triggered!");
//                emergencyClickCount = 0;
//            }
//        });
//        chatButton.setOnAction(e -> buildChatView());
//        appointmentButton.setOnAction(e -> showAppointmentScene());
//        logoutButton.setOnAction(e->{
//            ((Stage) logoutButton.getScene().getWindow()).close();
//            new LoginApp().start(new Stage());
//        });
//
//
//        // Add all to layout
//        root.getChildren().addAll(
//                title, idText,
//                vitalsButton,
//                profileButton,
//                updateProfileButton,
//                feedbackButton,
//                emergencyButton,
//                chatButton,
//                appointmentButton,
//                logoutButton
//        );
//
//        Scene scene = new Scene(root, 500, 650);
//        primaryStage.setTitle("Patient Profile");
//        primaryStage.setScene(scene);
//        primaryStage.show();
//    }
//
//
//    private void showVitalsOptionsScene() {
//        VBox root = new VBox(20);
//        root.setAlignment(Pos.CENTER);
//        root.setStyle("-fx-background-color: linear-gradient(to bottom, #a1c4fd, #c2e9fb);");
//
//        Button addVitalsButton = createStyledButton("➕ Add/Update Vitals", "#ffdd59");
//        Button showVitalsButton = createStyledButton("📊 Show Vitals", "#70a1ff");
//        Button uploadButton = createStyledButton(" Upload CSV", "#7bed9f");
//
//        addVitalsButton.setPrefWidth(200);
//        showVitalsButton.setPrefWidth(200);
//        uploadButton.setPrefWidth(200);
//
//        Button backButton = createStyledButton("⬅ Back", "#57606f");
//
//        addVitalsButton.setOnAction(e -> showAddVitalsScene());
//        showVitalsButton.setOnAction(e -> showVitalsChartScene(primaryStage,"patient"));
//        uploadButton.setOnAction(e -> uploadVitalsFromCSV());
//        backButton.setOnAction(e -> showDashboardScene());
//
//        root.getChildren().addAll(addVitalsButton, showVitalsButton, uploadButton,backButton);
//
//        Scene scene = new Scene(root, 400, 500);
//        primaryStage.setTitle("Vitals Options");
//        primaryStage.setScene(scene);
//    }
//
//    private void showAddVitalsScene() {
//        VBox root = new VBox(15);
//        root.setAlignment(Pos.CENTER);
//        root.setPadding(new Insets(20));
//        root.setStyle("-fx-background-color: linear-gradient(to right, #fbc2eb, #a6c1ee);");
//
//        TextField tempField = createSizedTextField();
//        TextField systolicField = createSizedTextField();
//        TextField diastolicField = createSizedTextField();
//        TextField heartRateField = createSizedTextField();
//        TextField oxygenField = createSizedTextField();
//
//        VBox inputs = new VBox(10,
//                createLabeledInput("🌡 Temperature (°C):", tempField),
//                createLabeledInput("🩺 Systolic BP (mmHg):", systolicField),
//                createLabeledInput("🩸 Diastolic BP (mmHg):", diastolicField),
//                createLabeledInput("❤ Heart Rate (bpm):", heartRateField),
//                createLabeledInput("🫁 Oxygen Level (%):", oxygenField)
//        );
//        inputs.setAlignment(Pos.CENTER);
//
//        Button saveButton = createStyledButton("💾 Save Vitals", "#2ed573");
//        Button backButton = createStyledButton("⬅ Back", "#57606f");
//        backButton.setOnAction(e -> showVitalsOptionsScene());
//        saveButton.setOnAction(e -> {
//            try {
//                double temperature = Double.parseDouble(tempField.getText());
//                double systolic = Double.parseDouble(systolicField.getText());
//                double diastolic = Double.parseDouble(diastolicField.getText());
//                double heartRate = Double.parseDouble(heartRateField.getText());
//                double oxygen = Double.parseDouble(oxygenField.getText());
//
//                LocalDateTime now = LocalDateTime.now();
//                vitalsHistory.add(new VitalRecord(now, temperature, systolic, diastolic, heartRate, oxygen));
//
//                // Insert into database
//                try (Connection conn = getConnection()) {
//                    String sql = "INSERT INTO vitals (patient_id, timestamp, temperature, systolic, diastolic, heart_rate, oxygen) " +
//                            "VALUES (?, ?, ?, ?, ?, ?, ?)";
//                    PreparedStatement stmt = conn.prepareStatement(sql);
//                    stmt.setString(1, currentPatientId);
//                    stmt.setTimestamp(2, Timestamp.valueOf(now));
//                    stmt.setDouble(3, temperature);
//                    stmt.setDouble(4, systolic);
//                    stmt.setDouble(5, diastolic);
//                    stmt.setDouble(6, heartRate);
//                    stmt.setDouble(7, oxygen);
//                    stmt.executeUpdate();
//                } catch (SQLException ex) {
//                    ex.printStackTrace();
//                    showErrorAlert("Failed to save vitals to database!");
//                    return;
//                }
//
//                if (isEmergencyCondition(temperature, systolic, diastolic, heartRate, oxygen)) {
//                    showEmergencyAlert("Automatic Emergency Detected based on Vitals!");
//                } else {
//                    tempField.clear();systolicField.clear();diastolicField.clear();heartRateField.clear();oxygenField.clear();
//                    DoctorDashboard.showAlert(Alert.AlertType.INFORMATION,"Success","Vitals Uploaded Successfully");
//                    showDashboardScene();
//                }
//            } catch (NumberFormatException ex) {
//                showErrorAlert("Invalid input! Please enter valid numbers.");
//            }
//        });
//
//        root.getChildren().addAll(inputs, saveButton, backButton);
//
//        Scene scene = new Scene(root, 550, 650);
//        primaryStage.setTitle("Add/Update Vitals");
//        primaryStage.setScene(scene);
//    }
//
//
//    private TextField createSizedTextField() {
//        TextField tf = new TextField();
//        tf.setMinWidth(100);
//        tf.setPrefWidth(350);
//        tf.setMaxWidth(900);
//        tf.setStyle(
//                "-fx-background-color: #ffffff;" +
//                        "-fx-background-radius: 10;" +
//                        "-fx-border-radius: 10;" +
//                        "-fx-border-color: #ccc;" +
//                        "-fx-padding: 5 10 5 10;" +
//                        "-fx-font-size: 14px;"
//        );
//
//        return tf;
//    }
//
//    private HBox createLabeledInput(String labelText, TextField inputField) {
//        Label label = createStyledLabel(labelText);
//        label.setMinWidth(180);
//        HBox box = new HBox(10, label, inputField);
//        box.setAlignment(Pos.CENTER);
//
//        return box;
//    }
//
//
//    public  void showVitalsChartScene(Stage primaryStage,String callerType) {
//        this.primaryStage=primaryStage;
//        this.caller=callerType;
//
//        NumberAxis xAxis = new NumberAxis();
//        NumberAxis yAxis = new NumberAxis();
//        xAxis.setLabel("Time Index");
//
//        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
//        lineChart.setTitle("📈 Vitals Over Time");
//
//        // Define all series
//        XYChart.Series<Number, Number> tempSeries = new XYChart.Series<>();
//        XYChart.Series<Number, Number> sysSeries = new XYChart.Series<>();
//        XYChart.Series<Number, Number> diaSeries = new XYChart.Series<>();
//        XYChart.Series<Number, Number> hrSeries = new XYChart.Series<>();
//        XYChart.Series<Number, Number> oxySeries = new XYChart.Series<>();
//
//        tempSeries.setName("Temperature");
//        sysSeries.setName("Systolic");
//        diaSeries.setName("Diastolic");
//        hrSeries.setName("Heart Rate");
//        oxySeries.setName("Oxygen");
//
//        // Fill data once
//        List<VitalRecord> vitals = new ArrayList<>();
//        String selectQuery="SELECT timestamp, temperature, systolic, diastolic, heart_rate, oxygen FROM vitals WHERE patient_id = ? ORDER BY timestamp";
//        try (Connection conn = getConnection();
//             PreparedStatement stmt = conn.prepareStatement(selectQuery);){
//            stmt.setString(1,currentPatientId);
//            try (ResultSet rs = stmt.executeQuery();) {
//
//                while (rs.next()) {
//                    VitalRecord vr = new VitalRecord();
//                    vr.temperature = rs.getDouble("temperature");
//                    vr.systolic = rs.getDouble("systolic");
//                    vr.diastolic = rs.getDouble("diastolic");
//                    vr.heartRate = rs.getDouble("heart_rate");
//                    vr.oxygen = rs.getDouble("oxygen");
//                    vitals.add(vr);
//                }
//            }catch (SQLException ex) {
//                showErrorAlert("Failed to get vitals from database!");
//                ex.printStackTrace();
//                return;
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        if (vitals.isEmpty()) {
//            showErrorAlert("No vital records found for this patient.");
//            return;
//        }
//        for (int i = 0; i < vitals.size(); i++) {
//            VitalRecord vr = vitals.get(i);
//            tempSeries.getData().add(new XYChart.Data<>(i, vr.temperature));
//            sysSeries.getData().add(new XYChart.Data<>(i, vr.systolic));
//            diaSeries.getData().add(new XYChart.Data<>(i, vr.diastolic));
//            hrSeries.getData().add(new XYChart.Data<>(i, vr.heartRate));
//            oxySeries.getData().add(new XYChart.Data<>(i, vr.oxygen));
//        }
//
//        // Checkboxes
//        CheckBox tempCheck = new CheckBox("🌡 Temperature");
//        CheckBox sysCheck = new CheckBox("🩺 Systolic");
//        CheckBox diaCheck = new CheckBox("🩸 Diastolic");
//        CheckBox hrCheck = new CheckBox("❤ Heart Rate");
//        CheckBox oxyCheck = new CheckBox("🫁 Oxygen");
//
//        // Default all selected
//        tempCheck.setSelected(true);
//        sysCheck.setSelected(true);
//        diaCheck.setSelected(true);
//        hrCheck.setSelected(true);
//        oxyCheck.setSelected(true);
//
//        // Add all selected series initially
//        lineChart.getData().addAll(tempSeries, sysSeries, diaSeries, hrSeries, oxySeries);
//
//        // Checkbox event handler
//        tempCheck.setOnAction(e -> toggleSeries(lineChart, tempCheck, tempSeries));
//        sysCheck.setOnAction(e -> toggleSeries(lineChart, sysCheck, sysSeries));
//        diaCheck.setOnAction(e -> toggleSeries(lineChart, diaCheck, diaSeries));
//        hrCheck.setOnAction(e -> toggleSeries(lineChart, hrCheck, hrSeries));
//        oxyCheck.setOnAction(e -> toggleSeries(lineChart, oxyCheck, oxySeries));
//
//        VBox checkboxBox = new VBox(10, tempCheck, sysCheck, diaCheck, hrCheck, oxyCheck);
//        checkboxBox.setPadding(new Insets(10));
//        checkboxBox.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #333;");
//        checkboxBox.setAlignment(Pos.TOP_LEFT);
//
//        Button backButton = createStyledButton("⬅ Back", "#57606f");
//        backButton.setOnAction(e -> {
//            Stage stage = (Stage) backButton.getScene().getWindow();
//            if ("doctor".equalsIgnoreCase(caller)) {
//                stage.close();
//            } else {
//                showDashboardScene();
//            }
//        });
//
//        VBox leftPanel = new VBox(20, checkboxBox, backButton);
//        leftPanel.setPadding(new Insets(10));
//
//        HBox root = new HBox(10, leftPanel, lineChart);
//        root.setPadding(new Insets(10));
//
//        Scene scene = new Scene(root, 1000, 550);
//        primaryStage.setScene(scene);
//        primaryStage.setTitle("Show Vitals");
//        primaryStage.show();
//    }
//
//
//    private void toggleSeries(LineChart<Number, Number> chart, CheckBox checkBox, XYChart.Series<Number, Number> series) {
//        if (checkBox.isSelected()) {
//            if (!chart.getData().contains(series)) {
//                chart.getData().add(series);
//            }
//        } else {
//            chart.getData().remove(series);
//        }
//    }
//
//
//    private void uploadVitalsFromCSV() {
//        FileChooser fileChooser = new FileChooser();
//        fileChooser.setTitle("Select Vitals CSV File");
//        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
//        File file = fileChooser.showOpenDialog(null);
//
//        if (file == null) return;
//
//        try (BufferedReader reader = new BufferedReader(new FileReader(file));
//             Connection conn = getConnection()) {
//
//            String insertSQL = "INSERT INTO vitals (patient_id, timestamp, temperature, systolic, diastolic, heart_rate, oxygen) " +
//                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
//            PreparedStatement pstmt = conn.prepareStatement(insertSQL);
//
//            String line;
//            int lineNum = 0;
//
//            while ((line = reader.readLine()) != null) {
//                lineNum++;
//                if (lineNum == 1) continue; // Skip header
//
//                String[] parts = line.split(",");
//                if (parts.length < 7) {
//                    System.err.println("Invalid line at " + lineNum + ": " + line);
//                    continue;
//                }
//
//                String patientId = parts[0].trim();
//                String timestamp = parts[1].trim();
//                double temperature = Double.parseDouble(parts[2].trim());
//                double systolic = Double.parseDouble(parts[3].trim());
//                double diastolic = Double.parseDouble(parts[4].trim());
//                double heartRate = Double.parseDouble(parts[5].trim());
//                double oxygen = Double.parseDouble(parts[6].trim());
//
//                pstmt.setString(1, patientId);
//                pstmt.setString(2, timestamp); // Ensure this is in correct format (e.g. "yyyy-MM-dd HH:mm:ss")
//                pstmt.setDouble(3, temperature);
//                pstmt.setDouble(4, systolic);
//                pstmt.setDouble(5, diastolic);
//                pstmt.setDouble(6, heartRate);
//                pstmt.setDouble(7, oxygen);
//
//                pstmt.addBatch();
//            }
//
//            pstmt.executeBatch();
//            DoctorDashboard.showAlert(Alert.AlertType.INFORMATION, "Success", "Vitals uploaded successfully!");
//
//        } catch (Exception ex) {
//            DoctorDashboard.showAlert(Alert.AlertType.ERROR, "Error", "Failed to upload: " + ex.getMessage());
//            ex.printStackTrace();
//        }
//    }
//
//    private boolean isEmergencyCondition(double temperature, double systolic, double diastolic, double heartRate, double oxygen) {
//        return temperature > 38.0 || temperature < 35.0 ||
//                systolic > 140 || systolic < 90 ||
//                diastolic > 90 || diastolic < 60 ||
//                heartRate > 120 || heartRate < 50 ||
//                oxygen < 90;
//    }
//
//    private void showEmergencyAlert(String message) {
//
//        Alert alert = new Alert(Alert.AlertType.ERROR);
//        alert.setTitle("Emergency Alert");
//        alert.setHeaderText(null);
//        alert.setContentText(message);
//        alert.showAndWait();
//        EmailUtil.sendEmergencyEmail(patientName, message);
//        showDashboardScene();
//    }
//
//    private void showErrorAlert(String message) {
//        Alert alert = new Alert(Alert.AlertType.WARNING);
//        alert.setTitle("Alert");
//        alert.setHeaderText(null);
//        alert.setContentText(message);
//        alert.showAndWait();
//    }
//
//    private Button createStyledButton(String text, String color) {
//        Button button = new Button(text);
//        button.setStyle(
//                "-fx-background-color: " + color + ";" +
//                        "-fx-text-fill: white;" +
//                        "-fx-font-size: 16px;" +
//                        "-fx-font-weight: bold;" +
//                        "-fx-padding: 10 20 10 20;" +
//                        "-fx-background-radius: 20;" +
//                        "-fx-cursor: hand;"
//        );
//        return button;
//    }
//    private void buildChatView() {
//        // Launch chat interface with proper identification
//        new ChatClient(currentPatientId, "Patient").start(new Stage());
//    }
//    private Label createStyledLabel(String text) {
//        Label label = new Label(text);
//        label.setFont(Font.font("Arial", FontWeight.BOLD, 14));
//        label.setStyle("-fx-text-fill: #2f3542;");
//        return label;
//    }
//
//    static class VitalRecord {
//        LocalDateTime timestamp;
//        double temperature, systolic, diastolic, heartRate, oxygen;
//        public VitalRecord(){}
//        public VitalRecord(LocalDateTime timestamp, double temperature, double systolic, double diastolic, double heartRate, double oxygen) {
//            this.timestamp = timestamp;
//            this.temperature = temperature;
//            this.systolic = systolic;
//            this.diastolic = diastolic;
//            this.heartRate = heartRate;
//            this.oxygen = oxygen;
//        }
//
//        public String toCSV() {
//            return timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + "," + temperature + "," + systolic + "," + diastolic + "," + heartRate + "," + oxygen;
//        }
//    }
//
//    static class EmailUtil {
//        public static void sendEmergencyEmail(String patientName, String messageBody) {
//            String to = "ahmedali253721@gmail.com";
//            String from = "doctor420.420.420f@gmail.com";
//            String password = "mmqmvvicipvpoyom";
//            String host = "smtp.gmail.com";
//
//            Properties props = new Properties();
//            props.put("mail.smtp.host", host);
//            props.put("mail.smtp.port", "587");
//            props.put("mail.smtp.auth", "true");
//            props.put("mail.smtp.starttls.enable", "true");
//
//            Session session = Session.getInstance(props,
//                    new Authenticator() {
//                        protected PasswordAuthentication getPasswordAuthentication() {
//                            return new PasswordAuthentication(from, password);
//                        }
//                    });
//
//            try {
//                Message message = new MimeMessage(session);
//                message.setFrom(new InternetAddress(from));
//                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
//                message.setSubject("🚨 Emergency Alert for " + patientName);
//                message.setText(messageBody);
//
//                Transport.send(message);
//                System.out.println("Email sent successfully.");
//            } catch (MessagingException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private void showAppointmentScene() {
//        VBox root = new VBox(20);
//        root.setPadding(new Insets(20));
//        root.setAlignment(Pos.CENTER);
//
//        GridPane grid = new GridPane();
//        grid.setHgap(10);
//        grid.setVgap(15);
//        grid.setAlignment(Pos.CENTER);
//
//        ComboBox<String> doctorCombo = new ComboBox<>();
//        ComboBox<java.sql.Date> dateCombo = new ComboBox<>();
//        ComboBox<String> timeCombo = new ComboBox<>();
//        TextField reasonField = new TextField();
//        reasonField.setPromptText("Enter reason for appointment");
//
//        Button submitBtn = new Button("Apply for Appointment");
//        Button backBtn = new Button("⬅ Back");
//        backBtn.setStyle("-fx-background-color: #57606f; -fx-text-fill: white;");
//
//        Map<String, String> nameToId = new HashMap<>();
//
//        // Load doctors with availability
//        try (Connection conn = getConnection();
//             PreparedStatement stmt = conn.prepareStatement(
//                     "SELECT DISTINCT d.doctor_id, d.name, d.specialization " +
//                             "FROM doctor d JOIN doctor_availability a ON d.doctor_id = a.doctor_id")) {
//            ResultSet rs = stmt.executeQuery();
//            while (rs.next()) {
//                String id = rs.getString("doctor_id");
//                String name = rs.getString("name");
//                String spec = rs.getString("specialization");
//                String display = name + " (" + spec + ")";
//                nameToId.put(display, id);
//                doctorCombo.getItems().add(display);
//            }
//        } catch (SQLException e) {
//            showErrorAlert("Unable to load doctors from database.");
//            e.printStackTrace();
//        }
//
//        doctorCombo.setOnAction(e -> {
//            dateCombo.getItems().clear();
//            timeCombo.getItems().clear();
//            String selectedDoctor = doctorCombo.getValue();
//            if (selectedDoctor != null) {
//                try (Connection conn = getConnection();
//                     PreparedStatement stmt = conn.prepareStatement(
//                             "SELECT DISTINCT date FROM doctor_availability WHERE doctor_id = ?")) {
//                    stmt.setString(1, nameToId.get(selectedDoctor));
//                    ResultSet rs = stmt.executeQuery();
//                    while (rs.next()) {
//                        LocalDate date = rs.getDate("date").toLocalDate();
//                        if (!date.isBefore(LocalDate.now())) {
//                            dateCombo.getItems().add(java.sql.Date.valueOf(date));
//                        }
//                    }
//                } catch (SQLException ex) {
//                    showErrorAlert("Unable to load dates.");
//                    ex.printStackTrace();
//                }
//            }
//        });
//
//        dateCombo.setOnAction(e -> {
//            timeCombo.getItems().clear();
//            String selectedDoctor = doctorCombo.getValue();
//            java.sql.Date selectedDate = dateCombo.getValue();
//            if (selectedDoctor != null && selectedDate != null) {
//                try (Connection conn = getConnection();
//                     PreparedStatement stmt = conn.prepareStatement(
//                             "SELECT time FROM doctor_availability WHERE doctor_id = ? AND date = ?")) {
//                    stmt.setString(1, nameToId.get(selectedDoctor));
//                    stmt.setDate(2, selectedDate);
//                    ResultSet rs = stmt.executeQuery();
//                    while (rs.next()) {
//                        timeCombo.getItems().add(rs.getString("time"));
//                    }
//                } catch (SQLException ex) {
//                    showErrorAlert("Unable to load times.");
//                    ex.printStackTrace();
//                }
//            }
//        });
//
//        submitBtn.setOnAction(e -> {
//            String doctorDisplay = doctorCombo.getValue();
//            java.sql.Date date = dateCombo.getValue();
//            String time = timeCombo.getValue();
//            String reason = reasonField.getText();
//
//            if (doctorDisplay != null && date != null && time != null && !reason.isBlank()) {
//                String docId = nameToId.get(doctorDisplay);
//                Appointment request = new Appointment("pending",docId,currentPatientId,date.toString(),time,reason);
//
//                try (Connection conn = getConnection();
//                     PreparedStatement stmt = conn.prepareStatement(
//                             "INSERT INTO appointments VALUES (?, ?, ?, ?, ?, ?, ?)")) {
//                    stmt.setString(1, request.getAppointmentId());
//                    stmt.setString(2, request.getDoctor());
//                    stmt.setString(3, currentPatientId);
//                    stmt.setDate(4, date);
//                    stmt.setString(5, request.getTime());
//                    stmt.setString(6, request.getStatus());
//                    stmt.setString(7, request.getReason());
//
//                    stmt.executeUpdate();
//                    DoctorDashboard.showAlert(Alert.AlertType.INFORMATION, "Success", "Appointment request submitted successfully.");
//                    doctorCombo.getSelectionModel().clearSelection();
//                    dateCombo.getItems().clear();
//                    timeCombo.getItems().clear();
//                    reasonField.clear();
//                } catch (SQLException ex) {
//                    ex.printStackTrace();
//                    DoctorDashboard.showAlert(Alert.AlertType.ERROR, "Failed", "Error submitting appointment.");
//                }
//            } else {
//                DoctorDashboard.showAlert(Alert.AlertType.ERROR, "Missing Fields", "Please complete all fields.");
//            }
//        });
//
//        backBtn.setOnAction(e -> showDashboardScene());
//
//        grid.add(new Label("Select Doctor:"), 0, 0);
//        grid.add(doctorCombo, 1, 0);
//        grid.add(new Label("Select Date:"), 0, 1);
//        grid.add(dateCombo, 1, 1);
//        grid.add(new Label("Select Time:"), 0, 2);
//        grid.add(timeCombo, 1, 2);
//        grid.add(new Label("Reason:"), 0, 3);
//        grid.add(reasonField, 1, 3);
//        grid.add(submitBtn, 1, 4);
//        grid.add(backBtn, 0, 4);
//
//        root.getChildren().add(grid);
//        Scene scene = new Scene(root, 500, 400);
//        primaryStage.setScene(scene);
//    }
//
//
//    public static void main(String[] args) {
//        launch(args);
//    }

package com.example.system;

import AppointmentScheduling.Appointment;
import javafx.application.Application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;

import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.io.*;
import java.sql.*;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import static Database.DataBaseConnection.getConnection;


public class PatientDashboardApp extends Application {

    private String caller = "patient"; // default
    private Stage primaryStage;
    private String patientName;
    private Scene profileScene,updateProfileScene;
    public static  String currentPatientId;
    private List<VitalRecord> vitalsHistory = new ArrayList<>();
    private int emergencyClickCount = 0;


    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        profileScene = buildProfileView(currentPatientId);
        updateProfileScene=buildUpdateProfileView();
        showDashboardScene();
    }
    private Scene buildProfileView(String currentPatientId) {
        VBox view = new VBox(10);
        view.setStyle("-fx-font-size:16px;");
        view.setPadding(new Insets(20));

        Label subtitle=new Label("Patients's Information");
        subtitle.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 22));


        ArrayList<Label> profileLabels= new ArrayList<> ();
        ArrayList<TextField> profileField=new ArrayList<>();
        ArrayList<HBox> line =new ArrayList<>();

        profileLabels.add(new Label("ID"));
        profileLabels.add(new Label("Name"));
        profileLabels.add(new Label("Birth Date"));
        profileLabels.add(new Label("Contact"));
        profileLabels.add(new Label("E-mail"));
        profileLabels.add(new Label("Blood Group"));
        profileLabels.add(new Label("Gender"));
        profileLabels.add(new Label("Registration"));

        String readQuery = "SELECT * FROM patient WHERE patient_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(readQuery)) {
            stmt.setString(1,currentPatientId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String id = rs.getString("patient_id");
                    String name = rs.getString("name");
                    patientName=name;
                    java.sql.Date dob = rs.getDate("Dob");
                    String contact = rs.getString("contact");
                    String email = rs.getString("email");
                    String bloodGroup = rs.getString("blood_group");
                    String gender = rs.getString("gender");
                    java.sql.Date regDate = rs.getDate("registration_date");

                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

                    profileField.add(new TextField(id));
                    profileField.add(new TextField(name));
                    profileField.add(new TextField(formatter.format(dob)));
                    profileField.add(new TextField(contact));
                    profileField.add(new TextField(email));
                    profileField.add(new TextField(bloodGroup));
                    profileField.add(new TextField(gender));
                    profileField.add(new TextField(formatter.format(regDate)));
                } else {
                    System.out.println("No patient found with that email.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        view.getChildren().addAll(subtitle);

        Button backBtn = createStyledButton("⬅ Back", "#57606f");
        backBtn.setOnAction(e -> showDashboardScene());
        backBtn.setAlignment(Pos.CENTER);

        for (int i = 0; i < profileField.size(); i++) {
            if(i==0) profileField.get(i).setStyle("-fx-background-color : transparent; -fx-font-size:16;");
            profileField.get(i).setEditable(false);
            profileField.get(i).setPrefWidth(400);
            line.add(DoctorDashboard.createLabeledTextField(profileLabels.get(i).getText(), profileField.get(i)));
            view.getChildren().add(line.get(i));
        }
        view.getChildren().add(backBtn);

        return  new Scene(view, 600, 600);

    }
    private Scene buildUpdateProfileView() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        content.setStyle("-fx-font-size: 16;  -fx-border-color: transparent;");
        content.setSpacing(10);
        content.setFillWidth(true);

        Label header = new Label("Update Profile");
        header.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 22));

        ArrayList<Label> fieldNames = new ArrayList<>();
        fieldNames.add(new Label("ID"));
        fieldNames.add(new Label("Name"));
        fieldNames.add(new Label("Birth Date"));
        fieldNames.add(new Label("Contact"));
        fieldNames.add(new Label("E-mail"));
        fieldNames.add(new Label("Blood Group"));
        fieldNames.add(new Label("Gender"));
        fieldNames.add(new Label("Registration"));


        ArrayList<TextField> profileFields = new ArrayList<>();
        DatePicker dobPicker = null;

        // Read doctor info from DB
        String readQuery = "SELECT * FROM patient WHERE patient_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(readQuery)) {
            stmt.setString(1, currentPatientId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    profileFields.add(new TextField(rs.getString("patient_id"))); // ID
                    profileFields.add(new TextField(rs.getString("name")));      // Name
                    profileFields.add(new TextField(formatter.format(rs.getDate("Dob")))); // DOB
                    profileFields.add(new TextField(rs.getString("contact")));
                    profileFields.add(new TextField(rs.getString("email")));
                    profileFields.add(new TextField(rs.getString("blood_group")));
                    profileFields.add(new TextField(rs.getString("gender")));
                    profileFields.add(new TextField(formatter.format(rs.getDate("registration_date"))));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // ID and registration date are not editable
        if (!profileFields.isEmpty()) {
            profileFields.get(0).setStyle("-fx-background-color : transparent; -fx-font-size:16;");
            profileFields.get(0).setEditable(false);
            profileFields.get(7).setEditable(false);
        }

        content.getChildren().add(header);

        for (int i = 0; i < fieldNames.size(); i++) {
            if (i == 2) {
                dobPicker = new DatePicker(LocalDate.parse(profileFields.get(i).getText()));
                HBox dobBox = new HBox(15);
                dobBox.setAlignment(Pos.CENTER_LEFT);
                Label label = new Label("Birth Date");
                label.setMinWidth(110);
                label.setStyle("-fx-font-weight: bold;");
                dobBox.getChildren().addAll(label, dobPicker);
                HBox.setHgrow(dobPicker, Priority.ALWAYS);
                content.getChildren().add(dobBox);
            } else {
                TextField tf = profileFields.get(i);
                tf.setPrefWidth(400);
                HBox fieldBox = DoctorDashboard.createLabeledTextField(fieldNames.get(i).getText(), tf);
                content.getChildren().add(fieldBox);
            }
        }

        Button saveBtn = new Button("Save Changes");
        saveBtn.setAlignment(Pos.CENTER);
        Button backBtn = createStyledButton("⬅ Back", "#57606f");
        content.getChildren().addAll(saveBtn,backBtn);
        backBtn.setOnAction(e->{showDashboardScene();});

        // Set Save Action
        DatePicker finalDobPicker = dobPicker;
        saveBtn.setOnAction(e -> {
            try {
                String name = profileFields.get(1).getText();
                String contact = profileFields.get(3).getText();
                String email = profileFields.get(4).getText();
                String bloodGroup = profileFields.get(5).getText();
                String gender = profileFields.get(6).getText();
                String regDate = profileFields.get(7).getText();

                if (finalDobPicker == null || finalDobPicker.getValue() == null ||
                        name.isEmpty() || contact.isEmpty() || email.isEmpty() ||
                        bloodGroup.isEmpty() || gender.isEmpty() || regDate.isEmpty()) {
                    DoctorDashboard.showAlert(Alert.AlertType.ERROR, "Validation Error", "All fields must be filled out.");
                    return;
                }

                if (!DoctorDashboard.isValidEmail(email)) {
                    DoctorDashboard.showAlert(Alert.AlertType.ERROR, "Validation Error", "Invalid email format.");
                    return;
                }

                String updateQuery = "UPDATE patient SET name = ?, Dob = ?, contact = ?, email = ?, " +
                        "blood_group = ?, gender = ?, registration_date = ? WHERE patient_id = ?";

                try (Connection conn = getConnection();
                     PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
                    stmt.setString(1, name);
                    stmt.setDate(2, java.sql.Date.valueOf(finalDobPicker.getValue()));
                    stmt.setString(3, contact);
                    stmt.setString(4, email);
                    stmt.setString(5, bloodGroup);
                    stmt.setString(6, gender);
                    stmt.setDate(7, java.sql.Date.valueOf(regDate));
                    stmt.setString(8, profileFields.get(0).getText());

                    stmt.executeUpdate();
                    DoctorDashboard.showAlert(Alert.AlertType.INFORMATION, "Success", "Profile updated successfully.");
                    profileScene = buildProfileView(currentPatientId); // refresh
                }
                patientName=name;
            } catch (Exception ex) {
                DoctorDashboard.showAlert(Alert.AlertType.ERROR, "Error", "Failed to update profile.");
            }

        });
        return new Scene(content,600,600);
    }

    private void showDashboardScene() {
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #fbc2eb, #a6c1ee);");
        root.setSpacing(10);

        VBox contentBox = new VBox(12); // Reduced spacing between elements
        contentBox.setAlignment(Pos.CENTER);

        Text title = new Text("Welcome, " + patientName + " \uD83D\uDE0A");
        title.setFont(Font.font("Arial", 22));
        title.setStyle("-fx-fill: #333;");

        Text idText = new Text("Patient ID: " + currentPatientId);
        idText.setStyle("-fx-font-size: 14;");

        double buttonWidth = 260;
        double buttonHeight = 38;

        // Create styled buttons
        Button vitalsButton = createStyledButton("Vitals ❤", "#ffb347");
        Button profileButton = createStyledButton("View Profile", "#70a1ff");
        Button updateProfileButton = createStyledButton("Update Profile", "#2ed573");
        Button feedbackButton = createStyledButton("View Feedback", "#ffa502");
        Button prescriptionButton = createStyledButton("View Prescriptions", "#1abc9c");
        Button emergencyButton = createStyledButton("Emergency 🚨 (x2)", "#ff4c4c");
        Button chatButton = createStyledButton("Chat 💬", "#ffb447");
        Button appointmentButton = createStyledButton("Appointment 🩺", "#9b59b6");
        Button logoutButton = createStyledButton("Log Out", "#ff4c4c");

        // Set uniform size
        for (Button btn : new Button[]{
                vitalsButton, profileButton, updateProfileButton,
                feedbackButton, prescriptionButton, emergencyButton,
                chatButton, appointmentButton
        }) {
            btn.setPrefWidth(buttonWidth);
            btn.setPrefHeight(buttonHeight);
        }
        logoutButton.setPrefWidth(buttonWidth / 2);
        logoutButton.setPrefHeight(buttonHeight);

        // Actions
        vitalsButton.setOnAction(e->showVitalsOptionsScene());
        profileButton.setOnAction(e->primaryStage.setScene(profileScene));
        updateProfileButton.setOnAction(e->primaryStage.setScene(updateProfileScene));
        feedbackButton.setOnAction(e->showFeedback());
        prescriptionButton.setOnAction(e->showPrescriptions());

        emergencyButton.setOnAction(e -> {
            emergencyClickCount++;
            if (emergencyClickCount >= 2) {
                showEmergencyAlert("Manual Emergency Triggered!");
                emergencyClickCount = 0;
            }
        });
        chatButton.setOnAction(e->buildChatView());
        appointmentButton.setOnAction(e->showAppointmentScene());
        logoutButton.setOnAction(e -> {
            ((Stage)logoutButton.getScene().getWindow()).close();
            new LoginApp().start(new Stage());
        });

        // Add to contentBox
        contentBox.getChildren().addAll(
                title, idText,
                vitalsButton, profileButton, updateProfileButton,
                feedbackButton, prescriptionButton,
                emergencyButton, chatButton,
                appointmentButton, logoutButton
        );

        // Add ScrollPane in case of overflow
        ScrollPane scrollPane = new ScrollPane(contentBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");

        root.getChildren().add(scrollPane);

        Scene scene = new Scene(root, 500, 520); // Reduced height by ~20%
        primaryStage.setTitle("Patient Dashboard");
        primaryStage.setScene(scene);
        primaryStage.show();


    }


    private void showVitalsOptionsScene() {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #a1c4fd, #c2e9fb);");

        Button addVitalsButton = createStyledButton("➕ Add/Update Vitals", "#ffdd59");
        Button showVitalsButton = createStyledButton("📊 Show Vitals", "#70a1ff");
        Button uploadButton = createStyledButton(" Upload CSV", "#7bed9f");

        addVitalsButton.setPrefWidth(200);
        showVitalsButton.setPrefWidth(200);
        uploadButton.setPrefWidth(200);

        Button backButton = createStyledButton("⬅ Back", "#57606f");

        addVitalsButton.setOnAction(e -> showAddVitalsScene());
        showVitalsButton.setOnAction(e -> showVitalsChartScene(primaryStage,"patient"));
        uploadButton.setOnAction(e -> uploadVitalsFromCSV());
        backButton.setOnAction(e -> showDashboardScene());

        root.getChildren().addAll(addVitalsButton, showVitalsButton, uploadButton,backButton);

        Scene scene = new Scene(root, 400, 500);
        primaryStage.setTitle("Vitals Options");
        primaryStage.setScene(scene);
    }

    private void showAddVitalsScene() {
        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: linear-gradient(to right, #fbc2eb, #a6c1ee);");

        TextField tempField = createSizedTextField();
        TextField systolicField = createSizedTextField();
        TextField diastolicField = createSizedTextField();
        TextField heartRateField = createSizedTextField();
        TextField oxygenField = createSizedTextField();

        VBox inputs = new VBox(10,
                createLabeledInput("🌡 Temperature (°C):", tempField),
                createLabeledInput("🩺 Systolic BP (mmHg):", systolicField),
                createLabeledInput("🩸 Diastolic BP (mmHg):", diastolicField),
                createLabeledInput("❤ Heart Rate (bpm):", heartRateField),
                createLabeledInput("🫁 Oxygen Level (%):", oxygenField)
        );
        inputs.setAlignment(Pos.CENTER);

        Button saveButton = createStyledButton("💾 Save Vitals", "#2ed573");
        Button backButton = createStyledButton("⬅ Back", "#57606f");
        backButton.setOnAction(e -> showVitalsOptionsScene());
        saveButton.setOnAction(e -> {
            try {
                double temperature = Double.parseDouble(tempField.getText());
                double systolic = Double.parseDouble(systolicField.getText());
                double diastolic = Double.parseDouble(diastolicField.getText());
                double heartRate = Double.parseDouble(heartRateField.getText());
                double oxygen = Double.parseDouble(oxygenField.getText());

                LocalDateTime now = LocalDateTime.now();
                vitalsHistory.add(new VitalRecord(now, temperature, systolic, diastolic, heartRate, oxygen));

                // Insert into database
                try (Connection conn = getConnection()) {
                    String sql = "INSERT INTO vitals (patient_id, timestamp, temperature, systolic, diastolic, heart_rate, oxygen) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, currentPatientId);
                    stmt.setTimestamp(2, Timestamp.valueOf(now));
                    stmt.setDouble(3, temperature);
                    stmt.setDouble(4, systolic);
                    stmt.setDouble(5, diastolic);
                    stmt.setDouble(6, heartRate);
                    stmt.setDouble(7, oxygen);
                    stmt.executeUpdate();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    showErrorAlert("Failed to save vitals to database!");
                    return;
                }

                if (isEmergencyCondition(temperature, systolic, diastolic, heartRate, oxygen)) {
                    showEmergencyAlert("Automatic Emergency Detected based on Vitals!");
                } else {
                    tempField.clear();systolicField.clear();diastolicField.clear();heartRateField.clear();oxygenField.clear();
                    DoctorDashboard.showAlert(Alert.AlertType.INFORMATION,"Success","Vitals Uploaded Successfully");
                    showDashboardScene();
                }
            } catch (NumberFormatException ex) {
                showErrorAlert("Invalid input! Please enter valid numbers.");
            }
        });

        root.getChildren().addAll(inputs, saveButton, backButton);

        Scene scene = new Scene(root, 550, 650);
        primaryStage.setTitle("Add/Update Vitals");
        primaryStage.setScene(scene);
    }


    private TextField createSizedTextField() {
        TextField tf = new TextField();
        tf.setMinWidth(100);
        tf.setPrefWidth(350);
        tf.setMaxWidth(900);
        tf.setStyle(
                "-fx-background-color: #ffffff;" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-radius: 10;" +
                        "-fx-border-color: #ccc;" +
                        "-fx-padding: 5 10 5 10;" +
                        "-fx-font-size: 14px;"
        );

        return tf;
    }

    private HBox createLabeledInput(String labelText, TextField inputField) {
        Label label = createStyledLabel(labelText);
        label.setMinWidth(180);
        HBox box = new HBox(10, label, inputField);
        box.setAlignment(Pos.CENTER);

        return box;
    }


    public  void showVitalsChartScene(Stage primaryStage,String callerType) {
        this.primaryStage=primaryStage;
        this.caller=callerType;

        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Time Index");

        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("📈 Vitals Over Time");

        // Define all series
        XYChart.Series<Number, Number> tempSeries = new XYChart.Series<>();
        XYChart.Series<Number, Number> sysSeries = new XYChart.Series<>();
        XYChart.Series<Number, Number> diaSeries = new XYChart.Series<>();
        XYChart.Series<Number, Number> hrSeries = new XYChart.Series<>();
        XYChart.Series<Number, Number> oxySeries = new XYChart.Series<>();

        tempSeries.setName("Temperature");
        sysSeries.setName("Systolic");
        diaSeries.setName("Diastolic");
        hrSeries.setName("Heart Rate");
        oxySeries.setName("Oxygen");

        // Fill data once
        List<VitalRecord> vitals = new ArrayList<>();
        String selectQuery="SELECT timestamp, temperature, systolic, diastolic, heart_rate, oxygen FROM vitals WHERE patient_id = ? ORDER BY timestamp";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(selectQuery);){
            stmt.setString(1,currentPatientId);
            try (ResultSet rs = stmt.executeQuery();) {

                while (rs.next()) {
                    VitalRecord vr = new VitalRecord();
                    vr.temperature = rs.getDouble("temperature");
                    vr.systolic = rs.getDouble("systolic");
                    vr.diastolic = rs.getDouble("diastolic");
                    vr.heartRate = rs.getDouble("heart_rate");
                    vr.oxygen = rs.getDouble("oxygen");
                    vitals.add(vr);
                }
            }catch (SQLException ex) {
                showErrorAlert("Failed to get vitals from database!");
                ex.printStackTrace();
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (vitals.isEmpty()) {
            showErrorAlert("No vital records found for this patient.");
            return;
        }
        for (int i = 0; i < vitals.size(); i++) {
            VitalRecord vr = vitals.get(i);
            tempSeries.getData().add(new XYChart.Data<>(i, vr.temperature));
            sysSeries.getData().add(new XYChart.Data<>(i, vr.systolic));
            diaSeries.getData().add(new XYChart.Data<>(i, vr.diastolic));
            hrSeries.getData().add(new XYChart.Data<>(i, vr.heartRate));
            oxySeries.getData().add(new XYChart.Data<>(i, vr.oxygen));
        }

        // Checkboxes
        CheckBox tempCheck = new CheckBox("🌡 Temperature");
        CheckBox sysCheck = new CheckBox("🩺 Systolic");
        CheckBox diaCheck = new CheckBox("🩸 Diastolic");
        CheckBox hrCheck = new CheckBox("❤ Heart Rate");
        CheckBox oxyCheck = new CheckBox("🫁 Oxygen");

        // Default all selected
        tempCheck.setSelected(true);
        sysCheck.setSelected(true);
        diaCheck.setSelected(true);
        hrCheck.setSelected(true);
        oxyCheck.setSelected(true);

        // Add all selected series initially
        lineChart.getData().addAll(tempSeries, sysSeries, diaSeries, hrSeries, oxySeries);

        // Checkbox event handler
        tempCheck.setOnAction(e -> toggleSeries(lineChart, tempCheck, tempSeries));
        sysCheck.setOnAction(e -> toggleSeries(lineChart, sysCheck, sysSeries));
        diaCheck.setOnAction(e -> toggleSeries(lineChart, diaCheck, diaSeries));
        hrCheck.setOnAction(e -> toggleSeries(lineChart, hrCheck, hrSeries));
        oxyCheck.setOnAction(e -> toggleSeries(lineChart, oxyCheck, oxySeries));

        VBox checkboxBox = new VBox(10, tempCheck, sysCheck, diaCheck, hrCheck, oxyCheck);
        checkboxBox.setPadding(new Insets(10));
        checkboxBox.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #333;");
        checkboxBox.setAlignment(Pos.TOP_LEFT);

        Button backButton = createStyledButton("⬅ Back", "#57606f");
        backButton.setOnAction(e -> {
            Stage stage = (Stage) backButton.getScene().getWindow();
            if ("doctor".equalsIgnoreCase(caller)) {
                stage.close();
            } else {
                showDashboardScene();
            }
        });

        VBox leftPanel = new VBox(20, checkboxBox, backButton);
        leftPanel.setPadding(new Insets(10));

        HBox root = new HBox(10, leftPanel, lineChart);
        root.setPadding(new Insets(10));

        Scene scene = new Scene(root, 1000, 550);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Show Vitals");
        primaryStage.show();
    }


    private void toggleSeries(LineChart<Number, Number> chart, CheckBox checkBox, XYChart.Series<Number, Number> series) {
        if (checkBox.isSelected()) {
            if (!chart.getData().contains(series)) {
                chart.getData().add(series);
            }
        } else {
            chart.getData().remove(series);
        }
    }


    private void uploadVitalsFromCSV() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Vitals CSV File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showOpenDialog(null);

        if (file == null) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file));
             Connection conn = getConnection()) {

            String insertSQL = "INSERT INTO vitals (patient_id, timestamp, temperature, systolic, diastolic, heart_rate, oxygen) " +
                    "VALUES ( ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(insertSQL);

            String line;
            int lineNum = 0;

            while ((line = reader.readLine()) != null) {
                lineNum++;
                if (lineNum == 1) continue; // Skip header

                String[] parts = line.split(",");
                if (parts.length < 5) {
                    System.err.println("Invalid line at " + lineNum + ": " + line);
                    continue;
                }

                double temperature = Double.parseDouble(parts[0].trim());
                double systolic = Double.parseDouble(parts[1].trim());
                double diastolic = Double.parseDouble(parts[2].trim());
                double heartRate = Double.parseDouble(parts[3].trim());
                double oxygen = Double.parseDouble(parts[4].trim());

                pstmt.setString(1, currentPatientId);
//                ,DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss")
                pstmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now())); // Ensure this is in correct format (e.g. "yyyy-MM-dd HH:mm:ss")
                pstmt.setDouble(3, temperature);
                pstmt.setDouble(4, systolic);
                pstmt.setDouble(5, diastolic);
                pstmt.setDouble(6, heartRate);
                pstmt.setDouble(7, oxygen);

                pstmt.addBatch();
            }

            pstmt.executeBatch();
            DoctorDashboard.showAlert(Alert.AlertType.INFORMATION, "Success", "Vitals uploaded successfully!");

        } catch (Exception ex) {
            DoctorDashboard.showAlert(Alert.AlertType.ERROR, "Error", "Failed to upload: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private boolean isEmergencyCondition(double temperature, double systolic, double diastolic, double heartRate, double oxygen) {
        return temperature > 38.0 || temperature < 35.0 ||
                systolic > 140 || systolic < 90 ||
                diastolic > 90 || diastolic < 60 ||
                heartRate > 120 || heartRate < 50 ||
                oxygen < 90;
    }

    private void showEmergencyAlert(String message) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Emergency Alert");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
        EmailUtil.sendEmergencyEmail(patientName, message);
        showDashboardScene();
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Alert");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private Button createStyledButton(String text, String color) {
        Button button = new Button(text);
        button.setStyle(
                "-fx-background-color: " + color + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 10 20 10 20;" +
                        "-fx-background-radius: 20;" +
                        "-fx-cursor: hand;"
        );
        return button;
    }
    private void buildChatView() {
        // Launch chat interface with proper identification
        new ChatClient(currentPatientId, "Patient").start(new Stage());
    }
    private Label createStyledLabel(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        label.setStyle("-fx-text-fill: #2f3542;");
        return label;
    }

    static class VitalRecord {
        LocalDateTime timestamp;
        double temperature, systolic, diastolic, heartRate, oxygen;
        public VitalRecord(){}
        public VitalRecord(LocalDateTime timestamp, double temperature, double systolic, double diastolic, double heartRate, double oxygen) {
            this.timestamp = timestamp;
            this.temperature = temperature;
            this.systolic = systolic;
            this.diastolic = diastolic;
            this.heartRate = heartRate;
            this.oxygen = oxygen;
        }

        public String toCSV() {
            return timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + "," + temperature + "," + systolic + "," + diastolic + "," + heartRate + "," + oxygen;
        }
    }

    static class EmailUtil {
        public static void sendEmergencyEmail(String patientName, String messageBody) {
            String to = "ahmedali253721@gmail.com";
            String from = "doctor420.420.420f@gmail.com";
            String password = "mmqmvvicipvpoyom";
            String host = "smtp.gmail.com";

            Properties props = new Properties();
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");

            Session session = Session.getInstance(props,
                    new Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(from, password);
                        }
                    });

            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(from));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
                message.setSubject("🚨 Emergency Alert for " + patientName);
                message.setText(messageBody);

                Transport.send(message);
                System.out.println("Email sent successfully.");
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
    }

    private void showAppointmentScene() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(15);
        grid.setAlignment(Pos.CENTER);

        ComboBox<String> doctorCombo = new ComboBox<>();
        ComboBox<java.sql.Date> dateCombo = new ComboBox<>();
        ComboBox<String> timeCombo = new ComboBox<>();
        TextField reasonField = new TextField();
        reasonField.setPromptText("Enter reason for appointment");

        Button submitBtn = new Button("Apply for Appointment");
        Button backBtn = new Button("⬅ Back");
        backBtn.setStyle("-fx-background-color: #57606f; -fx-text-fill: white;");

        Map<String, String> nameToId = new HashMap<>();

        // Load doctors with availability
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT DISTINCT d.doctor_id, d.name, d.specialization " +
                             "FROM doctor d JOIN doctor_availability a ON d.doctor_id = a.doctor_id")) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String id = rs.getString("doctor_id");
                String name = rs.getString("name");
                String spec = rs.getString("specialization");
                String display = name + " (" + spec + ")";
                nameToId.put(display, id);
                doctorCombo.getItems().add(display);
            }
        } catch (SQLException e) {
            showErrorAlert("Unable to load doctors from database.");
            e.printStackTrace();
        }

        doctorCombo.setOnAction(e -> {
            dateCombo.getItems().clear();
            timeCombo.getItems().clear();
            String selectedDoctor = doctorCombo.getValue();
            if (selectedDoctor != null) {
                try (Connection conn = getConnection();
                     PreparedStatement stmt = conn.prepareStatement(
                             "SELECT DISTINCT date FROM doctor_availability WHERE doctor_id = ?")) {
                    stmt.setString(1, nameToId.get(selectedDoctor));
                    ResultSet rs = stmt.executeQuery();
                    while (rs.next()) {
                        LocalDate date = rs.getDate("date").toLocalDate();
                        if (!date.isBefore(LocalDate.now())) {
                            dateCombo.getItems().add(java.sql.Date.valueOf(date));
                        }
                    }
                } catch (SQLException ex) {
                    showErrorAlert("Unable to load dates.");
                    ex.printStackTrace();
                }
            }
        });

        dateCombo.setOnAction(e -> {
            timeCombo.getItems().clear();
            String selectedDoctor = doctorCombo.getValue();
            java.sql.Date selectedDate = dateCombo.getValue();
            if (selectedDoctor != null && selectedDate != null) {
                try (Connection conn = getConnection();
                     PreparedStatement stmt = conn.prepareStatement(
                             "SELECT time FROM doctor_availability WHERE doctor_id = ? AND date = ?")) {
                    stmt.setString(1, nameToId.get(selectedDoctor));
                    stmt.setDate(2, selectedDate);
                    ResultSet rs = stmt.executeQuery();
                    while (rs.next()) {
                        timeCombo.getItems().add(rs.getString("time"));
                    }
                } catch (SQLException ex) {
                    showErrorAlert("Unable to load times.");
                    ex.printStackTrace();
                }
            }
        });

        submitBtn.setOnAction(e -> {
            String doctorDisplay = doctorCombo.getValue();
            java.sql.Date date = dateCombo.getValue();
            String time = timeCombo.getValue();
            String reason = reasonField.getText();

            if (doctorDisplay != null && date != null && time != null && !reason.isBlank()) {
                String docId = nameToId.get(doctorDisplay);
                Appointment request = new Appointment("pending",docId,currentPatientId,date.toString(),time,reason);

                try (Connection conn = getConnection();
                     PreparedStatement stmt = conn.prepareStatement(
                             "INSERT INTO appointments VALUES (?, ?, ?, ?, ?, ?, ?)")) {
                    stmt.setString(1, request.getAppointmentId());
                    stmt.setString(2, request.getDoctor());
                    stmt.setString(3, currentPatientId);
                    stmt.setDate(4, date);
                    stmt.setString(5, request.getTime());
                    stmt.setString(6, request.getStatus());
                    stmt.setString(7, request.getReason());

                    stmt.executeUpdate();
                    DoctorDashboard.showAlert(Alert.AlertType.INFORMATION, "Success", "Appointment request submitted successfully.");
                    doctorCombo.getSelectionModel().clearSelection();
                    dateCombo.getItems().clear();
                    timeCombo.getItems().clear();
                    reasonField.clear();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    DoctorDashboard.showAlert(Alert.AlertType.ERROR, "Failed", "Error submitting appointment.");
                }
            } else {
                DoctorDashboard.showAlert(Alert.AlertType.ERROR, "Missing Fields", "Please complete all fields.");
            }
        });

        backBtn.setOnAction(e -> showDashboardScene());

        grid.add(new Label("Select Doctor:"), 0, 0);
        grid.add(doctorCombo, 1, 0);
        grid.add(new Label("Select Date:"), 0, 1);
        grid.add(dateCombo, 1, 1);
        grid.add(new Label("Select Time:"), 0, 2);
        grid.add(timeCombo, 1, 2);
        grid.add(new Label("Reason:"), 0, 3);
        grid.add(reasonField, 1, 3);
        grid.add(submitBtn, 1, 4);
        grid.add(backBtn, 0, 4);

        root.getChildren().add(grid);
        Scene scene = new Scene(root, 500, 400);
        primaryStage.setScene(scene);
    }



    private void showFeedback() {
        String patientId = PatientDashboardApp.currentPatientId;
        String sql = "SELECT doctor_id, message FROM feedback WHERE patient_id = ?";

        StringBuilder feedbackMessages = new StringBuilder();

        try (Connection conn = Database.DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, patientId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String doctorId = rs.getString("doctor_id");
                String message = rs.getString("message");
                feedbackMessages.append("From Doctor ID: ").append(doctorId).append("\n")
                        .append("Feedback: ").append(message).append("\n\n");
            }

            if (feedbackMessages.length() == 0) {
                feedbackMessages.append("No feedback found.");
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Your Feedback / Prescription");
            alert.setHeaderText("Feedback from Doctors");
            alert.setContentText(feedbackMessages.toString());
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE); // expand alert if text is long
            alert.showAndWait();

        }
        catch (SQLException e) {
            e.printStackTrace();
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Database Error");
            errorAlert.setHeaderText("Could not fetch feedback");
            errorAlert.setContentText(e.getMessage());
            errorAlert.showAndWait();
        }
    }


    private void showPrescriptions() {
        Stage stage = new Stage();
        stage.setTitle("Prescriptions");

        VBox root = new VBox(10);
        root.setPadding(new Insets(15));
        root.setAlignment(Pos.CENTER_LEFT);
        root.setStyle("-fx-background-color: #f0f8ff;");

        Label title = new Label("Your Prescriptions:");
        title.setFont(Font.font("Arial", 18));
        title.setTextFill(Color.DARKSLATEBLUE);
        root.getChildren().add(title);

        String query = "SELECT * FROM prescriptions WHERE patient_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, currentPatientId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String prescrip = String.format(
                        "Date: %s\nMedicine: %s\nDosage: %s\nSchedule: %s\nDoctor ID: %s\n",
                        rs.getDate("date_issued"),
                        rs.getString("medicine_name"),
                        rs.getString("dosage"),
                        rs.getString("schedule"),
                        rs.getString("doctor_id")
                );

                TextArea area = new TextArea(prescrip);
                area.setEditable(false);
                area.setWrapText(true);
                area.setPrefWidth(400);
                root.getChildren().add(area);
            }

        }
        catch (SQLException e) {
            e.printStackTrace();
            Label error = new Label("Error loading prescriptions.");
            error.setTextFill(Color.RED);
            root.getChildren().add(error);
        }

        Scene scene = new Scene(root, 450, 500);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
