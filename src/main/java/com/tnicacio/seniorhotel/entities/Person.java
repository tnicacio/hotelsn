package com.tnicacio.seniorhotel.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "tb_person")
public class Person implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	@Setter
	private Long id;
	@Setter
	private String name;
	@Setter
	private String email;
	@Setter
	private Integer age;
	
	@OneToMany(mappedBy = "person")
	private List<Booking> bookings = new ArrayList<>();

	public Person(Long id, String name, String email, Integer age) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.age = age;
	}

}
