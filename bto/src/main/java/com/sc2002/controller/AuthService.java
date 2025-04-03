package com.sc2002.controller;

import com.sc2002.model.User;
import com.sc2002.enums.*;
public class AuthService {
    public boolean isOfficer(User currentUser){
        if(currentUser.getUsersRole()==UserRole.HDB_OFFICER)return true;
        return false;
    }
    public boolean isManager(User currentUser){
        if(currentUser.getUsersRole()==UserRole.HDB_MANAGER)return true;
        return false;
    }
    public boolean isApplicant(User currentUser){
        if(currentUser.getUsersRole()==UserRole.APPLICANT)return true;
        return false;
    }
}
