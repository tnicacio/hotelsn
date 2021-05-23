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

import com.tnicacio.seniorhotel.dto.GarageDTO;
import com.tnicacio.seniorhotel.entities.Garage;
import com.tnicacio.seniorhotel.repositories.GarageRepository;
import com.tnicacio.seniorhotel.services.exceptions.DatabaseException;
import com.tnicacio.seniorhotel.services.exceptions.ResourceNotFoundException;

@Service
public class GarageService {
	
	@Autowired
	GarageRepository repository;

	@Transactional
	public GarageDTO insert(GarageDTO garageDto) {
		Garage entity = new Garage();
		copyDtoToEntity(garageDto, entity);
		entity = repository.save(entity);
		return new GarageDTO(entity);
	}

	@Transactional(readOnly = true)
	public List<GarageDTO> findAll() {
		List<Garage> list = repository.findAll();
		return list.stream().map(garage -> new GarageDTO(garage)).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public GarageDTO findById(long id) {
		Optional<Garage> optionalGarage = repository.findById(id);
		Garage entity = optionalGarage.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
		return new GarageDTO(entity);
	}

	@Transactional
	public GarageDTO update(Long id, GarageDTO garageDto) {
		try {
			Garage entity = repository.getOne(id);
			copyDtoToEntity(garageDto, entity);
			entity = repository.save(entity);
			return new GarageDTO(entity);
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

	private void copyDtoToEntity(GarageDTO dto, Garage entity) {
		entity.setName(dto.getName());
		entity.setIsAvailable(dto.getIsAvailable());
	}
	
}
