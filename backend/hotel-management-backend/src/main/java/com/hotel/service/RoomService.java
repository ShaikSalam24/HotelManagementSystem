package com.hotel.service;


import java.util.List;

import com.hotel.dto.room.RoomRequest;
import com.hotel.dto.room.RoomResponse;
import com.hotel.enums.RoomStatus;
import com.hotel.enums.RoomType;

public interface RoomService {

    RoomResponse createRoom(RoomRequest request);

    List<RoomResponse> getAllRooms();

    RoomResponse getRoomById(Long id);

    RoomResponse updateRoom(Long id, RoomRequest request);

    void deleteRoom(Long id);

    List<RoomResponse> getRoomsByStatus(RoomStatus status);

    List<RoomResponse> getRoomsByType(RoomType roomType);

    List<RoomResponse> getRoomsByTypeAndStatus(RoomType roomType,
                                               RoomStatus status);
}