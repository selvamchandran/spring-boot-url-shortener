package com.selvam.urlshortener.domain.services;

import com.selvam.urlshortener.domain.entities.User;
import com.selvam.urlshortener.domain.models.CreateUserCmd;
import com.selvam.urlshortener.domain.models.Role;
import com.selvam.urlshortener.domain.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.print.attribute.standard.OutputDeviceAssigned;
import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void createUser(CreateUserCmd cmd){
        if(userRepository.existsByEmail(cmd.email())){
            throw new RuntimeException("Email already exists");
        }
        var user = new User();
        user.setEmail(cmd.email());
        user.setPassword(passwordEncoder.encode(cmd.password()));
        user.setName(cmd.name());
        user.setRole(Role.ROLE_USER);
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);

    }
}
