/**
 * 
 */
package de.svenwillrich.htw.spezprog.logik;

import java.util.Date;
import java.util.List;

import de.svenwillrich.htw.spezprog.exception.CalDataNotLoadedException;
import de.svenwillrich.htw.spezprog.exception.DataCannotReceivedException;
import de.svenwillrich.htw.spezprog.exception.LoginDataIncorrectException;
import de.svenwillrich.htw.spezprog.model.Event;

/**
 * @author Sven Willrich
 * Spezielle Programmierung: Android
 * Datum: 20.10.2013
 */

public interface ICal {
	public String updateCalendar(char[] username, char[] password)
			throws LoginDataIncorrectException, DataCannotReceivedException;

	public List<Event> getEvents() throws CalDataNotLoadedException;

	public List<Event> getEventsFromDate(Date date)
			throws CalDataNotLoadedException;

	public List<Event> getEventsBetweenDates(Date from, Date to)
			throws CalDataNotLoadedException;
	
	public void loadCalendar(String calAsString);
}
