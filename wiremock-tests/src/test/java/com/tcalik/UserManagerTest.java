package com.tcalik;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserManagerTest {

    @Mock
    private UserService userService;

    @Test
    void shouldReturnUserDisplayName() throws IOException, InterruptedException {
        when(userService.getUserObjectById(1))
                .thenReturn(new User(1, "Tomasz"));

        UserManager userManager = new UserManager(userService);

        String result = userManager.getUserDisplayName(1);

        assertThat(result).isEqualTo("User: Tomasz");

        verify(userService).getUserObjectById(1);
    }
}