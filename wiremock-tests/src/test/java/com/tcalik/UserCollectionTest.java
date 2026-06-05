package com.tcalik;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Map;

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

    @Test
    void shouldMapUsersToNames() {
        List<User> users = List.of(
                new User(1, "Tomasz"),
                new User(2, "Anna"),
                new User(3, "Jan")
        );

        List<String> names = users.stream()
                .map(User::getName)
                .collect(Collectors.toList());

        assertThat(names)
                .containsExactly("Tomasz", "Anna", "Jan");
    }

    @Test
    void shouldSortUsersByName() {
        List<User> users = List.of(
                new User(1, "Tomasz"),
                new User(2, "Anna"),
                new User(3, "Jan")
        );

        List<String> names = users.stream()
                .map(User::getName)
                .sorted()
                .collect(Collectors.toList());

        assertThat(names)
                .containsExactly("Anna", "Jan", "Tomasz");
    }

    @Test
    void shouldReturnDefaultUserWhenOptionalIsEmpty() {
        Optional<User> result = Optional.empty();

        User user = result.orElse(new User(0, "Default User"));

        assertThat(user.getName())
                .isEqualTo("Default User");
    }

    @Test
    void shouldThrowExceptionWhenOptionalIsEmpty() {
        Optional<User> result = Optional.empty();

        assertThatThrownBy(() ->
                result.orElseThrow(() -> new UserNotFoundException(999)))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User with id 999 was not found");
    }

    @Test
    void shouldGroupUsersByFirstLetter() {
        List<User> users = List.of(
                new User(1, "Tomasz"),
                new User(2, "Anna"),
                new User(3, "Adam")
        );

        Map<Character, List<User>> result = users.stream()
                .collect(Collectors.groupingBy(user -> user.getName().charAt(0)));

        assertThat(result.get('A')).hasSize(2);
        assertThat(result.get('T')).hasSize(1);
    }

    @Test
    void shouldPartitionUsersByIdGreaterThanOne() {
        List<User> users = List.of(
                new User(1, "Tomasz"),
                new User(2, "Anna"),
                new User(3, "Jan")
        );

        Map<Boolean, List<User>> result = users.stream()
                .collect(Collectors.partitioningBy(user -> user.getId() > 1));

        assertThat(result.get(true)).hasSize(2);
        assertThat(result.get(false)).hasSize(1);
    }
}