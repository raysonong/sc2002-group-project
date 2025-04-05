package com.sc2002.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import com.sc2002.enums.FlatType; // for FlatType enums
import com.sc2002.enums.UserRole; // for UserRole enums

/**
 * Represents a BTO (Build-To-Order) project with details such as project name, neighborhood, 
 * flat counts, application dates, and managing officers.
 */
public class BTOProjectModel {
    /**
     * The next ID of the project. Auto increments everytime a project is created.
     */
    private static int nextprojectID;
    /**
     * The unique ID of the project.
     */
    private final int projectID;

    /**
     * The name of the project.
     */
    private String projectName;

    /**
     * The neighborhood where the project is located.
     */
    private String neighborhood;

    /**
     * The number of 2-room flats available.
     */
    private int twoRoomCount;

    /**
     * The number of 3-room flats available.
     */
    private int threeRoomCount;

    /**
     * The application opening date.
     */
    private LocalDate openingDate;

    /**
     * The application closing date.
     */
    private LocalDate closingDate;

    /**
     * The UserID of the manager assigned to the project.
     */
    private int managerUserID;

    /**
     * The visibility status of the project.
     */
    private boolean isVisibility;

    /**
     * The maximum number of managing officers allowed.
     */
    private int maxManagingOfficer;

    /**
     * The list of managing officer UserIDs assigned to the project.
     */
    private ArrayList<String> managingOfficerUserID;

    /**
     * Constructs a new BTOProjectModel with the specified details.
     *
     * @param projectID          The unique ID of the project.
     * @param projectName        The name of the project.
     * @param neighborhood       The neighborhood where the project is located.
     * @param twoRoomCount       The number of 2-room flats available.
     * @param threeRoomCount     The number of 3-room flats available.
     * @param openingDate        The application opening date.
     * @param closingDate        The application closing date.
     * @param maxManagingOfficer The maximum number of managing officers allowed.
     */
    public BTOProjectModel(String projectName, String neighborhood, int twoRoomCount, int threeRoomCount, LocalDate openingDate, LocalDate closingDate, int maxManagingOfficer, int managerUserID) {

        managingOfficerUserID = new ArrayList<>();
        this.projectID = this.nextprojectID;
        this.nextprojectID += 1; 
        this.projectName = projectName;
        this.neighborhood = neighborhood;
        this.twoRoomCount = twoRoomCount;
        this.threeRoomCount = threeRoomCount;
        this.openingDate = openingDate;
        this.closingDate = closingDate;
        this.maxManagingOfficer = maxManagingOfficer;
        this.managerUserID = managerUserID;

        this.isVisibility = false;
    }

    /**
     * Gets the unique ID of the project.
     *
     * @return The project ID.
     */
    public int getProjectID() {
        return projectID;
    }

    /**
     * Gets the name of the project.
     *
     * @return The project name.
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * Sets the name of the project.
     *
     * @param projectName The project name to set.
     */
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    /**
     * Gets the neighborhood where the project is located.
     *
     * @return The neighborhood.
     */
    public String getNeighborhood() {
        return neighborhood;
    }

    /**
     * Sets the neighborhood where the project is located.
     *
     * @param neighborhood The neighborhood to set.
     */
    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    /**
     * Gets the number of 2-room flats available.
     *
     * @return The count of 2-room flats.
     */
    public int getTwoRoomCount() {
        return twoRoomCount;
    }

    /**
     * Sets the number of 2-room flats available.
     *
     * @param twoRoomCount The count of 2-room flats to set.
     */
    public void setTwoRoomCount(int twoRoomCount) {
        this.twoRoomCount = twoRoomCount;
    }

    /**
     * Gets the number of 3-room flats available.
     *
     * @return The count of 3-room flats.
     */
    public int getThreeRoomCount() {
        return this.threeRoomCount;
    }

    /**
     * Sets the number of 3-room flats available.
     *
     * @param threeRoomCount The count of 3-room flats to set.
     */
    public void setThreeRoomCount(int threeRoomCount) {
        this.threeRoomCount = threeRoomCount;
    }

