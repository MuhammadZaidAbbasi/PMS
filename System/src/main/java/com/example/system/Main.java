package com.example.system;

//import javafx.application.*;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Scene;
//import javafx.scene.control.*;
//import javafx.stage.*;
//
//
//import java.io.IOException;
//
//public class HelloApplication extends Application {
//    @Override
//    public void start(Stage stage) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
//        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
//        stage.setTitle("Java FX first Project");
//        TextField tf= new TextField();
//        stage.setScene(scene);
//        stage.show();
//    }
//
//    public static void main(String[] args) {
//        launch();
//    }
//}
import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import management.*;


import java.util.ArrayList;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        ArrayList<Patient> patient=new ArrayList<>();
        ArrayList<PatientCard> cards= new ArrayList<>();
        TextField nameInput = new TextField();
        nameInput.setPromptText("Enter patient name");

        TextField mailInput = new TextField();
        TextField phoneInput = new TextField();
        TextField bloodInput = new TextField();

        mailInput.setPromptText("Enter Email");
        phoneInput.setPromptText("Enter Contact Number");
        bloodInput.setPromptText("Enter Blood Group");


        Button addButton = new Button("Add Patient");
        Button viewButton = new Button("View All Patient");
        Button clearButton = new Button("Clear ");

        VBox patientList = new VBox(10);

        VBox layout = new VBox(10, nameInput, mailInput,phoneInput,bloodInput,
                new HBox(addButton,new Separator(Orientation.VERTICAL),viewButton,new Separator(Orientation.VERTICAL),clearButton),
                new Separator(),patientList);
        layout.setStyle("-fx-padding: 20;");


        addButton.setOnAction(e -> {
            String name = nameInput.getText();
            String phone = phoneInput.getText();
            String mail = mailInput.getText();
            String blood = bloodInput.getText();

            try {
                patient.add(new Patient(name,mail,phone,blood ));
                cards.add(new PatientCard("12",patient.get(patient.size()-1)));

                nameInput.clear();
                mailInput.clear();
                phoneInput.clear();
                bloodInput.clear();

            } catch (IllegalArgumentException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Less Argument");
                alert.showAndWait();
            }
        });

        viewButton.setOnAction(e -> {
            try {for(PatientCard card:cards){
                patientList.getChildren().add(card);}
            }catch(IllegalArgumentException E){
                Alert alert = new Alert(Alert.AlertType.ERROR, "No Patient Data found.");
                alert.showAndWait();
            }
        });
        clearButton.setOnAction(e -> {
            try {
                patientList.getChildren().removeAll(cards);
            }catch(IllegalArgumentException E){
                Alert alert = new Alert(Alert.AlertType.ERROR, "No Patient Data found.");
                alert.showAndWait();
            }
        });


        Scene scene = new Scene(layout, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Patient Info Viewer");

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
