package com.uxpsystems.assignment.service;

import com.uxpsystems.assignment.dao.User;

import java.util.List;

public interface UserService {
    List<User> getUsers();
    User getUser(long id);
    User deleteUser(long id);
    User createUser(User user);
    User updateUser(User user);
}
