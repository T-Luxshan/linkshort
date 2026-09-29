package com.luxshan.snapify.controller;

import com.luxshan.snapify.dto.LoginRequest;
import com.luxshan.snapify.dto.RegisterRequest;
import com.luxshan.snapify.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@Valid @RequestBody RegisterRequest request){
        authService.register(request);
    }

    @GetMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public void login(@Valid @RequestBody LoginRequest request){
        authService.login(request);
    }
}
