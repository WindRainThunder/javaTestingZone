package com.tcalik;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class UserManager {

    private final UserService userService;

    public UserManager(UserService userService) {
        this.userService = userService;
    }

    public String getUserDisplayName(int id) throws IOException, InterruptedException {
        User user = userService.getUserObjectById(id);

        return "User: " + user.getName();
    }

    public String getDefaultDisplayName() {
        return "Unknown User";
    }

    public Optional<User> findUserById(
            List<User> users,
            int id
    ) {
        return users.stream()
                .filter(user -> user.getId() == id)
                .findFirst();
    }
}