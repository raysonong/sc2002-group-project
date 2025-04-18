package com.sc2002.enums;

/**
 * Represents the possible states of a BTO application throughout its lifecycle.
 */
public enum ApplicationStatus {
    /** Application submitted, awaiting processing/balloting. */
    PENDING,
    /** Application has been successfully balloted. */
    SUCCESSFUL,
    /** Application was not successful in the ballot. */
    UNSUCCESSFUL,
    /** Applicant has booked a flat after a successful ballot. */
    BOOKED,
    /** Application has been withdrawn by the applicant or system. */
    WITHDRAWN;
}
