package com.tnicacio.seniorhotel.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tnicacio.seniorhotel.entities.Person;

public interface PersonRepository extends JpaRepository<Person, Long>{

}
