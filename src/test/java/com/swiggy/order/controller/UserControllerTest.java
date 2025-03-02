package com.swiggy.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swiggy.order.dto.UserDto;
import com.swiggy.order.exceptions.GlobalExceptionHandler;
import com.swiggy.order.exceptions.UserAlreadyExistsException;
import com.swiggy.order.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest {

    public static final String USER_REGISTER_URL = "/users";

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private UserDto userDto;

    @BeforeEach
    void setUp() {
        userDto = UserDto.builder().username("username").password("password").build();
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new GlobalExceptionHandler()).build();
    }

    @Test
    public void testRegisterUserSuccessfullyWhenValidInputFromUser() throws Exception {
        doNothing().when(userService).register(userDto);

        mockMvc.perform(post(USER_REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isCreated())
                .andExpect(content().string("User registered successfully"));
    }

    @Test
    public void testRegisterUserFailureWhenSameUserRegistersAgain() throws Exception {
        doThrow(new UserAlreadyExistsException("User already exists", HttpStatus.CONFLICT)).when(userService).register(userDto);

        mockMvc.perform(post(USER_REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isConflict())
                .andExpect(content().string("User already exists"));
    }
}
