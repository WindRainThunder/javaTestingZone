package com.tcalik;

import java.io.IOException;

public interface UserService {

    User getUserObjectById(int id)
            throws IOException, InterruptedException;
}