package com.sc2002.controller;

import java.util.List;
import java.util.Scanner;

import javax.naming.AuthenticationException;

import com.sc2002.enums.UserRole;
import com.sc2002.model.ApplicantModel;
import com.sc2002.model.User;
import com.sc2002.repositories.UserRepo;
import com.sc2002.utilities.NRICValidator;

public class UserService {
    
    /**
     * Authenticates a user with NRIC and password
     * 
     * @param nric The NRIC of the user
     * @param password The password of the user
     * @param userRepo The user repository
     * @return The authenticated user or null if authentication fails
     */
    public User authenticateUser(String nric, String password, UserRepo userRepo) {
        try {
            // Format NRIC to uppercase
            nric = NRICValidator.formatNRIC(nric);
            
            // Find user by NRIC
            User user = userRepo.getUserByNRIC(nric);
            
            if (user == null) {
                throw new AuthenticationException("User with this NRIC not found");
            }
            
            // Authenticate with password
            user.authenticate(password);
            
            return user;
        } catch (AuthenticationException e) {
            System.out.println("Authentication failed: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Registers a new applicant user
     * 
     * @param nric The NRIC of the user
     * @param name The name of the user
     * @param age The age of the user
     * @param maritalStatus The marital status of the user
     * @param password The password of the user
     * @param userRepo The user repository
     * @return The registered user or null if registration fails
     */
    public User registerApplicant(String nric, String name, int age, String maritalStatus, 
                                 String password, UserRepo userRepo) {
        try {
            // Validate NRIC format
            if (!NRICValidator.isValidNRIC(nric)) {
                throw new IllegalArgumentException("Invalid NRIC format");
            }
            
            // Format NRIC to uppercase
            nric = NRICValidator.formatNRIC(nric);
            
            // Check if user already exists
            if (userRepo.getUserByNRIC(nric) != null) {
                throw new IllegalArgumentException("User with this NRIC already exists");
            }
            
            // Create new applicant
            User newUser = new ApplicantModel(name, nric, age, maritalStatus, password);
            
            // Add to repository
            userRepo.addUser(newUser);
            
            return newUser;
        } catch (Exception e) {
            System.out.println("Registration failed: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Updates a user's password
     * 
     * @param user The user to update
     * @param currentPassword The current password for verification
     * @param newPassword The new password to set
     * @return true if update successful, false otherwise
     */
    public boolean updatePassword(User user, String currentPassword, String newPassword) {
        try {
            // Verify current password
            user.authenticate(currentPassword);
            
            // Set new password
            user.setPassword(newPassword);
            
            return true;
        } catch (AuthenticationException e) {
            System.out.println("Current password is incorrect");
            return false;
        }
    }
}
