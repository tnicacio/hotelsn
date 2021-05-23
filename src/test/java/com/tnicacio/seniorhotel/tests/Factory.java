package com.tnicacio.seniorhotel.tests;

import com.tnicacio.seniorhotel.dto.GarageDTO;
import com.tnicacio.seniorhotel.dto.RoomDTO;
import com.tnicacio.seniorhotel.entities.Garage;
import com.tnicacio.seniorhotel.entities.Person;
import com.tnicacio.seniorhotel.entities.Room;

public class Factory {

	public static Garage createGarage() {
		return new Garage(1L, "D01", true);
	}
	
	public static Room createRoom() {
		return new Room(1L, "105", true);
	}
	
	public static Person createPerson() {
		return new Person(1L, "Tiago Luiz", "tiago@email.com");
	}

	public static GarageDTO createGarageDto() {
		return new GarageDTO(createGarage());
	}

	public static RoomDTO createRoomDto() {
		return new RoomDTO(createRoom());
	}
}
