package com.tnicacio.seniorhotel.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@AutoConfigureMockMvc
public class PersonControllerDataIntegrityIT {

	@Autowired
	private MockMvc mockMvc;
	
	@Test
	public void deleteShouldReturnBadRequestWhenDependentId() throws Exception {		

		Long dependentId = 1L;
		
		ResultActions result =
				mockMvc.perform(delete("/persons/{id}", dependentId));
				
		result.andExpect(status().isBadRequest());
	}
}
