package com.hotel.service;

import com.hotel.dto.request.auth.LoginRequest;
import com.hotel.dto.request.auth.RegisterRequest;
import com.hotel.dto.response.ApiResponse;

public interface AuthService {

    ApiResponse<?> register(RegisterRequest request);

    ApiResponse<?> login(LoginRequest request);

}
