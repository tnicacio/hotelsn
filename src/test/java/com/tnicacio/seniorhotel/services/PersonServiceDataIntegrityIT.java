package com.tnicacio.seniorhotel.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.tnicacio.seniorhotel.services.exceptions.DatabaseException;

@SpringBootTest
public class PersonServiceDataIntegrityIT {
	
	@Autowired
	private PersonService service;

	@Test
	public void deleteShouldThrowDatabaseExceptionWhenIdDoesNotExists() {
		
		Long dependentId = 1L;
		
		Assertions.assertThrows(DatabaseException.class, () -> {			
			service.delete(dependentId);
		});
	}
}
