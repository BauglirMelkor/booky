package com.booky.security.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.booky.entity.BookyUser;
import com.booky.repository.BookyUserRepository;
import com.booky.security.JwtUserFactory;


@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private BookyUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<BookyUser> user = userRepository.findOneByEmail(username);
        if (!user.isPresent()) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        } else {
            return JwtUserFactory.create(user.get());
        }
    }
}