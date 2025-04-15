package com.sc2002.controllers;

import com.sc2002.config.AppContext;
import com.sc2002.model.BTOProjectModel;
import com.sc2002.services.ReportingService;

/**
 * Controller for handling reporting-related operations.
 * Acts as an intermediary between the view and the ReportingService.
 */
public class ReportingController {
    
    private ReportingService reportingService;
    
    /**
     * Constructor for ReportingController.
     * 
     * @param appContext The application context
     */
    public ReportingController(AppContext appContext) {
        this.reportingService = new ReportingService(appContext);
    }
    
    /**
     * Constructor for ReportingController with an existing ReportingService.
     * 
     * @param reportingService The reporting service to use
     */
    public ReportingController(ReportingService reportingService) {
        this.reportingService = reportingService;
    }
    
    /**
     * Generates a project report with filters.
     * 
     * @param project The project to generate a report for
     * @param generateType The type of filter to apply (1: No filter, 2: Married, 3: Single, 4: 2-Room, 5: 3-Room)
     */
    public void generateProjectReport(BTOProjectModel project, int generateType) {
        reportingService.generateProjectReport(project, generateType);
    }
}
