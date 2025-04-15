package com.sc2002.controllers;

import com.sc2002.config.AppContext;
import com.sc2002.model.UserModel;
import com.sc2002.services.AuthService;

/**
 * Controller for handling authentication-related operations.
 * Acts as an intermediary between the view and the AuthService.
 */
public class AuthController {
    
    private AuthService authService;
    
    /**
     * Constructor for AuthController.
     * 
     * @param appContext The application context
     */
    public AuthController() {
        this.authService = new AuthService();
    }
    
    /**
     * Checks if the user is an officer.
     * 
     * @param currentUser The user to check
     * @return true if the user is an officer, false otherwise
     */
    public boolean isOfficer(UserModel currentUser) {
        return authService.isOfficer(currentUser);
    }
    
    /**
     * Checks if the user is a manager.
     * 
     * @param currentUser The user to check
     * @return true if the user is a manager, false otherwise
     */
    public boolean isManager(UserModel currentUser) {
        return authService.isManager(currentUser);
    }
    
    /**
     * Checks if the user is an applicant.
     * 
     * @param currentUser The user to check
     * @return true if the user is an applicant, false otherwise
     */
    public boolean isApplicant(UserModel currentUser) {
        return authService.isApplicant(currentUser);
    }
}
