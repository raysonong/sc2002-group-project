package com.sc2002.config;

import java.util.Scanner;

import com.sc2002.controllers.AuthController;
import com.sc2002.model.UserModel;
import com.sc2002.repositories.ApplicationRepo;
import com.sc2002.repositories.EnquiryRepo;
import com.sc2002.repositories.OfficerRegistrationRepo;
import com.sc2002.repositories.ProjectRepo;
import com.sc2002.repositories.UserRepo;

public class AppContext {
    private final Scanner scanner;
    private final AuthController authController;
    private UserModel currentUser; // only this guy will need to keep changing
    private final UserRepo userRepo;
    private final ProjectRepo projectRepo;
    private final EnquiryRepo enquiryRepo;
    private final ApplicationRepo applicationRepo;
    private final OfficerRegistrationRepo officerRegistrationRepo;

    public AppContext(Scanner scanner, AuthController authController, UserModel currentUser, UserRepo userRepo, ProjectRepo projectRepo, EnquiryRepo enquiryRepo, ApplicationRepo applicationRepo, OfficerRegistrationRepo officerRegistrationRepo){
        this.scanner = scanner;
        this.authController = authController;
        this.currentUser = currentUser;
        this.userRepo = userRepo;
        this.projectRepo = projectRepo;
        this.enquiryRepo = enquiryRepo;
        this.applicationRepo = applicationRepo;
        this.officerRegistrationRepo = officerRegistrationRepo;
    }
    public Scanner getScanner() { return scanner; }
    public AuthController getAuthService() { return authController; }
    public UserModel getCurrentUser(){ return currentUser; }
    public UserRepo getUserRepo() { return userRepo; }
    public ProjectRepo getProjectRepo() { return projectRepo; }
    public EnquiryRepo getEnquiryRepo() { return enquiryRepo; }
    public ApplicationRepo getApplicationRepo() { return applicationRepo; }
    public OfficerRegistrationRepo getOfficerRegistrationRepo() { return officerRegistrationRepo; }

    public void setCurrentUser(UserModel currentUser){ this.currentUser=currentUser; }

}
