package com.winners.lostbutfound.services.auth;

import com.winners.lostbutfound.dtos.AuthResponse;
import com.winners.lostbutfound.dtos.LoginRequest;
import com.winners.lostbutfound.dtos.RegistrationRequest;
import com.winners.lostbutfound.dtos.RegistrationResponse;
import com.winners.lostbutfound.exceptions.RegistrationException;
import com.winners.lostbutfound.models.User;
import com.winners.lostbutfound.services.UserService;
import com.winners.lostbutfound.utils.UserValidator.*;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;


import static com.winners.lostbutfound.utils.UserValidator.ValidationResult.SUCCESS;
import static com.winners.lostbutfound.utils.UserValidator.isValidEmail;
import static com.winners.lostbutfound.utils.UserValidator.isValidPhoneNumber;


@Service
@Slf4j
public class AuthService {


    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserService userService, JwtService jwtService, PasswordEncoder passwordEncoder, RefreshTokenService refreshTokenService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenService = refreshTokenService;
        this.authenticationManager = authenticationManager;
    }

    public RegistrationResponse register(@Valid RegistrationRequest registrationRequest){
        validateRegistrationRequest(registrationRequest);

        User user = User.builder()
                .firstName(registrationRequest.firstName())
                .lastName(registrationRequest.lastName())
                .email(registrationRequest.email())
                .phoneNumber(registrationRequest.phoneNumber())
                .password(passwordEncoder.encode(registrationRequest.password()))
                .build();
        userService.save(user);

        return RegistrationResponse.builder()
                .message("Registration successful")
                .build();
    }

    public AuthResponse login(LoginRequest loginRequest){
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.username(),
                            loginRequest.password()
                    )
            );
        }catch (BadCredentialsException e){
            log.warn("Failed login attempt for email: {}", loginRequest.username());
            throw new BadCredentialsException("Wrong username or password");
        }

        var user = userService.findByEmail(loginRequest.username())
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));

        var jwtToken = jwtService.generateToken(user);
        var refreshToken = refreshTokenService.createRefreshToken(user.getUsername());

        return AuthResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken.getToken())
                .build();
    }

    private void validateRegistrationRequest(RegistrationRequest registrationRequest) {
        ValidationResult isValidEmail = isValidEmail()
                .apply(registrationRequest.email());
        ValidationResult isValidPhoneNumber =  isValidPhoneNumber()
                .apply(registrationRequest.phoneNumber());
        Optional<User> optionalAdmin = userService.findByEmail(
                registrationRequest.email());
        Optional<User> optionalAdmin1 = userService.findByPhoneNumber(
                registrationRequest.phoneNumber());

        if (isValidEmail != SUCCESS)
            throw  new RegistrationException("Not Authorized!");
        if (isValidPhoneNumber != SUCCESS)
            throw new RegistrationException("Incorrect phone number format!");
        if (!Objects.equals(registrationRequest.password(), registrationRequest.confirmPassword()))
            throw new RegistrationException("Password does not match!");
        if (optionalAdmin.isPresent())
            throw new RegistrationException("User with email: " + registrationRequest.email() + " already exist!");
        if (optionalAdmin1.isPresent())
            throw new RegistrationException("User with phone number: " + registrationRequest.phoneNumber() + " already exist!");
    }
}
