package com.example.system;

import AppointmentScheduling.Appointment;
import NotificationAndRemainder.EmailNotification;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.collections.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import kotlin.OverloadResolutionByLambdaReturnType;
import management.*;
//import AppointmentScheduling.*;

import static Database.DataBaseConnection.getConnection;

/**
 * DoctorDashboard Application
 *
 * A JavaFX application for a doctor's dashboard,
 * including sidebar navigation, patient list, profile view, feedback form, and profile update form.
 *
 * This single-file implementation uses modular methods for each view and helper methods for UI components.
 * It also includes basic validation and alert dialogs for success/error notifications.
 */
public class DoctorDashboard extends Application {
    private static final Logger LOGGER = Logger.getLogger(DoctorDashboard.class.getName());

    // In-memory dummy data for Doctor and Patients
    private String currentDoctorId;
    private Doctor doc;
    private List<Patient> patients;

    // Main layout pane
    private BorderPane root;

    // Views
    private VBox sidebar;
    private VBox profileView;
    private Pane patientListView;
    private VBox updateProfileView;
    private Pane feedbackFormView;
    private Pane AppointmentView;
    private Pane EmailView;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Initialize dummy doctor and patients
        doc=new Doctor("John","Cardiologist","15 months","B-52475");
        doc.setDate_of_birth(LocalDate.parse("1999-10-25"));
        doc.setEmail("john1234@gmail.com");
        doc.setPhoneNumber("331-750-85");
//Patient saim=

//        patients = new ArrayList<>();
//        Patient saim=new Patient("Saim Abbasi","saim12abc@gmail.com","12-3456","A+");
//        saim.setGender("male");
//        saim.setDate_of_birth(LocalDate.parse("2024-10-23"));
//        patients.add(saim);
//        Patient ayesha=new Patient("Ayesha", "Smith@gmail.com", "30", "B-");
//        ayesha.setGender("female");
//        ayesha.setDate_of_birth(LocalDate.parse("2012-11-13"));
//        patients.add(ayesha);
//
//        //        patients.add(new Patient("Bobby", "Johnson@gmail.com", "45", "O+"));
////        patients.add(new Patient("Ahmed", "Williams@gmail.com", "29", "AB+"));
//
//for(Patient p:patients) {
//    insertPatient(p);
//}

        sidebar = buildSidebar();
        // Main layout
        root = new BorderPane();


