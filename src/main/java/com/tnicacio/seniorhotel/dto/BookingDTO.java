package com.tnicacio.seniorhotel.dto;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import com.tnicacio.seniorhotel.entities.Booking;

public class BookingDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final double DAILY_PRICE_ON_WEEKDAYS = 120.0;
	private static final double DAILY_PRICE_ON_WEEKENDS = 150.0;
	private static final double GARAGE_PRICE_ON_WEEKDAYS = 15.0;
	private static final double GARAGE_PRICE_ON_WEEKENDS = 20.0;
	
	private static final OffsetTime TIME_LIMIT_TO_CHECKOUT = OffsetTime.of(16, 30, 0, 0, ZoneOffset.UTC);

	private Long id;
	private Instant startDate;
	private Instant endDate;
	private Instant dtCheckin;
	private Instant dtCheckout;
	private Long personId;
	private Long roomId;
	private Long garageId;
	
	public BookingDTO() {}
	
	public BookingDTO(Long id, Instant startDate, Instant endDate, Instant dtCheckin,
			Instant dtCheckout, Long personId, Long roomId, Long garageId) {
		this.id = id;
		this.startDate = startDate;
		this.endDate = endDate;
		this.dtCheckin = dtCheckin;
		this.dtCheckout = dtCheckout;
		this.personId = personId;
		this.roomId = roomId;
		this.garageId = garageId;
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
		return expectedPriceBetween(startDate, endDate);
	}
	
	public Double getRealPrice() {
		return expectedPriceBetween(dtCheckin, dtCheckout);
	}
	
	private boolean isWeekend(Instant dtCheckedOut) {
		DayOfWeek dayOfWeek = dtCheckedOut.atOffset(ZoneOffset.UTC).getDayOfWeek();
		return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
	}
	
	private boolean passedTimeLimitOnDay(Instant day) {
		OffsetTime timeOfTheDay = OffsetTime.ofInstant(day, ZoneOffset.UTC);
		return timeOfTheDay.isAfter(TIME_LIMIT_TO_CHECKOUT);
	}
	
	private Map<String, Long> countBusinessAndWeekendDaysBetween(Instant startDate, Instant endDate){        
        long daysBetweenWithLastDayIncluded = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        
        if (passedTimeLimitOnDay(endDate)) {
        	daysBetweenWithLastDayIncluded += 1;
        }
 
        long businessDays = Stream.iterate(startDate, date -> date.plus(1, ChronoUnit.DAYS))
        		.limit(daysBetweenWithLastDayIncluded)
                .filter(date -> !isWeekend(date)).count();
        long weekendDays = daysBetweenWithLastDayIncluded - businessDays;
        
        Map<String, Long> mapWithReturnData = new HashMap<>();
        mapWithReturnData.put("BUSINESS_DAYS", businessDays);
        mapWithReturnData.put("WEEKEND_DAYS", weekendDays);
        return mapWithReturnData;
    }
	
	private Double expectedPriceBetween(Instant startDate, Instant endDate) {
		if (startDate == null || endDate == null) {
			return null;
		}
		
		double expectedPrice = 0.0;
		boolean hasGarage = getGarageId() != null;
		
		Map<String, Long> businessAndWeekendDays = countBusinessAndWeekendDaysBetween(startDate, endDate);
		expectedPrice += businessAndWeekendDays.get("BUSINESS_DAYS") * DAILY_PRICE_ON_WEEKDAYS;
		expectedPrice += businessAndWeekendDays.get("WEEKEND_DAYS") * DAILY_PRICE_ON_WEEKENDS;
		
		if (hasGarage) {
			expectedPrice += businessAndWeekendDays.get("BUSINESS_DAYS") * GARAGE_PRICE_ON_WEEKDAYS;
			expectedPrice += businessAndWeekendDays.get("WEEKEND_DAYS") * GARAGE_PRICE_ON_WEEKENDS;
		}
		
		return expectedPrice;
	}

}
