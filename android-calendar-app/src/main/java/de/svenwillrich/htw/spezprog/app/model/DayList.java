/**
 * 
 */
package de.svenwillrich.htw.spezprog.app.model;

import java.util.ArrayList;
import java.util.Date;

import net.fortuna.ical4j.model.DateTime;

import de.svenwillrich.htw.spezprog.logik.Utils;

/**
 * @author Sven Willrich Spezielle Programmierung: Android Datum: 23.10.2013
 */

public class DayList extends ArrayList<Day> {
	public DayList(Date from, Date to) {
		for (long timeCounter = from.getTime(); timeCounter <= to.getTime(); timeCounter = timeCounter
				+ Utils.MILLIS_IN_DAY) {
			add(new Day(Utils.getDateWithoutTime(new DateTime(timeCounter))));
		}
	}

	public DayList() {
		// TODO Auto-generated constructor stub
	}

	public Day[] getAsArray() {
		return (Day[]) this.toArray();
	}
}
