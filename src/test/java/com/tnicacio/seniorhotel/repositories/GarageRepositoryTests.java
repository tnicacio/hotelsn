package com.tnicacio.seniorhotel.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.tnicacio.seniorhotel.entities.Garage;
import com.tnicacio.seniorhotel.tests.Factory;

@DataJpaTest
public class GarageRepositoryTests {
	
	@Autowired
	private GarageRepository repository;
	
	private long existingId;
	private long nonExistingId;
	private long countTotalGarageSpots;

	
	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 1000L;
		countTotalGarageSpots = 6L;
	}
	
	@Test
	public void saveShouldPersistWithAutoIncrementWhenIdIsNull() {
		
		Garage garage = Factory.createGarage();
		garage.setId(null);
		
		garage = repository.save(garage);
		Optional<Garage> result = repository.findById(garage.getId());
		
		Assertions.assertNotNull(garage.getId());
		Assertions.assertEquals(countTotalGarageSpots + 1, garage.getId());
		Assertions.assertTrue(result.isPresent());
		Assertions.assertSame(result.get(), garage);
	}
	
	@Test
	public void deleteShouldDeleteObjectWhenIdExists() {
		
		repository.deleteById(existingId);
		
		Optional<Garage> result = repository.findById(existingId);
		Assertions.assertFalse(result.isPresent());;
	}
	
	@Test
	public void deleteShouldThrowEmptyResultDataAccessExceptionWhenIdDoesNotExists() {
		
		Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
			repository.deleteById(nonExistingId);
		});
	}
	
	@Test
	public void findByIdShouldReturnNonEmptyOptionalGarageWhenIdExists() {
		
		Optional<Garage> result = repository.findById(existingId);
		
		Assertions.assertTrue(result.isPresent());
	}
	
	@Test
	public void findByIdShouldReturnEmptyOptionalGarageWhenIdDoesNotExists() {
		
		Optional<Garage> result = repository.findById(nonExistingId);
		
		Assertions.assertTrue(result.isEmpty());
	}
}
