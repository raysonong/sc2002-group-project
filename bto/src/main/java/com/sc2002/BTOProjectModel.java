package com.sc2002;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * Represents a BTO (Build-To-Order) project with details such as project name, neighborhood, 
 * flat counts, application dates, and managing officers.
 */
public class BTOProjectModel {

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
     * The ID of the manager assigned to the project.
     */
    private String managerID;

    /**
     * The visibility status of the project.
     */
    private boolean isVisibility;

    /**
     * The maximum number of managing officers allowed.
     */
    private int maxManagingOfficer;

    /**
     * The list of managing officer IDs assigned to the project.
     */
    private ArrayList<String> managingOfficerID;

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
    public BTOProjectModel(int projectID, String projectName, String neighborhood, int twoRoomCount, int threeRoomCount, LocalDate openingDate, LocalDate closingDate, int maxManagingOfficer, String managerID) {

        managingOfficerID = new ArrayList<>();

        this.projectID = projectID;
        this.projectName = projectName;
        this.neighborhood = neighborhood;
        this.twoRoomCount = twoRoomCount;
        this.threeRoomCount = threeRoomCount;
        this.openingDate = openingDate;
        this.closingDate = closingDate;
        this.maxManagingOfficer = maxManagingOfficer;
        this.managerID = managerID;

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
        return threeRoomCount;
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
        return openingDate;
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
        return closingDate;
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
     * Gets the ID of the manager assigned to the project.
     *
     * @return The manager ID.
     */
    public String getManagerID() {
        return managerID;
    }

    /**
     * Sets the ID of the manager assigned to the project.
     *
     * @param managerID The manager ID to set.
     */
    public void setManagerID(String managerID) {
        this.managerID = managerID;
    }

    /**
     * Gets the visibility status of the project.
     *
     * @return True if the project is visible, false otherwise.
     */
    public boolean isVisibility() {
        return isVisibility;
    }

    /**
     * Sets the visibility status of the project.
     *
     * @param visibility The visibility status to set.
     */
    public void setVisibility(boolean visibility) {
        isVisibility = visibility;
    }

    /**
     * Gets the maximum number of managing officers allowed.
     *
     * @return The maximum number of managing officers.
     */
    public int getMaxManagingOfficer() {
        return maxManagingOfficer;
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
     * Gets the list of managing officer IDs assigned to the project.
     *
     * @return The list of managing officer IDs.
     */
    public ArrayList<String> getManagingOfficerID() {
        return managingOfficerID;
    }

    /**
     * Adds a managing officer ID to the list.
     *
     * @param managingOfficerID The ID of the managing officer to add.
     */
    public void addManagingOfficerID(String managingOfficerID) {
        this.managingOfficerID.add(managingOfficerID);
    }

    /**
     * Removes a managing officer ID from the list.
     *
     * @param managingOfficerID The ID of the managing officer to remove.
     * @throws NoSuchElementException If the ID is not found in the list.
     */
    public void removeManagingOfficerID(String managingOfficerID) {
        if (this.managingOfficerID.remove(managingOfficerID)) {
        } else {
            throw new NoSuchElementException("Managing Officer ID '" + managingOfficerID + "' not found.");
        }
    }

    /**
     * Prints all details of the BTO project.
     */
    public void printAll() {
        System.out.println("BTO Project Details:");
        System.out.println("Project ID: " + projectID);
        System.out.println("Project Name: " + projectName);
        System.out.println("Neighborhood: " + neighborhood);
        System.out.println("2-Room Flats Count: " + twoRoomCount);
        System.out.println("3-Room Flats Count: " + threeRoomCount);
        System.out.println("Opening Date: " + openingDate);
        System.out.println("Closing Date: " + closingDate);
        System.out.println("Manager ID: " + (managerID));
        System.out.println("Visibility: " + (isVisibility ? "Visible" : "Not Visible"));
        System.out.println("Max Managing Officers: " + maxManagingOfficer);
        System.out.println("Managing Officer IDs: " + (managingOfficerID.isEmpty() ? "None" : managingOfficerID));
    }
}
