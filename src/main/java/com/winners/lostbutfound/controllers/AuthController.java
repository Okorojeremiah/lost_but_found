package com.winners.lostbutfound.controllers;

import com.winners.lostbutfound.dtos.*;
import com.winners.lostbutfound.exceptions.TokenRefreshException;
import com.winners.lostbutfound.models.RefreshToken;
import com.winners.lostbutfound.models.User;
import com.winners.lostbutfound.services.UserService;
import com.winners.lostbutfound.services.auth.AuthService;
import com.winners.lostbutfound.services.auth.JwtService;
import com.winners.lostbutfound.services.auth.RefreshTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;


    public AuthController(AuthService authService, RefreshTokenService refreshTokenService, JwtService jwtService) {
        this.authService = authService;
        this.refreshTokenService = refreshTokenService;
        this.jwtService = jwtService;
    }


    @Operation(summary = "Create a new User")
    @ApiResponse(responseCode = "201", description = "User created successfully")
    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> register(@RequestBody
                                                         RegistrationRequest registrationRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body((authService.register(registrationRequest)));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@RequestBody
                                                     RefreshTokenRequest refreshTokenRequest){
        String requestRefreshToken = refreshTokenRequest.refreshToken();
        RefreshToken refreshToken = refreshTokenService.verifyRefreshToken(requestRefreshToken);
        User user = refreshToken.getUser();

        String accessToken = jwtService.generateToken(user);
        return  ResponseEntity.ok(RefreshTokenResponse
                .builder()
                .accessToken(accessToken)
                .build());
    }
}
