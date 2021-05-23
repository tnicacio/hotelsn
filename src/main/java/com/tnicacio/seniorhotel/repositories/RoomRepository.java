package com.tnicacio.seniorhotel.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tnicacio.seniorhotel.entities.Room;

public interface RoomRepository extends JpaRepository<Room, Long>{

}
