package com.winners.lostbutfound.services.auth;

import com.winners.lostbutfound.models.RefreshToken;
import com.winners.lostbutfound.models.User;
import com.winners.lostbutfound.repositories.RefreshTokenRepository;
import com.winners.lostbutfound.repositories.UserRepo;
import com.winners.lostbutfound.services.UserService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserService userService) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userService = userService;
    }

    public RefreshToken createRefreshToken(String username){
        User user = userService.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(
                "User not found with email: " + username));

            long tokenValidity = 120 * 1000;
            RefreshToken refreshToken = RefreshToken.builder()
                    .token(UUID.randomUUID().toString())
                    .expirationTime(Instant.now().plusMillis(tokenValidity))
                    .user(user)
                    .build();

        return refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String refreshToken){
        return refreshTokenRepository.findByToken(refreshToken);
    }
    public RefreshToken verifyRefreshToken(String refreshToken){
        RefreshToken refToken = findByToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));
        if(refToken.getExpirationTime().compareTo(Instant.now()) < 0){
            refreshTokenRepository.deleteAll();
            throw new RuntimeException("Refresh token expired");
        }
        return refToken;
    }
}
