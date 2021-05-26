package com.tnicacio.seniorhotel.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tnicacio.seniorhotel.entities.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long>{

	@Query("SELECT bk "
			+ "FROM Booking AS bk "
			+ "INNER JOIN bk.person p "
			+ "WHERE bk.dtCheckin is not null "
			+ "ORDER BY bk.dtCheckin DESC")
	Page<Booking> checkedIn(Pageable pageable);
	
	@Query("SELECT bk "
			+ "FROM Booking AS bk "
			+ "INNER JOIN bk.person p "
			+ "WHERE bk.dtCheckin is not null "
			+ "AND bk.dtCheckout is null "
			+ "ORDER BY bk.dtCheckin DESC")
	Page<Booking> current(Pageable pageable);

	@Query("SELECT bk "
			+ "FROM Booking AS bk "
			+ "WHERE bk.dtCheckin is null "
			+ "AND bk.dtCheckout is null "
			+ "ORDER BY bk.startDate DESC")
	Page<Booking> open(Pageable pageable);

	@Query("SELECT bk "
			+ "FROM Booking AS bk "
			+ "INNER JOIN bk.person p "
			+ "WHERE bk.dtCheckin is not null "
			+ "AND bk.dtCheckout is not null "
			+ "ORDER BY bk.dtCheckout DESC")
	Page<Booking> checkedOut(Pageable pageable);
	
	Page<Booking> findAllByPersonId(Long id, Pageable pageable);
}
