package com.tnicacio.seniorhotel.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tnicacio.seniorhotel.dto.GuestDTO;
import com.tnicacio.seniorhotel.entities.Person;

public interface PersonRepository extends JpaRepository<Person, Long>{

	@Query("SELECT new com.tnicacio.seniorhotel.dto.GuestDTO(p.id, p.name, p.email, p.age) "
			+ "FROM Person p "
			+ "INNER JOIN p.bookings bk "
			+ "WHERE bk.dtCheckout is not null "
			+ "ORDER BY p.name")
	List<GuestDTO> exGuests();
	
	@Query("SELECT new com.tnicacio.seniorhotel.dto.GuestDTO(p.id, p.name, p.email, p.age) "
			+ "FROM Person p "
			+ "INNER JOIN p.bookings bk "
			+ "WHERE bk.dtCheckout is null "
			+ "AND bk.dtCheckin is not null "
			+ "ORDER BY p.name")
	List<GuestDTO> currentGuests();
}
