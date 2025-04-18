package com.sc2002.services;

import com.sc2002.enums.UserRole;
import com.sc2002.model.UserModel;

/**
 * Provides simple utility methods for checking user roles.
 * This service encapsulates the logic for determining if a user belongs to a specific role.
 */
public class AuthService {

    /**
     * Default constructor for AuthService.
     */
    public AuthService() {
        // Default constructor
    }

    /**
     * Checks if the given user has the HDB_OFFICER role.
     *
     * @param currentUser The user to check.
     * @return True if the user is an HDB Officer, false otherwise.
     */
    public boolean isOfficer(UserModel currentUser) {
        return currentUser.getUsersRole() == UserRole.HDB_OFFICER;
    }

    /**
     * Checks if the given user has the HDB_MANAGER role.
     *
     * @param currentUser The user to check.
     * @return True if the user is an HDB Manager, false otherwise.
     */
    public boolean isManager(UserModel currentUser) {
        return currentUser.getUsersRole() == UserRole.HDB_MANAGER;
    }

    /**
     * Checks if the given user has the APPLICANT role.
     *
     * @param currentUser The user to check.
     * @return True if the user is an Applicant, false otherwise.
     */
    public boolean isApplicant(UserModel currentUser) {
        return currentUser.getUsersRole() == UserRole.APPLICANT;
    }
}
