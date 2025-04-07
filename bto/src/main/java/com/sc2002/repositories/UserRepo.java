package com.sc2002.repositories;

import java.util.ArrayList;
import java.util.List;

import com.sc2002.model.User;



public class UserRepo {
    private List<User> users;
    
    public UserRepo() {
        this.users = new ArrayList<>();
    }

    public User getUserByNRIC(String nric) {
        for (User user : users) {
            if (user.getNRIC().equals(nric)) {
                return user;
            }
        }
        return null;
    }

    public User getUserByName(String name) {
        for (User user : users) {
            if (user.getName().equals(name)) {
                return user;
            }
        }
        return null;
    }
    public User getUserByUserID(int UserID){
        for (User user : users) {
            if (user.getUserID() == UserID) {
                return user;
            }
        }
        return null;
    }
    public void addUser(User user) throws IllegalArgumentException {
        // Check if the user already exists
        if(getUserByNRIC(user.getNRIC())==null){
            if(getUserByUserID(user.getUserID())==null){
                users.add(user);
            }else{
                throw new IllegalArgumentException("A User with ID: " + user.getUserID() + " already exists.");
            }
        }else{
            throw new IllegalArgumentException("User with NRIC " + user.getNRIC() + " already exists.");
        }
    }

    public void removeUser(User user) {
        users.remove(user);
    }



}
