package com.hotel.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.hotel.dto.room.RoomRequest;
import com.hotel.dto.room.RoomResponse;
import com.hotel.entity.Room;
import com.hotel.enums.RoomStatus;
import com.hotel.enums.RoomType;
import com.hotel.exception.DuplicateResourceException;
import com.hotel.exception.ResourceNotFoundException;
import com.hotel.repository.RoomRepository;
import com.hotel.service.RoomService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class RoomServiceImpl implements RoomService {

	private final RoomRepository roomRepository;

	@Override
	public RoomResponse createRoom(RoomRequest request) {
		// Check if room number already exists
		if (roomRepository.existsByRoomNumber(request.getRoomNumber().trim())) {
			throw new DuplicateResourceException("Room number " + request.getRoomNumber() + " already exists");
		}

		Room room = Room.builder()
				.roomNumber(request.getRoomNumber())
				.roomType(request.getRoomType())
				.pricePerNight(request.getPricePerNight())
				.capacity(request.getCapacity())
				.floor(request.getFloor())
				.status(request.getStatus())
				.description(request.getDescription())
				.build();

		Room savedRoom = roomRepository.save(room);
		return mapToResponse(savedRoom);
	}

	@Override
	public List<RoomResponse> getAllRooms() {
		return roomRepository.findAll()
				.stream()
				.map(this::mapToResponse)
				.collect(Collectors.toList());
	}

	@Override
	public RoomResponse getRoomById(Long id) {
		Room room = roomRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + id));
		return mapToResponse(room);
	}

	@Override
	public RoomResponse updateRoom(Long id, RoomRequest request) {
		Room room = roomRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + id));

		// Check if new room number already exists (and it's not the same room)
		if (!room.getRoomNumber().equals(request.getRoomNumber()) 
				&& roomRepository.existsByRoomNumber(request.getRoomNumber())) {
			throw new DuplicateResourceException("Room number " + request.getRoomNumber() + " already exists");
		}

		room.setRoomNumber(request.getRoomNumber().trim());
		room.setRoomType(request.getRoomType());
		room.setPricePerNight(request.getPricePerNight());
		room.setCapacity(request.getCapacity());
		room.setFloor(request.getFloor());
		room.setStatus(request.getStatus());
		room.setDescription(request.getDescription());

		Room updatedRoom = roomRepository.save(room);
		return mapToResponse(updatedRoom);
	}

	@Override
	public void deleteRoom(Long id) {
		Room room = roomRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + id));
		roomRepository.delete(room);
	}

	@Override
	public List<RoomResponse> getRoomsByStatus(RoomStatus status) {
		return roomRepository.findByStatus(status)
				.stream()
				.map(this::mapToResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<RoomResponse> getRoomsByType(RoomType roomType) {
		return roomRepository.findByRoomType(roomType)
				.stream()
				.map(this::mapToResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<RoomResponse> getRoomsByTypeAndStatus(RoomType roomType, RoomStatus status) {
		return roomRepository.findByRoomTypeAndStatus(roomType, status)
				.stream()
				.map(this::mapToResponse)
				.collect(Collectors.toList());
	}

	private RoomResponse mapToResponse(Room room) {
		return RoomResponse.builder()
				.id(room.getId())
				.roomNumber(room.getRoomNumber().trim())
				.roomType(room.getRoomType())
				.pricePerNight(room.getPricePerNight())
				.capacity(room.getCapacity())
				.floor(room.getFloor())
				.status(room.getStatus())
				.description(room.getDescription())
				.build();
	}
}
