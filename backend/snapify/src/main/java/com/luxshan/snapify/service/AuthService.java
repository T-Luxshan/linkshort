package com.luxshan.snapify.service;

import com.luxshan.snapify.dto.RegisterRequest;
import com.luxshan.snapify.exception.EmailAlreadyExistsException;
import com.luxshan.snapify.model.User;
import com.luxshan.snapify.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(RegisterRequest request){

        String email = request.getEmail()
                .trim()
                .toLowerCase(Locale.ROOT);

        if(userRepository.existsByEmail(email)){
            throw new EmailAlreadyExistsException("An account with this email already exists");
        }
        String passwordHash = passwordEncoder.encode(request.getPassword());

        User user = User.builder()
                .email(email)
                .passwordHash(passwordHash)
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user);
    }
}
