package com.hotel.dto.response;

public record LoginResponse(

        String token,

        String tokenType

) {
}