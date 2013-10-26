/**
 * 
 */
package de.svenwillrich.htw.spezprog.exception;

/**
 * @author Sven Willrich
 * Spezielle Programmierung: Android
 * Datum: 22.10.2013
 */

public class CalDataNotLoadedException extends RuntimeException {
	public CalDataNotLoadedException() {
		super("Calendar Data are not loaded");
	}
}
