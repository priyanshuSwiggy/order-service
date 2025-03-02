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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new GlobalExceptionHandler()).build();
    }

    @Test
    public void testRegisterUserSuccessfullyWhenValidInputFromUser() throws Exception {
        UserDto userDto = UserDto.builder().username("username").password("password").build();
        doNothing().when(userService).register(userDto);

        mockMvc.perform(post(USER_REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isCreated())
                .andExpect(content().string("User registered successfully"));
    }

    @Test
    public void testRegisterUserFailureWhenSameUserRegistersAgain() throws Exception {
        UserDto userDto = UserDto.builder().username("username").password("password").build();
        doThrow(new UserAlreadyExistsException("User already exists", HttpStatus.CONFLICT)).when(userService).register(userDto);

        mockMvc.perform(post(USER_REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isConflict())
                .andExpect(content().string("User already exists"));
    }

    @Test
    public void testRegisterUserFailureWhenUsernameIsNull() throws Exception {
        UserDto userDto = UserDto.builder().username(null).password("password").build();

        mockMvc.perform(post(USER_REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"username\":\"Username cannot be null or empty\"}"));
    }

    @Test
    public void testRegisterUserFailureWhenPasswordIsNull() throws Exception {
        UserDto userDto = UserDto.builder().username("username").password(null).build();

        mockMvc.perform(post(USER_REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"password\":\"Password cannot be null or empty\"}"));
    }

    @Test
    public void testRegisterUserFailureWhenUsernameIsEmpty() throws Exception {
        UserDto userDto = UserDto.builder().username("").password("password").build();

        mockMvc.perform(post(USER_REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"username\":\"Username cannot be null or empty\"}"));
    }

    @Test
    public void testRegisterUserFailureWhenPasswordIsEmpty() throws Exception {
        UserDto userDto = UserDto.builder().username("username").password("").build();

        mockMvc.perform(post(USER_REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"password\":\"Password cannot be null or empty\"}"));
    }

    @Test
    public void testRegisterUserFailureWhenUsernameIsTooLong() throws Exception {
        String longUsername = "a".repeat(256);
        UserDto userDto = UserDto.builder().username(longUsername).password("password").build();

        mockMvc.perform(post(USER_REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"username\":\"Username cannot be longer than 255 characters\"}"));
    }

    @Test
    public void testRegisterUserFailureWhenPasswordIsTooLong() throws Exception {
        String longPassword = "a".repeat(256);
        UserDto userDto = UserDto.builder().username("username").password(longPassword).build();

        mockMvc.perform(post(USER_REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"password\":\"Password cannot be longer than 255 characters\"}"));
    }
}