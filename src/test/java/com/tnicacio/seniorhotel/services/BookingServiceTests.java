package com.tnicacio.seniorhotel.services;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.tnicacio.seniorhotel.dto.BookingDTO;
import com.tnicacio.seniorhotel.entities.Booking;
import com.tnicacio.seniorhotel.repositories.BookingRepository;
import com.tnicacio.seniorhotel.services.exceptions.ResourceNotFoundException;
import com.tnicacio.seniorhotel.tests.Factory;

@ExtendWith(SpringExtension.class)
public class BookingServiceTests {

	@InjectMocks
	private BookingService service;
	
	@Mock
	private BookingRepository repository;
	
	private long existingId;
	private long nonExistingId;
	private Booking booking;
	private BookingDTO bookingDto;
	private PageImpl<Booking> bookinPage;

	
	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 2L;
		booking = Factory.createBooking();
		bookingDto = Factory.createBookingDto();
		bookinPage = new PageImpl<>(List.of(booking));
		
		Mockito.when(repository.findAll(ArgumentMatchers.any(Pageable.class))).thenReturn(bookinPage);
		
		Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(booking));
		Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
		
		Mockito.when(repository.getOne(existingId)).thenReturn(booking);
		Mockito.when(repository.getOne(nonExistingId)).thenThrow(EntityNotFoundException.class);
		
		Mockito.when(repository.save(ArgumentMatchers.any(Booking.class))).thenReturn(booking);
		
		Mockito.doNothing().when(repository).deleteById(existingId);
		Mockito.doThrow(ResourceNotFoundException.class).when(repository).deleteById(nonExistingId);	
	}
	
	@Test
	public void findAllShouldReturnPageOfBookingDTO() {
		Pageable pageable = PageRequest.of(0, 10);
		Page<BookingDTO> result = service.findAll(pageable);
		
		Assertions.assertNotNull(result);
		Mockito.verify(repository, Mockito.times(1)).findAll(pageable);
	}
	
	@Test
	public void findByIdShouldReturnBookingDTOWhenIdExists() {
		BookingDTO result = service.findById(existingId);
		
		Assertions.assertNotNull(result);
		Mockito.verify(repository, Mockito.times(1)).findById(existingId);
	}
	
	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.findById(nonExistingId);
		});
		Mockito.verify(repository, Mockito.times(1)).findById(nonExistingId);
	}
	
	@Test
	public void insertShouldPersistAndReturnBookingDTO() {
		bookingDto.setId(null);
		
		bookingDto = service.insert(bookingDto);
		
		Assertions.assertNotNull(bookingDto);
	}
	
	@Test
	public void insertShouldThrowDatabaseExceptionWhenPersonOrRoomAreNull() {
		Assertions.assertDoesNotThrow(() -> {
			
			bookingDto.setPersonId(null);
			bookingDto.setRoomId(null);
			service.insert(bookingDto);
		});
	}
	
	@Test
	public void updateShouldReturnBookingDTOWhenIdExists() {
		
		BookingDTO result = service.update(existingId, bookingDto);
		
		Assertions.assertNotNull(result);
	}
	
	@Test
	public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.update(nonExistingId, bookingDto);
		});
	}
	
	@Test
	public void deleteShouldDoNothingWhenIdExists() {
		Assertions.assertDoesNotThrow(() -> {
			service.delete(existingId);
			
		});
		Mockito.verify(repository, Mockito.times(1)).deleteById(existingId);
	}
	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.delete(nonExistingId);
			
		});
		Mockito.verify(repository, Mockito.times(1)).deleteById(nonExistingId);
	}

}
