package com.hotel.dto.response;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.Builder;
import lombok.RequiredArgsConstructor;


public record ErrorResponse(
        
        LocalDateTime timestamp,

        int status,

        String error,

        String message,

        Map<String, String> errors,

        String path
) {
}