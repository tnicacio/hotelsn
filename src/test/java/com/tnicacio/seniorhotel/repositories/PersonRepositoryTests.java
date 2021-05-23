package com.tnicacio.seniorhotel.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.tnicacio.seniorhotel.entities.Person;
import com.tnicacio.seniorhotel.tests.Factory;

@DataJpaTest
public class PersonRepositoryTests {
	
	@Autowired
	private PersonRepository repository;
	
	private long existingId;
	private long nonExistingId;
	private long countTotalPersons;

	
	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 1000L;
		countTotalPersons = 13L;
	}
	
	@Test
	public void saveShouldPersistWithAutoIncrementWhenIdIsNull() {
		
		Person person = Factory.createPerson();
		person.setId(null);
		
		person = repository.save(person);
		Optional<Person> result = repository.findById(person.getId());
		
		Assertions.assertNotNull(person.getId());
		Assertions.assertEquals(countTotalPersons + 1, person.getId());
		Assertions.assertTrue(result.isPresent());
		Assertions.assertSame(result.get(), person);
	}
	
	@Test
	public void deleteShouldDeleteObjectWhenIdExists() {
		
		repository.deleteById(existingId);
		
		Optional<Person> result = repository.findById(existingId);
		Assertions.assertFalse(result.isPresent());;
	}
	
	@Test
	public void deleteShouldThrowEmptyResultDataAccessExceptionWhenIdDoesNotExists() {
		
		Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
			repository.deleteById(nonExistingId);
		});
	}
	
	@Test
	public void findByIdShouldReturnNonEmptyOptionalPersonWhenIdExists() {
		
		Optional<Person> result = repository.findById(existingId);
		
		Assertions.assertTrue(result.isPresent());
	}
	
	@Test
	public void findByIdShouldReturnEmptyOptionalPersonWhenIdDoesNotExists() {
		
		Optional<Person> result = repository.findById(nonExistingId);
		
		Assertions.assertTrue(result.isEmpty());
	}
}
