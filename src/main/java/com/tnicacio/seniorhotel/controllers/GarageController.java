package com.tnicacio.seniorhotel.controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.tnicacio.seniorhotel.dto.GarageDTO;
import com.tnicacio.seniorhotel.services.GarageService;

@RestController
@RequestMapping(value = "/garages")
public class GarageController {
	
	@Autowired
	private GarageService service;

	@PostMapping
	public ResponseEntity<GarageDTO> insert(@RequestBody GarageDTO dto) {
		dto = service.insert(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(dto.getId()).toUri();
		return ResponseEntity.created(uri).body(dto);
	}
}
