package com.uxpsystems.assignment.service;

import com.uxpsystems.assignment.dao.User;
import com.uxpsystems.assignment.exception.NotFoundException;
import com.uxpsystems.assignment.exception.RequestNotAllowedException;
import com.uxpsystems.assignment.exception.RequestNotValidException;
import com.uxpsystems.assignment.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    static final List<String> STATUS_VALUES = Arrays.asList("ACTIVATED","DEACTIVATED");

    @Override
    @Secured({"ROLE_ADMIN","ROLE_USER"})
    public List<User> getUsers() {
        LOGGER.debug("Get All Users");
        return userRepository.findAll();
    }

    @Override
    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @Cacheable(value = "user",key = "#id")
    public User getUser(long id) {
        LOGGER.debug("Get User for {}", id);
        Optional<User> byId = userRepository.findById(id);
        if(!byId.isPresent()){
            LOGGER.error("Requested user is not available {} ", id);
            throw new NotFoundException(String.format("User with id %d not found",id));
        }
       return byId.get();
    }

    @Override
    @Secured({"ROLE_ADMIN"})
    @CacheEvict(value = "user",key = "#id")
    public User deleteUser(long id) {
        LOGGER.info("Delete User for {}", id);
        if(isNullOrZero(id)){
            LOGGER.error("Delete user request must required user id ");
            throw  new RequestNotValidException("User detail missing");
        }
        User user = getUser(id);
        userRepository.delete(user);
        LOGGER.info("User deleted for {}", id);
        return user;
    }

    @Override
    @Secured({"ROLE_ADMIN"})
    @CachePut(value = "user" , keyGenerator = "userCustomKeyGenerator")
    public User createUser(User user) {
        LOGGER.debug("Create user for {}", user);
        validateRequest(user);
        if(!isUsernameAvailable(user)) {
            LOGGER.error("Create user request should not allowed to use existing registered username {}", user);
            throw new RequestNotAllowedException("Username not available");
        }
        LOGGER.debug("User is created for {}", user);
        return userRepository.save(user);
    }

    @Override
    @Secured({"ROLE_ADMIN"})
    @CachePut(value = "user", key = "#user.id", condition="#result != null")
    public User updateUser(User user) {
        LOGGER.debug("Update User for {}", user);
        if(isNullOrZero(user.getId())){
            LOGGER.error("Update user request must required user id {} ", user);
            throw new RequestNotValidException("User detail missing");
        }
        validateRequest(user);
        checkIsUsernameChanged(user);
        return userRepository.save(user);
    }

    private void checkIsUsernameChanged(User user){
        User regUser = getUser(user.getId());
        if(!regUser.getUsername().equals(user.getUsername())){
            LOGGER.error("Update user request should not allowed to change username {} ", user);
            throw new RequestNotValidException("Username change not allowed.");
        }
    }

    private boolean isUsernameAvailable(User user) {
        User existingUser = userRepository.findByUsername(user.getUsername());
        return existingUser == null;
    }

    private void validateRequest(User user){
        if(isNullOrEmpty(user.getUsername())){
            LOGGER.error("Request should not be null or empty username {} ", user);
            throw  new RequestNotValidException("User username should not be empty");
        }

        if(isNullOrEmpty(user.getPassword())){
            LOGGER.error("Request should not be null or empty password {} ", user);
            throw  new RequestNotValidException("User password should not be empty");
        }

        if(isNullOrEmpty(user.getStatus()) || !STATUS_VALUES.contains(user.getStatus().toUpperCase())){
            LOGGER.error("Request should be contain 'Activated/Deactivated' value for status {} ", user);
            throw  new RequestNotValidException("User status only permit Activated/Deactivated values");
        }
    }

    private boolean isNullOrEmpty(String strValue){
        return strValue == null || strValue.isEmpty();
    }

    private boolean isNullOrZero(Long longValue){
        return longValue == null || longValue == 0;
    }
}
