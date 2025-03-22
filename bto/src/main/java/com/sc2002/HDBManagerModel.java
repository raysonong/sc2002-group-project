package com.sc2002;

/**
 * This class is a model class that stores the information of each HDB Manager
 */

class HDBManagerModel extends User {

    /**
     * The projectID that they are handling
     */
    private int projectID;

    /**
     * The constructor
     * @param name The name of the user
     * @param nric The nric of the user
     * @param age The age of the user
     * @param isMarried Marital Status of the user
     * @param password The password of the account 
     */
    HDBManagerModel(String name, String nric, int age, String isMarried, String password) {
        super(nric, name, age, isMarried, password);
    }

    /**
     * To set the project ID
     * 
     * @param projectID the project ID they are handling
     */
    private void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    /**
     * To get the project ID
     * 
     * @return the project ID 
     */
    private int getProjectID() {
        return this.projectID;
    }
}