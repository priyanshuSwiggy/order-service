package com.swiggy.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserDto {

    @NotBlank(message = "Username cannot be null or empty")
    @Size(max = 255, message = "Username cannot be longer than 255 characters")
    private String username;

    @NotBlank(message = "Password cannot be null or empty")
    @Size(max = 255, message = "Password cannot be longer than 255 characters")
    private String password;
}
