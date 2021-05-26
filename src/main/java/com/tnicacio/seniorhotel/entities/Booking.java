package com.tnicacio.seniorhotel.entities;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

@Entity
@Table(name = "tb_booking")
public class Booking implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private static final double DAILY_PRICE_ON_WEEKDAYS = 120.0;
	private static final double DAILY_PRICE_ON_WEEKENDS = 150.0;
	private static final double GARAGE_PRICE_ON_WEEKDAYS = 15.0;
	private static final double GARAGE_PRICE_ON_WEEKENDS = 20.0;
	private static final ZoneOffset ZONE_OFFSET = ZoneOffset.ofHours(-3);
	private static final OffsetTime TIME_LIMIT_TO_CHECKOUT = OffsetTime.of(16, 30, 0, 0, ZONE_OFFSET);
	
	
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
	 
	@ManyToOne(fetch = FetchType.LAZY)
	private Person person;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Room room;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Garage garage;
	
	private Double expectedPrice;
	private Double realPrice;
	
	public Booking() {}

	public Booking(Long id, Instant startDate,Instant endDate, Instant dtCheckin, Instant dtCheckout,
			 Person person, Room room, Garage garage, Double expectedPrice, Double realPrice) {
		this.id = id;
		this.startDate = startDate;
		this.endDate = endDate;
		this.dtCheckin = dtCheckin;
		this.dtCheckout = dtCheckout;
		this.person = person;
		this.room = room;
		this.garage = garage;
		this.expectedPrice = expectedPrice;
		this.realPrice = realPrice;
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

	@PrePersist
	public void prePersist() {
		setExpectedPrice(calculatePriceBetween(startDate, endDate));
	}
	
	private boolean passedTimeLimitOnDay(Instant day) {
		OffsetTime timeOfTheDay = OffsetTime.ofInstant(day, ZONE_OFFSET);
		return timeOfTheDay.isAfter(TIME_LIMIT_TO_CHECKOUT);
	}
	
	private Map<String, Long> countBusinessAndWeekendDaysBetween(Instant startDate, Instant endDate){        
        long daysBetweenWithLastDayIncluded = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        
        if (passedTimeLimitOnDay(endDate)) {
        	daysBetweenWithLastDayIncluded += 1;
        }
        
        Predicate<Instant> isWeekend = date -> {
        	DayOfWeek dayOfWeek = date.atOffset(ZONE_OFFSET).getDayOfWeek();
        	return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
        };
 
        long businessDays = Stream.iterate(startDate, date -> date.plus(1, ChronoUnit.DAYS))
        		.limit(daysBetweenWithLastDayIncluded)
                .filter(isWeekend.negate()).count();
        long weekendDays = daysBetweenWithLastDayIncluded - businessDays;
        
        Map<String, Long> mapWithReturnData = new HashMap<>();
        mapWithReturnData.put("BUSINESS_DAYS", businessDays);
        mapWithReturnData.put("WEEKEND_DAYS", weekendDays);
        return mapWithReturnData;
    }
	
	public Double calculatePriceBetween(Instant startDate, Instant endDate) {
		if (startDate == null || endDate == null) {
			return null;
		}
		
		double expectedPrice = 0.0;
		boolean hasGarage = garage != null;
		
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
