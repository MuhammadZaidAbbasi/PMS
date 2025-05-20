# 🩺 Remote Patient Monitoring System (RPMS)

This document explains how to set up and use the **Remote Patient Monitoring System (RPMS)** — a Java-based desktop application developed using **JavaFX**, **OOP principles**, and **MySQL** for backend storage. The goal is to help doctors monitor patient vitals remotely and offer timely feedback.

---

## 📘 Project Overview

The **Remote Patient Monitoring System** enables seamless interaction between doctors and patients. Key functionalities include:

-  **Doctors** can view patient health data and provide feedback :
  - Doctor can Acept or Reject Appointments. 
- 📈 **Patients** can submit vital signs like:
  - Blood Pressure - Heart Rate - Body Temperature
  - Patient can apply for Appointments
-   **Admin** can manage System and perform duties like:
  - Add or Remove Patient/Doctor
  - generate System Report and View System Logs 

This allows remote healthcare monitoring without in-person visits.
---
## 📘 Google Deive link 
[https://drive.google.com/drive/folders/18126j6PTtK6uMkL552zc4TFy7D95_aH7](https://drive.google.com/drive/folders/18126j6PTtK6uMkL552zc4TFy7D95_aH7)
---
---
## 📘 GUI Look
![image](https://github.com/user-attachments/assets/04e7cd4f-a2fd-4e38-a447-e8ee1437845f)
---

## 💻 Installation & Running (IntelliJ + Maven)

Follow these steps to **clone**, **configure**, and **run** the project in **IntelliJ IDEA**.

---

### 🔧 Prerequisites

Make sure the following tools are installed:

- ✅ Java JDK 17 or higher  
- ✅ IntelliJ IDEA  
- ✅ Git  
- ✅ JavaFX SDK (matching your JDK version)  
- ✅ MySQL Server  
- ✅ MySQL JDBC Driver (automatically added via Maven)  
- ✅ Gmail account with **App Password** enabled *(for sending email notifications)*  
- ✅ Internet connection (Maven will fetch dependencies)

---

### 🔐 Configure Gmail App Password (for email notifications)

This project sends email alerts using **Gmail SMTP**, which **requires an app password**.

#### Steps to generate:

1. Go to [https://myaccount.google.com/](https://myaccount.google.com/)
2. Enable **2-Step Verification**
3. Navigate to **Security > App Passwords**
4. Choose:
   - App: Mail
   - Device: Other → name it (e.g., RPMS App)
5. Click **Generate**
6. Copy the **16-character password**

✅ Use this password in the following files:
- EmailNotification.java
- EmailUtil class inside PatientDashboard.java

---

### 📥 Step 1: Clone the Repository

Open your terminal or Git Bash and run:

git clone [https://github.com/MuhammadZaidAbbasi/PMS](https://github.com/MuhammadZaidAbbasi/PMS).git
cd PMS
### 🧰 Step 2: Open Project in IntelliJ

1. Launch **IntelliJ IDEA**
2. Go to **File > Open...**
3. Select the folder where you cloned the project
4. IntelliJ will detect the `pom.xml` file and import the Maven project structure automatically

---

### ⚙️ Step 3: Configure JavaFX SDK

1. Download JavaFX SDK from: [https://openjfx.io/](https://openjfx.io/)
2. Extract it to a known location, for example:
   - `C:\javafx-sdk-20\` (on Windows)
   - `/opt/javafx-sdk-20/` (on Linux/macOS)
3. In IntelliJ:
   - Open **Project Structure** (`Ctrl + Alt + Shift + S`)
   - Navigate to **Libraries > + > Java**
   - Select the directory: `/path/to/javafx-sdk-XX/lib`
   - Click **OK** and **Apply**

---

### ⚙️ Step 4: Add VM Options for JavaFX

1. Go to **Run > Edit Configurations...**
2. Under **VM Options**, add the following:

   ```bash
   --module-path /path/to/javafx-sdk-XX/lib --add-modules javafx.controls,javafx.fxml
✅ Replace `/path/to/javafx-sdk-XX/lib` with the actual path where you extracted the JavaFX SDK.

---

### 📦 Step 5: Let Maven Install Dependencies

IntelliJ will automatically download all required dependencies listed in `pom.xml`.

If it doesn’t:

1. Open the **Maven** tab (usually on the right side of IntelliJ)
2. Click the **Refresh icon** 🔄  
**OR** run the following command in your terminal:
    ```bash
    mvn clean install

---
###  Step 6: Create Database

1. Create databse named " healthmanagement " in Your MySQL WorkBench.
2. Open OOP_Schema.sql ( provided ) file in Your MySQL WorkBench.
3. Select **Run** or Press ctrl + Enter databse with all tables and data will be created in Your Workbench .


### 🏃 Step 7: Run the Application

1. Locate the `LoginApp.java` file (this contains the `main()` method).
2. Right-click on the file.
3. Select **Run 'LoginApp.main()'**.
4. To Check the Chat Feature First You have To run File named `ChatServer.java` and then Run `LoguinApp.java`.

✅ If configured correctly, the GUI will launch!

Users in the System
-------------------
There are two main users in the system:

1. Doctor
   - Can view patient data
   - Can give feedback
   - Can check list of patients assigned

2. Patient
   - Can upload vitals (like heart rate, temperature, etc.)
   - Can view feedback from the doctor

Packages and Modules
--------------------
The system is divided into these packages/modules:

1. management - Handles core logic like users, patients, doctors, etc.
2. DoctorPatientInteraction - Handles feedback and logs
3. HealthDataHandling - Handles vitals and their storage
4. NotificationAndRemainder - Sends email reminders and notifications

Class Responsibilities
----------------------
1. User: Base class for Doctor and Patient. Manages basic user info (email, phone, etc.).
2. Doctor: Inherits from User. Can give feedback, view patients and vitals.
3. Patient: Inherits from User. Can upload and view vitals.
4. Feedback: Contains doctor’s notes for the patient.
5. VitalSign: Stores health data like heart rate, temperature, etc.
6. EmailNotification: Sends emails using Gmail SMTP server.
7. RemainderService: Inherits from EmailNotification and sends appointment/medicine reminders.

How It Works (Step-by-Step)
---------------------------
1. The system starts and asks users to login or signup.
2. If logged in as a patient:
   - Upload your vitals.
   - Your doctor can see your data.
3. If logged in as a doctor:
   - View your assigned patients.
   - Check their vitals and give feedback.
4. Emails can be sent as reminders or feedback notifications.

Technologies Used
-----------------
1. Java (OOP concepts like inheritance, encapsulation, abstraction)
2. JavaFX for GUI (Optional)
3. Jakarta Mail API for sending emails
4. JDBC for database interaction
5. MySQL as backend database

Health Data (Vitals)
--------------------
Patients upload the following health information:

1. Heart Rate (BPM)
2. Blood Pressure (Systolic & Diastolic)
3. Body Temperature (°F)
4. Breathing Rate (breaths/min)
5. Oxygen Level (%)

Final Notes
-----------
This project demonstrates a working prototype of a healthcare system. It shows how patients and doctors can interact digitally and how health data can be used effectively.


