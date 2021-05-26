package com.tnicacio.seniorhotel.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tnicacio.seniorhotel.controllers.PersonController;
import com.tnicacio.seniorhotel.dto.BookingDTO;
import com.tnicacio.seniorhotel.dto.GuestDTO;
import com.tnicacio.seniorhotel.dto.PersonDTO;
import com.tnicacio.seniorhotel.entities.Booking;
import com.tnicacio.seniorhotel.entities.Person;
import com.tnicacio.seniorhotel.repositories.BookingRepository;
import com.tnicacio.seniorhotel.repositories.PersonRepository;
import com.tnicacio.seniorhotel.services.exceptions.DatabaseException;
import com.tnicacio.seniorhotel.services.exceptions.ResourceNotFoundException;

@Service
public class PersonService {
	
	@Autowired
	PersonRepository repository;
	
	@Autowired
	BookingRepository bookingRepository;

	@Transactional
	public PersonDTO insert(PersonDTO personDto) {
		Person entity = new Person();
		copyDtoToEntity(personDto, entity);
		entity = repository.save(entity);
		return new PersonDTO(entity);
	}

	@Transactional(readOnly = true)
	public Page<PersonDTO> findAll(Pageable pageable) {
		Page<Person> list = repository.findAll(pageable);
		return list.map(person -> {
				PersonDTO pDTO = new PersonDTO(person, person.getBookings());
				pDTO.add(linkTo(methodOn(PersonController.class).findById(pDTO.getId())).withSelfRel());
				return pDTO;
			});
	}

	@Transactional(readOnly = true)
	public PersonDTO findById(long id) {
		Optional<Person> optionalPerson = repository.findById(id);
		Person entity = optionalPerson.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
		return new PersonDTO(entity, entity.getBookings());
	}

	@Transactional
	public PersonDTO update(Long id, PersonDTO personDto) {
		try {
			Person entity = repository.getOne(id);
			copyDtoToEntity(personDto, entity);
			entity = repository.save(entity);
			return new PersonDTO(entity);
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
	
	public List<GuestDTO> exGuests() {
		List<GuestDTO> list = repository.exGuests();
		list.stream().map(guest -> {
			guest.add(linkTo(methodOn(PersonController.class).findById(guest.getId())).withSelfRel());
			return guest;
		}).collect(Collectors.toList());
		return list;
	}

	public List<GuestDTO> currentGuests() {
		List<GuestDTO> list = repository.currentGuests();
		list.stream().map(guest -> {
			guest.add(linkTo(methodOn(PersonController.class).findById(guest.getId())).withSelfRel());
			return guest;
		}).collect(Collectors.toList());
		return list;
	}

	private void copyDtoToEntity(PersonDTO dto, Person entity) {
		entity.setName(dto.getName());
		entity.setEmail(dto.getEmail());
		entity.setAge(dto.getAge());

		entity.getBookings().clear();
		for (BookingDTO bookingDTO : dto.getBookings()) {
			Booking booking = bookingRepository.getOne(bookingDTO.getId());
			entity.getBookings().add(booking);
		}
	}
	
}
