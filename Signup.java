package com.example.system;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import management.*;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;


import static Database.DataBaseConnection.getConnection;


public class Signup extends Application {

    private TextField nameField,idField, emailField, contactField, experienceField, specializationField, genderField, bloodGroupField;
    private DatePicker dobPicker;
    private ComboBox<String> roleBox;
    private PasswordField passwordField;
    private Label statusLabel;
    private List<Node> signupFields=new ArrayList<>();
    private User currentUser;
    @Override
    public void start(Stage primaryStage) {
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #e3f2fd, #90caf9);");

        VBox form = new VBox(10);
        form.setPadding(new Insets(100,0,0,0));
        form.setAlignment(Pos.TOP_CENTER);
        form.setStyle("-fx-background-color: white; -fx-background-radius: 15; " +
                "-fx-effect: dropshadow(two-pass-box, rgba(0,0,0,0.2), 15, 0, 5, 5);");

        Label title = new Label("Sign Up");
        title.setFont(Font.font("Arial",FontWeight.EXTRA_BOLD, 32));
        title.setPadding(new Insets(0,0,25,0));
        title.setTextFill(Color.web("#1565c0"));

        idField = new TextField();
        idField.setEditable(false);
        nameField = new TextField();
        emailField = new TextField();
        contactField = new TextField();
        dobPicker = new DatePicker();
        experienceField = new TextField();
        specializationField = new TextField();
        genderField = new TextField();
        bloodGroupField = new TextField();

        dobPicker.setPromptText("Date of Birth");
        dobPicker.setMaxWidth(300);

        passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(300);

        roleBox = new ComboBox<>();
        roleBox.getItems().addAll("Doctor", "Patient");
        roleBox.setPromptText("Select Role");
        roleBox.setMaxWidth(300);
        roleBox.setStyle("-fx-background-radius: 8; -fx-font-size: 14; -fx-border-color: #64b5f6; " +
                "-fx-border-radius: 8; -fx-background-color: #e3f2fd;");


        HBox idBox = createLabeledField("ID ",idField);
        HBox nameBox = createLabeledField("Full Name", nameField);
        HBox emailBox = createLabeledField("Email", emailField);
        HBox contactBox = createLabeledField("Contact ", contactField);
        HBox dobBox = createLabeledField("Date of Birth", dobPicker);
        HBox passBox = createLabeledField("Password", passwordField);
        HBox roleBoxContainer = createLabeledField("Role", roleBox);
        HBox expBox = createLabeledField("Experience ", experienceField);
        HBox specBox = createLabeledField("Specialization", specializationField);
        HBox genderBox = createLabeledField("Gender ", genderField);
        HBox bloodBox = createLabeledField("Blood Group ", bloodGroupField);

        signupFields.add(roleBoxContainer);
        signupFields.add(idBox);
        signupFields.add(nameBox);
        signupFields.add(emailBox);
        signupFields.add(passBox);
        signupFields.add(contactBox);
        signupFields.add(dobBox);


        Button signupBtn = new Button("Sign Up");
        signupBtn.setStyle("-fx-background-color: #1976d2; -fx-text-fill: white; " +
                "-fx-font-size: 18px; -fx-background-radius: 10; -fx-cursor: hand;");

        signupBtn.setPrefWidth(200);
        signupBtn.setAlignment(Pos.BASELINE_CENTER);
        signupBtn.setOnAction(e -> signup());

        Button backToLoginBtn = new Button("Back to Login");
        backToLoginBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #1976d2; " +
                "-fx-font-size: 13px; -fx-underline: true;");
        backToLoginBtn.setOnAction(e -> {
            // Replace this with actual login navigation logic
            System.out.println("Returning to Login...");
        });

        // Handle role-based field visibility
        roleBox.setOnAction(e -> {
            String role = roleBox.getValue();
            boolean isDoctor = "Doctor".equals(role);
            boolean isPatient = "Patient".equals(role);
            if(isPatient  && !signupFields.contains(genderBox) && !signupFields.contains(bloodBox)) {
                    if(form.getChildren().contains(expBox) && form.getChildren().contains(specBox)){
                        form.getChildren().remove(8);
                        form.getChildren().remove(8);
                    }
                    form.getChildren().add(8,genderBox);
                    form.getChildren().add(9,bloodBox);

                    currentUser = new Patient();
                    idField.setText(currentUser.getUserId());
            }
            else if(isDoctor && !signupFields.contains(expBox) && !signupFields.contains(specBox)) {
                    if(form.getChildren().contains(genderBox) && form.getChildren().contains(bloodBox)){
                        form.getChildren().remove(8);
                        form.getChildren().remove(8);
                    }
                    form.getChildren().add(8,specBox);
                    form.getChildren().add(9,expBox);

                    currentUser = new Doctor();
                    idField.setText(currentUser.getUserId());
            }

        });

