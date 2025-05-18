🩺 Remote Patient Monitoring System (RPMS)
This document explains how to set up and use the Remote Patient Monitoring System (RPMS) — a Java-based desktop application developed using JavaFX, OOP principles, and MySQL for backend storage. The goal is to help doctors monitor patient vitals remotely and offer timely feedback.

📘 Project Overview
The Remote Patient Monitoring System enables seamless interaction between doctors and patients. Key functionalities include:

🩻 Doctors can view patient health data and provide feedback.

📈 Patients can submit vital signs like:

Blood Pressure

Heart Rate

Body Temperature

This allows remote healthcare monitoring without in-person visits.

💻 Installation & Running (IntelliJ + Maven)
Follow these steps to clone, configure, and run the project in IntelliJ IDEA.

🔧 Prerequisites
Make sure the following tools are installed:

✅ Java JDK 17 or higher

✅ IntelliJ IDEA

✅ Git

✅ JavaFX SDK (matching your JDK version)

✅ MySQL Server

✅ MySQL JDBC Driver (automatically added via Maven)

✅ Gmail account with App Password enabled (for sending email notifications)

✅ Internet connection (Maven will fetch dependencies)

🔐 Configure Gmail App Password (for email notifications)
This project sends email alerts using Gmail SMTP, which requires an app password.

Steps to generate:
Go to https://myaccount.google.com/

Enable 2-Step Verification

Navigate to Security > App Passwords

Choose:

App: Mail

Device: Other → name it (e.g., RPMS App)

Click Generate

Copy the 16-character password

✅ Use this password in the following files:

EmailNotification.java

EmailUtil class inside PatientDashboard.java

📥 Step 1: Clone the Repository
Open your terminal or Git Bash and run:

bash
Copy
Edit
git clone https://github.com/your-username/your-repo-name.git
cd your-repo-name
🧰 Step 2: Open Project in IntelliJ
Launch IntelliJ IDEA

Go to File > Open...

Select the folder where you cloned the project

IntelliJ will detect the pom.xml and import the Maven project structure

⚙️ Step 3: Configure JavaFX SDK
Download JavaFX SDK from https://openjfx.io/

Extract it to a known location, e.g.,:

C:\javafx-sdk-20\ (Windows)

/opt/javafx-sdk-20/ (Linux/macOS)

In IntelliJ:

Open Project Structure (Ctrl + Alt + Shift + S)

Go to Libraries > + > Java

Select /path/to/javafx-sdk-XX/lib

Click OK and Apply

⚙️ Step 4: Add VM Options for JavaFX
To ensure JavaFX modules load properly:

Go to Run > Edit Configurations...

Under VM Options, add:

bash
Copy
Edit
--module-path /path/to/javafx-sdk-XX/lib --add-modules javafx.controls,javafx.fxml
✅ Replace /path/to/javafx-sdk-XX/lib with your actual path.

📦 Step 5: Let Maven Install Dependencies
IntelliJ will automatically download dependencies via pom.xml

If it doesn’t, do the following:

Open the Maven tab (usually on the right)

Click the Refresh icon 🔄
OR run from terminal:

bash
Copy
Edit
mvn clean install
🏃 Step 6: Run the Application
Locate the LoginApp.java file (contains the main() method)

Right-click the file → Run 'LoginApp.main()'

✅ If configured correctly, the GUI will launch!
