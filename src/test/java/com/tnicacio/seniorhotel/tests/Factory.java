package com.tnicacio.seniorhotel.tests;

import com.tnicacio.seniorhotel.entities.Garage;

public class Factory {

	public static Garage createGarage() {
		return new Garage(1L, "D01", true);
	}
}
