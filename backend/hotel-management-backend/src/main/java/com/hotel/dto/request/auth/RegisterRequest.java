package com.hotel.dto.request.auth;

import java.time.LocalDate;

import com.hotel.enums.Gender;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest (
		
		  @NotBlank(message = "First name is required")
		    @Size(max = 50)
		    String firstName,

		    @NotBlank(message = "Last name is required")
		    @Size(max = 50)
		    String lastName,

		    @Email(message = "Invalid email")
		    @NotBlank(message = "Email is required")
		    String email,

		    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid mobile number")
		    String phone,

		    @Pattern(
		    	    regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,20}$",
		    	    message = "Password must contain uppercase, lowercase, number, special character, and be 8-20 characters long"
		    	)
		    	String password,

		    @NotNull(message = "Gender is required")
		    Gender gender,

		    @NotNull(message = "Date of birth is required")
		    LocalDate dateOfBirth,

		    @NotBlank(message = "Address is required")
		    String address
		
		
		
		) {

}
