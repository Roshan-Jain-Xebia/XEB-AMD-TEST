package com.uxpsystems.assignment.service;

import com.uxpsystems.assignment.dao.User;
import com.uxpsystems.assignment.exception.NotFoundException;
import com.uxpsystems.assignment.exception.RequestNotAllowedException;
import com.uxpsystems.assignment.exception.RequestNotValidException;
import com.uxpsystems.assignment.repository.UserRepository;
import org.hibernate.annotations.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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
    //@Cache()
    //@Cacheable(value = "usersCache",key = "#id",unless = "#result==null")
    public User getUser(long id) {
        Optional<User> byId = userRepository.findById(id);
        if(!byId.isPresent()){
            throw new NotFoundException(String.format("User with id %d not found",id));
        }
       return byId.get();
    }

    @Override
    @Secured({"ROLE_ADMIN"})
    //@CacheEvict(value = "usersCache",key = "#id")
    public User deleteUser(long id) {
        if(isNullOrZero(id)){
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
    //@CachePut(value = "usersCache",key = "#id")
    public User updateUser(User user) {
        if(isNullOrZero(user.getId())){
            throw new RequestNotValidException("User detail missing");
        }
        validateRequest(user);

        User regUser = getUser(user.getId());
        if(!regUser.getUsername().equals(user.getUsername())){
            throw new RequestNotValidException("Username change not allowed.");
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
        if(isNullOrEmpty(user.getUsername())){
            throw  new RequestNotValidException("User username should not be empty");
        }

        if(isNullOrEmpty(user.getPassword())){
            throw  new RequestNotValidException("User password should not be empty");
        }

        if(isNullOrEmpty(user.getStatus()) || !STATUS_VALUES.contains(user.getStatus().toUpperCase())){
            throw  new RequestNotValidException("User status only permit Activated/Deactivated values");
        }
    }

    private boolean isNullOrEmpty(String strValue){
        return strValue == null ? true : strValue.isEmpty();
    }

    private boolean isNullOrZero(Long longValue){
        return longValue == null ? true : longValue == 0;
    }
}
