package com.winners.lostbutfound.dtos;

import lombok.Builder;

@Builder
public record RegistrationRequest(
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        String password,
        String confirmPassword
) {
}
