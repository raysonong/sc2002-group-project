package com.sc2002.utilities;

import com.sc2002.model.BTOApplicationModel;

public class Receipt {
    private BTOApplicationModel application;


    public Receipt(BTOApplicationModel application) {
        this.application = application;
    }

    public void printReceipt() {
        System.out.println("--BTO Selection Receipt--");
        System.out.println("Application ID: " + application.getApplicationID());
        System.out.println("Applicant: " + application.getApplicantName());
        System.out.println("Age: " + application.getApplicantAge());
        System.out.println("Marital Status: " + (application.getApplicantMaritialStatus() ? "Married" : "Single"));
        System.out.println("Flat Type: " + application.getFlatType());
        System.out.println("Project ID: " + application.getProjectID());
        System.out.println("Application Status: " + application.getStatus());
        System.out.println("Submission Date: " + application.getSubmissionDate());
        System.out.println("--------------------------");
        System.out.println("Receipt Generated Successfully!");
    }
}
