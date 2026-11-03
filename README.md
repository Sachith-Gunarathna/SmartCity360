# SmartCity360 🏙️
**Object-Oriented Design Patterns II - Final Assessment**

SmartCity360 is a digital solution created by UrbanTech Solutions to manage city infrastructure, services, and resident interactions[cite: 50]. This project demonstrates the practical application of 5 fundamental Object-Oriented Design Patterns to solve real-world system architecture problems[cite: 50].

## 💻 Technologies Used
* **Language:** Java (JDK 8 / 11 or higher)
* **GUI Framework:** Java Swing (Desktop Application)
* **UI Theme:** FlatLaf (Modern Dark Theme)
* **IDE:** Apache NetBeans

## 🚀 Key Features & Design Patterns

1. **Part A: Data Stream Security (Decorator Pattern)**[cite: 50]
   Dynamically adds security layers (Encryption, Authentication, and Compression) to energy meter data streams without altering the core object logic[cite: 2, 50].

2. **Part B: Smart Zone Hierarchy (Composite Pattern)**[cite: 50]
   Represents city zones hierarchically (City -> District -> Street -> Building) in a tree-like structure, allowing uniform execution of commands like reporting usage and shutting down power[cite: 5, 6, 50].

3. **Part C: Citizen Complaints Escalation (Chain of Responsibility Pattern)**[cite: 51]
   Handles dynamic escalation paths for resident complaints, routing them from Local Staff to Supervisors to City Managers based on the severity level[cite: 17, 18, 51].

4. **Part D: Smart Street Lights Management (Flyweight Pattern)**[cite: 51]
   Minimizes memory usage when generating tens of thousands of streetlights by sharing common intrinsic properties (Model, Wattage, Color) across instances[cite: 21, 23, 51].

5. **Part E: City Policy Interpreter (Interpreter Pattern)**[cite: 51]
   Parses and dynamically evaluates city operational rules defined in a Domain-Specific Language (DSL) to calculate final limits[cite: 25, 26, 51].

## 🛠️ How to Run
1. Open the project folder using **Apache NetBeans IDE**.
2. Navigate to the project `Libraries`, click **"Add JAR/Folder"**, and include the downloaded `flatlaf.jar` file.
3. Right-click the project and select **"Clean and Build"**.
4. Run the `dashboard.java` (Main Class) file to launch the application.

## 👨‍💻 Developer
* **Name:** L.K. Sachith Niromal Gunarathna
* **Institute:** Java Institute for Advanced Technology
