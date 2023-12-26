package com.example.web.auth;

import com.example.web.config.JwtService;
import com.example.web.mapstruct.UsersDto;
import com.example.web.model.Role;
import com.example.web.model.User;
import com.example.web.repository.UserRepository;
import com.example.web.mapstruct.AuthenticationRequest;
import com.example.web.responses.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    public AuthenticationResponse register(UsersDto request) {
        Long nextUserId = getNextUserId();
        logger.info(String.valueOf(nextUserId));
        var user = User.builder()
                .id(nextUserId)
                .name(request.getName())
                .email(request.getEmail())
                .address(request.getAddress())
                .photoUrl(request.getPhotoUrl())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        user.setId(nextUserId);
        repository.save(user);

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();

    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    private Long getNextUserId() {
        List<User> existingUsers = repository.findAll();

        User newUser = new User();

        if (existingUsers.isEmpty()) {
            newUser.setId(1001L);
        } else {
            // Find the maximum existing user ID
            Long maxId = existingUsers.stream()
                    .map(User::getId)
                    .max(Long::compare)
                    .orElse(0L);

            // Set the new user ID
            newUser.setId(maxId + 1);
        }

        return newUser.getId(); // If the table is empty, start from 1001
    }
}
