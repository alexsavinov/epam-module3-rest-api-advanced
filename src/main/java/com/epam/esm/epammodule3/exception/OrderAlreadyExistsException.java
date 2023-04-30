package com.epam.esm.epammodule3.exception;

public class OrderAlreadyExistsException extends RuntimeException {

    public OrderAlreadyExistsException(String message) {
        super(message);
    }
}
