package com.hotel.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hotel.constants.ApiConstants;
import com.hotel.dto.request.auth.LoginRequest;
import com.hotel.dto.request.auth.RegisterRequest;
import com.hotel.dto.response.ApiResponse;
import com.hotel.dto.response.LoginResponse;
import com.hotel.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(ApiConstants.API_V1 + ApiConstants.AUTH)
@RequiredArgsConstructor
public class AuthController {
	
	private final AuthService authService;
	
	@PostMapping(ApiConstants.REGISTER)
	public ResponseEntity<ApiResponse<?>> register(
	        @Valid @RequestBody RegisterRequest request) {

	    return ResponseEntity
	            .status(HttpStatus.CREATED)
	            .body(authService.register(request));
	}
	
	@PostMapping(ApiConstants.LOGIN)
	public ResponseEntity<ApiResponse<LoginResponse>> login(
	        @Valid @RequestBody LoginRequest request) {

	    return ResponseEntity.ok(authService.login(request));
	}
}