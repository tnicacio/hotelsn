package com.tnicacio.seniorhotel.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import com.tnicacio.seniorhotel.dto.PersonDTO;
import com.tnicacio.seniorhotel.repositories.PersonRepository;
import com.tnicacio.seniorhotel.services.exceptions.ResourceNotFoundException;

@SpringBootTest
@Transactional
public class PersonServiceIT {

	@Autowired
	private PersonService service;
	
	@Autowired
	private PersonRepository repository;
	
	private Long existingNonDependentId;
	private Long nonExistingId;
	private long countTotalPersons;
	
	@BeforeEach
	void setUp() throws Exception {
		existingNonDependentId = 13L;
		nonExistingId = 1000L;
		countTotalPersons = 13L;
	}
	
	@Test
	public void deleteShouldDeleteResourceWhenIdExistsAndIsNonDependent() {
		
		service.delete(existingNonDependentId);
		
		Assertions.assertEquals(countTotalPersons - 1, repository.count());
	}
	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {			
			service.delete(nonExistingId);
		});
	}

	
	@Test
	public void findAllShouldReturnPageWhenPageZeroAndSizeEqualsToTen() {
		PageRequest pageRequest = PageRequest.of(0, 10);
		Page<PersonDTO> result = service.findAll(pageRequest);
		
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals(0, result.getNumber());
		Assertions.assertEquals(10 , result.getSize());
		Assertions.assertEquals(countTotalPersons, result.getTotalElements());
	}
}
