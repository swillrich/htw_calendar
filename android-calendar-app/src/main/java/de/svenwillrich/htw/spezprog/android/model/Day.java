/**
 * 
 */
package de.svenwillrich.htw.spezprog.android.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import de.svenwillrich.htw.spezprog.logik.Utils;

/**
 * @author Sven Willrich Spezielle Programmierung: Android Datum: 23.10.2013
 */

public class Day {
	private EDAYS DAY_NAME;
	private Date dateOfDay;
	public static final String DATE_TIME_LONG = "dd.MM.yyyy HH:mm:ss";
	public static final String DATE_TIME_SHORT = "dd.MM.yy HH:mm:ss";
	public static final String DATE_LONG = "dd.MM.yyyy";
	public static final String DATE_SHORT = "dd.MM.yy";
	public static final String TIME_LONG = "HH:mm:ss";
	public static final String TIME_WITHOUT_SECS = "HH:mm";
	public static final String WEEKDAY = "EEEE";
	public static final String WEEKDAY_SHORT = "EEE";

	public Day(String dateAsString) {
		this(DATE_TIME_LONG, dateAsString);
	}

	public Day(String pattern, String dateAsString) {
		try {
			dateOfDay = new SimpleDateFormat(pattern).parse(dateAsString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		determineDayName();
	}

	public Day(Date date) {
		this.dateOfDay = date;
		determineDayName();
	}

	private void determineDayName() {
		Calendar c = Calendar.getInstance();
		c.setTime(dateOfDay);
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		switch (dayOfWeek) {
		case 1:
			DAY_NAME = EDAYS.SUNDAY;
			break;
		case 2:
			DAY_NAME = EDAYS.MONDAY;
			break;
		case 3:
			DAY_NAME = EDAYS.TUESDAY;
			break;
		case 4:
			DAY_NAME = EDAYS.WEDNESDAY;
			break;
		case 5:
			DAY_NAME = EDAYS.THURSDAY;
			break;
		case 6:
			DAY_NAME = EDAYS.FRIDAY;
			break;
		case 7:
			DAY_NAME = EDAYS.SATURDAY;
			break;
		}
	}

	public Date getDateOfDay() {
		return dateOfDay;
	}

	public EDAYS getDAY_NAME() {
		return DAY_NAME;
	}

	public String getDateFormatted(String pattern) {
		if (pattern == null) {
			pattern = DATE_TIME_LONG;
		}
		return new SimpleDateFormat(pattern).format(dateOfDay);
	}
}
