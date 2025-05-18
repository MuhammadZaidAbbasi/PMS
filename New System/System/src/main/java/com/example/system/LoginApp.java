package com.example.system;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static Database.DataBaseConnection.getConnection;

public class LoginApp extends Application {

    private TextField emailField;
    private PasswordField passwordField;
    private Label statusLabel;

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #e3f2fd, #90caf9);");

        Label title = new Label("Welcome to RPMS");
        title.setFont(Font.font("Arial", 24));
        title.setTextFill(Color.web("#0d47a1"));

        emailField = new TextField();
        emailField.setPromptText("Enter Your Email");
        styleInput(emailField);

        passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        styleInput(passwordField);


        Button loginButton = new Button("Login");
        loginButton.setStyle(
                "-fx-background-color: #1976d2; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 16px; " +
                        "-fx-background-radius: 8;"
        );
        loginButton.setPrefWidth(250);
        loginButton.setOnAction(e -> login());

        Hyperlink signupLink = new Hyperlink("Don't have an account? Sign up");
        signupLink.setStyle("-fx-text-fill: #0d47a1; -fx-font-size: 16px; ");
        signupLink.setOnAction(e-> {
            try {
                ((Stage) emailField.getScene().getWindow()).close();
                new Signup().start(new Stage());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        statusLabel = new Label();
        statusLabel.setFont(Font.font("Arial", 13));
        statusLabel.setTextFill(Color.RED);

        root.getChildren().addAll(title, emailField, passwordField,  loginButton, signupLink, statusLabel);

        Scene scene = new Scene(root, 450, 500);
        primaryStage.setTitle("Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void styleInput(TextField field) {
        field.setStyle(
                "-fx-background-radius: 8; " +
                        "-fx-border-radius: 8; " +
                        "-fx-border-color: #b0bec5; " +
                        "-fx-font-size: 16;"
        );
        field.setMaxWidth(250);
        field.setPrefHeight(40);
    }

    private void login() {
        String authorizedUserId;
        String email = emailField.getText();
        String password = passwordField.getText();

        String get="SELECT user_id FROM authorization  WHERE email = ? AND password = ? ";
        try(Connection conn = getConnection();
            PreparedStatement stmt= conn.prepareStatement(get)){
            stmt.setString(1,email);
            stmt.setString(2,password);

            ResultSet rs = stmt.executeQuery();

            if(!rs.next()){
                DoctorDashboard.showAlert(Alert.AlertType.ERROR,"Authorization Declined","Login failed. Please check your credentials.");
                SystemLog.addLog(new SystemLog("Unsuccessful Login Attempt for "+email));
                emailField.clear();
                passwordField.clear();
                return;
            }
            authorizedUserId=rs.getString("user_id");
            SystemLog.addLog(new SystemLog("Successful Login Attempt for "+email));
            if(authorizedUserId.startsWith("P")){
                PatientDashboardApp.currentPatientId=authorizedUserId;

                ((Stage) emailField.getScene().getWindow()).close();//close current window/Stage
                new PatientDashboardApp().start(new Stage());//opens patient's dashboard
            }
            else if (authorizedUserId.startsWith("D")){
                DoctorDashboard.currentDoctorId=authorizedUserId;

                ((Stage) emailField.getScene().getWindow()).close();//close current window/Stage
                new DoctorDashboard().start(new Stage());//opens doctor's dashboard
            }
            else {
                ((Stage) emailField.getScene().getWindow()).close();//close current window/Stage
                new AdminDashboard().start(new Stage());//opens admin's dashboard
            }

        }catch(SQLException e){
            DoctorDashboard.showAlert(Alert.AlertType.ERROR,"Connection error","Unable to connect to Database");
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}
