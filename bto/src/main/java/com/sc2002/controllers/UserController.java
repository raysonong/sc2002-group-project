package com.sc2002.controllers;

import java.util.Scanner;

import com.sc2002.config.AppContext;
import com.sc2002.model.UserModel;
import com.sc2002.repositories.UserRepo;
import com.sc2002.services.UserService;

/**
 * Controller for handling user-related operations like login, registration, and password reset.
 * Acts as an intermediary between the view and the UserService.
 */
public class UserController {
    /** The service layer handling core user logic. */
    private UserService userService;
    
    /**
     * Constructor for UserController.
     * Initializes the UserService.
     *
     */
    public UserController() {
        this.userService = new UserService();
    }
    
    /**
     * Authenticates a user with NRIC and password.
     *
     * @param nric The NRIC of the user
     * @param password The password of the user
     * @param userRepo The user repository
     * @return The authenticated user or null if authentication fails
     */
    public UserModel authenticateUser(String nric, String password, UserRepo userRepo) {
        return userService.authenticateUser(nric, password, userRepo);
    }
    
    /**
     * Registers a new applicant user.
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
        return userService.registerApplicant(nric, name, age, maritalStatus, password, userRepo);
    }
    
    /**
     * Updates a user's password.
     *
     * @param user The user to update
     * @param currentPassword The current password for verification
     * @param newPassword The new password to set
     * @return true if update successful, false otherwise
     */
    public boolean updatePassword(UserModel user, String currentPassword, String newPassword) {
        return userService.updatePassword(user, currentPassword, newPassword);
    }
    
    /**
     * Resets a user's password with interactive prompts.
     *
     * @param user The user to update
     * @param scanner The scanner for user input
     */
    public void resetPassword(UserModel user, Scanner scanner) {
        userService.resetPassword(user, scanner);
    }
}
