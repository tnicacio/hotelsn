package com.tnicacio.seniorhotel.dto;

import java.io.Serializable;

import com.tnicacio.seniorhotel.entities.Garage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GarageDTO implements Serializable{
	private static final long serialVersionUID = 1L;

	private Long id;
	private String name;
	private Boolean isAvailable;

	public GarageDTO(Garage entity) {
		this.id = entity.getId();
		this.name = entity.getName();
		this.isAvailable = entity.getIsAvailable();
	}

}
