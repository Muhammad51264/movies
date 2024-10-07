package com.task.movies.api.controllers;

import com.task.movies.Dto.LoginDto;
import com.task.movies.Dto.RegisterDto;
import com.task.movies.api.responses.AuthResponse;
import com.task.movies.model.User;
import com.task.movies.repository.UserRepository;
import com.task.movies.service.UserService;
import com.task.movies.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;


    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, UserService userService, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;

    }

    @PostMapping("register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterDto registerDto) {

        if (userRepository.existsByEmail(registerDto.getEmail())) {
            return new ResponseEntity<>(new AuthResponse("Email is taken!"), HttpStatus.BAD_REQUEST);
        }
        userService.saveUser(new User(registerDto.getEmail(), registerDto.getUsername(), passwordEncoder.encode(registerDto.getPassword())));
        return new ResponseEntity<>(new AuthResponse("User register success"), HttpStatus.CREATED);
    }

    @PostMapping("login")
    public ResponseEntity<HashMap<String,String>> login(@Valid @RequestBody LoginDto loginDto, HttpServletRequest request) {
        HashMap<String, String> responseMessage = new HashMap<>();

        if (!userRepository.existsByEmail(loginDto.getEmail())) {
            responseMessage.put("message","Email not found!");
            return new ResponseEntity<>(responseMessage, HttpStatus.NOT_FOUND);
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
        );

        User foundUser = userRepository.findByEmail(loginDto.getEmail());
        String token = jwtUtil.generateToken(loginDto.getEmail(),foundUser.getId());
        responseMessage.put("token",token);
        responseMessage.put("message","User login success");
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

}
