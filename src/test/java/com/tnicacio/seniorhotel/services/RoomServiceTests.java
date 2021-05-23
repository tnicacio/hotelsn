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
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.tnicacio.seniorhotel.dto.RoomDTO;
import com.tnicacio.seniorhotel.entities.Room;
import com.tnicacio.seniorhotel.repositories.RoomRepository;
import com.tnicacio.seniorhotel.services.exceptions.ResourceNotFoundException;
import com.tnicacio.seniorhotel.tests.Factory;

@ExtendWith(SpringExtension.class)
public class RoomServiceTests {

	@InjectMocks
	private RoomService service;
	
	@Mock
	private RoomRepository repository;
	
	private long existingId;
	private long nonExistingId;
	private Room room;
	private RoomDTO roomDto;
	private List<Room> roomList;

	
	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 2L;
		room = Factory.createRoom();
		roomDto = Factory.createRoomDto();
		roomList = List.of(room);
		
		Mockito.when(repository.findAll()).thenReturn(roomList);
		
		Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(room));
		Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
		
		Mockito.when(repository.getOne(existingId)).thenReturn(room);
		Mockito.when(repository.getOne(nonExistingId)).thenThrow(EntityNotFoundException.class);
		
		Mockito.when(repository.save(ArgumentMatchers.any(Room.class))).thenReturn(room);
		
		Mockito.doNothing().when(repository).deleteById(existingId);
		Mockito.doThrow(ResourceNotFoundException.class).when(repository).deleteById(nonExistingId);	
	}
	
	@Test
	public void findAllShouldReturnListOfRoomDTO() {
		List<RoomDTO> result = service.findAll();
		Assertions.assertNotNull(result);
		
		Mockito.verify(repository, Mockito.times(1)).findAll();
	}
	
	@Test
	public void findByIdShouldReturnRoomDTOWhenIdExists() {
		RoomDTO result = service.findById(existingId);
		
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
	public void insertShouldPersistAndReturnRoomDTO() {
		roomDto.setId(null);
		
		roomDto = service.insert(roomDto);
		
		Assertions.assertNotNull(roomDto);
	}
	
	@Test
	public void updateShouldReturnRoomDTOWhenIdExists() {
		
		RoomDTO result = service.update(existingId, roomDto);
		
		Assertions.assertNotNull(result);
	}
	
	@Test
	public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.update(nonExistingId, roomDto);
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
