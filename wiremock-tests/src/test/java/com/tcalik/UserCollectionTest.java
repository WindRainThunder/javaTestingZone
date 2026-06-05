package com.tcalik;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class UserCollectionTest {

    @Test
    void shouldFindActiveUsers() {

        List<User> users = List.of(
                new User(1, "Tomasz"),
                new User(2, "Anna"),
                new User(3, "Jan")
        );

        assertThat(users)
                .hasSize(3);
    }

    @Test
    void shouldReturnOnlyUsersWithIdGreaterThanOne() {

        List<User> users = List.of(
                new User(1, "Tomasz"),
                new User(2, "Anna"),
                new User(3, "Jan")
        );

        List<User> result = users.stream()
                .filter(user -> user.getId() > 1)
                .collect(Collectors.toList());

        assertThat(result)
                .hasSize(2);
    }

    @Test
    void shouldReturnUserWhenFound() {

        UserManager manager =
                new UserManager(new MockUserService());

        List<User> users = List.of(
                new User(1, "Tomasz"),
                new User(2, "Anna")
        );

        Optional<User> result =
                manager.findUserById(users, 1);

        assertThat(result).isPresent();
        assertThat(result.get().getName())
                .isEqualTo("Tomasz");
    }
}