package com.swiggy.order.exceptions;

import org.springframework.http.HttpStatus;

public class MenuItemNotFoundException extends RuntimeException {
  public MenuItemNotFoundException(String message, HttpStatus status) {
    super(message);
  }
}
