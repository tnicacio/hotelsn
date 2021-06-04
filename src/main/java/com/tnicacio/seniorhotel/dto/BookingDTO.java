package com.tnicacio.seniorhotel.dto;

import java.io.Serializable;
import java.time.Instant;

import com.tnicacio.seniorhotel.entities.Booking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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

}
