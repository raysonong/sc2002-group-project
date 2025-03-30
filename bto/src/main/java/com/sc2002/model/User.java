package com.sc2002.model;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;

import javax.naming.AuthenticationException;

import com.sc2002.enums.UserRole;

/**
 * Abstract class representing a user in the system. This class provides basic
 * user attributes and functionalities
 */
public abstract class User {
    // Contains user's role
    private UserRole usersRole; // view com.sc2002.enums.UserRole for more info 
    
    /**
     * The National Registration Identity Card (NRIC) of the user.
     */
    private String nric;
    /**
     * The name of the user.
     */
    private String name;
    /**
     * The age of the user.
     */
    private int age;
    /**
     * The marital status, note that users will parse String "Married"/"Single"
     * as params, ensure to handle it.
     */
    private boolean isMarried;
    /**
     * The hashed password of the user.
     */
    private String password;

    private static int nextUserId = 0; // Static counter for auto-incrementing IDs
    private int userID;
    /**
     * The constructor
     *
     * @param name The name of the user
     * @param nric The nric of the user
     * @param age The age of the user
     * @param isMarried Marital Status of the user
     * @param password The password of the account
     */
    User(String nric, String name, int age, String isMarried, String password, UserRole role) {
        this.password = hashPasswd(password);
        if (this.password == null) {
            System.out.println("User not created");
            return;
        }
        if (isMarried == "Married") {
            this.isMarried = true;
        } else {
            this.isMarried = false;
        }
        this.usersRole=role;
        this.nric = nric;
        this.name = name;
        this.age = age;
        this.userID = nextUserId++; // Assign current ID and increment for next use
    }

    /**
     * Retrieves the NRIC of the user.
     *
     * @return The NRIC of the user.
     */
    public String getNRIC() {
        return this.nric;
    }

    /**
     * Retrieves the name of the user.
     *
     * @return The name of the user.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Retrieves the age of the user.
     *
     * @return The age of the user.
     */

    public int getAge() {
        return this.age;
    }

    /**
     * Retrieves the marital status of the user.
     *
     * @return True if the user is married, false otherwise.
     */
    public Boolean MaritialStatus() {
        return this.isMarried;
    }

    public UserRole getUsersRole(){
        return this.usersRole;
    }

    public int getUserID() {
        return this.userID;
    }
    /**
     * Sets the password of the user after hashing it.
     *
     * @param password The new password to set.
     */
    public void setPassword(String password) {
        String newpassword = hashPasswd(password); // we do this to prevent possibility of overwriting password if error
        if (newpassword != null) {
            this.password = newpassword;
            return;
        }
    }

    /**
     * Authenticates the user with the given NRIC and password.
     *
     * @param nric The NRIC of the user attempting to authenticate.
     * @param password The password of the user attempting to authenticate.
     * @throws AuthenticationException If the NRIC or password does not match
     * the stored credentials.
     */
    public void authenticate(String password) throws AuthenticationException {
        if (!hashPasswd(password).equals(this.password)) { // very good tool for a lot of things!
            throw new AuthenticationException("Password does not match.");
        }
    }

    /**
     * Hashes the given plaintext password using SHA-256 and Base64 encoding.
     *
     * @param plaintext The password to hash.
     * @return The hashed password, or null if an error occurs.
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

    public abstract List<String> getMenuOptions();
}
