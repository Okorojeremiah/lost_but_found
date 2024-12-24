package com.winners.lostbutfound.dtos;

import lombok.Builder;

@Builder
public record RefreshTokenResponse(
        String accessToken
) {
}
