package com.hotel.security.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.hotel.constants.AppMessages;
import com.hotel.entity.User;
import com.hotel.repository.UserRepository;
import com.hotel.security.model.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;
	
	User user;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		
		if (username.contains("@")) {
			username = username.trim().toLowerCase();
			 user = userRepository
			        .findByEmail(username)
			        .orElseThrow(()-> 
				     new UsernameNotFoundException(
				                AppMessages.USER_NOT_FOUND
				        		));
		}
		else {
			username = username.trim();
			 user = userRepository
			        .findByPhone(username)
			        .orElseThrow(()-> 
			     new UsernameNotFoundException(
			                AppMessages.USER_NOT_FOUND
			        		));
	
		}
		return new CustomUserDetails(user);
		
	}

}
