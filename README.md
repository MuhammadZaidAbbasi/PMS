# 🩺 Remote Patient Monitoring System (RPMS)

This document explains how to set up and use the **Remote Patient Monitoring System (RPMS)** — a Java-based desktop application developed using **JavaFX**, **OOP principles**, and **MySQL** for backend storage. The goal is to help doctors monitor patient vitals remotely and offer timely feedback.

---

## 📘 Project Overview

The **Remote Patient Monitoring System** enables seamless interaction between doctors and patients. Key functionalities include:

- 🩻 **Doctors** can view patient health data and provide feedback.
- 📈 **Patients** can submit vital signs like:
  - Blood Pressure
  - Heart Rate
  - Body Temperature

This allows remote healthcare monitoring without in-person visits.

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

git clone [https://github.com/your-username/your-repo-name](https://github.com/MuhammadZaidAbbasi/PMS).git
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

###🏃 Step 6: Run the Application

1. Locate the `LoginApp.java` file (this contains the `main()` method).
2. Right-click on the file.
3. Select **Run 'LoginApp.main()'**.

✅ If configured correctly, the GUI will launch!

