package com.tnicacio.seniorhotel.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tnicacio.seniorhotel.dto.PersonDTO;
import com.tnicacio.seniorhotel.entities.Person;
import com.tnicacio.seniorhotel.repositories.PersonRepository;
import com.tnicacio.seniorhotel.services.exceptions.DatabaseException;
import com.tnicacio.seniorhotel.services.exceptions.ResourceNotFoundException;

@Service
public class PersonService {
	
	@Autowired
	PersonRepository repository;

	@Transactional
	public PersonDTO insert(PersonDTO personDto) {
		Person entity = new Person();
		copyDtoToEntity(personDto, entity);
		entity = repository.save(entity);
		return new PersonDTO(entity);
	}

	@Transactional(readOnly = true)
	public List<PersonDTO> findAll() {
		List<Person> list = repository.findAll();
		return list.stream().map(person -> new PersonDTO(person)).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public PersonDTO findById(long id) {
		Optional<Person> optionalPerson = repository.findById(id);
		Person entity = optionalPerson.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
		return new PersonDTO(entity);
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

	private void copyDtoToEntity(PersonDTO dto, Person entity) {
		entity.setName(dto.getName());
		entity.setEmail(dto.getEmail());
		entity.setAge(dto.getAge());
	}
	
}
