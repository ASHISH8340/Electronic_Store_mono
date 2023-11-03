package com.electronicstore.exceptions;

public class IdNotFoundException extends RuntimeException {

    private String message;

    public IdNotFoundException(String message) {
        super();
        this.message = message;
    }

    public String getMessage() {

        return message;
    }
}
