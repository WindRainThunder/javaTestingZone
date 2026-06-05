package com.tcalik;

public class MockUserService implements UserService {

    @Override
    public User getUserObjectById(int id) {
        return new User(999, "Mock User");
    }
}