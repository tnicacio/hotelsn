package com.tnicacio.seniorhotel.services;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.tnicacio.seniorhotel.dto.GarageDTO;
import com.tnicacio.seniorhotel.repositories.GarageRepository;
import com.tnicacio.seniorhotel.services.exceptions.ResourceNotFoundException;

@SpringBootTest
@Transactional
public class GarageServiceIT {

	@Autowired
	private GarageService service;
	
	@Autowired
	private GarageRepository repository;
	
	private Long existingNonDependentId;
	private Long nonExistingId;
	private long countTotalGarageSpots;
	
	@BeforeEach
	void setUp() throws Exception {
		existingNonDependentId = 10L;
		nonExistingId = 1000L;
		countTotalGarageSpots = 10L;
	}
	
	@Test
	public void deleteShouldDeleteResourceWhenIdExistsAndIsNonDependent() {
		
		service.delete(existingNonDependentId);
		
		Assertions.assertEquals(countTotalGarageSpots - 1, repository.count());
	}
	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {			
			service.delete(nonExistingId);
		});
	}

	
	@Test
	public void findAllShouldReturnListOfGarageDTO () {
		List<GarageDTO> result = service.findAll();
		
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals(countTotalGarageSpots, result.size());
	}
}
