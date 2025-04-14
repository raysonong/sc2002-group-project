package com.sc2002.repositories;

import java.util.ArrayList;
import java.util.List;

import com.sc2002.model.UserModel;
import com.sc2002.interfaces.RepoInterface; // Ensure this is the correct package for RepoInterface

public class UserRepo implements RepoInterface<UserModel, Integer> {
    private List<UserModel> users;
    
    public UserRepo() {
        this.users = new ArrayList<>();
    }

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

    @Override
    public List<UserModel> findAll() {
        return new ArrayList<>(users);
    }

    @Override
    public UserModel findByID(Integer userID) {
        return getUserByUserID(userID);
    }

    @Override
    public boolean delete(Integer userID) {
        UserModel user = getUserByUserID(userID);
        if (user != null) {
            users.remove(user);
            return true;
        }
        return false;
    }
    
    public UserModel getUserByNRIC(String nric) {
        for (UserModel user : users) {
            if (user.getNRIC().equals(nric)) {
                return user;
            }
        }
        return null;
    }

    public UserModel getUserByName(String name) {
        for (UserModel user : users) {
            if (user.getName().equals(name)) {
                return user;
            }
        }
        return null;
    }

    public UserModel getUserByUserID(int UserID) {
        for (UserModel user : users) {
            if (user.getUserID() == UserID) {
                return user;
            }
        }
        return null;
    }

}
