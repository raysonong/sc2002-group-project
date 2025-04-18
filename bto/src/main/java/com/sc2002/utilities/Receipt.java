package com.sc2002.utilities;

import com.sc2002.model.BTOApplicationModel;

/**
 * Represents a receipt generated for a BTO application, typically after successful booking.
 * Contains details of the application to be printed.
 */
public class Receipt {
    /** The BTO application associated with this receipt. */
    private BTOApplicationModel application;

    /**
     * Constructs a Receipt object based on a BTO application.
     *
     * @param application The BTOApplicationModel to generate the receipt for.
     */
    public Receipt(BTOApplicationModel application) {
        this.application = application;
    }

    /**
     * Prints the details of the receipt (application information) to the console.
     */
    public void printReceipt() {
        System.out.println("--BTO Selection Receipt--");
        System.out.println("Application ID: " + application.getApplicationID());
        System.out.println("Applicant: " + application.getApplicantName());
        System.out.println("Age: " + application.getApplicantAge());
        System.out.println("Marital Status: " + (application.getApplicantMaritalStatus() ? "Married" : "Single"));
        System.out.println("Flat Type: " + application.getFlatType());
        System.out.println("Project ID: " + application.getProjectID());
        System.out.println("Application Status: " + application.getStatus());
        System.out.println("Submission Date: " + application.getSubmissionDate());
        System.out.println("--------------------------");
        System.out.println("Receipt Generated Successfully!");
    }
}
