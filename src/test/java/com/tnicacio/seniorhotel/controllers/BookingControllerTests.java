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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tnicacio.seniorhotel.dto.BookingDTO;
import com.tnicacio.seniorhotel.services.BookingService;
import com.tnicacio.seniorhotel.services.exceptions.DatabaseException;
import com.tnicacio.seniorhotel.services.exceptions.ResourceNotFoundException;
import com.tnicacio.seniorhotel.tests.Factory;

@WebMvcTest(BookingController.class)
public class BookingControllerTests {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private BookingService service;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private Long existingId;
	private Long nonExistingId;
	private Long dependentId;
	private BookingDTO bookingDTO;
	private PageImpl<BookingDTO> page;
	
	@BeforeEach
	void setUpd() throws Exception {
		existingId = 1L;
		nonExistingId = 2L;
		dependentId = 3L;
		
		bookingDTO = Factory.createBookingDto();
		page = new PageImpl<>(List.of(bookingDTO));
		
		when(service.findAll(any(Pageable.class))).thenReturn(page);

		when(service.findById(existingId)).thenReturn(bookingDTO);
		when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
		
		when(service.insert(any())).thenReturn(bookingDTO);
		
		when(service.update(eq(existingId), any())).thenReturn(bookingDTO);
		when(service.update(eq(nonExistingId), any())).thenThrow(ResourceNotFoundException.class);
		
		doNothing().when(service).delete(existingId);
		doThrow(ResourceNotFoundException.class).when(service).delete(nonExistingId);
		doThrow(DatabaseException.class).when(service).delete(dependentId);
	}
	
	@Test
	public void insertShouldReturnBookingDTOCreated() throws Exception {
		
		String jsonBody = objectMapper.writeValueAsString(bookingDTO);
		
		ResultActions result = 
				mockMvc.perform(post("/bookings")
						.content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isCreated());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.startDate").exists());
		result.andExpect(jsonPath("$.endDate").exists());
		result.andExpect(jsonPath("$.personId").exists());
		result.andExpect(jsonPath("$.roomId").exists());
	}
	
	@Test
	public void findAllShouldReturnPage() throws Exception {
		ResultActions result = 
				mockMvc.perform(get("/bookings")
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
	}
	
	@Test
	public void findByIdShouldReturnBookingWhenIdExists() throws Exception {
		ResultActions result = 
				mockMvc.perform(get("/bookings/{id}", existingId)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.startDate").exists());
		result.andExpect(jsonPath("$.endDate").exists());
		result.andExpect(jsonPath("$.personId").exists());
		result.andExpect(jsonPath("$.roomId").exists());
	}
	
	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
		ResultActions result = 
				mockMvc.perform(get("/bookings/{id}", nonExistingId)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNotFound());
	}
	
	@Test
	public void updateShouldReturnBookingDTOWhenIdExists() throws Exception {
		
		String jsonBody = objectMapper.writeValueAsString(bookingDTO);
		
		ResultActions result = 
				mockMvc.perform(put("/bookings/{id}", existingId)
						.content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.startDate").exists());
		result.andExpect(jsonPath("$.endDate").exists());
		result.andExpect(jsonPath("$.personId").exists());
		result.andExpect(jsonPath("$.roomId").exists());
	}

	@Test
	public void updateShouldReturnNotFoundWhenIdDoesNorExists() throws Exception {
		String jsonBody = objectMapper.writeValueAsString(bookingDTO);
		
		ResultActions result = 
				mockMvc.perform(put("/bookings/{id}", nonExistingId)
						.content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNotFound());	
	}
	
	@Test
	public void deleteShouldReturnNoContentWhenIdExists() throws Exception {
		ResultActions result = 
				mockMvc.perform(delete("/bookings/{id}", existingId)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNoContent());
	}

	@Test
	public void deleteShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
		ResultActions result = 
				mockMvc.perform(delete("/bookings/{id}", nonExistingId)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNotFound());
	}
}
