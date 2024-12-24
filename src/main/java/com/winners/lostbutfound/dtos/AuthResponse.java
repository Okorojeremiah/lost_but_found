package com.winners.lostbutfound.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
public record AuthResponse(
    String accessToken,
    String refreshToken
) {
}
