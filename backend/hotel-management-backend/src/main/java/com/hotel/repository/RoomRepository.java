package com.hotel.repository;


import java.util.List;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hotel.entity.Room;
import com.hotel.enums.RoomStatus;
import com.hotel.enums.RoomType;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    Optional<Room> findByRoomNumber(String roomNumber);

    boolean existsByRoomNumber(String roomNumber);

    List<Room> findByStatus(RoomStatus status);

    List<Room> findByRoomType(RoomType roomType);

    List<Room> findByRoomTypeAndStatus(RoomType roomType, RoomStatus status);
}