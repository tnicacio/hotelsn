package com.tnicacio.seniorhotel.dto;

import java.io.Serializable;

import com.tnicacio.seniorhotel.entities.Room;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RoomDTO implements Serializable{
	private static final long serialVersionUID = 1L;

	private Long id;
	private String name;
	private Boolean isAvailable;
	
	public RoomDTO(Room entity) {
		this.id = entity.getId();
		this.name = entity.getName();
		this.isAvailable = entity.getIsAvailable();
	}

}
