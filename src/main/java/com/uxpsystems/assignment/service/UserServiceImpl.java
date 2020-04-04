package com.uxpsystems.assignment.service;

import com.uxpsystems.assignment.dao.User;
import com.uxpsystems.assignment.exception.NotFoundException;
import com.uxpsystems.assignment.exception.RequestNotAllowedException;
import com.uxpsystems.assignment.exception.RequestNotValidException;
import com.uxpsystems.assignment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    static final List<String> STATUS_VALUES = Arrays.asList("ACTIVATED","DEACTIVATED");

    @Override
    @Secured({"ROLE_ADMIN","ROLE_USER"})
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    @Secured({"ROLE_ADMIN","ROLE_USER"})
    public User getUser(long id) {
        Optional<User> byId = userRepository.findById(id);
        if(!byId.isPresent()){
            throw new NotFoundException(String.format("User with id %d not found",id));
        }
       return byId.get();
    }

    @Override
    @Secured({"ROLE_ADMIN"})
    public User deleteUser(long id) {
        if(id == 0){
            throw  new RequestNotValidException("User detail missing");
        }
        User user = getUser(id);
        userRepository.delete(user);
        return user;
    }

    @Override
    @Secured({"ROLE_ADMIN"})
    public User createUser(User user) {
        validateRequest(user);
        if(!isUsernameAvailable(user)){
            throw  new RequestNotAllowedException("Username not available");
        }
        return userRepository.save(user);
    }

    @Override
    @Secured({"ROLE_ADMIN"})
    public User updateUser(User user) {
        if(null == user.getId() ||  user.getId() == 0L){
            throw new RequestNotValidException("User detail missing");
        }
        validateRequest(user);

        User regUser = getUser(user.getId());
        if(!regUser.getUsername().equals(user.getUsername())){
            throw new RequestNotValidException("To username change not allowed.");
        }

        return userRepository.save(user);
    }

    private boolean isUsernameAvailable(User user) {
        User existingUser = userRepository.findByUsername(user.getUsername());
        if(existingUser != null){
            return false;
        }
        return true;
    }

    private void validateRequest(User user){
        if(user.getUsername()==null || user.getUsername().isEmpty()){
            throw  new RequestNotValidException("User username should not be empty");
        }

        if(user.getPassword()==null || user.getPassword().isEmpty()){
            throw  new RequestNotValidException("User password should not be empty");
        }

        if(user.getStatus()==null || user.getStatus().isEmpty() || !STATUS_VALUES.contains(user.getStatus().toUpperCase())){
            throw  new RequestNotValidException("User status only permit Activated/Deactivated values");
        }
    }
}