        statusLabel = new Label();
        statusLabel.setFont(Font.font("Arial", 15));
        statusLabel.setTextFill(Color.RED);

        form.getChildren().add(title);
        form.getChildren().addAll(signupFields);
        form.getChildren().addAll(signupBtn,backToLoginBtn,statusLabel);


        root.getChildren().add(form);
        Scene scene = new Scene(root, 600, 700);
        primaryStage.setTitle("Signup Page");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private HBox createLabeledField(String labelText, Control inputField) {
        Label label = new Label(labelText);
        label.setMinWidth(110);
        label.setTextFill(Color.web("#1e88e5"));
        label.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 15));
        label.setPadding(new Insets(10,0,10,0));
        inputField.setPrefWidth(300);
        inputField.setStyle("-fx-background-color: #f5faff; -fx-background-radius: 8; " +
                "-fx-border-radius: 8; -fx-border-color: #90caf9; -fx-font-size: 16;");
        HBox hbox = new HBox(90, label, inputField);
        hbox.setAlignment(Pos.TOP_CENTER);
        return hbox;
    }


    private void signup() {
        String id = idField.getText();
        String name = nameField.getText();
        String email = emailField.getText();
        String contact = contactField.getText();
        String password = passwordField.getText();
        String dob = (dobPicker.getValue()).toString();
        String role = roleBox.getValue();
        String exp = experienceField.getText();
        String spec= specializationField.getText();
        String gender=genderField.getText();
        String blood=bloodGroupField.getText();

        boolean valid = !name.isEmpty() && !email.isEmpty() && !contact.isEmpty() && !dob.isEmpty()
                && !password.isEmpty() && role != null && DoctorDashboard.isValidEmail(email);
        if ("Doctor".equals(role)) {
            valid &= !exp.isEmpty() && !spec.isEmpty();
        } else if ("Patient".equals(role)) {
            valid &= !gender.isEmpty() && !blood.isEmpty();
        }

            if (!valid) {
                DoctorDashboard.showAlert(Alert.AlertType.ERROR, "Validation Error", "Please fill all required fields correctly ");
                return;
            }
            else {
                DoctorDashboard.showAlert(Alert.AlertType.CONFIRMATION, "Confirmation", "Sign-up successful as " + role);
            }


        String insert="INSERT INTO "+role.toLowerCase()+" VALUES (?,?,?,?,?,?,?,?) ";
        String auth="INSERT INTO authorization VALUES (?,?,?)";
            try (Connection conn = getConnection();
                 PreparedStatement stmt = conn.prepareStatement(insert);
                PreparedStatement stmt2 = conn.prepareStatement(auth) ) {
                stmt.setString(1, id);
                stmt.setString(2, name);
                stmt.setDate(3, java.sql.Date.valueOf(dob));
                stmt.setString(4, contact);
                stmt.setString(5, email);
                if ("Doctor".equals(role)) {
                    stmt.setString(6, exp);
                    stmt.setString(7, spec);
                } else {
                    stmt.setString(6, blood);
                    stmt.setString(7, gender);
                }
                stmt.setDate(8, java.sql.Date.valueOf(currentUser.getRegistrationDate()));

                stmt.executeUpdate();
                DoctorDashboard.showAlert(Alert.AlertType.INFORMATION, "Success", "Added to Database successfully.");
                try {
                    stmt2.setString(1, id);
                    stmt2.setString(2, email);
                    stmt2.setString(3, password);
                    stmt2.executeUpdate();
                    DoctorDashboard.showAlert(Alert.AlertType.INFORMATION, "Success", "Added to Authorization successfully.");
                }catch(Exception exc){
                    exc.printStackTrace();
                    DoctorDashboard.showAlert(Alert.AlertType.ERROR, "Error", "Failed to insert Data into Database.");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                DoctorDashboard.showAlert(Alert.AlertType.ERROR, "Error", "Failed to insert Data into Database.");
            }

    }

    public static void main(String[] args) {
        launch(args);
    }
}
