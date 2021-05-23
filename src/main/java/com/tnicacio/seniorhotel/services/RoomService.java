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

import com.tnicacio.seniorhotel.dto.RoomDTO;
import com.tnicacio.seniorhotel.entities.Room;
import com.tnicacio.seniorhotel.repositories.RoomRepository;
import com.tnicacio.seniorhotel.services.exceptions.DatabaseException;
import com.tnicacio.seniorhotel.services.exceptions.ResourceNotFoundException;

@Service
public class RoomService {
	
	@Autowired
	RoomRepository repository;

	@Transactional
	public RoomDTO insert(RoomDTO roomDto) {
		Room entity = new Room();
		copyDtoToEntity(roomDto, entity);
		entity = repository.save(entity);
		return new RoomDTO(entity);
	}

	@Transactional(readOnly = true)
	public List<RoomDTO> findAll() {
		List<Room> list = repository.findAll();
		return list.stream().map(room -> new RoomDTO(room)).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public RoomDTO findById(long id) {
		Optional<Room> optionalRoom = repository.findById(id);
		Room entity = optionalRoom.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
		return new RoomDTO(entity);
	}

	@Transactional
	public RoomDTO update(Long id, RoomDTO roomDto) {
		try {
			Room entity = repository.getOne(id);
			copyDtoToEntity(roomDto, entity);
			entity = repository.save(entity);
			return new RoomDTO(entity);
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

	private void copyDtoToEntity(RoomDTO dto, Room entity) {
		entity.setName(dto.getName());
		entity.setIsAvailable(dto.getIsAvailable());
	}
	
}
