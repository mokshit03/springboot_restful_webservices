# Target Web Application for SailPoint Integration

## 1. PROJECT DESCRIPTION
This web application serves as a target system not only for SailPoint Integrations but also for various similar tools, facilitating seamless provisioning and deprovisioning processes, enabling efficient provisioning and deprovisioning. 

It supports basic authentication and RBAC for secure permission management. The application handles the complete user management lifecycle, including creating, updating, and managing users, as well as role management and import records via CSV files, Helps maintaining a complete Joiner-Mover-Leaver (JML) flow. Additionally, it offers profile management and self-service capabilities such as password resets, ensuring secure, efficient, and user-friendly operations.

## 2. INSTALLATIONS

### 2.1 PREREQUISITES

#### 2.1.1 Direct Installations:
Visual Studio Code: Ensure you have Visual Studio Code installed.
Java: The project requires Java version 17.0.12 or above.
MySQL: Install the Mysql version 8.0.35 or above.

#### 2.1.2 Installations Using Linux Commands:
- Update your system:
    - ##### COMMAND: sudo apt update
    - ##### COMMAND: sudo apt upgrade

- Install Java using Linux commands:
    - ##### COMMAND: sudo apt install default-jdk

- Install MySQL using Linux commands:
    - ##### COMMAND: sudo apt install mysql-server

#### 2.1.3 Verifying Installations & Resloving Installation Errors:
- Java Installation Verification:
    - Verification : 'java --version' [Returns instaled vesrion - Check Java version is above 17.0.12] 
        - If Facing Error: Make sure to Set Java on environment variable path.

- MySQL Installation Verification:
    - Verification : 'mysql --version' [Returns instaled vesrion - Check MySQL version is above 8.0.35]

### 2.2 Set-Up Steps -

1. UNZIP THE FILE -
Unzip the file (TargetApplication.zip) in your local system by running the below commands:
- Install unzip (if not already installed):
    - ##### COMMAND: sudo apt install unzip
- Unzip the file:
    - ##### COMMAND: unzip filename.zip

1. KEEP ALL THE FILES IN ONE FOLDER -
- You will find: (application.properties, TargetApp.Jar, file.csv, superadmin.sql, README.md & HELP.md files) Keep all of the above mentioned files in a single folder

2. MODIFY 'application.properties' file -
- [spring.datasource.username] = {replace_with_your_username}
- [spring.datasource.password] = {replace_with_your_password}
- [server.port=8080] : (Make sure the server port is available) (default is 8080)
- [Unique_DB-schema] : Schema for this Target Web App is: "user_management", make sure it does not conflict with any existing DB schema - If it does [spring.datasource.url=jdbc:mysql://localhost/{change_db_name}]

3. RUN THE BACK-END APPLICATION -
- Open the command prompt from the folder path and run this command to get started with the application -
    - ##### COMMAND: "java -jar Targetapp.jar --spring.config.location=./application.properties"

- In case of errors try running the below commands:
    - ##### COMMAND: ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'Admin@123';
    - ##### COMMAND: flush privileges;

4. SET-UP SERVICE ACCOUNT
- Now the application is up and running, let's set-up a service account to access the application!
- Navigate to the project, right click on the folder & "open in Terminal"
- Run the following command to create a super admin in the application:
##### COMMAND: "mysql -u root -p user_management < superadmin.sql"
- Access the application with [username:serviceadmin] & [password:c2FkbWlu]

## 3. USAGE:
1. It's Simple:  You are all set to access all our APIs form POSTMAN! 
2. SOME USAGE NOTES -
- Default password for every new user will be   - "welcome@123".
- You can not disable/delete/remove anything of - "serviceadmin".
- Every user created will automatically get the "USER" Role.
- The application have "ADMIN", "USER", "USER_MANAGER" & "ADMIN" Roles only.
- The SERVICE_ADMIN Role should be only used for Connecting with Sailpoint.
- ROLE_SERVICE_ADMIN must not be assigned to any new normal users if not needed.

## 4. APPLICATION'S IMPORTANT APIs
- This Application lists 13 APIs, major APIs are listed below for quick use:
- Login: http://localhost/api/v1
- Create User: http://localhost/api/v1
- Assign/Reomve Role: http://localhost/api/v1/
- Enable User: http://localhost/api/v1/users/{user_id}/
- Disable User: http://localhost/api/v1/users/{user_id}/
- Import Users (CSV): http://localhost/api/v1/admin/users/
- Change Password: http://localhost/api/v1/users/{user_id}/##### NOTE: Make sure to check the PORT 8080 and change as configured in above steps!

## 4. FEATURES
- Authentication: Supports Basic authentication (Make Sure While SailPoint Integration).
- Authorization: Facilitates Role Based access control
- User Management: Handles Complete User-management-lifecycle (Create, update and manage users).
- Role Management: Create new roles and assign/remove them to/from users.
- Role-Based Access Requests: Authorizes requests for different types of user's permissions.
- User Import: Import records via CSV file & maintain complete Joiner-Mover-Leaver (JML) Flow.
- Profile Management: Users can View other user's profile and can manage their profiles.
- Self-Service Capabilities: Users can reset their passwords.

### 5. CONCLUSION
This web application provides a robust platform for managing user access and roles, integrated seamlessly with SailPoint for efficient provisioning and deprovisioning processes. With its comprehensive feature set, it ensures streamlined and secure user management.
