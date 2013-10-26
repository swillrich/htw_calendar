/**
 * 
 */
package de.svenwillrich.htw.spezprog.exception;

/**
 * @author Sven Willrich
 * Spezielle Programmierung: Android
 * Datum: 22.10.2013
 */

public class LoginDataIncorrectException extends RuntimeException {
	public LoginDataIncorrectException() {
		super("Login data incorrect");
	}
}
