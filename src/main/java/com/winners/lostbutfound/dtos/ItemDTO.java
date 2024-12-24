package com.winners.lostbutfound.dtos;

import java.time.LocalDate;

public record ItemDTO(
        String name,
        long itemNumber,
        String description,
        String itemStatus,
        LocalDate dateFound,
        String finderName,
        String finderEmail,
        String finderPhoneNumber,
        String image
) {
}
