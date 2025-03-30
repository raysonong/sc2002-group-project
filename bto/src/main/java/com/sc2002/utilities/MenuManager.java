package com.sc2002.utilities;

import java.util.List;

import com.sc2002.model.User;

public class MenuManager {

    public static List<String> getMenuForUser(User user) {
        return user.getMenuOptions();
    }
}