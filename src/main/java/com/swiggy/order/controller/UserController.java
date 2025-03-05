package com.swiggy.order.controller;

import com.swiggy.order.dto.UserDto;
import com.swiggy.order.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Tag(name = "UserController", description = "APIs for managing users")
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Create a new user",
            description = "Create a new user")
    @Parameter(name = "userRequestDto", description = "The DTO containing information for creating a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Successfully created a new user",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Invalid input for creating a user")
    })
    @PostMapping
    public ResponseEntity<String> registerClient(@Valid @RequestBody UserDto userDto) {
        userService.register(userDto);
        return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
    }
}
