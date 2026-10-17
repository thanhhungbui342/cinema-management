package com.thanhhungbui342.cinema.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thanhhungbui342.cinema.entity.Room;

public interface RoomRepository extends JpaRepository<Room, Long>{
    Optional<Room> findById(Long id);
}
