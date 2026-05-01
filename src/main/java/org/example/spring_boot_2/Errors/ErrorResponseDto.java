package org.example.spring_boot_2.Errors;

import java.time.LocalDateTime;

public record ErrorResponseDto(
        String message,
        String detailedMessage,
        LocalDateTime errorTime
) {
}
