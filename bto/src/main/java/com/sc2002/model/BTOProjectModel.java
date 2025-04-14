package com.sc2002.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import com.sc2002.enums.FlatType;
import com.sc2002.enums.Neighborhood;

/**
 * Represents a BTO (Build-To-Order) project with details such as project name,
 * neighborhood, flat counts, application dates, and managing officers.
 */
public class BTOProjectModel {

    /**
     * The next ID of the project. Auto increments everytime a project is
     * created.
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
    private Neighborhood neighborhood;

    /**
     * The number of 2-room flats available.
     */
    private int twoRoomCount;
    /**
     * The price of 2-room flat.
     */
    private int twoRoomPrice;

    /**
     * The number of 3-room flats available.
     */
    private int threeRoomCount;

    /**
     * The price of 3-room flat.
     */
    private int threeRoomPrice;
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
    private boolean isVisible;

    /**
     * The maximum number of managing officers allowed.
     */
    private int maxManagingOfficer;

    /**
     * The list of managing officer object who are assigned to the project.
     */
    private ArrayList<User> managingOfficerUsers;

    /**
     * Constructs a new BTOProjectModel with the specified details.
     *
     * @param projectID The unique ID of the project.
     * @param projectName The name of the project.
     * @param neighborhood The neighborhood where the project is located.
     * @param twoRoomCount The number of 2-room flats available.
     * @param twoRoomPrice The price for 1 2-room flat.
     * @param threeRoomCount The number of 3-room flats available.
     * @param threeRoomPrice The price for 1 3-room flat.
     * @param openingDate The application opening date.
     * @param closingDate The application closing date.
     * @param maxManagingOfficer The maximum number of managing officers
     * allowed.
     */
    public BTOProjectModel(String projectName, Neighborhood neighborhood, int twoRoomCount, int twoRoomPrice, int threeRoomCount, int threeRoomPrice, LocalDate openingDate, LocalDate closingDate, int maxManagingOfficer, int managerUserID) {

        this.managingOfficerUsers = new ArrayList<>();
        this.projectID = this.nextprojectID;
        this.nextprojectID += 1;
        this.projectName = projectName;
        this.neighborhood = neighborhood;
        this.twoRoomCount = twoRoomCount;
        this.twoRoomPrice = twoRoomPrice;
        this.threeRoomCount = threeRoomCount;
        this.threeRoomPrice = threeRoomPrice;
        this.openingDate = openingDate;
        this.closingDate = closingDate;
        this.maxManagingOfficer = maxManagingOfficer;
        this.managerUserID = managerUserID;

        this.isVisible = true; // default is visible
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
    public Neighborhood getNeighborhood() {
        return neighborhood;
    }

    /**
     * Sets the neighborhood where the project is located.
     *
     * @param neighborhood The neighborhood to set.
     */
    public void setNeighborhood(Neighborhood neighborhood) {
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
     * Gets the isVisible status of the project.
     *
     * @return True if the project is visible, false otherwise.
     */
    public boolean isVisible() {
        return this.isVisible;
    }

    /**
     * Sets the isVisible status of the project.
     *
     * @param isVisible The visible status to set.
     */
    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
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
    public ArrayList<User> getManagingOfficerUsers() {
        return this.managingOfficerUsers;
    }

    /**
     * Adds a managing officer User to the list. Returns Boolean base on
     * success.
     *
     * @param managingOfficerUser The User object of the managing officer to
     * add.
     */
    public boolean addManagingOfficerUser(User managingOfficerUser) {
        if (this.managingOfficerUsers.size() >= this.maxManagingOfficer) {
            return false;
        }
        this.managingOfficerUsers.add(managingOfficerUser);
        return true;
    }

    /**
     * Removes a managing officer User object from the list.
     *
     * @param currentUser The User object of the managing officer to remove.
     * @throws NoSuchElementException If the User object is not found in the
     * list.
     */
    public boolean removeManagingOfficerUser(User currentUser) {
        if (this.managingOfficerUsers.remove(currentUser)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks if a given User is in the list of managing officers.
     *
     * @param user The User object to check.
     * @return True if the User is in the list, false otherwise.
     */
    public boolean isManagingOfficer(User currentUser) {
        return this.managingOfficerUsers.contains(currentUser);
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
        System.out.println("2-Room Flat Price: $" + this.twoRoomPrice);
        System.out.println("3-Room Flats Count: " + this.threeRoomCount);
        System.out.println("3-Room Flat Price: $" + this.threeRoomPrice);
        System.out.println("Opening Date: " + this.openingDate.format(DateTimeFormatter.ofPattern("dd/MMM/yyyy")));
        System.out.println("Closing Date: " + this.closingDate);
        System.out.println("Manager ID: " + (this.managerUserID));
        System.out.println("Visibility: " + (this.isVisible ? "Visible" : "Not Visible"));
        System.out.println("Max Managing Officers: " + this.maxManagingOfficer);
        System.out.println("Managing Officers: "
                + String.join(", ", managingOfficerUsers.stream()
                        .map(User::getName)
                        .toList()));
        System.out.println("---------------------");
    }

    public List<FlatType> getAvailableFlatTypes() {
        List<FlatType> availableFlatTypes = new ArrayList<>();
        if (this.twoRoomCount > 0) {
            availableFlatTypes.add(FlatType.TWO_ROOM);
        }
        if (this.threeRoomCount > 0) {
            availableFlatTypes.add(FlatType.THREE_ROOM);
        }
        return availableFlatTypes;
    }

}
