package com.tnicacio.seniorhotel.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tnicacio.seniorhotel.dto.PersonDTO;
import com.tnicacio.seniorhotel.services.PersonService;
import com.tnicacio.seniorhotel.services.exceptions.DatabaseException;
import com.tnicacio.seniorhotel.services.exceptions.ResourceNotFoundException;
import com.tnicacio.seniorhotel.tests.Factory;

@WebMvcTest(PersonController.class)
public class PersonControllerTests {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private PersonService service;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private Long existingId;
	private Long nonExistingId;
	private Long dependentId;
	private PersonDTO personDTO;
	private List<PersonDTO> personDTOList;
	
	@BeforeEach
	void setUpd() throws Exception {
		existingId = 1L;
		nonExistingId = 2L;
		dependentId = 3L;
		
		personDTO = Factory.createPersonDto();
		personDTOList = List.of(personDTO);
		
		when(service.findAll()).thenReturn(personDTOList);

		when(service.findById(existingId)).thenReturn(personDTO);
		when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
		
		when(service.insert(any())).thenReturn(personDTO);
		
		when(service.update(eq(existingId), any())).thenReturn(personDTO);
		when(service.update(eq(nonExistingId), any())).thenThrow(ResourceNotFoundException.class);
		
		doNothing().when(service).delete(existingId);
		doThrow(ResourceNotFoundException.class).when(service).delete(nonExistingId);
		doThrow(DatabaseException.class).when(service).delete(dependentId);
	}
	
	@Test
	public void insertShouldReturnPersonDTOCreated() throws Exception {
		
		String jsonBody = objectMapper.writeValueAsString(personDTO);
		
		ResultActions result = 
				mockMvc.perform(post("/persons")
						.content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isCreated());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.email").exists());
		result.andExpect(jsonPath("$.age").exists());
	}
	
	@Test
	public void findAllShouldReturnListOfPersonDTO() throws Exception {
		ResultActions result = 
				mockMvc.perform(get("/persons")
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
	}
	
	@Test
	public void findByIdShouldReturnPersonWhenIdExists() throws Exception {
		ResultActions result = 
				mockMvc.perform(get("/persons/{id}", existingId)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.email").exists());
		result.andExpect(jsonPath("$.age").exists());
	}
	
	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
		ResultActions result = 
				mockMvc.perform(get("/persons/{id}", nonExistingId)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNotFound());
	}
	
	@Test
	public void updateShouldReturnPersonDTOWhenIdExists() throws Exception {
		
		String jsonBody = objectMapper.writeValueAsString(personDTO);
		
		ResultActions result = 
				mockMvc.perform(put("/persons/{id}", existingId)
						.content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.email").exists());
		result.andExpect(jsonPath("$.age").exists());
	}

	@Test
	public void updateShouldReturnNotFoundWhenIdDoesNorExists() throws Exception {
		String jsonBody = objectMapper.writeValueAsString(personDTO);
		
		ResultActions result = 
				mockMvc.perform(put("/persons/{id}", nonExistingId)
						.content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNotFound());	
	}
	
	@Test
	public void deleteShouldReturnNoContentWhenIdExists() throws Exception {
		ResultActions result = 
				mockMvc.perform(delete("/persons/{id}", existingId)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNoContent());
	}

	@Test
	public void deleteShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
		ResultActions result = 
				mockMvc.perform(delete("/persons/{id}", nonExistingId)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNotFound());
	}
}
