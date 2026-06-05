package com.tcalik;

import java.io.IOException;

public class UserManager {

    private final UserService userService;

    public UserManager(UserService userService) {
        this.userService = userService;
    }

    public String getUserDisplayName(int id) throws IOException, InterruptedException {
        User user = userService.getUserObjectById(id);

        return "User: " + user.getName();
    }
}