package com.sc2002.controllers;

import com.sc2002.config.AppContext;
import com.sc2002.model.OfficerRegistrationModel;
import com.sc2002.services.OfficerRegistrationService;

/**
 * Controller for handling officer registration-related operations.
 * Acts as an intermediary between the view and the OfficerRegistrationService.
 */
public class OfficerRegistrationController {
    
    /** The service layer handling core officer registration logic. */
    private OfficerRegistrationService officerRegistrationService;
    
    /**
     * Constructor for OfficerRegistrationController.
     * 
     * @param appContext The application context
     */
    public OfficerRegistrationController(AppContext appContext) {
        this.officerRegistrationService = new OfficerRegistrationService(appContext);
    }
    
    /**
     * Registers the current officer for a project.
     * 
     * @return true if successfull, or false if registration failed
     */
    public Boolean registerForProject() {
        return officerRegistrationService.registerForProject();
    }
    
    /**
     * Approves an officer registration.
     * 
     * @param registration The registration to approve
     * @return true if successful, false otherwise
     */
    public boolean approveRegistration(OfficerRegistrationModel registration) {
        return officerRegistrationService.approveRegistration(registration);
    }
    
    /**
     * Rejects an officer registration.
     * 
     * @param registration The registration to reject
     * @return true if successful, false otherwise
     */
    public boolean rejectRegistration(OfficerRegistrationModel registration) {
        return officerRegistrationService.rejectRegistration(registration);
    }
}
