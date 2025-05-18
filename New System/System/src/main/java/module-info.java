module com.example.system {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.mail;
    requires java.desktop;
    requires java.sql;
    requires mysql.connector.j;
    requires kotlin.stdlib;
    requires jdk.jshell;

    opens com.example.system;

}