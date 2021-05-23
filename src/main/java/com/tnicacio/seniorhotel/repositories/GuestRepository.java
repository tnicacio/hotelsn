package com.tnicacio.seniorhotel.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tnicacio.seniorhotel.entities.Guest;

public interface GuestRepository extends JpaRepository<Guest, Long>{

}
