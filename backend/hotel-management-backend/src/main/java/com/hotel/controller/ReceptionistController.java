package com.hotel.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/receptionist")
public class ReceptionistController {

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('RECEPTIONIST')")
    public ResponseEntity<String> dashboard() {

        return ResponseEntity.ok("Welcome Receptionist");
    }
}