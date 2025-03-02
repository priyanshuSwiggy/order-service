package com.swiggy.order.service;

import com.swiggy.order.dto.UserDto;
import com.swiggy.order.entity.User;
import com.swiggy.order.exceptions.UserAlreadyExistsException;
import com.swiggy.order.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    public void setup() {
        userRepository = mock(UserRepository.class);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        userService = new UserService(userRepository, passwordEncoder);
    }

    @Test
    public void testRegisterNewUserSuccessfullyRegisters() {
        final UserDto userDto = UserDto.builder().username("username").password("password").build();
        final User user = User.builder().username("username").password("password").build();
        when(userRepository.findByUsername("username")).thenReturn(Optional.empty());

        userService.register(userDto);

        verify(userRepository, times(1)).findByUsername("username");
        verify(userRepository, times(1)).save(argThat(savedUser ->
                savedUser.getUsername().equals("username") &&
                        new BCryptPasswordEncoder().matches("password", savedUser.getPassword())
        ));
    }

    @Test
    public void testRegisterUserThrowsExceptionWhenUserAlreadyExists() {
        final UserDto userDto = UserDto.builder().username("username").password("password").build();
        final User user = User.builder().username("username").password("password").build();
        when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));

        assertThrows(UserAlreadyExistsException.class, () -> userService.register(userDto));

        verify(userRepository, times(1)).findByUsername("username");
        verify(userRepository, never()).save(user);
    }
}
