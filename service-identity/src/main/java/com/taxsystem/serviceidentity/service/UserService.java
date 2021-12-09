package com.taxsystem.serviceidentity.service;

import com.taxsystem.serviceidentity.model.enums.UserType;
import com.taxsystem.serviceidentity.model.User;

import java.util.List;

public interface UserService {
    List<User> getAll();
    User getUserById(long id) throws IllegalArgumentException;
    long createUser(String name, String password, String passportId, UserType userType);
    void updateUser(long id, String name, String password, String passportId, UserType userType)
            throws IllegalArgumentException;
    void deleteUser(long id);
}
