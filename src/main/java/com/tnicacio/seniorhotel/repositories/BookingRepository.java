package com.tnicacio.seniorhotel.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tnicacio.seniorhotel.dto.BookingDTO;
import com.tnicacio.seniorhotel.entities.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long>{

	@Query("SELECT bk "
			+ "FROM Booking AS bk "
			+ "INNER JOIN bk.person p "
			+ "WHERE bk.dtCheckin is not null "
			+ "ORDER BY bk.dtCheckin DESC")
	Page<BookingDTO> checkedIn(Pageable pageable);
	
	@Query("SELECT new com.tnicacio.seniorhotel.dto.GuestDTO(bk.id, p.id, p.name, p.email, p.age, bk.dtCheckin, bk.dtCheckout, bk.room.id, bk.garage.id) "
			+ "FROM Booking AS bk "
			+ "INNER JOIN bk.person p "
			+ "WHERE bk.dtCheckin is not null "
			+ "AND bk.dtCheckout is null "
			+ "ORDER BY bk.dtCheckin DESC")
	Page<BookingDTO> currentGuests(Pageable pageable);

	@Query("SELECT bk "
			+ "FROM Booking AS bk "
			+ "WHERE bk.dtCheckin is null "
			+ "AND bk.dtCheckout is null "
			+ "ORDER BY bk.startDate DESC")
	Page<BookingDTO> openBookings(Pageable pageable);


	@Query("SELECT bk "
			+ "FROM Booking AS bk "
			+ "INNER JOIN bk.person p "
			+ "WHERE bk.dtCheckin is not null "
			+ "AND bk.dtCheckout is not null "
			+ "ORDER BY bk.dtCheckout DESC")
	Page<BookingDTO> checkedOut(Pageable pageable);
}
