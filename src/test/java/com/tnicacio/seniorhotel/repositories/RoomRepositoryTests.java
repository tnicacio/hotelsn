package com.tnicacio.seniorhotel.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.tnicacio.seniorhotel.entities.Room;
import com.tnicacio.seniorhotel.tests.Factory;

@DataJpaTest
public class RoomRepositoryTests {
	
	@Autowired
	private RoomRepository repository;
	
	private long existingId;
	private long nonExistingId;
	private long countTotalRooms;

	
	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 1000L;
		countTotalRooms = 6L;
	}
	
	@Test
	public void saveShouldPersistWithAutoIncrementWhenIdIsNull() {
		
		Room room = Factory.createRoom();
		room.setId(null);
		
		room = repository.save(room);
		Optional<Room> result = repository.findById(room.getId());
		
		Assertions.assertNotNull(room.getId());
		Assertions.assertEquals(countTotalRooms + 1, room.getId());
		Assertions.assertTrue(result.isPresent());
		Assertions.assertSame(result.get(), room);
	}
	
	@Test
	public void deleteShouldDeleteObjectWhenIdExists() {
		
		repository.deleteById(existingId);
		
		Optional<Room> result = repository.findById(existingId);
		Assertions.assertFalse(result.isPresent());;
	}
	
	@Test
	public void deleteShouldThrowEmptyResultDataAccessExceptionWhenIdDoesNotExists() {
		
		Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
			repository.deleteById(nonExistingId);
		});
	}
	
	@Test
	public void findByIdShouldReturnNonEmptyOptionalRoomWhenIdExists() {
		
		Optional<Room> result = repository.findById(existingId);
		
		Assertions.assertTrue(result.isPresent());
	}
	
	@Test
	public void findByIdShouldReturnEmptyOptionalRoomWhenIdDoesNotExists() {
		
		Optional<Room> result = repository.findById(nonExistingId);
		
		Assertions.assertTrue(result.isEmpty());
	}
}
