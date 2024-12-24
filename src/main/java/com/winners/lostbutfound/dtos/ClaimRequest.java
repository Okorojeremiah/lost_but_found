package com.winners.lostbutfound.dtos;

import lombok.Builder;

@Builder
public record ClaimRequest(
        String ownerFirstName,
        String ownerLastName,
        String ownerEmail,
        String ownerPhoneNumber,
        String itemNo,
        String adminName
) {
}
