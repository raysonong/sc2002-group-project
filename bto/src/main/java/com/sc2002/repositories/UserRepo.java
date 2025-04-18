package com.sc2002.repositories;

import java.util.ArrayList;
import java.util.List;

import com.sc2002.model.UserModel;
import com.sc2002.interfaces.RepoInterface; // Ensure this is the correct package for RepoInterface

/**
 * Manages the storage and retrieval of user data (UserModel and its subclasses).
 * Implements the RepoInterface for standard repository operations and adds user-specific finders.
 */
public class UserRepo implements RepoInterface<UserModel, Integer> {
    /** In-memory list to store all user instances. */
    private List<UserModel> users;
    
    /**
     * Constructs a UserRepo, initializing an empty list to hold users.
     */
    public UserRepo() {
        this.users = new ArrayList<>();
    }

    /**
     * Saves a new user to the repository.
     * Checks for existing users with the same NRIC or User ID before adding.
     *
     * @param user The UserModel object to save.
     * @throws IllegalArgumentException If a user with the same NRIC or User ID already exists.
     */
    @Override
    public void save(UserModel user) throws IllegalArgumentException {
        // Check if the user already exists
        if (getUserByNRIC(user.getNRIC()) == null) {
            if (getUserByUserID(user.getUserID()) == null) {
                users.add(user);
            } else {
                throw new IllegalArgumentException("A User with ID: " + user.getUserID() + " already exists.");
            }
        } else {
            throw new IllegalArgumentException("User with NRIC " + user.getNRIC() + " already exists.");
        }
    }

    /**
     * Retrieves a list of all users currently stored in the repository.
     *
     * @return A new ArrayList containing all UserModel objects.
     */
    @Override
    public List<UserModel> findAll() {
        return new ArrayList<>(users);
    }

    /**
     * Finds a user by their unique User ID.
     * Delegates to the getUserByUserID method.
     *
     * @param userID The ID of the user to find.
     * @return The found UserModel object, or null if no user matches the ID.
     */
    @Override
    public UserModel findByID(Integer userID) {
        return getUserByUserID(userID);
    }

    /**
     * Deletes a user from the repository based on their unique User ID.
     *
     * @param userID The ID of the user to delete.
     * @return True if the user was found and deleted, false otherwise.
     */
    @Override
    public boolean delete(Integer userID) {
        UserModel user = getUserByUserID(userID);
        if (user != null) {
            users.remove(user);
            return true;
        }
        return false;
    }
    
    /**
     * Finds a user by their NRIC number.
     * Iterates through the list to find a match.
     *
     * @param nric The NRIC number of the user to find.
     * @return The found UserModel object, or null if no user matches the NRIC.
     */
    public UserModel getUserByNRIC(String nric) {
        for (UserModel user : users) {
            if (user.getNRIC().equals(nric)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Finds a user by their full name.
     * Iterates through the list to find the first match. Note: Names may not be unique.
     *
     * @param name The full name of the user to find.
     * @return The first found UserModel object with the matching name, or null if no user matches.
     */
    public UserModel getUserByName(String name) {
        for (UserModel user : users) {
            if (user.getName().equals(name)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Finds a user by their unique User ID.
     * Iterates through the list to find a match.
     *
     * @param UserID The ID of the user to find.
     * @return The found UserModel object, or null if no user matches the ID.
     */
    public UserModel getUserByUserID(int UserID) {
        for (UserModel user : users) {
            if (user.getUserID() == UserID) {
                return user;
            }
        }
        return null;
    }

}
