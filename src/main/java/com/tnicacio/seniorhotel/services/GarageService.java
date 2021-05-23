package com.tnicacio.seniorhotel.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tnicacio.seniorhotel.dto.GarageDTO;
import com.tnicacio.seniorhotel.entities.Garage;
import com.tnicacio.seniorhotel.repositories.GarageRepository;

@Service
public class GarageService {
	
	@Autowired
	GarageRepository repository;

	public List<GarageDTO> findAll() {
		List<Garage> list = repository.findAll();
		return list.stream().map(garage -> new GarageDTO(garage)).collect(Collectors.toList());
	}

	public GarageDTO findById(long existingId) {
		// TODO Auto-generated method stub
		return null;
	}

	public GarageDTO update(long existingId, GarageDTO garageDto) {
		// TODO Auto-generated method stub
		return null;
	}

	public void delete(long existingId) {
		// TODO Auto-generated method stub
		
	}

}
