package com.tnicacio.seniorhotel.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tnicacio.seniorhotel.dto.PersonDTO;
import com.tnicacio.seniorhotel.tests.Factory;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class PersonControllerIT {


	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private PersonDTO dto;
	
	@BeforeEach
	void setUp() throws Exception {
		dto = Factory.createPersonDto();
		dto.setId(null);
	}
	
	@Test
	public void insertShouldInsertResource() throws Exception {

		String jsonBody = objectMapper.writeValueAsString(dto);
		
		ResultActions result =
				mockMvc.perform(post("/persons")
					.content(jsonBody)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isCreated());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").value("Tiago Luiz"));
		result.andExpect(jsonPath("$.email").value("tiago@email.com"));
		result.andExpect(jsonPath("$.age").value(29));
	}
	
	@Test
	public void findAllShouldReturnPage() throws Exception {
		
		ResultActions result =
				mockMvc.perform(get("/persons")
					.contentType(MediaType.APPLICATION_JSON));

		result.andExpect(status().isOk());
	}
	
	@Test
	public void updateShouldUpdateResourceWhenIdExists() throws Exception {

		long existingId = 1L;
		dto.setName("Joãozinho Joãozão");
		dto.setEmail("jj@email.com");
		dto.setAge(30);
		
		String jsonBody = objectMapper.writeValueAsString(dto);
		
		ResultActions result =
				mockMvc.perform(put("/persons/{id}", existingId)
					.content(jsonBody)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.id").value(1L));		
		result.andExpect(jsonPath("$.name").value("Joãozinho Joãozão"));
		result.andExpect(jsonPath("$.email").value("jj@email.com"));
		result.andExpect(jsonPath("$.age").value(30));
	}

	@Test
	public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {

		long nonExistingId = 1000L;
		
		String jsonBody = objectMapper.writeValueAsString(dto);
		
		ResultActions result =
				mockMvc.perform(put("/persons/{id}", nonExistingId)
					.content(jsonBody)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNotFound());
	}
	
	@Test
	public void deleteShouldReturnNoContentWhenIndependentId() throws Exception {		
		
		Long independentId = 10L;
		
		ResultActions result =
				mockMvc.perform(delete("/persons/{id}", independentId));
		
		
		result.andExpect(status().isNoContent());
	}

	@Test
	public void deleteShouldReturnNotFoundWhenNonExistingId() throws Exception {		

		Long nonExistingId = 50L;
		
		ResultActions result =
				mockMvc.perform(delete("/persons/{id}", nonExistingId));

		result.andExpect(status().isNotFound());
	}
}
