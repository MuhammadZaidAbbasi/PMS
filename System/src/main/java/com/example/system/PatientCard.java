package com.example.system;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import management.*;


public class PatientCard extends VBox {
    public PatientCard(String id,Patient patient) {
        Label idLabel = new Label("ID : " + id);
        Label nameLabel = new Label("Name : " + patient.getName());
        Label BGLabel = new Label("Blood Group : " + patient.getBloodType());
        Label phoneLabel = new Label("Contact : " + patient.getPhoneNumber());
        Label mailLabel = new Label("Email:");
        mailLabel.setMinWidth(40);
        TextField mailField = new TextField( patient.getEmail());
        mailField.setEditable(false);
        mailField.setPadding(new Insets(0,0,0,5));
        HBox emailBox=new HBox(mailLabel,mailField);
        mailField.setStyle("-fx-background-color : transparent; ");
        Label dobLabel = new Label("DOB : " + (patient.getDate_of_birth()).toString());
        Label genderLabel = new Label("Gender : " + patient.getGender());


        this.getChildren().addAll(idLabel,nameLabel,emailBox,BGLabel,phoneLabel,genderLabel,dobLabel);
        this.setSpacing(5);
        this.setPrefWidth(230);
        this.setStyle("-fx-padding: 10; -fx-border-color: gray; -fx-border-radius: 10; -fx-font-size:15;");
    }
}

