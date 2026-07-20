package com.hotel.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<String> dashboard(Authentication authentication) {

        return ResponseEntity.ok(
                "Welcome Customer : " + authentication.getName()
        );
    }
}