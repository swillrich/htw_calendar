/**
 * 
 */
package de.svenwillrich.htw.spezprog.android.model;

/**
 * @author Sven Willrich
 * Spezielle Programmierung: Android
 * Datum: 23.10.2013
 */

public enum EDAYS {
	MONDAY("Montag"), TUESDAY("Dienstag"), WEDNESDAY("Mittwoch"), THURSDAY(
			"Donnertag"), FRIDAY("Freitag"), SATURDAY("Samstag"), SUNDAY(
			"Sonntag");
	private String des;

	EDAYS(String des) {

	}

	public String getDes() {
		return des;
	}
}