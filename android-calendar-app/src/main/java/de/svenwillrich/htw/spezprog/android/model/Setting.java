/**
 * 
 */
package de.svenwillrich.htw.spezprog.android.model;

import java.util.Date;

/**
 * @author Sven Willrich
 * Spezielle Programmierung: Android
 * Datum: 27.10.2013
 */

public class Setting {
	public final static boolean SHOW_ONLY_DAYS_WITH_EVENTS = true;
	private char[] username;
	private byte[] password;
	private Date lastUpdate;
	private char[] calendar;
	public final static String FILE_NAEME = "SETTING";

	public char[] getCalendar() {
		return calendar;
	}

	public void setCalendar(char[] calendar) {
		this.calendar = calendar;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public byte[] getPassword() {
		return password;
	}

	public char[] getUsername() {
		return username;
	}

	public void setPassword(byte[] password) {
		this.password = password;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public void setUsername(char[] username) {
		this.username = username;
	}
}
