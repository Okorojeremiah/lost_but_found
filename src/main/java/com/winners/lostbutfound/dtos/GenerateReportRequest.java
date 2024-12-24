package com.winners.lostbutfound.dtos;

import java.time.LocalDate;

public record GenerateReportRequest(
        LocalDate startDate,
        LocalDate endDate
) {
}
