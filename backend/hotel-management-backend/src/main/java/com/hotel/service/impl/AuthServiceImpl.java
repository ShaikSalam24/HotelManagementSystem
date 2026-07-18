package com.hotel.service.impl;

import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hotel.constants.AppMessages;
import com.hotel.constants.SecurityConstants;
import com.hotel.dto.request.auth.LoginRequest;
import com.hotel.dto.request.auth.RegisterRequest;
import com.hotel.dto.response.ApiResponse;
import com.hotel.dto.response.LoginResponse;
import com.hotel.entity.Role;
import com.hotel.entity.User;
import com.hotel.enums.RoleName;
import com.hotel.exception.DuplicateResourceException;
import com.hotel.exception.ResourceNotFoundException;
import com.hotel.repository.RoleRepository;
import com.hotel.repository.UserRepository;
import com.hotel.security.jwt.JwtService;
import com.hotel.service.AuthService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

	private final UserRepository userRepository;

	private final RoleRepository roleRepository;

	private final PasswordEncoder passwordEncoder;
	
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;
	
	@Override
	public ApiResponse<LoginResponse> login(LoginRequest request) {
		
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						request.username(),
						request.password()
				)
		);
		
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		
		String jwtToken = jwtService.generateToken(userDetails);
		LoginResponse loginResponse = new LoginResponse(jwtToken,SecurityConstants.TOKEN_TYPE);
		
		return new ApiResponse<>(true, 
				AppMessages.LOGIN_SUCCESS,
				loginResponse);
	}

	@Transactional
	@Override
	public ApiResponse<?> register(RegisterRequest request) {
		checkDuplicateEmail(request.email());

		checkDuplicatePhone(request.phone());

		Role role = getCustomerRole();

		User user = buildUser(request, role);

		User savedUser = userRepository.save(user);

		return new ApiResponse<>(true, AppMessages.USER_REGISTERED_SUCCESSFULLY, null);

	}

	private void checkDuplicateEmail(String email) {

		if (userRepository.existsByEmail(email)) {
			throw new DuplicateResourceException(AppMessages.EMAIL_ALREADY_EXISTS);
		}

	}

	private void checkDuplicatePhone(String phone) {

		if (userRepository.existsByPhone(phone)) {
			throw new DuplicateResourceException(AppMessages.PHONE_ALREADY_EXISTS);
		}

	}

	private Role getCustomerRole() {

	    return roleRepository.findByName(RoleName.CUSTOMER)
	            .orElseThrow(() ->
	                    new ResourceNotFoundException(
	                            AppMessages.CUSTOMER_ROLE_NOT_FOUND));

	}

	private User buildUser(RegisterRequest request, Role role) {

	    return new User(
	            request.firstName(),
	            request.lastName(),
	            request.email(),
	            request.phone(),
	            passwordEncoder.encode(request.password()),
	            request.gender(),
	            request.dateOfBirth(),
	            request.address(),
	            null,
	            role
	    );
	}

}