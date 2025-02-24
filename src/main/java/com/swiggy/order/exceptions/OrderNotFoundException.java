package com.swiggy.order.exceptions;

import org.springframework.http.HttpStatus;

public class OrderNotFoundException extends RuntimeException {
  public OrderNotFoundException(String message, HttpStatus status) {
    super(message);
  }
}
