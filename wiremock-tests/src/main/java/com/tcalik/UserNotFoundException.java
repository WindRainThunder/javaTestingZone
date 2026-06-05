package com.tcalik;

public class UserNotFoundException extends ApiException {

    public UserNotFoundException(int id) {
        super("User with id " + id + " was not found");
    }
}