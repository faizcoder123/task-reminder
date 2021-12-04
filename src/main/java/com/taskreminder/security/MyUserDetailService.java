package com.taskreminder.security;


import com.taskreminder.entities.UserEntity;
import com.taskreminder.handler.ApiRequestException;
import com.taskreminder.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, ApiRequestException {

        final UserEntity customer = userService.getUser(username);
        if (customer == null) {
            throw new UsernameNotFoundException(username);
        }
        return User.withUsername(customer.getEmail()).password(customer.getPassword()).authorities("USER").build();
    }
}