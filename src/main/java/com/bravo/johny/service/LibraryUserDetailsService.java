package com.bravo.johny.service;

import com.bravo.johny.dto.LibraryUserDetails;
import com.bravo.johny.entity.UserEntity;
import com.bravo.johny.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class LibraryUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    public LibraryUserDetailsService() {
        super();
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        Optional<UserEntity> userEntity = userRepository.findByUserName(userName);

        userEntity.orElseThrow(() -> new UsernameNotFoundException("User with Username : "+userName+" was not found !!"));

        System.out.println(this +" load user by user name");

        return userEntity.map(LibraryUserDetails::new).get();
    }
}