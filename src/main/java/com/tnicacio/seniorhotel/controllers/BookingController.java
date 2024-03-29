package com.tnicacio.seniorhotel.controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.tnicacio.seniorhotel.dto.BookingDTO;
import com.tnicacio.seniorhotel.services.BookingService;

@RestController
@RequestMapping(value = "/bookings")
public class BookingController {

	@Autowired
	private BookingService service;

	@PostMapping
	public ResponseEntity<BookingDTO> insert(@RequestBody BookingDTO dto) {
		dto = service.insert(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(dto.getId()).toUri();
		return ResponseEntity.created(uri).body(dto);
	}
	
	@GetMapping
	public ResponseEntity<Page<BookingDTO>> findAllByPersonId(@RequestParam(value = "personId", required = false) Long personId, Pageable pageable) {
		Page<BookingDTO> page;
		if (personId != null && personId != 0) {
			page = service.findAllByPersonId(personId, pageable);			
		} else {
			page = service.findAll(pageable);
		}
		return ResponseEntity.ok().body(page);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<BookingDTO> findById(@PathVariable(value = "id") Long id) {
		BookingDTO dto = service.findById(id);
		return ResponseEntity.ok().body(dto);
	}
	
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<BookingDTO> update(@PathVariable(value = "id") Long id, @RequestBody BookingDTO dto) {
		dto = service.update(id, dto);
		return ResponseEntity.ok().body(dto);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable(value = "id") Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping(value = "/confirmed")
	public ResponseEntity<Page<BookingDTO>> checkedIn(Pageable pageable){
		Page<BookingDTO> bookingDto = service.checkedIn(pageable);
		return ResponseEntity.ok(bookingDto);
	}

	@GetMapping(value = "/completed")
	public ResponseEntity<Page<BookingDTO>> checkedOut(Pageable pageable){
		Page<BookingDTO> bookingDto = service.checkedOut(pageable);
		return ResponseEntity.ok(bookingDto);
	}

	@GetMapping(value = "/current")
	public ResponseEntity<Page<BookingDTO>> current(Pageable pageable){
		Page<BookingDTO> bookingDto = service.current(pageable);
		return ResponseEntity.ok(bookingDto);
	}
	
	@GetMapping(value = "/open")
	public ResponseEntity<Page<BookingDTO>> openBookings(Pageable pageable){
		Page<BookingDTO> openBookings = service.open(pageable);
		return ResponseEntity.ok(openBookings);
	}
	
	@PatchMapping(value = "/{id}/checkin")
	public ResponseEntity<BookingDTO> doCheckin(@PathVariable(value = "id") Long id) {
		BookingDTO dto = service.checkIn(id);
		return ResponseEntity.ok().body(dto);
	}

	@PatchMapping(value = "/{id}/checkout")
	public ResponseEntity<BookingDTO> doCheckout(@PathVariable(value = "id") Long id) {
		BookingDTO dto = service.checkOut(id);
		return ResponseEntity.ok().body(dto);
	}
	
}
