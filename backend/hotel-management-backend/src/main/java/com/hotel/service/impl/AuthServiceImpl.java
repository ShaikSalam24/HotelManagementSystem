package com.hotel.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hotel.constants.AppMessages;
import com.hotel.dto.request.auth.LoginRequest;
import com.hotel.dto.request.auth.RegisterRequest;
import com.hotel.dto.response.ApiResponse;
import com.hotel.entity.Role;
import com.hotel.entity.User;
import com.hotel.enums.RoleName;
import com.hotel.exception.DuplicateResourceException;
import com.hotel.exception.ResourceNotFoundException;
import com.hotel.repository.RoleRepository;
import com.hotel.repository.UserRepository;
import com.hotel.service.AuthService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

	private final UserRepository userRepository;

	private final RoleRepository roleRepository;

	private final PasswordEncoder passwordEncoder;
	
	@Override
	public ApiResponse<?> login(LoginRequest request) {
		// TODO Auto-generated method stub
		return null;
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