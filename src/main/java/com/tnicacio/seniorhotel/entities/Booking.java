package com.tnicacio.seniorhotel.entities;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "tb_booking")
public class Booking implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
	private Instant startDate;
	
	@Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
	private Instant endDate;
	
	@Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
	private Instant dtCheckin;
	
	@Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
	private Instant dtCheckout;
	
	@OneToOne(fetch = FetchType.LAZY)
	@NotNull
	private Person person;
	
	@OneToOne(fetch = FetchType.LAZY)
	@NotNull
	private Room room;
	
	@OneToOne(fetch = FetchType.LAZY)
	private Garage garage;
	
	public Booking() {}

	public Booking(Long id, Instant startDate, Instant endDate, Instant dtCheckin, Instant dtCheckout,
			@NotNull Person person, @NotNull Room room, Garage garage) {
		this.id = id;
		this.startDate = startDate;
		this.endDate = endDate;
		this.dtCheckin = dtCheckin;
		this.dtCheckout = dtCheckout;
		this.person = person;
		this.room = room;
		this.garage = garage;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Instant getStartDate() {
		return startDate;
	}

	public void setStartDate(Instant startDate) {
		this.startDate = startDate;
	}

	public Instant getEndDate() {
		return endDate;
	}

	public void setEndDate(Instant endDate) {
		this.endDate = endDate;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public Garage getGarage() {
		return garage;
	}

	public void setGarage(Garage garage) {
		this.garage = garage;
	}

	public Instant getDtCheckin() {
		return dtCheckin;
	}

	public void setDtCheckin(Instant dtCheckin) {
		this.dtCheckin = dtCheckin;
	}

	public Instant getDtCheckout() {
		return dtCheckout;
	}

	public void setDtCheckout(Instant dtCheckout) {
		this.dtCheckout = dtCheckout;
	}
	
}
