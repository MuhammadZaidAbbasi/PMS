💻 Installation & Running (IntelliJ + Maven)
Follow the steps below to clone this Maven-based JavaFX project from GitHub and run it using IntelliJ IDEA:

🔧 Prerequisites
Ensure the following tools are installed:

✅ Java JDK 17 or higher
✅ IntelliJ IDEA
✅ Git
✅ JavaFX SDK (match your JDK version)
✅ MySQL Server
✅ MySQL JDBC Driver (handled by Maven)
✅ Gmail Account with App Password Enabled (required to send emails)
✅ Internet connection for Maven to download dependencies

🔐 How to Generate an App Password (Gmail)
Go to https://myaccount.google.com/
Enable 2-Step Verification if not already enabled.
Navigate to Security > App Passwords
Select the app: Mail
Select the device: Other, and name it (e.g., AdminDashboard)
Click Generate
Copy the 16-character app password shown — and use it in " EmailNotification.java" and "EmailUtil in PatientDashboard.java ".



📥 Step 1: Clone the Repository
Open terminal or Git Bash and run:

git clone https://github.com/your-username/your-repo-name.git
cd your-repo-name

🧰 Step 2: Open Project in IntelliJ
Launch IntelliJ IDEA.

Click File > Open...

Select the root folder of the cloned project.

IntelliJ will automatically detect the pom.xml and import the Maven structure.

⚙️ Step 3: Configure JavaFX SDK
Download JavaFX SDK from https://openjfx.io/.

Extract it to a known location (e.g., C:\javafx-sdk-20 or /opt/javafx-sdk-20).

In IntelliJ:

Open Project Structure (Ctrl+Alt+Shift+S)

Go to Libraries > + > Java

Select /path/to/javafx-sdk-XX/lib

Apply the changes.

⚙️ Step 4: Add VM Options for JavaFX
To run your app with JavaFX:

Go to Run > Edit Configurations...

Under VM Options, add:

--module-path /path/to/javafx-sdk-XX/lib --add-modules javafx.controls,javafx.fxml
✅ Replace /path/to/javafx-sdk-XX/lib with your actual path.

📦 Step 5: Let Maven Install Dependencies
IntelliJ will auto-download dependencies from pom.xml. If not:

Open the Maven tab (right sidebar).

Click the refresh button 🔄


