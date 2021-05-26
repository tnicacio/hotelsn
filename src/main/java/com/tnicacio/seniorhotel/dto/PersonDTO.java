package com.tnicacio.seniorhotel.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.hateoas.RepresentationModel;

import com.tnicacio.seniorhotel.entities.Booking;
import com.tnicacio.seniorhotel.entities.Person;

public class PersonDTO extends RepresentationModel<PersonDTO> implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private String name;
	private String email;
	private Integer age;
	
	private List<BookingDTO> bookings = new ArrayList<>();
	
	public PersonDTO() {}

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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}
	
	public List<BookingDTO> getBookings() {
		return bookings;
	}
	
}
