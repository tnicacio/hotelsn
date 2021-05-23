package com.tnicacio.seniorhotel.services;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
	public void findAllShouldReturnListOfPersonDTO () {
		List<PersonDTO> result = service.findAll();
		
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals(countTotalPersons, result.size());
	}
}