        profileView = buildProfileView();
        profileView.setPadding(new Insets(0,20,0,20));
        patientListView = buildPatientListView();
        updateProfileView = buildUpdateProfileView();
        feedbackFormView = buildFeedbackForm();
        AppointmentView=buildAppointmentView();
        EmailView=buildEmailView();
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
        Scene scene = new Scene(root, 900, 600); // starting size
        primaryStage.setTitle("Doctor Dashboard");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(700);
        primaryStage.setMinHeight(500);
        primaryStage.show();
    }
    public void insertPatient(Patient patient) {
        String sql = "INSERT INTO patient (patient_id, name, Dob, contact, email, blood_group, gender, registration_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

//            DateTimeFormatter formatter= DateTimeFormatter.ofPattern("yyyy-MM-dd");

            LocalDate dob = patient.getDate_of_birth();
            LocalDate regDate = patient.getRegistrationDate();

            if (dob == null) {
                System.err.println("DOB is null!");
            }
            if (regDate == null) {
                System.err.println("DOB is null!");
            }
            java.sql.Date sqlDob = (dob != null) ? java.sql.Date.valueOf(dob) : null;
            java.sql.Date sqlRegDate = (regDate != null) ? java.sql.Date.valueOf(regDate) : null;



            stmt.setString(1, patient.getUserId());
            stmt.setString(2, patient.getName());
            stmt.setDate(3, sqlDob);
            stmt.setString(4, patient.getPhoneNumber());
            stmt.setString(5, patient.getEmail());
            stmt.setString(6, patient.getBloodType());
            stmt.setString(7, patient.getGender());
            stmt.setDate(8, sqlRegDate);

            stmt.executeUpdate();
            System.out.println("Patient added successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private Pane buildAppointmentView() {

            VBox mainView = new VBox(20);
            mainView.setPadding(new Insets(20));
            mainView.setStyle("-fx-font-size: 14px;");

            Label header = new Label("Appointment Manager");
            header.setFont(Font.font("Arial", FontWeight.BOLD, 22));

            // ----------------- Availability Section -----------------
            Label availHeader = new Label("Set Availability");
            availHeader.setFont(Font.font("Arial", FontWeight.BOLD, 16));

            ComboBox<String> dayOfWeekBox = new ComboBox<>();
            dayOfWeekBox.getItems().addAll("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday");

            ComboBox<LocalTime> startTimeBox = new ComboBox<>();
            ComboBox<LocalTime> endTimeBox = new ComboBox<>();
            for (int hour = 8; hour <= 18; hour++) {
                startTimeBox.getItems().add(LocalTime.of(hour, 0));
                endTimeBox.getItems().add(LocalTime.of(hour, 0));
            }

            Button addAvailabilityBtn = new Button("Add Availability");
            addAvailabilityBtn.setOnAction(e -> {
                String day = dayOfWeekBox.getValue();
                LocalTime start = startTimeBox.getValue();
                LocalTime end = endTimeBox.getValue();

                if (day == null || start == null || end == null || !start.isBefore(end)) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please select valid day and time range.");
                    return;
                }

                // TODO: Store in database - doctor_availability

                System.out.println("Availability added: " + day + " " + start + " to " + end);
            });

            HBox availInput = new HBox(10, new Label("Day:"), dayOfWeekBox,
                    new Label("From:"), startTimeBox, new Label("To:"), endTimeBox, addAvailabilityBtn);
            availInput.setAlignment(Pos.CENTER_LEFT);

            // ----------------- Appointment Request Section -----------------
            Label requestHeader = new Label("Appointment Requests");
            requestHeader.setFont(Font.font("Arial", FontWeight.BOLD, 16));

            TableView<Appointment> table = new TableView<>();
            table.setPrefHeight(300);

            TableColumn<Appointment, String> patientCol = new TableColumn<>("Patient");
            patientCol.setCellValueFactory(new PropertyValueFactory<>("patient"));
            patientCol.setPrefWidth(130);

            TableColumn<Appointment, String> dateCol = new TableColumn<>("Date");
            dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
            dateCol.setPrefWidth(100);

            TableColumn<Appointment, String> timeCol = new TableColumn<>("Time");
            timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
            timeCol.setPrefWidth(80);

            TableColumn<Appointment, String> statusCol = new TableColumn<>("Status");
            statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
            statusCol.setPrefWidth(90);

            TableColumn<Appointment, String> reasonCol = new TableColumn<>("Reason");
            reasonCol.setCellValueFactory(new PropertyValueFactory<>("reason"));
            reasonCol.setPrefWidth(100);

            TableColumn<Appointment, Void> actionCol = new TableColumn<>("Actions");
            actionCol.setCellFactory(param -> new TableCell<>() {
                final Button approveBtn = new Button("Approve");
                final Button rejectBtn = new Button("Reject ");
                final HBox hbox = new HBox(8, approveBtn, rejectBtn);

                {
                    approveBtn.setOnAction(event -> {
                        Appointment request = getTableView().getItems().get(getIndex());
                        request.setStatus("Approved");
                        table.refresh();
                        // TODO: Update status in DB
                    });
                    rejectBtn.setOnAction(event -> {
                        Appointment request = getTableView().getItems().get(getIndex());
                        request.setStatus("Rejected");
                        table.refresh();
                        // TODO: Update status in DB
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) setGraphic(null);
                    else setGraphic(hbox);
                }

            });
            actionCol.setPrefWidth(180);

            table.getColumns().addAll(patientCol, dateCol, timeCol, statusCol, reasonCol, actionCol);

            // TODO: Load appointments from database
            ObservableList<Appointment> requests = FXCollections.observableArrayList();
//                    (
//                    new Appointment("pending", currentDoctorId, "Taha", "2025-05-02","10:00","headache from last 5 days "),
//                    new Appointment("pending",  currentDoctorId, "Zaid", "2025-05-02","11:00","pain in left shoulder")
//            );
                String selectQuery = "SELECT * FROM appointments WHERE doctor_id = ? AND status = ? ";
                String subQuery = "SELECT name FROM patient WHERE patient_id = ? ";
            try (Connection conn = getConnection();
                 PreparedStatement statement = conn.prepareStatement(selectQuery);){

                statement.setString(1, currentDoctorId);
                statement.setString(2,"pending");

                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    String status = resultSet.getString("status");
                    String doctorId = resultSet.getString("doctor_id");
                    String patientId = resultSet.getString("patient_id");
                            PreparedStatement subStatement = conn.prepareStatement(subQuery);
                            subStatement.setString(1,patientId);
                            ResultSet rs=subStatement.executeQuery();
                    rs.next();
                        String patientName = rs.getString("name");

                    String date = resultSet.getString("date");
                    String time = resultSet.getString("time");
                    String reason = resultSet.getString("reason");

                    Appointment appointment = new Appointment(status, doctorId, patientName, date, time, reason);
                    requests.add(appointment);
                }
            }
            catch(Exception e){

                e.printStackTrace();
            }
         table.setItems(requests);

            // Combine everything into the main layout
            mainView.getChildren().addAll(header, availHeader, availInput, requestHeader, table);
            return mainView;

    }

    /**
     * Builds the sidebar navigation with buttons for each section.
     * returns a VBox containing the sidebar components.
     */
    void UpdateBtn(Button btn){
        String style=btn.getStyle();
        if (style.contains("-fx-background-color: green")) {
            btn.setStyle("-fx-background-color: #ffffff; -fx-text-fill: #336699;-fx-font-size: 14; -fx-font-weight: bold;");// reset style
        } else {
            btn.setStyle("-fx-background-color: green; -fx-border-color: black;-fx-border-width: 3px;" +
                    "-fx-font-size: 14; -fx-text-fill: black; -fx-font-weight: bold;    ");
        }
    }
    private VBox buildSidebar() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(15));
        box.setPrefWidth(200);
        box.setStyle("-fx-background-color: #336699;");


        Button profileBtn = new Button("Profile");
        profileBtn.setMaxWidth(Double.MAX_VALUE);
        profileBtn.setOnAction(e -> {
            UpdateBtn(profileBtn);
            try {
                root.setCenter(profileView);
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to show profile view.");
            }
        });

        Button patientsBtn = new Button("Patients");
        patientsBtn.setMaxWidth(Double.MAX_VALUE);
        patientsBtn.setOnAction(e -> {
            UpdateBtn(patientsBtn);
            try {
                root.setCenter(patientListView);
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to show patient list.");
            }
        });

        Button updateBtn = new Button("Update Profile");
        updateBtn.setMaxWidth(Double.MAX_VALUE);
        updateBtn.setOnAction(e -> {
            UpdateBtn(updateBtn);
            try {
                root.setCenter(updateProfileView);
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to show update profile view.");
            }
        });

        Button feedbackBtn = new Button("Feedback");
        feedbackBtn.setMaxWidth(Double.MAX_VALUE);
        feedbackBtn.setOnAction(e -> {
            UpdateBtn(feedbackBtn);
            try {
                root.setCenter(feedbackFormView);
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to show feedback form.");
            }
        });
        Button AppointmentBtn=new Button("Appointment");
        AppointmentBtn.setMaxWidth(Double.MAX_VALUE);
        AppointmentBtn.setOnAction(e -> {
            UpdateBtn(AppointmentBtn);
            try {
                root.setCenter(AppointmentView);
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to show Appointment view.");
            }
        });

        Button EmailBtn=new Button("Send Eamil");
        EmailBtn.setMaxWidth(Double.MAX_VALUE);
        EmailBtn.setOnAction(e -> {
            UpdateBtn(EmailBtn);
            try {
                root.setCenter(EmailView);
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to show Email view.");
            }
        });


        Button logoutBtn=new Button("Logout");
        logoutBtn.setMaxWidth(Double.MAX_VALUE);
        logoutBtn.setOnAction(e -> {
            UpdateBtn(logoutBtn);

        });

        // Style buttons
        for (Button btn : new Button[]{profileBtn, patientsBtn, updateBtn, feedbackBtn,logoutBtn,AppointmentBtn,EmailBtn}) {
            btn.setStyle("-fx-background-color: #ffffff; -fx-text-fill: #336699;-fx-font-size: 14; -fx-font-weight: bold;");

        }
        box.getChildren().addAll( profileBtn, updateBtn, patientsBtn, feedbackBtn,AppointmentBtn,EmailBtn,logoutBtn);
        return box;

    }

    /**
     * Builds the doctor's profile view (read-only information).
     * @return a Pane containing the profile view.
     */
    private VBox buildProfileView() {
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

        String readQuery = "SELECT * FROM doctor WHERE email=?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(readQuery)) {
                stmt.setString(1,"zk417704@gmail.com");
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String id = rs.getString("doctor_id");
                    currentDoctorId=id;
                    String name = rs.getString("name");
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
    private VBox buildUpdateProfileView() {
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
        String readQuery = "SELECT * FROM doctor WHERE email=?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(readQuery)) {
            stmt.setString(1, "zk417704@gmail.com");

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
                    profileView = buildProfileView(); // refresh
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
               // LocalDate regDate = rs.getDate("registration_date").toLocalDate();

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
    private Pane buildPatientListView() {
        VBox view = new VBox(10);
        view.setPadding(new Insets(20));

        Label header = new Label("Patient List");
        header.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 22));

        view.getChildren().addAll(header, getPatientList());
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
    private void showAlert(Alert.AlertType type, String title, String message) {
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
    private boolean isValidEmail(String email) {
        // Basic regex for email validation
        String emailRegex = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$";
        return Pattern.matches(emailRegex, email);
    }

    public static class AppointmentRequest {
        private final SimpleStringProperty patientName;
        private final SimpleStringProperty date;
        private final SimpleStringProperty time;
        private final SimpleStringProperty status;

        public AppointmentRequest(String patientName, String date, String time, String status) {
            this.patientName = new SimpleStringProperty(patientName);
            this.date = new SimpleStringProperty(date);
            this.time = new SimpleStringProperty(time);
            this.status = new SimpleStringProperty(status);
        }

        public String getPatientName() { return patientName.get(); }
        public String getDate() { return date.get(); }
        public String getTime() { return time.get(); }
        public String getStatus() { return status.get(); }

        public void setStatus(String newStatus) { status.set(newStatus); }
    }
    public static class Appointment{

        private final SimpleStringProperty appointmentId;
        private final SimpleStringProperty status;
        private final SimpleStringProperty doctor;
        private final SimpleStringProperty patient;
        private final SimpleStringProperty date;
        private final SimpleStringProperty time;
        private final SimpleStringProperty reason;


//        public static ArrayList<AppointmentScheduling.Appointment> appointmentList = new ArrayList<>();
//        public static ArrayList<String> appointmentIdList = new ArrayList<>();

        // Default constructor (if needed)
        public Appointment() {
            this("Pending", "", "", "", "","");
        }

        public Appointment(String status, String doctor, String patient, String date, String time,String reason) {
            this.appointmentId = new SimpleStringProperty("A-"+User.generate_id());
            this.status = new SimpleStringProperty(status);
            this.doctor = new SimpleStringProperty(doctor);
            this.patient = new SimpleStringProperty(patient);
            this.date = new SimpleStringProperty(date);
            this.time = new SimpleStringProperty(time);
            this.reason=new SimpleStringProperty(reason);
        }

        // === Getters for TableView ===

        public String getAppointmentId() {
            return appointmentId.get();
        }
        public String getStatus() {
            return status.get();
        }
        public String getDoctor() {
            return doctor.get();
        }
        public String getPatient() {
            return patient.get();
        }
        public String getDate() {
            return date.get();
        }
        public String getTime() {
            return time.get();
        }
        public String getReason() {return reason.get();}

        // === Setters ===

        public void setStatus(String status) {
            this.status.set(status);
        }

        // === Property getters (optional, useful for binding) ===
//
//        public StringProperty appointmentIdProperty() { return appointmentId; }
//        public StringProperty statusProperty() { return status; }
//        public StringProperty doctorProperty() { return doctor; }
//        public StringProperty patientProperty() { return patient; }
//        public StringProperty dateProperty() { return date; }
//        public StringProperty timeProperty() { return time; }

        @Override
        public String toString() {
            return "\n\t------ Appointment Detail ------" +
                    "\nAppointment ID      : " + getAppointmentId() +
                    "\t\tDate  : " + getDate() +
                    "\nDoctor              : " + (getDoctor().isEmpty() ? "Not Assigned" : "Dr. " + getDoctor()) +
                    "\nPatient             : " + (getPatient().isEmpty() ? "Not Assigned" : getPatient()) +
                    "\nStatus              : " + getStatus()+
                    "\nReason             : " + getReason();

        }
    }

}



