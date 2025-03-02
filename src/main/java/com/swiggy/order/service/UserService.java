package com.swiggy.order.service;

import com.swiggy.order.dto.UserDto;
import com.swiggy.order.entity.User;
import com.swiggy.order.exceptions.UserAlreadyExistsException;
import com.swiggy.order.exceptions.UserNotFoundException;
import com.swiggy.order.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(UserDto userDto) {
        String username = userDto.getUsername();
        String password = userDto.getPassword();
        User user = User.builder().username(username).password(password).build();
        userRepository.findByUsername(username).ifPresent(u -> {
            throw new UserAlreadyExistsException("User already exists", HttpStatus.CONFLICT);
        });
        user.encodePassword(passwordEncoder);
        userRepository.save(user);
    }

    public User fetchUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found", HttpStatus.NOT_FOUND));
    }
}
