package com.sc2002.enums;

/**
 * Contains the different roles a user can have within the BTO Management System.
 * Used for controlling access via AuthService and determining available actions.
 */
public enum UserRole {
    /** Represents a standard applicant user applying for BTO projects. */
    APPLICANT,
    /** Represents an HDB officer responsible for managing specific BTO projects. */
    HDB_OFFICER,
    /** Represents an HDB manager with higher privileges, such as creating/deleting projects. */
    HDB_MANAGER;
}
