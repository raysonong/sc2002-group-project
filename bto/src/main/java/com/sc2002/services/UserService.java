package com.sc2002.services;

import java.util.Scanner;

import javax.naming.AuthenticationException;

import com.sc2002.enums.UserRole;
import com.sc2002.model.ApplicantModel;
import com.sc2002.model.UserModel;
import com.sc2002.repositories.UserRepo;
import com.sc2002.utilities.NRICValidator;
import com.sc2002.utilities.PasswordValidator;

/**
 * Provides services related to user management, including authentication,
 * registration, and password updates.
 */
public class UserService {

    /**
     * Default constructor for UserService.
     */
    public UserService() {
        // Default constructor
    }

    /**
     * Authenticates a user with NRIC and password
     *
     * @param nric The NRIC of the user
     * @param password The password of the user
     * @param userRepo The user repository
     * @return The authenticated user or null if authentication fails
     */
    public UserModel authenticateUser(String nric, String password, UserRepo userRepo) {
        try {
            // Format NRIC to uppercase
            nric = NRICValidator.formatNRIC(nric);

            // Find user by NRIC
            UserModel user = userRepo.getUserByNRIC(nric);
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
    public UserModel registerApplicant(String nric, String name, int age, String maritalStatus,
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
            UserModel newUser = new ApplicantModel(name, nric, age, maritalStatus, password, UserRole.APPLICANT);

            // Add to repository
            userRepo.save(newUser);

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
    public boolean updatePassword(UserModel user, String currentPassword, String newPassword) {
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

    /**
     * Handles the password resetting.
     * Prompts for the old password, new password, and confirmation.
     * Validates the new password complexity and ensures confirmation matches.
     * Calls `updatePassword` to perform the actual update after validation.
     *
     * @param user The UserModel of the user resetting their password.
     * @param scanner A Scanner instance to read user input from the console.
     */
    public void resetPassword(UserModel user, Scanner scanner) {
        String oldPassword = "";
        String password = "";
        String confirmPassword = "";
        boolean successful = false;
        do {
            if (oldPassword.isEmpty()) {
                System.out.printf("Enter your old password: ");
                oldPassword = scanner.nextLine().trim();
            }

            if (password.isEmpty()) {
                System.out.print("Enter your new password: ");
                password = scanner.nextLine().trim();

                if (!PasswordValidator.isValid(password)) {
                    for (String message : PasswordValidator.getValidationMessages(password)) {
                        System.out.println(message);
                    }
                    password = "";
                    continue;
                }
            }

            System.out.print("Confirm your password: ");
            confirmPassword = scanner.nextLine().trim();

            if (!password.equals(confirmPassword)) {
                System.out.println("Passwords do not match. Please try again.");
                continue;
            }

            successful = updatePassword(user, oldPassword, password);
            if (!successful) {
                oldPassword = "";
                password = "";
                confirmPassword = "";
            }
        } while (!PasswordValidator.isValid(password) || !password.equals(confirmPassword) || !successful);
    }
}
