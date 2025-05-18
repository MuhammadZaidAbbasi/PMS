package com.example.system;

import AppointmentScheduling.Appointment;
import DoctorPatientInteraction.Prescription;
import NotificationAndRemainder.EmailNotification;
import NotificationAndRemainder.RemainderService;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import management.*;
import static Database.DataBaseConnection.getConnection;

/**
 * DoctorDashboard Application
 * A JavaFX application for a doctor's dashboard,
 * including sidebar navigation, patient list, profile view, feedback form, and profile update form.
 * This single-file implementation uses modular methods for each view and helper methods for UI components.
 * It also includes basic validation and alert dialogs for success/error notifications.
 */
public class DoctorDashboard extends Application {
    private static final Logger LOGGER = Logger.getLogger(DoctorDashboard.class.getName());

    // In-memory dummy data for Doctor and Patients
    public static String currentDoctorId;
    String currentDoctorName;
    RemainderService notify = new RemainderService();
    // Main layout pane
    private BorderPane root;

    // Views
    private VBox sidebar;
    private VBox profileView;
    private Pane patientListView;
    private VBox updateProfileView;
    private Pane interactionView;
    private Pane AppointmentView;
    private Pane EmailView;
    private Pane chatView;
    private Pane vitalView;


    @Override
    public void start(Stage primaryStage) {

        sidebar = buildSidebar();
        // Main layout
        root = new BorderPane();

        profileView = buildProfileView(currentDoctorId);
        profileView.setPadding(new Insets(0,20,0,20));
        patientListView = buildPatientListView();
        updateProfileView = buildUpdateProfileView();
        interactionView = buildInteractionPane();
        AppointmentView=buildAppointmentView();
        EmailView=buildEmailView();
        vitalView=buildVitalView();
        // Build sidebar

        Label title = new Label("Welcome To Doctor's Dashboard");
        title.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 26));
        title.setTextFill(Color.BLACK);

        HBox topBox = new HBox();
        topBox.setAlignment(Pos.CENTER);
        topBox.getChildren().add(title);

        root.setTop(topBox);
        root.setLeft(sidebar);
        root.setCenter(profileView); // Default view

        // Scene and stage setup
        Scene scene = new Scene(root, 1000, 600); // starting size
        primaryStage.setTitle("Doctor Dashboard");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(500);
        primaryStage.show();
    }

    private void buildChatView() {
        // Launch chat interface with proper identification
        new ChatClient(currentDoctorId, "Doctor").start(new Stage());
    }

    private Pane buildVitalView() {
    VBox view = new VBox();
        view.setStyle("-fx-font-size: 16 ;");
        view.setPadding(new Insets(20));

        Label header = new Label("Patient's Vitals ");
        header.setAlignment(Pos.TOP_CENTER);
        header.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 22));

        TextField patientIdField = new TextField();
        patientIdField.setPromptText("Enter patient id here");

        HBox idBox = createLabeledTextField("Patient Id :", patientIdField);
        idBox.setPadding(new Insets(10,0,10,0));
        Button vitalBtn = new Button("See Vitals");

        vitalBtn.setOnAction(e -> {
            try {
                String id = patientIdField.getText();
                if (id == null || id.isEmpty()) {
                    showAlert(Alert.AlertType.WARNING, "Missing Input", "Please enter a patient ID.");
                    return;
                }

                PatientDashboardApp vit = new PatientDashboardApp();
                PatientDashboardApp.currentPatientId = id;
                Stage stage = new Stage();
                vit.showVitalsChartScene(stage, "doctor");
            } catch(Exception ex){
                showAlert(Alert.AlertType.ERROR,"Error","Unable to show vital chart");
                ex.printStackTrace(); // Add this for debugging
            }
        });


        view.getChildren().addAll(header,idBox,vitalBtn);
        return view;
    }

    private Pane buildAppointmentView(){
        VBox mainView = new VBox(20);
        mainView.setPadding(new Insets(20));
        mainView.setStyle("-fx-font-size: 14px;");

        Label header = new Label("Appointment Manager");
        header.setFont(Font.font("Arial", FontWeight.BOLD, 22));

        // ----------------- Availability Section -----------------
        Label availHeader = new Label("Set Availability");
        availHeader.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Select Date");

        ComboBox<String> TimeBox = new ComboBox<>();
        TimeBox.setPromptText("Select Time Slot");
        for (int hour = 8; hour <= 17; hour++) {
            TimeBox.getItems().add(LocalTime.of(hour, 0) + " - " + LocalTime.of(hour + 1, 0));
        }

        datePicker.valueProperty().addListener((obs, oldDate, newDate) -> {
            if (newDate != null && newDate.isBefore(LocalDate.now())) {
                showAlert(Alert.AlertType.WARNING, "Invalid Date", "You cannot select a past date.");
                datePicker.setValue(null);
                TimeBox.setDisable(true);
            } else {
                TimeBox.setDisable(false);
            }
        });

        Button addAvailabilityBtn = new Button("Add Availability");
        addAvailabilityBtn.setOnAction(e -> {
            LocalDate date = datePicker.getValue();
            String time = TimeBox.getValue();

            if (date == null || time == null) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please Select Both Date and Time First.");
            } else {
                String insert = "INSERT INTO doctor_availability VALUES (?, ?, ?)";
                try (Connection conn = getConnection();
                     PreparedStatement statement = conn.prepareStatement(insert)) {

                    statement.setString(1, currentDoctorId);
                    statement.setString(2, time);
                    statement.setDate(3, java.sql.Date.valueOf(date));

                    statement.executeUpdate();
                } catch (Exception ex) {
                    LOGGER.log(Level.SEVERE, "Database error occurred", ex);
                }
                showAlert(Alert.AlertType.INFORMATION, "Confirmation", "Availability Added To Database Successfully");

                datePicker.setValue(null);
                TimeBox.setValue(null);
            }
        });

        HBox availInput = new HBox(10, new Label("Date:"), datePicker,
                new Label("Time:"), TimeBox, addAvailabilityBtn);
        availInput.setAlignment(Pos.CENTER_LEFT);

        // ----------------- Appointment Toggle Section -----------------
        HBox toggleButtons = new HBox(10);
        Button requestsBtn = new Button("Appointment Requests");
        Button upcomingBtn = new Button("Upcoming Appointments");

        String selectedStyle = "-fx-background-color: lightgray; -fx-font-weight:bolder;";
        String defaultStyle = "-fx-background-color: white;-fx-font-weight:bold;";
        requestsBtn.setStyle(selectedStyle); // default selected
        upcomingBtn.setStyle(defaultStyle);

        toggleButtons.getChildren().addAll(requestsBtn, upcomingBtn);
        toggleButtons.setAlignment(Pos.CENTER_LEFT);

        // ----------------- Appointment List Section -----------------
        VBox appointmentList = new VBox(10);
        appointmentList.setPadding(new Insets(10));
        appointmentList.setStyle("-fx-background-color: #f4f4f4; -fx-border-radius: 5px;");

        ScrollPane scrollPane = new ScrollPane(appointmentList);
        scrollPane.setFitToWidth(true);

        appointmentList.setPrefWidth(Region.USE_COMPUTED_SIZE);
        appointmentList.setMaxWidth(Double.MAX_VALUE);

        // ----------------- Button Actions -----------------
        requestsBtn.setOnAction(e -> {
            requestsBtn.setStyle(selectedStyle);
            upcomingBtn.setStyle(defaultStyle);
            appointmentList.getChildren().clear();
            loadAppointmentsByStatus(currentDoctorId, "pending", appointmentList);
        });

        upcomingBtn.setOnAction(e -> {
            upcomingBtn.setStyle(selectedStyle);
            requestsBtn.setStyle(defaultStyle);
            appointmentList.getChildren().clear();
            loadAppointmentsByStatus(currentDoctorId, "approved", appointmentList);
        });

        // ----------------- Default Load -----------------
        loadAppointmentsByStatus(currentDoctorId, "pending", appointmentList);

        // ----------------- Add to Main View -----------------
        mainView.getChildren().addAll(header, availHeader, availInput, toggleButtons, scrollPane);
        return mainView;
    }

    // ----------------- Load Appointments by Status -----------------
    private void loadAppointmentsByStatus (String doctorId, String status, VBox appointmentList) {
        appointmentList.getChildren().clear();
        ArrayList<Appointment> requests = new ArrayList<>();
        ArrayList<String> appIdList = new ArrayList<>();
        String selectQuery = "SELECT * FROM appointments WHERE doctor_id = ? AND status = ?";
        String subQuery = "SELECT name FROM patient WHERE patient_id = ?";


        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(selectQuery)) {

            statement.setString(1, doctorId);
            statement.setString(2, status);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                appIdList.add(resultSet.getString("app_id"));
                String patientId = resultSet.getString("patient_id");

                PreparedStatement subStatement = conn.prepareStatement(subQuery);
                subStatement.setString(1, patientId);
                ResultSet rs = subStatement.executeQuery();
                rs.next();
                String patientName = rs.getString("name");

                Appointment appointment = new Appointment(
                        resultSet.getString("status"),
                        doctorId,
                        patientName,
                        resultSet.getDate("date").toString(),
                        resultSet.getString("time"),
                        resultSet.getString("reason")
                );
                requests.add(appointment);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error occurred", e);
        }

        if (requests.isEmpty()) {
            Label noAppointments = new Label("No Appointment Request Available.");
            noAppointments.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
            noAppointments.setTextFill(Color.GRAY);
            appointmentList.getChildren().add(noAppointments);
        } else {
            for (int i = 0; i < requests.size(); i++) {
                Appointment appt = requests.get(i);
                String appId = appIdList.get(i);

                Label id = new Label("Appointment Id  :  " + appId);
                Label patientLabel = new Label("Patient  : " + appt.getPatient());
                Label dateLabel    = new Label("Date      : " + appt.getDate());
                Label timeLabel    = new Label("Time      : " + appt.getTime());
                Label reasonLabel  = new Label("Reason  : "+ appt.getReason());
                Label statusLabel  = new Label("Status    : " + appt.getStatus());

                VBox apptBox;
                if (status.equalsIgnoreCase("pending")) {
                    Button approveBtn = new Button("Approve");
                    Button rejectBtn = new Button("Reject");

                    approveBtn.setOnAction(e ->{
                        updateAppointmentStatus(appId,"Approved");
                        notify.sendAppointmentRemainder("Your Appointment with Dr."+ currentDoctorName+"  On "+
                                appt.getDate()+" at "+ appt.getTime()+"  is Approved.","zk417704@gmail.com");
                        assignPatient(appId);
                        deleteAvailability(currentDoctorId, appt.getDate(),appt.getTime());
                        loadAppointmentsByStatus(doctorId,status,appointmentList);
                            });

                    rejectBtn.setOnAction(e -> {
                        updateAppointmentStatus(appId, "Rejected");
                        notify.sendAppointmentRemainder("Your Appointment with Dr."+ currentDoctorName +"  On "+
                                appt.getDate()+" at "+ appt.getTime()+"  is Rejected.","zk417704@gmail.com");
                        loadAppointmentsByStatus(doctorId, status, appointmentList);
                    });

                    HBox actionBox = new HBox(10, approveBtn, rejectBtn);
                    actionBox.setAlignment(Pos.CENTER_LEFT);

                    apptBox = new VBox(5, id, patientLabel, dateLabel, timeLabel, reasonLabel, statusLabel, actionBox);
                } else {
                    // Add Cancel button for upcoming appointments
                    Button cancelBtn = new Button("Cancel");

                    cancelBtn.setOnAction(e -> {
                        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                        confirm.setTitle("Cancel Appointment");
                        confirm.setHeaderText("Are you sure you want to cancel this appointment?");
                        confirm.setContentText("This action cannot be undone.");

                        Optional<ButtonType> result = confirm.showAndWait();
                        if (result.isPresent() && result.get() == ButtonType.OK) {
                            updateAppointmentStatus(appId, "Cancelled");
                            notify.sendAppointmentRemainder("Your Appointment with Dr."+ currentDoctorName +" On "+
                                    appt.getDate()+" at "+ appt.getTime()+" is Cancelled","zk417704@gmail.com");
                            loadAppointmentsByStatus(doctorId, status, appointmentList);
                        }
                    });

                    HBox actionBox = new HBox(10, cancelBtn);
                    actionBox.setAlignment(Pos.CENTER_LEFT);

                    apptBox = new VBox(5, id, patientLabel, dateLabel, timeLabel, reasonLabel, statusLabel, actionBox);
                }

                apptBox.setPadding(new Insets(5, 0, 10, 10));
                apptBox.setStyle("-fx-background-color: #fff; -fx-border-color: #ddd; -fx-border-radius: 5px;");
                appointmentList.getChildren().add(apptBox);
            }
        }
    }

    private void deleteAvailability(String currentDoctorId, String date, String time) {
        try(Connection conn = getConnection();
            PreparedStatement statement = conn.prepareStatement("DELETE FROM" +
                    " doctor_availability WHERE doctor_id = ? AND time = ? AND date = ?")){
            statement.setString(1,currentDoctorId);
            statement.setString(2,time);
            statement.setDate(3,java.sql.Date.valueOf(date));
            statement.executeUpdate();

        }catch(SQLException e){
            showAlert(Alert.AlertType.ERROR,"Database error","Unable to access database");
        }
    }

    private void assignPatient(String appId){
        String get = "SELECT patient_id FROM appointments WHERE app_id = ?";
        String patientId = null;
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(get)) {

            statement.setString(1, appId);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                patientId = rs.getString("patient_id");
            } else {
                LOGGER.log(Level.WARNING, "No appointment found with app_id: " + appId);
                return;
            }

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Database error occurred while fetching patient ID", ex);
            return;
        }
        String insert = "INSERT INTO doctor_patient_assignment(doctor_id, patient_id, app_id, AssignedDate) VALUES (?, ?, ?, ?) ";
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(insert)) {
            statement.setString(1, currentDoctorId);
            statement.setString(2, patientId);
            statement.setString(3, appId);
            statement.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));

            statement.executeUpdate();

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Database error occurred while inserting assignment", ex);
        }
    }

    // ----------------- Update Appointment Status -----------------
    private void updateAppointmentStatus(String appId, String status) {
        String update = "UPDATE appointments SET status = ? WHERE app_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(update)) {
            statement.setString(1, status);
            statement.setString(2, appId);
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error occurred", e);
        }

    }


    private void UpdateBtn(List<Button> btn,Button current){
        for(Button currentBtn :btn) {
            if (currentBtn.equals(current)){
                currentBtn.setStyle("-fx-background-color: green; -fx-border-color: black;-fx-border-width: 3px;" +
                        "-fx-font-size: 14; -fx-text-fill: black; -fx-font-weight: bold;   ");
            }
            else{
                currentBtn.setStyle("-fx-background-color: #ffffff; -fx-text-fill: #336699;-fx-font-size: 14; -fx-font-weight: bold;");// reset style
            }
        }
    }

    /**
     * Builds the sidebar navigation with buttons for each section.
     * returns a VBox containing the sidebar components.
     */
    private VBox buildSidebar() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(15));
        box.setPrefWidth(200);
        box.setStyle("-fx-background-color: #336699;");

        List<Button> allButton=new ArrayList<>();

        Button profileBtn = new Button("Profile");
        allButton.add(profileBtn);
        profileBtn.setMaxWidth(Double.MAX_VALUE);
        profileBtn.setOnAction(e -> {
            UpdateBtn(allButton,profileBtn);
            try {
                root.setCenter(profileView);
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to show profile view.");
            }
        });

        Button updateBtn = new Button("Update Profile");
        allButton.add(updateBtn);
        updateBtn.setMaxWidth(Double.MAX_VALUE);
        updateBtn.setOnAction(e -> {
            UpdateBtn(allButton,updateBtn);
            try {
               root.setCenter(updateProfileView);
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to show update profile view.");
            }
        });

        Button patientsBtn = new Button("Patients");
        allButton.add(patientsBtn);
        patientsBtn.setMaxWidth(Double.MAX_VALUE);
        patientsBtn.setOnAction(e -> {
            UpdateBtn(allButton,patientsBtn);
            try {
                root.setCenter(patientListView);
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to show patient list.");
            }
        });

        Button feedbackBtn = new Button("Interaction ");
        allButton.add(feedbackBtn);
        feedbackBtn.setMaxWidth(Double.MAX_VALUE);
        feedbackBtn.setOnAction(e -> {
            UpdateBtn(allButton,feedbackBtn);
            try {
                root.setCenter(interactionView);
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to show feedback form.");
            }
        });


        Button appointmentBtn=new Button("Appointment");
        allButton.add(appointmentBtn);
        appointmentBtn.setMaxWidth(Double.MAX_VALUE);
        appointmentBtn.setOnAction(e -> {
            UpdateBtn(allButton,appointmentBtn);
            try {
                root.setCenter(AppointmentView);
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to show Appointment view.");
            }
        });

        Button emailBtn=new Button("Send Email");
        allButton.add(emailBtn);
        emailBtn.setMaxWidth(Double.MAX_VALUE);
        emailBtn.setOnAction(e -> {
            UpdateBtn(allButton,emailBtn);
            try {
                root.setCenter(EmailView);
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to show Email view.");
            }
        });

        Button chatBtn = new Button("Start Chat");
        allButton.add(chatBtn);
        chatBtn.setMaxWidth(Double.MAX_VALUE);
        chatBtn.setOnAction(e -> {
            UpdateBtn(allButton,chatBtn);
            try {
                buildChatView();
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to start chat.");
            }
        });

        Button vitalBtn = new Button("View Vitals");
        allButton.add(vitalBtn);
        vitalBtn.setMaxWidth(Double.MAX_VALUE);
        vitalBtn.setOnAction(e -> {
            UpdateBtn(allButton,vitalBtn);
            try {
                root.setCenter(vitalView);
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to view Vitals.");
            }
        });

        Button logoutBtn=new Button("Logout");
        allButton.add(logoutBtn);
        logoutBtn.setMaxWidth(Double.MAX_VALUE);
        logoutBtn.setOnAction(e -> {
            UpdateBtn(allButton,logoutBtn);
            try {
                ((Stage) logoutBtn.getScene().getWindow()).close();
                new LoginApp().start(new Stage());
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to Logout ! Try Again.");
            }
        });

        Button exitBtn=new Button("Exit");
        allButton.add(exitBtn);
        exitBtn.setMaxWidth(Double.MAX_VALUE);
        exitBtn.setOnAction(e -> {
            UpdateBtn(allButton,exitBtn);
            try {
                ((Stage) exitBtn.getScene().getWindow()).close();
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to close window.");
            }
        });

        // Style buttons
        for (Button btn : allButton) {
            btn.setStyle("-fx-background-color: #ffffff; -fx-text-fill: #336699;-fx-font-size: 14; -fx-font-weight: bold;");
        }
        box.getChildren().addAll(allButton);
        return box;

    }

    /**
     * Builds the doctor's profile view (read-only information).
     * @return a Pane containing the profile view.
     */
    private VBox buildProfileView(String currentDoctorId) {
        VBox view = new VBox(10);
        view.setStyle("-fx-font-size:16px;");
        view.setPadding(new Insets(20));

        Label subtitle=new Label("Doctor's Information");
        subtitle.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 22));


        ArrayList<Label> profileLabels= new ArrayList<> ();
        ArrayList<TextField> profileField=new ArrayList<>();
        ArrayList<HBox> line =new ArrayList<>();

        profileLabels.add(new Label("ID"));
        profileLabels.add(new Label("Name"));
        profileLabels.add(new Label("Birth Date"));
        profileLabels.add(new Label("Contact"));
        profileLabels.add(new Label("E-mail"));
        profileLabels.add(new Label("Experience"));
        profileLabels.add(new Label("Specialization"));
        profileLabels.add(new Label("Registration"));

        String readQuery = "SELECT * FROM doctor WHERE doctor_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(readQuery)) {
                stmt.setString(1,currentDoctorId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String id = rs.getString("doctor_id");
                    String name = rs.getString("name");
                    currentDoctorName=name;
                    java.sql.Date dob = rs.getDate("Dob");
                    String contact = rs.getString("contact");
                    String email = rs.getString("email");
                    String experience = rs.getString("experience");
                    String specialization = rs.getString("specialization");
                    java.sql.Date regDate = rs.getDate("registration_date");

                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

                    profileField.add(new TextField(id));
                    profileField.add(new TextField(name));
                    profileField.add(new TextField(formatter.format(dob)));
                    profileField.add(new TextField(contact));
                    profileField.add(new TextField(email));
                    profileField.add(new TextField(experience));
                    profileField.add(new TextField(specialization));
                    profileField.add(new TextField(formatter.format(regDate)));
                } else {
                    System.out.println("No doctor found with that email.");
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error occurred", e);
        }

            view.getChildren().addAll(subtitle);

            for (int i = 0; i < profileField.size(); i++) {
                if(i==0) profileField.get(i).setStyle("-fx-background-color : transparent; -fx-font-size:16;");
                profileField.get(i).setEditable(false);
                profileField.get(i).setPrefWidth(400);
                line.add(createLabeledTextField(profileLabels.get(i).getText(), profileField.get(i)));
                view.getChildren().add(line.get(i));

        }

        return view;
    }

    /**
     * Builds the update profile view, allowing the doctor to edit their profile.
     * @return a Pane containing the update profile form.
     */
    public VBox buildUpdateProfileView() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        content.setStyle("-fx-font-size: 16;  -fx-border-color: transparent;");
        content.setSpacing(10);
        content.setFillWidth(true);

        Label header = new Label("Update Profile");
        header.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 22));

        ArrayList<Label> fieldNames =new ArrayList<>();
        fieldNames.add(new Label("ID"));
        fieldNames.add(new Label("Name"));
        fieldNames.add(new Label("Birth Date"));
        fieldNames.add(new Label("Contact"));
        fieldNames.add(new Label("E-mail"));
        fieldNames.add(new Label("Experience"));
        fieldNames.add(new Label("Specialization"));
        fieldNames.add(new Label("Registration"));


        ArrayList<TextField> profileFields = new ArrayList<>();
        DatePicker dobPicker = null;

        // Read doctor info from DB
        String readQuery = "SELECT * FROM doctor WHERE doctor_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(readQuery)) {
            stmt.setString(1, currentDoctorId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    profileFields.add(new TextField(rs.getString("doctor_id"))); // ID
                    profileFields.add(new TextField(rs.getString("name")));      // Name
                    profileFields.add(new TextField(formatter.format(rs.getDate("Dob")))); // DOB
                    profileFields.add(new TextField(rs.getString("contact")));
                    profileFields.add(new TextField(rs.getString("email")));
                    profileFields.add(new TextField(rs.getString("experience")));
                    profileFields.add(new TextField(rs.getString("specialization")));
                    profileFields.add(new TextField(formatter.format(rs.getDate("registration_date"))));
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Database error occurred", e);
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
                HBox fieldBox = createLabeledTextField(fieldNames.get(i).getText(), tf);
                content.getChildren().add(fieldBox);
            }
        }

        Button saveBtn = new Button("Save Changes");
        content.getChildren().add(saveBtn);

        // Set Save Action
        DatePicker finalDobPicker = dobPicker;
        saveBtn.setOnAction(e -> {
            try {
                String name = profileFields.get(1).getText();
                String contact = profileFields.get(3).getText();
                String email = profileFields.get(4).getText();
                String experience = profileFields.get(5).getText();
                String specialization = profileFields.get(6).getText();
                String regDate = profileFields.get(7).getText();

                if (finalDobPicker == null || finalDobPicker.getValue() == null ||
                        name.isEmpty() || contact.isEmpty() || email.isEmpty() ||
                        experience.isEmpty() || specialization.isEmpty() || regDate.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Validation Error", "All fields must be filled out.");
                    return;
                }

                if (!isValidEmail(email)) {
                    showAlert(Alert.AlertType.ERROR, "Validation Error", "Invalid email format.");
                    return;
                }

                String updateQuery = "UPDATE doctor SET name = ?, Dob = ?, contact = ?, email = ?, " +
                        "experience = ?, specialization = ?, registration_date = ? WHERE doctor_id = ?";

                try (Connection conn = getConnection();
                     PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
                    stmt.setString(1, name);
                    stmt.setDate(2, java.sql.Date.valueOf(finalDobPicker.getValue()));
                    stmt.setString(3, contact);
                    stmt.setString(4, email);
                    stmt.setString(5, experience);
                    stmt.setString(6, specialization);
                    stmt.setDate(7, java.sql.Date.valueOf(regDate));
                    stmt.setString(8, profileFields.get(0).getText());

                    stmt.executeUpdate();
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Profile updated successfully.");
                    profileView = buildProfileView(currentDoctorId); // refresh
                    root.setCenter(profileView);
                }
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, "Database error occurred", ex);
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to update profile.");
            }

        });

  return content;
    }

    /**
     * Builds the patient list view using a TableView.
     * @return a Pane containing the patient list view.
     */
    private FlowPane getPatientList() {
        FlowPane patientListPane = new FlowPane();
        patientListPane.setHgap(10); // horizontal gap between cards
        patientListPane.setVgap(10); // vertical gap between rows
        patientListPane.setPadding(new Insets(0));


        ArrayList<PatientCard> cards =new ArrayList<>();
        String query = "SELECT * FROM patient";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String id = rs.getString("patient_id");
                String name = rs.getString("name");
                LocalDate dob = rs.getDate("Dob").toLocalDate();
                String contact = rs.getString("contact");
                String email = rs.getString("email");
                String bloodGroup = rs.getString("blood_group");
                String gender = rs.getString("gender");

                Patient patient = new Patient(name, email, contact, bloodGroup);
                patient.setDate_of_birth(dob);
                patient.setGender(gender);

                cards.add(new PatientCard(id,patient));
                patientListPane.getChildren().add(cards.get(cards.size()-1));

            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error occurred", e);
        }

        return patientListPane;
    }
    public FlowPane getAssignedPatientList(String doctorId) {
        FlowPane patientListPane = new FlowPane();
        patientListPane.setHgap(10);
        patientListPane.setVgap(10);
        patientListPane.setPadding(new Insets(0));


        String query = "SELECT DISTINCT p.* FROM patient p JOIN appointments a ON p.patient_id = a.patient_id WHERE a.status = 'Approved' AND a.doctor_id = ? ";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, doctorId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String id = rs.getString("patient_id");
                    String name = rs.getString("name");
                    LocalDate dob = rs.getDate("Dob").toLocalDate();
                    String contact = rs.getString("contact");
                    String email = rs.getString("email");
                    String bloodGroup = rs.getString("blood_group");
                    String gender = rs.getString("gender");

                    Patient patient = new Patient(name, email, contact, bloodGroup);
                    patient.setDate_of_birth(dob);
                    patient.setGender(gender);

                    PatientCard card = new PatientCard(id, patient);
                    patientListPane.getChildren().add(card);
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error occurred", e);
        }

        return patientListPane;
    }

    public BorderPane buildPatientListView()  {
        BorderPane view = new BorderPane();
        view.setPadding(new Insets(20));

        Label subheader = new Label("All Patients");
        subheader.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 22));
        subheader.setPadding(new Insets(10,0,10,0));

        Button allPatient = new Button("All Patients");
        allPatient.setFont(Font.font("Arial",FontWeight.BOLD,16));
        Button assignedPatient = new Button("Assigned Patients");
        assignedPatient.setFont(Font.font("Arial",FontWeight.BOLD,16));

        String baseStyle = "-fx-border-color: transparent; -fx-background-color: white; -fx-border-radius: 0px;";
        String selectedStyle = "-fx-background-color: lightgray; -fx-border-radius: 0px;";

        allPatient.setStyle(selectedStyle);
        assignedPatient.setStyle(baseStyle);

        allPatient.setMinSize(200, 24);
        assignedPatient.setMinSize(200, 24);

        HBox topBar = new HBox(allPatient, assignedPatient);
        topBar.setSpacing(10);

        FlowPane patList = getPatientList();
        ScrollPane scrollablePatientList = new ScrollPane(patList);
        scrollablePatientList.setFitToWidth(true);
        scrollablePatientList.setFitToHeight(true);
        scrollablePatientList.setPadding(new Insets(10));
        scrollablePatientList.setStyle("-fx-border-radius : 5px ;-fx-background-color: #f4f4f4; -fx-border-color:lightgrey;");
        scrollablePatientList.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollablePatientList.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        VBox centerContent = new VBox(subheader, scrollablePatientList);
        centerContent.setSpacing(10);


        allPatient.setOnAction(e -> {
            subheader.setText("All Patients");
            allPatient.setStyle(selectedStyle);
            assignedPatient.setStyle(baseStyle);

            FlowPane newList = getPatientList();
            ScrollPane newScroll = new ScrollPane(newList);
            newScroll.setFitToWidth(true);
            newScroll.setFitToHeight(true);
            newScroll.setPadding(new Insets(10));
            newScroll.setStyle("-fx-border-radius : 5px ;-fx-background-color: #f4f4f4 ; -fx-border-color:lightgrey;");
            newScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            newScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

            centerContent.getChildren().set(1, newScroll);
        });

        assignedPatient.setOnAction(e -> {
            subheader.setText("Assigned Patients");
            assignedPatient.setStyle(selectedStyle);
            allPatient.setStyle(baseStyle);

            FlowPane assignedList = getAssignedPatientList(currentDoctorId);
            ScrollPane assignedScroll = new ScrollPane(assignedList);
            assignedScroll.setFitToWidth(true);
            assignedScroll.setFitToHeight(true);
            assignedScroll.setPadding(new Insets(10));
            assignedScroll.setStyle("-fx-border-radius : 5px ;-fx-background-color: #f4f4f4; -fx-border-color:lightgrey;");
            assignedScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            assignedScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

            centerContent.getChildren().set(1, assignedScroll);
        });

        view.setTop(topBar);
        view.setCenter(centerContent);

        return view;
    }


    /**
     * Builds the feedback form view.
     * @return a Pane containing the feedback form.
     */

    private Pane buildFeedbackForm() {
        VBox view = new VBox(10);
        view.setStyle("-fx-font-size: 16 ;");
        view.setPadding(new Insets(20));

        Label header = new Label("Feedback Form");
        header.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 22));

        TextField patientIdField = new TextField();
        patientIdField.setPromptText("Enter Patient id here");
        TextArea feedbackArea = new TextArea();
        feedbackArea.setPromptText("Enter Feedback here ");
        feedbackArea.setPrefRowCount(8);

        HBox idBox = createLabeledTextField("Patient Id :", patientIdField);

        Label messageLabel = new Label("Feedback :");
        messageLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        VBox messageBox = new VBox(5, messageLabel, feedbackArea);

        Button sendBtn = new Button("Submit Feedback");
        sendBtn.setOnAction(e -> {
            try {
                String patientId = patientIdField.getText().trim();
                String message = feedbackArea.getText().trim();

                if (patientId.isEmpty() || message.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Validation Error", "patientId and message cannot be empty.");
                    return;
                }
                String insert="INSERT INTO feedback VALUE(?,?,?) ";

                try (Connection conn = getConnection();
                     PreparedStatement stmt = conn.prepareStatement(insert)) {

                    stmt.setString(1, currentDoctorId);
                    stmt.setString(2, patientId);
                    stmt.setString(3, message);

                    stmt.executeUpdate();
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Feedback Provided successfully.");

            } catch (Exception ex) {
                    LOGGER.log(Level.SEVERE, "Database error occurred", ex);
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to provide Feedback.");
            }

                // Clear fields after submission
                patientIdField.clear();
                feedbackArea.clear();
            } catch (Exception exception) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to submit feedback.");
            }
        });

        view.getChildren().addAll(header,idBox, messageBox, sendBtn);
        return view;
    }
    private Pane buildPrescriptionForm() {
        VBox view = new VBox(10);
        view.setStyle("-fx-font-size: 16;");
        view.setPadding(new Insets(20));

        Label header = new Label("Prescription Form");
        header.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 22));

        TextField patField = new TextField();
        patField.setPromptText("Enter Patient ID");
        HBox patientIdField = createLabeledTextField("Patient Id",patField);

        TextField medField = new TextField();
        medField.setPromptText("Enter Medicine Name");
        medField.setPrefWidth(300);
        HBox medicineField = createLabeledTextField("Medicine ",medField);

        TextField dosField = new TextField();
        dosField.setPromptText("Enter Dosage (e.g., 2 tablets)");
        dosField.setPrefWidth(300);
        HBox dosageField = createLabeledTextField("Dosage ",dosField);

        TextField schField = new TextField();
        schField.setPromptText("Enter Schedule (e.g., Morning and Evening)");
        schField.setPrefWidth(300);
        HBox scheduleField = createLabeledTextField("Schedule ",schField);

        Button sendBtn = new Button("Submit Prescription");
        sendBtn.setOnAction(e -> {
            String patientId = patField.getText().trim();
            String medicine = medField.getText().trim();
            String dosage = dosField.getText().trim();
            String schedule = schField.getText().trim();

            if (patientId.isEmpty() || medicine.isEmpty() || dosage.isEmpty() || schedule.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "All fields must be filled.");
                return;
            }

            Prescription prescription = new Prescription(patientId, currentDoctorId, dosage, medicine, schedule);

            String insert = "INSERT INTO prescriptions (prescription_id, patient_id, doctor_id, medicine_name, dosage, schedule, date_issued) VALUES (?, ?, ?, ?, ?, ?, ?)";

            try (Connection conn = getConnection();
                 PreparedStatement stmt = conn.prepareStatement(insert)) {

                stmt.setString(1, prescription.getPrescriptionID());
                stmt.setString(2, prescription.getPatientId());
                stmt.setString(3, prescription.getDoctorId());
                stmt.setString(4, prescription.getMedicineName());
                stmt.setString(5, prescription.getDosage());
                stmt.setString(6, prescription.getSchedule());
                stmt.setDate(7, java.sql.Date.valueOf(prescription.getDateIssued()));

                stmt.executeUpdate();
                showAlert(Alert.AlertType.INFORMATION, "Success", "Prescription submitted successfully.");

                // Clear fields
                patField.clear();
                medField.clear();
                dosField.clear();
                schField.clear();

            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, "Database error", ex);
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to submit prescription.");
            }
        });

        view.getChildren().addAll(header, patientIdField, medicineField, dosageField, scheduleField, sendBtn);
        return view;
    }

    private Pane buildInteractionPane() {

        // -----------------  Toggle Section -----------------
        HBox toggleButtons = new HBox(10);
        Button feedbackToggle = new Button("Feedback");
        feedbackToggle.setFont(Font.font("Arial",FontWeight.BOLD,16));
        Button prescriptionToggle = new Button("Prescription");
        prescriptionToggle.setFont(Font.font("Arial",FontWeight.BOLD,16));

        String defaultStyle = "-fx-border-color: transparent; -fx-background-color: white; -fx-border-radius: 0px;";
        String  selectedStyle = "-fx-background-color: lightgray; -fx-border-radius: 0px;";

        feedbackToggle.setStyle(selectedStyle); // default selected
        prescriptionToggle.setStyle(defaultStyle);

        feedbackToggle.setMinSize(200, 24);
        prescriptionToggle.setMinSize(200, 24);

        toggleButtons.getChildren().addAll(feedbackToggle, prescriptionToggle);
        toggleButtons.setAlignment(Pos.TOP_LEFT);

        StackPane contentPane = new StackPane();
        Pane feedbackForm = buildFeedbackForm();
        contentPane.getChildren().add(feedbackForm); // Default view

        // Toggle logic
        feedbackToggle.setOnAction(e -> {
            feedbackToggle.setStyle(selectedStyle);
            prescriptionToggle.setStyle(defaultStyle);
            contentPane.getChildren().setAll(buildFeedbackForm());
        });

        prescriptionToggle.setOnAction(e -> {
            prescriptionToggle.setStyle(selectedStyle);
            feedbackToggle.setStyle(defaultStyle);
            contentPane.getChildren().setAll(buildPrescriptionForm());
        });

        VBox mainLayout = new VBox(10, toggleButtons, contentPane);
        mainLayout.setPadding(new Insets(15));
        return mainLayout;
    }


    private Pane buildEmailView(){
        VBox view = new VBox(10);
        view.setStyle("-fx-font-size: 16 ;");
        view.setPadding(new Insets(20));

        TextField receiverField = new TextField();
        receiverField.setPromptText("Enter receiver's mail");
        receiverField.setPrefWidth(250);
        TextField subjectField = new TextField();
        subjectField.setPromptText("Enter subject ");
        subjectField.setPrefWidth(250);

        TextArea mainArea = new TextArea();
        mainArea.setPromptText("Enter Message here");
        mainArea.setPrefRowCount(8);


        HBox receiverBox = createLabeledTextField("To :", receiverField);
        HBox subjectBox = createLabeledTextField("Subject :", subjectField);


        Label messageLabel = new Label("Message :");
        messageLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        VBox messageBox = new VBox(5, messageLabel, mainArea);

        Button sendBtn = new Button("Send Email");
        sendBtn.setOnAction(e -> {
            try {
                String receiver = receiverField.getText().trim();
                String subject = subjectField.getText().trim();
                String message = mainArea.getText().trim();

                StringBuilder error = new StringBuilder();

                if (receiver.isEmpty()|| message.isEmpty() || subject.isEmpty()) {
                    if(receiver.isEmpty() ) error.append("Receiver cannot be empty \n");
                    if(message.isEmpty() ) error.append("Message cannot be empty \n");
                    if(subject.isEmpty() ) error.append("Subject cannot be empty ");

                    showAlert(Alert.AlertType.ERROR, "Validation Error", error.toString());
                    return;
                }
                if(!isValidEmail(receiver)){
                    showAlert(Alert.AlertType.ERROR, "Validation Error", "Invalid Email Format.");
                    return;
                }
                EmailNotification email=new EmailNotification();
                email.sendMail(message,receiver,subject);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Email Sent Successfully . Thank you!");

                // Clear fields after submission
                receiverField.clear();
                subjectField.clear();
                mainArea.clear();

            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to send Email.");
            }
        });

        view.getChildren().addAll(receiverBox,subjectBox, messageBox, sendBtn);
        return view;
    }


    /**
     * Helper method to create an HBox containing a label and a text field.
     * @param labelText the text for the label.
     * @param textField the TextField control.
     * @return an HBox containing the label and text field.
     */
    public static HBox createLabeledTextField(String labelText, TextField textField) {
        Label label = new Label(labelText);
        label.setMinWidth(110);
        label.setStyle("-fx-font-weight: bold;");
        HBox box = new HBox(15, label, textField);
        box.setAlignment(Pos.CENTER_LEFT);
        return box;
    }

    /**
     * Utility method to show an alert dialog.
     * @param type the type of alert.
     * @param title the title of the dialog.
     * @param message the message content.
     */
    public static  void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Validates the email format using a simple regex.
     * @param email the email string to validate.
     * @return true if the email is in a valid format, false otherwise.
     */
    public static boolean isValidEmail(String email) {
        // Basic regex for email validation
        String emailRegex = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$";
        return Pattern.matches(emailRegex, email);
    }

}



