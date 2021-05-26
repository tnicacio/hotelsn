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

import com.tnicacio.seniorhotel.dto.PersonDTO;
import com.tnicacio.seniorhotel.entities.Booking;
import com.tnicacio.seniorhotel.entities.Person;
import com.tnicacio.seniorhotel.repositories.BookingRepository;
import com.tnicacio.seniorhotel.repositories.PersonRepository;
import com.tnicacio.seniorhotel.services.exceptions.ResourceNotFoundException;
import com.tnicacio.seniorhotel.tests.Factory;

@ExtendWith(SpringExtension.class)
public class PersonServiceTests {

	@InjectMocks
	private PersonService service;
	
	@Mock
	private PersonRepository repository;
	
	@Mock
	private BookingRepository bookingRepository;
	
	private long existingId;
	private long nonExistingId;
	private Person person;
	private Booking booking;
	private PersonDTO personDto;
	private PageImpl<Person> personPage;

	
	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 2L;
		person = Factory.createPerson();
		booking = Factory.createBooking();
		personDto = Factory.createPersonDto();
		personPage = new PageImpl<>(List.of(person));
		
		Mockito.when(repository.findAll(ArgumentMatchers.any(Pageable.class))).thenReturn(personPage);
		
		Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(person));
		Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
		
		Mockito.when(repository.getOne(existingId)).thenReturn(person);
		Mockito.when(repository.getOne(nonExistingId)).thenThrow(EntityNotFoundException.class);
		
		Mockito.when(bookingRepository.getOne(existingId)).thenReturn(booking);
		Mockito.when(bookingRepository.getOne(nonExistingId)).thenThrow(EntityNotFoundException.class);
		
		Mockito.when(repository.save(ArgumentMatchers.any(Person.class))).thenReturn(person);
		
		Mockito.doNothing().when(repository).deleteById(existingId);
		Mockito.doThrow(ResourceNotFoundException.class).when(repository).deleteById(nonExistingId);	
	}
	
	@Test
	public void findAllShouldReturnPageOfPersonDTO() {
		Pageable pageable = PageRequest.of(0, 10);
		Page<PersonDTO> result = service.findAll(pageable);
		Assertions.assertNotNull(result);
		
		Mockito.verify(repository, Mockito.times(1)).findAll(pageable);
	}
	
	@Test
	public void findByIdShouldReturnPersonDTOWhenIdExists() {
		PersonDTO result = service.findById(existingId);
		
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
	public void insertShouldPersistAndReturnPersonDTO() {
		personDto.setId(null);
		
		personDto = service.insert(personDto);
		
		Assertions.assertNotNull(personDto);
	}
	
	@Test
	public void updateShouldReturnPersonDTOWhenIdExists() {
		
		PersonDTO result = service.update(existingId, personDto);
		
		Assertions.assertNotNull(result);
	}
	
	@Test
	public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.update(nonExistingId, personDto);
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
