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
import com.tnicacio.seniorhotel.dto.RoomDTO;
import com.tnicacio.seniorhotel.tests.Factory;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class RoomControllerIT {


	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private RoomDTO dto;
	private Long nonExistingId;
	
	@BeforeEach
	void setUp() throws Exception {
		dto = Factory.createRoomDto();
		dto.setId(null);
		nonExistingId = 500L;
	}
	
	@Test
	public void insertShouldInsertResource() throws Exception {

		String jsonBody = objectMapper.writeValueAsString(dto);
		
		ResultActions result =
				mockMvc.perform(post("/rooms")
					.content(jsonBody)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isCreated());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").value("105"));
		result.andExpect(jsonPath("$.isAvailable").value(true));
	}
	
	@Test
	public void findAllShouldReturnListOfRoomDTO() throws Exception {
		
		ResultActions result =
				mockMvc.perform(get("/rooms")
					.contentType(MediaType.APPLICATION_JSON));

		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.size()").value(8));
		result.andExpect(jsonPath("$[0].name").value("101"));
		result.andExpect(jsonPath("$[1].name").value("102"));
		result.andExpect(jsonPath("$[2].name").value("103"));
	}
	
	@Test
	public void updateShouldUpdateResourceWhenIdExists() throws Exception {

		long existingId = 1L;
		dto.setName("110");
		
		String jsonBody = objectMapper.writeValueAsString(dto);
		
		ResultActions result =
				mockMvc.perform(put("/rooms/{id}", existingId)
					.content(jsonBody)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.id").value(1L));		
		result.andExpect(jsonPath("$.name").value("110"));
		result.andExpect(jsonPath("$.isAvailable").value(true));
	}

	@Test
	public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		
		String jsonBody = objectMapper.writeValueAsString(dto);
		
		ResultActions result =
				mockMvc.perform(put("/rooms/{id}", nonExistingId)
					.content(jsonBody)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNotFound());
	}
	
	@Test
	public void deleteShouldReturnNoContentWhenIndependentId() throws Exception {		
		
		Long independentId = 7L;
		
		ResultActions result =
				mockMvc.perform(delete("/rooms/{id}", independentId));
		
		
		result.andExpect(status().isNoContent());
	}

	@Test
	public void deleteShouldReturnNotFoundWhenNonExistingId() throws Exception {		
		
		ResultActions result =
				mockMvc.perform(delete("/rooms/{id}", nonExistingId));

		result.andExpect(status().isNotFound());
	}
}
