package com.tnicacio.seniorhotel.repositories;

import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.tnicacio.seniorhotel.entities.Booking;
import com.tnicacio.seniorhotel.tests.Factory;

@DataJpaTest
public class BookingRepositoryTests {
	
	@Autowired
	private BookingRepository repository;
	
	private long existingId;
	private long nonExistingId;
	private long countTotalBookings;

	
	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 1000L;
		countTotalBookings = 10L;
	}
	
	@Test
	public void saveShouldPersistWithAutoIncrementWhenIdIsNullAndHasPersonAndRoomNotNull() {
		
		Booking booking = Factory.createBooking();
		booking.setId(null);
		
		booking = repository.save(booking);
		Optional<Booking> result = repository.findById(booking.getId());
		
		Assertions.assertNotNull(booking.getId());
		Assertions.assertEquals(countTotalBookings + 1, booking.getId());
		Assertions.assertTrue(result.isPresent());
		Assertions.assertSame(result.get(), booking);
	}
	
	@Test
	public void saveShouldThrowConstraintViolationExceptionWhenIdAndPersonAreNull() {
		
		Assertions.assertThrows(ConstraintViolationException.class, () -> {
			Booking booking = Factory.createBooking();
			booking.setId(null);
			booking.setPerson(null);
			
			repository.save(booking);
		});
	}
	
	@Test
	public void saveShouldThrowConstraintViolationExceptionWhenIdAndRoomAreNull() {
		
		Assertions.assertThrows(ConstraintViolationException.class, () -> {
			Booking booking = Factory.createBooking();
			booking.setId(null);
			booking.setRoom(null);
			
			repository.save(booking);
		});
	}

	@Test
	public void saveShouldThrowConstraintViolationExceptionWhenIdAndRoomAndPersonAreNull() {
		
		Assertions.assertThrows(ConstraintViolationException.class, () -> {
			Booking booking = Factory.createBooking();
			booking.setId(null);
			booking.setRoom(null);
			booking.setPerson(null);
			
			repository.save(booking);
		});
	}
	
	@Test
	public void deleteShouldDeleteObjectWhenIdExists() {
		
		repository.deleteById(existingId);
		
		Optional<Booking> result = repository.findById(existingId);
		Assertions.assertFalse(result.isPresent());;
	}
	
	@Test
	public void deleteShouldThrowEmptyResultDataAccessExceptionWhenIdDoesNotExists() {
		
		Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
			repository.deleteById(nonExistingId);
		});
	}
	
	@Test
	public void findByIdShouldReturnNonEmptyOptionalBookingWhenIdExists() {
		
		Optional<Booking> result = repository.findById(existingId);
		
		Assertions.assertTrue(result.isPresent());
	}
	
	@Test
	public void findByIdShouldReturnEmptyOptionalBookingWhenIdDoesNotExists() {
		
		Optional<Booking> result = repository.findById(nonExistingId);
		
		Assertions.assertTrue(result.isEmpty());
	}
}
