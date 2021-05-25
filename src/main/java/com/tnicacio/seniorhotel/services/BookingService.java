package com.tnicacio.seniorhotel.services;

import java.time.Instant;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tnicacio.seniorhotel.dto.BookingDTO;
import com.tnicacio.seniorhotel.entities.Booking;
import com.tnicacio.seniorhotel.entities.Garage;
import com.tnicacio.seniorhotel.entities.Person;
import com.tnicacio.seniorhotel.entities.Room;
import com.tnicacio.seniorhotel.repositories.BookingRepository;
import com.tnicacio.seniorhotel.services.exceptions.DatabaseException;
import com.tnicacio.seniorhotel.services.exceptions.ResourceNotFoundException;

@Service
public class BookingService {
	
	@Autowired
	BookingRepository repository;

	@Transactional
	public BookingDTO insert(BookingDTO guestDto) {
		try {
			Booking entity = new Booking();
			copyDtoToEntity(guestDto, entity);
			entity = repository.save(entity);
			return new BookingDTO(entity);
		} catch (ConstraintViolationException e) {
			throw new DatabaseException("Constraint violation");
		}
	}

	@Transactional(readOnly = true)
	public Page<BookingDTO> findAll(Pageable pageable) {
		Page<Booking> page = repository.findAll(pageable);
		return page.map(guest -> new BookingDTO(guest));
	}

	@Transactional(readOnly = true)
	public BookingDTO findById(long id) {
		Optional<Booking> optionalGuest = repository.findById(id);
		Booking entity = optionalGuest.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
		return new BookingDTO(entity);
	}

	@Transactional
	public BookingDTO update(Long id, BookingDTO guestDto) {
		try {
			Booking entity = repository.getOne(id);
			copyDtoToEntity(guestDto, entity);
			entity = repository.save(entity);
			return new BookingDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}
	}

	public void delete(long id) {
		try {
			repository.deleteById(id);					
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity violation");
		}		
	}
	
	@Transactional(readOnly = true)
	public Page<BookingDTO> checkedIn(Pageable pageable) {
		return repository.checkedIn(pageable);
	}

	@Transactional(readOnly = true)
	public Page<BookingDTO> checkedOut(Pageable pageable) {
		return repository.checkedOut(pageable);
	}

	@Transactional(readOnly = true)
	public Page<BookingDTO> currentGuests(Pageable pageable) {
		return repository.currentGuests(pageable);
	}

	@Transactional(readOnly = true)
	public Page<BookingDTO> openBookings(Pageable pageable) {
		return repository.openBookings(pageable);
	}
	
	@Transactional
	public BookingDTO checkIn(long id) {
		try {
			Booking entity = repository.getOne(id);
			entity.setDtCheckin(Instant.now());
			
			entity.getRoom().setIsAvailable(false);
			if (entity.getGarage() != null) {
				entity.getGarage().setIsAvailable(false);
			}

			entity = repository.save(entity);
			return new BookingDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}
	}

	@Transactional
	public BookingDTO checkOut(long id) {
		try {
			Booking entity = repository.getOne(id);
			entity.setDtCheckout(Instant.now());
			
			entity.getRoom().setIsAvailable(true);
			if (entity.getGarage() != null) {
				entity.getGarage().setIsAvailable(true);
			}
			
			entity = repository.save(entity);
			return new BookingDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}
	}

	private void copyDtoToEntity(BookingDTO dto, Booking entity) {
		entity.setStartDate(dto.getStartDate());
		entity.setEndDate(dto.getEndDate());
		entity.setDtCheckin(dto.getDtCheckin());
		entity.setDtCheckout(dto.getDtCheckout());
		entity.setPerson(new Person(dto.getId(), null, null, null));
		entity.setRoom(new Room(dto.getId(), null, null));
		if (entity.getGarage() != null) {
			entity.setGarage(new Garage(dto.getId(), null, null));			
		}
	}
	
}
