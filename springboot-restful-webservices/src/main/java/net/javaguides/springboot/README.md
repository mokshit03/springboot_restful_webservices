#### Web Application for SailPoint Integration

#### Project Description
This web application serves as a target system for SailPoint integration, facilitating seamless provisioning and deprovisioning processes governed by SailPoint. The application includes features such as role-based access requests and other self-service capabilities like new access requests, password resets, and more.

#### Installation

## Prerequisites -
Visual Studio Code: Ensure you have Visual Studio Code installed.
Java: The project requires Java version 17.0.12.
MySQL: Install the Mysql version 8.0.35.

## Steps -
Clone the repository to your local machine.
Open the project folder in Visual Studio Code.
The project contains a pom.xml file which will automatically include all the necessary dependencies.

#### Usage
Open the web application in Visual Studio Code.
Run the application.
Navigate to the project folder path.
Open a command prompt in the project folder by selecting the path, typing cmd, and pressing Enter.
Run the following command to create a super admin in the application:
# COMMAND: "mysql -u root -p user_management < superadmin.sql"
Access the application as a super admin.

#### Features
User Management: Create, update, and manage users.
Role Management: Create new roles and assign/remove them to/from users.
Role-Based Access Requests: Facilitate role-based access requests for users.
User Enable/Disable: Enable or disable user accounts.
Profile Management: View and manage user profiles.
Self-Service Capabilities: Allow users to reset passwords.
Oauth2.0: Supports Oauth 2.0 for authentication.

#### Conclusion
This web application provides a robust platform for managing user access and roles, integrated seamlessly with SailPoint for efficient provisioning and deprovisioning processes. With its comprehensive feature set, it ensures streamlined and secure user management.