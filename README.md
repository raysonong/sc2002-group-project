# BTO Management System (sc2002-group-project)

## Description 

This project is a console-based application designed to manage various aspects of the Build-To-Order (BTO) housing process. It allows different user roles (Applicants, HDB Officers, HDB Managers) to interact with the system for tasks such as viewing projects, submitting applications, managing officer registrations, and handling enquiries.

## Features 🎯

*   View Project (Filterable) with filter data saved
*   NRIC Validator
*   Data initialisation from a file
*   Reset Password
*   Apply/Create/Edit/Delete for BTO Project
*   View BTO Application Status
*   Approve/Reject BTO Application
*   Manage BTO Bookings (change status to booked)
*   Create/View/Reply Enquiry
*   Toggle Project Visibility
*   View All BTO Projects
*   View Managing BTO Projects
*   Approve/Reject BTO Application Withdrawal
*   Generate Booked Flats Reports
*   Generate Flat Selection Receipt
*   Register to be a Managing Officer
*   View Managing Officer Registrations
*   Approve/Reject Officer Registration


## Additional Features

*   First time login
*   Hashing passwords

## Technologies Used

*   Java
*   Maven

## Additional Libraries

*   Apache Maven-Javadoc-Plugin (3.11.2)

## How to Run

1.  Clone Github Repository to your preferred directory

2.  **Using Command Line:**
    From the `sc2002-group-project` directory, run:
    ```bash
     java ./bto/src/main/java/com/sc2002/Main.java
    ```

## Project Structure

The project follows a standard MVCS architecture with additional packages. All packages are stored within `bto/src/main/java/com/sc2002`:

*   `Main.java`: The main entry point of the application.
*   `model`: Contains the data model classes representing entities like `UserModel`, `BTOProjectModel`, `EnquiryModel`, etc.
*   `view`: Contains classes responsible for the console-based user interface (`mainAppView`, `ApplicantView`, etc.).
*   `controllers`: Handles user interactions and coordinates application flow (`AuthController`, `InitializationController`, etc.).
*   `services`: Implements the core business logic of the application (`AuthService`, `ProjectService`, etc.).
*   `repositories`: Manages data persistence and retrieval logic (`UserRepo`, `ProjectRepo`, etc.).
*   `interfaces`: Defines common interfaces, like `RepoInterface` used to define a contract to repositories.

Other packages:
*   `config`: Contains application context and configuration classes (`AppContext`).
*   `enums`: Defines enumeration types used across the application (e.g., `UserRole`, `ApplicationStatus`).
*   `utilities`: Provides helper functions and utility classes (`CSVReader`, validators).


