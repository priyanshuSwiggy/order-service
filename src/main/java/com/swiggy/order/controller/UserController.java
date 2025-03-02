package com.swiggy.order.controller;

import com.swiggy.order.dto.UserDto;
import com.swiggy.order.service.UserService;
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
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<String> registerClient(@Valid @RequestBody UserDto userDto) {
        userService.register(userDto);
        return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
    }
}
