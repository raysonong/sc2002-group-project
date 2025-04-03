package com.sc2002.controller;

import java.util.Scanner;

import com.sc2002.repositories.*;
import com.sc2002.model.*;

public class AppContext {
    private final Scanner scanner;
    private final AuthService authService;
    private User currentUser; // only this guy will need to keep changing
    private final UserRepo userRepo;
    private final ProjectRepo projectRepo;
    private final EnquiryRepo enquiryRepo;
    private final ApplicationRepo applicationRepo;

    public AppContext(Scanner scanner, AuthService authService, User currentUser, UserRepo userRepo, ProjectRepo projectRepo, EnquiryRepo enquiryRepo, ApplicationRepo applicationRepo){
        this.scanner = scanner;
        this.authService = authService;
        this.currentUser = currentUser;
        this.userRepo = userRepo;
        this.projectRepo = projectRepo;
        this.enquiryRepo = enquiryRepo;
        this.applicationRepo = applicationRepo;
    }
    public Scanner getScanner() { return scanner; }
    public AuthService getAuthService() { return authService; }
    public User getCurrentUser(){ return currentUser; }
    public UserRepo getUserRepo() { return userRepo; }
    public ProjectRepo getProjectRepo() { return projectRepo; }
    public EnquiryRepo getEnquiryRepo() { return enquiryRepo; }
    public ApplicationRepo getApplicationRepo() { return applicationRepo; }

    public void setCurrentUser(User currentUser){ this.currentUser=currentUser; }

}
