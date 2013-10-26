/**
 * 
 */
package de.svenwillrich.htw.spezprog.logik;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import net.fortuna.ical4j.model.DateTime;

/**
 * @author Sven Willrich
 * Spezielle Programmierung: Android
 * Datum: 19.10.2013
 *
 */
public class Utils {
	static private SimpleDateFormat format = new SimpleDateFormat(
			"dd.MM.yyyy HH:mm:ss");
	static public long MILLIS_IN_DAY = 60 * 60 * 24 * 1000;

	static public String getDateAsString(Date date) {
		return format.format(date);
	}

	static public Date getDate(String date) {
		try {
			return format.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	static public Date getDateFromCal4JDateString(String date) {
		try {
			return new DateTime(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	static public DateTime getCal4JDateTimeFromString(String dateAsString) {
		Date date = getDate(dateAsString);
		DateTime dateTime = new DateTime(date);
		return dateTime;
	}

	static public Date getDateWithoutTime(Date date) {
		//		long time = date.getTime();
		//		long dateOnly = (time / MILLIS_IN_DAY) * MILLIS_IN_DAY + MILLIS_IN_DAY;
		//		Date clearDate = new Date(dateOnly);
		String string = getDateAsString(date).substring(0,
				"xx.xx.xxxx".length())
				+ " 00:00:00";
		return getDate(string);
	}

	static public Date addDays(Date date, int numOfDays) {
		return new Date(date.getTime() + (numOfDays * MILLIS_IN_DAY));
	}

	static public Date subDays(Date date, int numOfDays) {
		return new Date(date.getTime() - (numOfDays * MILLIS_IN_DAY));
	}

	static public String fromHexToString(String str) {
		StringBuilder stb = new StringBuilder();
		for (int i = 0; i < str.length(); i += 2) {
			stb.append((char) Integer.parseInt(str.substring(i, i + 2), 16));
		}
		return stb.toString();
	}
}
