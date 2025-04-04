package com.sc2002.controller;

import com.sc2002.enums.UserRole;
import com.sc2002.model.User;

public class AuthService {

    public boolean isOfficer(User currentUser) {
        return currentUser.getUsersRole() == UserRole.HDB_OFFICER;
    }

    public boolean isManager(User currentUser) {
        return currentUser.getUsersRole() == UserRole.HDB_MANAGER;
    }

    public boolean isApplicant(User currentUser) {
        return currentUser.getUsersRole() == UserRole.APPLICANT;
    }
}
