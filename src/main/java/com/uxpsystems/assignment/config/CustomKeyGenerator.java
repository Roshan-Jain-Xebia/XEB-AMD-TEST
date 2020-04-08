package com.uxpsystems.assignment.config;

import com.uxpsystems.assignment.dao.User;
import com.uxpsystems.assignment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;

@Component
public class CustomKeyGenerator implements KeyGenerator {
    @Autowired
    UserRepository userRepository;

    @Override
    public Object generate(Object o, Method method, Object... params) {
        return getNewKey();
    }

    private Long getNewKey(){
        List<User> users = userRepository.findAll();
        if(users.isEmpty()) return new Long(0L);
        return users.stream().mapToLong(User::getId).max().orElse(1);
    }
}
