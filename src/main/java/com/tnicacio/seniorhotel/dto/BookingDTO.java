package com.tnicacio.seniorhotel.dto;

import java.io.Serializable;
import java.time.Instant;

import com.tnicacio.seniorhotel.entities.Booking;

public class BookingDTO implements Serializable{
	private static final long serialVersionUID = 1L;

	private Long id;
	private Instant startDate;
	private Instant endDate;
	private Instant dtCheckin;
	private Instant dtCheckout;
	private Long personId;
	private Long roomId;
	private Long garageId;
	
	private Double expectedPrice;
	private Double realPrice;
	
	public BookingDTO() {}
	
	public BookingDTO(Long id, Instant startDate, Instant endDate, Instant dtCheckin,
			Instant dtCheckout, Long personId, Long roomId, Long garageId, Double expectedPrice, Double realPrice) {
		this.id = id;
		this.startDate = startDate;
		this.endDate = endDate;
		this.dtCheckin = dtCheckin;
		this.dtCheckout = dtCheckout;
		this.personId = personId;
		this.roomId = roomId;
		this.garageId = garageId;
		this.expectedPrice = expectedPrice;
		this.realPrice = realPrice;
	}
	
	public BookingDTO(Booking entity) {
		this.id = entity.getId();
		this.startDate = entity.getStartDate();
		this.endDate = entity.getEndDate();
		this.dtCheckin = entity.getDtCheckin();
		this.dtCheckout = entity.getDtCheckout();
		this.personId = entity.getPerson().getId();
		this.roomId = entity.getRoom().getId();
		this.garageId = entity.getGarage() != null ? entity.getGarage().getId() : null;
		this.expectedPrice = entity.getExpectedPrice();
		this.realPrice = entity.getRealPrice();
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

	public Long getPersonId() {
		return personId;
	}

	public void setPersonId(Long personId) {
		this.personId = personId;
	}

	public Long getRoomId() {
		return roomId;
	}

	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}

	public Long getGarageId() {
		return garageId;
	}

	public void setGarageId(Long garageId) {
		this.garageId = garageId;
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
	
	public Double getExpectedPrice() {
		return expectedPrice;
	}

	public void setExpectedPrice(Double expectedPrice) {
		this.expectedPrice = expectedPrice;
	}

	public Double getRealPrice() {
		return realPrice;
	}

	public void setRealPrice(Double realPrice) {
		this.realPrice = realPrice;
	}

}
