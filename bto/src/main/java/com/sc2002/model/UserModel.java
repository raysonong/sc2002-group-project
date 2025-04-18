package com.sc2002.model;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;

import javax.naming.AuthenticationException;

import com.sc2002.enums.UserRole;

/**
 * Abstract base class representing a user within the BTO Management System.
 * This class encapsulates common attributes and behaviors shared by all user types (Applicant, HDB Officer, HDB Manager).
 * It serves as a foundation for specific user roles, promoting code reuse and polymorphism.
 * Utilizes abstraction for role-specific functionalities like menu options.
 */
public abstract class UserModel {
    /**
     * The role assigned to the user (e.g., APPLICANT, HDB_OFFICER, HDB_MANAGER).
     * Determines the user's permissions and available actions within the system.
     */
    private UserRole usersRole; 
    /**
     * Encapsulates the user's preferences for filtering project views.
     * Allows maintaining filter state specific to each user instance.
     */
    private ProjectViewFilterModel projectViewFilter;
    /**
     * The user's unique NRIC number.
     * Used for identification and login.
     */
    private String nric;
    /**
     * The full name of the user.
     */
    private String name;
    /**
     * The age of the user. Relevant for eligibility checks (e.g., BTO application).
     */
    private int age;
    /**
     * The user's marital status (true for married, false for single/divorced/widowed).
     * Relevant for BTO application eligibility and flat type restrictions.
     */
    private boolean isMarried;
    /**
     * The securely hashed password for user authentication.
     * Plain text passwords are never stored.
     */
    private String password;
        /**
     * Static counter to generate unique user IDs sequentially.
     * Ensures each user instance has a distinct identifier.
     */
    private static int nextUserID = 0;
        /**
     * The unique identifier for this user instance.
     * Auto-generated during object creation.
     */
    private int userID;
    /**
     * Protected constructor for initializing common user attributes.
     * Called by constructors of concrete subclasses (ApplicantModel, HDBOfficerModel, HDBManagerModel).
     * Enforces password hashing and initializes user state.
     *
     * @param nric The user's NRIC number.
     * @param name The user's name.
     * @param age The user's age.
     * @param isMarried A string indicating marital status ("Married" or "Single"). Case-insensitive.
     * @param password The user's chosen password in plaintext (will be hashed).
     * @param role The UserRole assigned to this user. Given by the subclasses.
     */
    protected UserModel(String nric, String name, int age, String isMarried, String password, UserRole role) {
        this.password = hashPasswd(password);
        if (this.password == null) {
            System.out.println("User not created");
            return;
        }
        if (isMarried.equalsIgnoreCase("married")) {
            this.isMarried = true;
        } else { //we assume if not married then single, since divorce is also single am i right :)
            this.isMarried = false;
        }

        this.usersRole=role;
        this.nric = nric;
        this.name = name;
        this.age = age;
        this.userID = nextUserID++; // Assign current ID and increment for next use
        this.projectViewFilter=new ProjectViewFilterModel(); // Initialized as empty
    }

    /**
     * Retrieves the user's NRIC number.
     *
     * @return The user's NRIC number.
     */
    public String getNRIC() {
        return this.nric;
    }

    /**
     * Retrieves the user's name.
     *
     * @return The user's name string.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Retrieves the user's age.
     *
     * @return The user's age as an integer.
     */

    public int getAge() {
        return this.age;
    }

    /**
     * Retrieves the user's marital status.
     *
     * @return True if the user is married, false otherwise.
     */
    public Boolean getMaritalStatus() {
        return this.isMarried;
    }

    /**
     * Retrieves the user's assigned role.
     *
     * @return The UserRole enum value representing the user's role.
     */
    public UserRole getUsersRole(){
        return this.usersRole;
    }

    /**
     * Retrieves the user's unique ID.
     *
     * @return The user's unique integer ID.
     */
    public int getUserID() {
        return this.userID;
    }
    /**
     * Sets the user's password. The provided plain text password will be securely hashed before storage.
     * If hashing fails, the password remains unchanged.
     *
     * @param password The plain text password to set.
     */
    public void setPassword(String password) {
        String newpassword = hashPasswd(password); // we do this to prevent possibility of overwriting password if error
        if (newpassword != null) {
            this.password = newpassword;
            return;
        }
    }

    /**
     * Authenticates the user by comparing the provided plain text password against the stored hash.
     *
     * @param password The plain text password provided during login attempt.
     * @throws AuthenticationException If the provided password does not match the stored hash.
     */
    public void authenticate(String password) throws AuthenticationException {
        if (!hashPasswd(password).equals(this.password)) { // very good tool for a lot of things!
            throw new AuthenticationException("Password does not match.");
        }
    }

    /**
     * Hashes a plain text password using the SHA-256 algorithm and encodes it using Base64.
     * 
     *
     * @param plaintext The plain text password to hash.
     * @return A Base64 encoded string representing the hashed password, or null if hashing fails.
     */
    private String hashPasswd(String plaintext) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256"); // tells java i want sha 256
            byte[] hash = digest.digest(plaintext.getBytes(StandardCharsets.UTF_8)); // translate string to bytes, then do hashing
            return Base64.getEncoder().encodeToString(hash); // encodes the binary into String before returning
        } catch (NoSuchAlgorithmException e) { // incase no algo found ;/
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Abstract method to be implemented by concrete subclasses.
     * Defines the specific menu options available to a user based on their role.
     * This promotes polymorphism, allowing different user types to present different interfaces.
     *
     * @return A List of strings, where each string represents a menu option available to the user.
     */
    public abstract List<String> getMenuOptions();

    /**
     * Retrieves the user's project view filter preferences.
     * Allows access to the encapsulated filter settings for this user.
     *
     * @return The ProjectViewFilterModel object associated with this user.
     */
    public ProjectViewFilterModel getProjectViewFilter(){
        return this.projectViewFilter;
    }
}
