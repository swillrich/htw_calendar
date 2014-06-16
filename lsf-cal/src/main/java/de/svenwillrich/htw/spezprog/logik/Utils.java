/**
 * 
 */
package de.svenwillrich.htw.spezprog.logik;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.fortuna.ical4j.model.DateTime;

/**
 * @author Sven Willrich
 * Spezielle Programmierung: Android
 * Datum: 19.10.2013
 *
 */
public class Utils {
	/**
	 * Das Default-Format wird festgelegt
	 */
	static private SimpleDateFormat format = new SimpleDateFormat(
			"dd.MM.yyyy HH:mm:ss");
	/**
	 * Die Milisekunden eins Tages
	 */
	static public long MILLIS_IN_DAY = 60 * 60 * 24 * 1000;

	/**
	 * Ein übergebenes Datum wird als String zurückgegeben
	 */
	static public String getDateAsString(Date date) {
		return format.format(date);
	}

	/**
	 * Ein als String vorliegendes Datum wird geparst und als Datum zurückgegeben
	 */
	static public Date getDate(String date) {
		try {
			return format.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Ein cal4j-Datum, das als String vorliegt, wird als Java-Datum zurückgegeben
	 */
	static public Date getDateFromCal4JDateString(String date) {
		try {
			return new DateTime(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Ein Datums-String wird als cal4j-Datum zurückgegeben
	 */
	static public DateTime getCal4JDateTimeFromString(String dateAsString) {
		Date date = getDate(dateAsString);
		DateTime dateTime = new DateTime(date);
		return dateTime;
	}

	/**
	 * Ein Datetime (mit Datum + Zeit) wird ohne Zeit-Angabe zurückgegeben
	 * Aus 12.12.2013 12.32.12 wird bspw. 12.12.2013 00:00:00
	 */
	static public Date getDateWithoutTime(Date date) {
		String string = getDateAsString(date).substring(0,
				"xx.xx.xxxx".length())
				+ " 00:00:00";
		return getDate(string);
	}

	/**
	 * Fügt einem Datum x Tage hinzu ab und gibt es zurück
	 */
	static public Date addDays(Date date, int numOfDays) {
		return new Date(date.getTime() + (numOfDays * MILLIS_IN_DAY));
	}
	
	/**
	 * Zieht von einem Datum x Tage ab und gibt es zurück
	 */
	static public Date subDays(Date date, int numOfDays) {
		return new Date(date.getTime() - (numOfDays * MILLIS_IN_DAY));
	}

	/**
	 * Hex-Zeichenkette zu einer char-Zeichenkette
	 */
	static public String fromHexToString(String str) {
		StringBuilder stb = new StringBuilder();
		for (int i = 0; i < str.length(); i += 2) {
			stb.append((char) Integer.parseInt(str.substring(i, i + 2), 16));
		}
		return stb.toString();
	}
}
