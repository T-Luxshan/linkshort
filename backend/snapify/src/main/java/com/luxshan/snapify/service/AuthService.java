package com.luxshan.snapify.service;

import com.luxshan.snapify.dto.LoginRequest;
import com.luxshan.snapify.dto.RegisterRequest;
import com.luxshan.snapify.exception.EmailAlreadyExistsException;
import com.luxshan.snapify.exception.InvalidCredentialsException;
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
    private final JwtService jwtService;

    public String register(RegisterRequest request){

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

        User savedUser = userRepository.save(user);
        return jwtService.generateToken(savedUser);
    }

    public String login(LoginRequest request) {
        String email = request.getEmail()
                .trim()
                .toLowerCase(Locale.ROOT);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new InvalidCredentialsException("Invalid email or password")
                );
        boolean passwordMatches = passwordEncoder.matches(
                request.getPassword(),
                user.getPasswordHash()
        );
        if (!passwordMatches) {
            throw new InvalidCredentialsException("Invalid email or password");
        }
        return jwtService.generateToken(user);
    }
}
