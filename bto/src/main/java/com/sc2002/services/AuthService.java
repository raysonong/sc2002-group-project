package com.sc2002.services;

import com.sc2002.enums.UserRole;
import com.sc2002.model.UserModel;

public class AuthService {

    public boolean isOfficer(UserModel currentUser) {
        return currentUser.getUsersRole() == UserRole.HDB_OFFICER;
    }

    public boolean isManager(UserModel currentUser) {
        return currentUser.getUsersRole() == UserRole.HDB_MANAGER;
    }

    public boolean isApplicant(UserModel currentUser) {
        return currentUser.getUsersRole() == UserRole.APPLICANT;
    }
}