    /**
     * Gets the application opening date.
     *
     * @return The opening date.
     */
    public LocalDate getOpeningDate() {
        return this.openingDate;
    }

    /**
     * Sets the application opening date.
     *
     * @param openingDate The opening date to set.
     */
    public void setOpeningDate(LocalDate openingDate) {
        this.openingDate = openingDate;
    }

    /**
     * Gets the application closing date.
     *
     * @return The closing date.
     */
    public LocalDate getClosingDate() {
        return this.closingDate;
    }

    /**
     * Sets the application closing date.
     *
     * @param closingDate The closing date to set.
     */
    public void setClosingDate(LocalDate closingDate) {
        this.closingDate = closingDate;
    }

    /**
     * Gets the UserID of the manager assigned to the project.
     *
     * @return The manager UserID.
     */
    public int getManagerUserID() {
        return this.managerUserID;
    }

    /**
     * Sets the UserID of the manager assigned to the project.
     *
     * @param managerUserID The manager UserID to set.
     */
    public void setManagerUserID(int managerUserID) {
        this.managerUserID = managerUserID;
    }

    /**
     * Gets the visibility status of the project.
     *
     * @return True if the project is visible, false otherwise.
     */
    public boolean isVisibility() {
        return this.isVisibility;
    }

    /**
     * Sets the visibility status of the project.
     *
     * @param visibility The visibility status to set.
     */
    public void setVisibility(boolean visibility) {
        this.isVisibility = visibility;
    }

    /**
     * Gets the maximum number of managing officers allowed.
     *
     * @return The maximum number of managing officers.
     */
    public int getMaxManagingOfficer() {
        return this.maxManagingOfficer;
    }

    /**
     * Sets the maximum number of managing officers allowed.
     *
     * @param maxManagingOfficer The maximum number to set.
     */
    public void setMaxManagingOfficer(int maxManagingOfficer) {
        this.maxManagingOfficer = maxManagingOfficer;
    }

    /**
     * Gets the list of managing officer UserIDs assigned to the project.
     *
     * @return The list of managing officer UserIDs.
     */
    public ArrayList<String> getManagingOfficerUserID() {
        return this.managingOfficerUserID;
    }

    /**
     * Adds a managing officer UserID to the list.
     *
     * @param managingOfficerUserID The UserID of the managing officer to add.
     */
    public void addManagingOfficerUserID(String managingOfficerUserID) {
        this.managingOfficerUserID.add(managingOfficerUserID);
    }

    /**
     * Removes a managing officer ID from the list.
     *
     * @param managingOfficerUserID The ID of the managing officer to remove.
     * @throws NoSuchElementException If the ID is not found in the list.
     */
    public void removeManagingOfficerUserID(String managingOfficerUserID) {
        if (this.managingOfficerUserID.remove(managingOfficerUserID)) {
        } else {
            throw new NoSuchElementException("Managing Officer ID '" + managingOfficerUserID + "' not found.");
        }
    }

    /**
     * Prints all details of the BTO project.
     */
    public void printAll() {
        System.out.println("--BTO Project Details--");
        System.out.println("Project ID: " + this.projectID);
        System.out.println("Project Name: " + this.projectName);
        System.out.println("Neighborhood: " + this.neighborhood);
        System.out.println("2-Room Flats Count: " + this.twoRoomCount);
        System.out.println("3-Room Flats Count: " + this.threeRoomCount);
        System.out.println("Opening Date: " + this.openingDate);
        System.out.println("Closing Date: " + this.closingDate);
        System.out.println("Manager ID: " + (this.managerUserID));
        System.out.println("Visibility: " + (this.isVisibility ? "Visible" : "Not Visible"));
        System.out.println("Max Managing Officers: " + this.maxManagingOfficer);
        System.out.println("Managing Officer IDs: " + (this.managingOfficerUserID.isEmpty() ? "None" : this.managingOfficerUserID));
        System.out.println("---------------------");
    }
}
