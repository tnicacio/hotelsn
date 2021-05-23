package com.tnicacio.seniorhotel.services;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.tnicacio.seniorhotel.dto.RoomDTO;
import com.tnicacio.seniorhotel.repositories.RoomRepository;
import com.tnicacio.seniorhotel.services.exceptions.ResourceNotFoundException;

@SpringBootTest
@Transactional
public class RoomServiceIT {

	@Autowired
	private RoomService service;
	
	@Autowired
	private RoomRepository repository;
	
	private Long existingNonDependentId;
	private Long nonExistingId;
	private long countTotalRooms;
	
	@BeforeEach
	void setUp() throws Exception {
		existingNonDependentId = 7L;
		nonExistingId = 1000L;
		countTotalRooms = 8L;
	}
	
	@Test
	public void deleteShouldDeleteResourceWhenIdExistsAndIsNonDependent() {
		
		service.delete(existingNonDependentId);
		
		Assertions.assertEquals(countTotalRooms - 1, repository.count());
	}
	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {			
			service.delete(nonExistingId);
		});
	}

	
	@Test
	public void findAllShouldReturnListOfRoomDTO () {
		List<RoomDTO> result = service.findAll();
		
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals(countTotalRooms, result.size());
	}
}
