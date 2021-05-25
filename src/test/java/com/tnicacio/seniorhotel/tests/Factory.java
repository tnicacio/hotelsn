package com.tnicacio.seniorhotel.tests;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import com.tnicacio.seniorhotel.dto.GarageDTO;
import com.tnicacio.seniorhotel.dto.BookingDTO;
import com.tnicacio.seniorhotel.dto.PersonDTO;
import com.tnicacio.seniorhotel.dto.RoomDTO;
import com.tnicacio.seniorhotel.entities.Garage;
import com.tnicacio.seniorhotel.entities.Booking;
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
		return new Person(1L, "Tiago Luiz", "tiago@email.com", 29);
	}

	public static Booking createBooking() {
		return new Booking(
				1L, 
				LocalDateTime.of(2021, 5, 19, 9, 30).toInstant(ZoneOffset.UTC),
				LocalDateTime.of(2021, 5, 24, 16, 30).toInstant(ZoneOffset.UTC),
				LocalDateTime.of(2021, 5, 24, 10, 20).toInstant(ZoneOffset.UTC),
				null,
				createPerson(),
				createRoom(),
				createGarage());
	}

	public static GarageDTO createGarageDto() {
		return new GarageDTO(createGarage());
	}

	public static RoomDTO createRoomDto() {
		return new RoomDTO(createRoom());
	}

	public static PersonDTO createPersonDto() {
		return new PersonDTO(createPerson());
	}
	
	public static BookingDTO createBookingDto() {
		return new BookingDTO(createBooking());
	}
}
