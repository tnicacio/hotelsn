package com.tnicacio.seniorhotel.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.tnicacio.seniorhotel.dto.BookingDTO;
import com.tnicacio.seniorhotel.repositories.BookingRepository;
import com.tnicacio.seniorhotel.services.exceptions.ResourceNotFoundException;

@SpringBootTest
@Transactional
public class BookingServiceIT {

	@Autowired
	private BookingService service;
	
	@Autowired
	private BookingRepository repository;
	
	private Long existingId;
	private Long nonExistingId;
	private long countTotalGuests;
	
	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 1000L;
		countTotalGuests = 10L;
	}
	
	@Test
	public void deleteShouldDeleteResourceWhenIdExistsAndIsNonDependent() {
		
		service.delete(existingId);
		
		Assertions.assertEquals(countTotalGuests - 1, repository.count());
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
		
		Page<BookingDTO> result = service.findAll(pageRequest);
		
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals(0, result.getNumber());
		Assertions.assertEquals(10 , result.getSize());
		Assertions.assertEquals(countTotalGuests, result.getTotalElements());
	}
	
	@Test
	public void findAllShouldEmptyPageWhenPageDoesNotExists() {
		
		PageRequest pageRequest = PageRequest.of(50, 10);
		
		Page<BookingDTO> result = service.findAll(pageRequest);
		
		Assertions.assertTrue(result.isEmpty());
	}
	
	@Test
	public void findAllShouldReturnSortedPageWhenSortByDtCheckin() {
		
		PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("dtCheckin"));
		
		Page<BookingDTO> result = service.findAll(pageRequest);
		
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals(9, result.getContent().get(0).getPersonId());
		Assertions.assertEquals(1, result.getContent().get(1).getPersonId());
		Assertions.assertEquals(2, result.getContent().get(2).getPersonId());
	}
	
}
