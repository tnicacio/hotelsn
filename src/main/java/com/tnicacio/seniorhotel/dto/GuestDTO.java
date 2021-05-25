package com.tnicacio.seniorhotel.dto;

import java.io.Serializable;
import java.time.Instant;

public class GuestDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long bookingId;
	private Long userId;
	private String name;
	private String email;
	private Integer age;
	private Instant dtCheckin;
	private Instant dtCheckOut;
	private Long roomId;
	private Long garageId;
	
	public GuestDTO() {}

	public GuestDTO(Long bookingId, Long userId, String name, String email, Integer age, Instant dtCheckin, Instant dtCheckOut,
			Long roomId, Long garageId) {
		this.bookingId = bookingId;
		this.userId = userId;
		this.name = name;
		this.email = email;
		this.age = age;
		this.dtCheckin = dtCheckin;
		this.dtCheckOut = dtCheckOut;
		this.roomId = roomId;
		this.garageId = garageId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Instant getDtCheckin() {
		return dtCheckin;
	}

	public void setDtCheckin(Instant dtCheckin) {
		this.dtCheckin = dtCheckin;
	}

	public Instant getDtCheckOut() {
		return dtCheckOut;
	}

	public void setDtCheckOut(Instant dtCheckOut) {
		this.dtCheckOut = dtCheckOut;
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

	public Long getBookingId() {
		return bookingId;
	}

	public void setBookingId(Long bookingId) {
		this.bookingId = bookingId;
	}
	
	
}
