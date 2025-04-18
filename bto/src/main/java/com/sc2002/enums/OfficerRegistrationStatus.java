package com.sc2002.enums;

/**
 * Represents the possible states of an HDB Officer's registration request to manage a project.
 */
public enum OfficerRegistrationStatus {
    /** Registration request submitted, awaiting manager approval. */
    PENDING,
    /** Registration request approved by the manager. */
    APPROVED,
    /** Registration request rejected by the manager. */
    REJECTED;
}
