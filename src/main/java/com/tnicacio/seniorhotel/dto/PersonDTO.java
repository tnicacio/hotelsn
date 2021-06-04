package com.tnicacio.seniorhotel.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.hateoas.RepresentationModel;

import com.tnicacio.seniorhotel.entities.Booking;
import com.tnicacio.seniorhotel.entities.Person;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
public class PersonDTO extends RepresentationModel<PersonDTO> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Setter
	private Long id;
	@Setter
	private String name;
	@Setter
	private String email;
	@Setter
	private Integer age;
	
	private List<BookingDTO> bookings = new ArrayList<>();

	public PersonDTO(Long id, String name, String email, Integer age) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.age = age;
	}
	
	public PersonDTO(Person entity) {
		this.id = entity.getId();
		this.name = entity.getName();
		this.email = entity.getEmail();
		this.age = entity.getAge();
	}
	
	public PersonDTO(Person entity, List<Booking> bookings) {
		this(entity);
		bookings.forEach(booking -> getBookings().add(new BookingDTO(booking)));
	}
	
}
