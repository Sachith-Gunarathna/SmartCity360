# SmartCity360 🏙️
**Object-Oriented Design Patterns II - Final Assessment**

SmartCity360 is a digital solution created by UrbanTech Solutions to manage city infrastructure, services, and resident interactions. This project demonstrates the practical application of 5 fundamental Object-Oriented Design Patterns to solve real-world system architecture problems.

## 💻 Technologies Used
* **Language:** Java (JDK 8 / 11 or higher)
* **GUI Framework:** Java Swing (Desktop Application)
* **UI Theme:** FlatLaf (Modern Dark Theme)
* **IDE:** Apache NetBeans

## 🚀 Key Features & Design Patterns

1. **Part A: Data Stream Security (Decorator Pattern)**
   Dynamically adds security layers (Encryption, Authentication, and Compression) to energy meter data streams without altering the core object logic.

2. **Part B: Smart Zone Hierarchy (Composite Pattern)**
   Represents city zones hierarchically (City -> District -> Street -> Building) in a tree-like structure, allowing uniform execution of commands like reporting usage and shutting down power.

3. **Part C: Citizen Complaints Escalation (Chain of Responsibility Pattern)**
   Handles dynamic escalation paths for resident complaints, routing them from Local Staff to Supervisors to City Managers based on the severity level.

4. **Part D: Smart Street Lights Management (Flyweight Pattern)**
   Minimizes memory usage when generating tens of thousands of streetlights by sharing common intrinsic properties (Model, Wattage, Color) across instances.

5. **Part E: City Policy Interpreter (Interpreter Pattern)**
   Parses and dynamically evaluates city operational rules defined in a Domain-Specific Language (DSL) to calculate final limits.

## 🛠️ How to Run
1. Open the project folder using **Apache NetBeans IDE**.
2. Navigate to the project `Libraries`, click **"Add JAR/Folder"**, and include the downloaded `flatlaf.jar` file.
3. Right-click the project and select **"Clean and Build"**.
4. Run the `dashboard.java` (Main Class) file to launch the application.

## 👨‍💻 Developer
* **Name:** L.K. Sachith Niromal Gunarathna
* **Institute:** Java Institute for Advanced Technology
