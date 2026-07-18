package com.hotel.service;

import com.hotel.dto.request.auth.LoginRequest;
import com.hotel.dto.request.auth.RegisterRequest;
import com.hotel.dto.response.ApiResponse;
import com.hotel.dto.response.LoginResponse;

public interface AuthService {

    ApiResponse<?> register(RegisterRequest request);
    
    ApiResponse<LoginResponse> login(LoginRequest request);

}
