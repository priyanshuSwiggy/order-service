package com.swiggy.order.exceptions;

import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends RuntimeException {
  public UserAlreadyExistsException(String message, HttpStatus status) {
    super(message);
  }
}
