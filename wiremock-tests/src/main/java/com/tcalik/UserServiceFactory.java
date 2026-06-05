package com.tcalik;

public class UserServiceFactory {

    public static UserService createRealService(String baseUrl) {
        return new UserClient(baseUrl);
    }

    public static UserService createMockService() {
        return new MockUserService();
    }
}