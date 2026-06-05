package com.tcalik;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    @Test
    void shouldThrowExceptionFromMock() throws IOException, InterruptedException {

        when(userService.getUserObjectById(999))
                .thenThrow(new UserNotFoundException(999));
        UserManager userManager =
                new UserManager(userService);
        assertThatThrownBy(() ->
                userManager.getUserDisplayName(999))
                .isInstanceOf(UserNotFoundException.class);
        verify(userService)
                .getUserObjectById(999);
    }

    @Test
    void shouldVerifyMethodCalledExactlyTwice()
            throws IOException, InterruptedException {

        when(userService.getUserObjectById(anyInt()))
                .thenReturn(new User(1, "Tomasz"));

        UserManager userManager =
                new UserManager(userService);

        userManager.getUserDisplayName(1);
        userManager.getUserDisplayName(2);

        verify(userService, times(2))
                .getUserObjectById(anyInt());
    }

    @Test
    void shouldNotCallUserService() {

        UserManager userManager =
                new UserManager(userService);
        String result =
                userManager.getDefaultDisplayName();
        assertThat(result)
                .isEqualTo("Unknown User");
        verifyNoInteractions(userService);
    }


}