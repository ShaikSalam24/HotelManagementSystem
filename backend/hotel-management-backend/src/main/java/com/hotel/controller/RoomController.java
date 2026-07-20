package com.hotel.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.hotel.dto.room.RoomRequest;
import com.hotel.dto.room.RoomResponse;
import com.hotel.enums.RoomStatus;
import com.hotel.enums.RoomType;
import com.hotel.service.RoomService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/rooms")
@RequiredArgsConstructor
@Validated
public class RoomController {

	private final RoomService roomService;

	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<RoomResponse> createRoom(@Valid @RequestBody RoomRequest request) {

		RoomResponse response = roomService.createRoom(request);

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping
	public ResponseEntity<List<RoomResponse>> getAllRooms() {

		return ResponseEntity.ok(roomService.getAllRooms());
	}

	@GetMapping("/{id}")
	public ResponseEntity<RoomResponse> getRoomById(@PathVariable Long id) {

		return ResponseEntity.ok(roomService.getRoomById(id));
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<RoomResponse> updateRoom(@PathVariable Long id, @Valid @RequestBody RoomRequest request) {

		return ResponseEntity.ok(roomService.updateRoom(id, request));
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> deleteRoom(@PathVariable Long id) {

		roomService.deleteRoom(id);

		return ResponseEntity.ok("Room deleted successfully.");
	}

	@GetMapping("/status/{status}")
	public ResponseEntity<List<RoomResponse>> getRoomsByStatus(@PathVariable RoomStatus status) {

		return ResponseEntity.ok(roomService.getRoomsByStatus(status));
	}

	@GetMapping("/type/{roomType}")
	public ResponseEntity<List<RoomResponse>> getRoomsByType(@PathVariable RoomType roomType) {

		return ResponseEntity.ok(roomService.getRoomsByType(roomType));
	}

	@GetMapping("/search")
	public ResponseEntity<List<RoomResponse>> searchRooms(@RequestParam RoomType roomType,
			@RequestParam RoomStatus status) {

		return ResponseEntity.ok(roomService.getRoomsByTypeAndStatus(roomType, status));
	}

}
