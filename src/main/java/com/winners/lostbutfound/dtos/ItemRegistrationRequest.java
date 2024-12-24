package com.winners.lostbutfound.dtos;

public record ItemRegistrationRequest(
        String name,
        String description,
        String locationFound,
        String finderName,
        String finderEmail,
        String finderPhoneNumber

)  {
}
