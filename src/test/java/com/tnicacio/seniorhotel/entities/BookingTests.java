package com.tnicacio.seniorhotel.entities;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.tnicacio.seniorhotel.tests.Factory;

public class BookingTests {

	@Test
	public void calculatePriceBetweenShouldReturnCorrectPriceToPayBetweenDatesWhenDatesAreNotNull() {
		Double expectedPrice = 0.0;
		Booking booking = Factory.createBooking();
		
		expectedPrice = booking.calculatePriceBetween(booking.getStartDate(), booking.getEndDate());
		Assertions.assertEquals(880.0, expectedPrice);
		
		booking.setDtCheckout(LocalDateTime.of(2021, 5, 24, 16, 30, 00).toInstant(ZoneOffset.ofHours(-3)));
		expectedPrice = booking.calculatePriceBetween(booking.getDtCheckin(), booking.getDtCheckout());
		Assertions.assertEquals(880.0, expectedPrice);

		booking.setDtCheckout(LocalDateTime.of(2021, 5, 24, 16, 30, 01).toInstant(ZoneOffset.ofHours(-3)));
		expectedPrice = booking.calculatePriceBetween(booking.getDtCheckin(), booking.getDtCheckout());
		Assertions.assertEquals(1015.0, expectedPrice);
	
		booking.setGarage(null);
		expectedPrice = booking.calculatePriceBetween(booking.getStartDate(), booking.getEndDate());
		Assertions.assertEquals(780.0, expectedPrice);

		expectedPrice = booking.calculatePriceBetween(booking.getDtCheckin(), booking.getDtCheckout());
		Assertions.assertEquals(900.0, expectedPrice);
	}
	
	@Test
	public void calculatePriceBetweenShouldReturnNullWhenSomeDateIsNull() {
		Booking booking = Factory.createBooking();
		Double expectedPrice = booking.calculatePriceBetween(booking.getDtCheckin(), booking.getDtCheckout());
		
		Assertions.assertNull(expectedPrice);
	}
}
