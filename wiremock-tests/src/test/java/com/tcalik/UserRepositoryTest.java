package com.tcalik;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled("Requires compatible Docker/Testcontainers environment")
@Testcontainers
class UserRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16-alpine");

    @Test
    void shouldSaveAndReadUserFromDatabase() throws Exception {
        UserRepository repository = new UserRepository(
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword()
        );

        repository.createTable();

        User user = User.builder()
                .id(1)
                .name("Tomasz")
                .build();

        repository.save(user);

        User foundUser = repository.findById(1);

        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getId()).isEqualTo(1);
        assertThat(foundUser.getName()).isEqualTo("Tomasz");
    }

}