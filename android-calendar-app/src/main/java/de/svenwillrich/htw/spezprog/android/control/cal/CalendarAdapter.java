/**
 * 
 */
package de.svenwillrich.htw.spezprog.android.control.cal;

import android.util.Log;
import de.svenwillrich.htw.spezprog.exception.DataCannotReceivedException;
import de.svenwillrich.htw.spezprog.exception.LoginDataIncorrectException;
import de.svenwillrich.htw.spezprog.logik.CalDAO;
import de.svenwillrich.htw.spezprog.logik.ICal;

/**
 * @author Sven Willrich
 * Spezielle Programmierung: Android
 * Datum: 23.10.2013
 */

public class CalendarAdapter {
	private static CalendarAdapter instance = null;
	private ICal calendar = null;
	private boolean isUpToDate = false;
	private IDataReceiverAttampter attampter;

	public void setAttampter(IDataReceiverAttampter attampter) {
		this.attampter = attampter;
	}

	private CalendarAdapter() {
		calendar = new CalDAO();
	}

	public static CalendarAdapter getInstance() {
		if (instance == null) {
			instance = new CalendarAdapter();
		}
		return instance;
	}

	public ICal getCalendar() {
		return calendar;
	}

	public void update() {
		Runnable calendarUpdater = new Runnable() {

			public void run() {
				Log.i(this.getClass().getName(), "try to load calendar data");
				for (int attampts = 1; (attampts <= IDataReceiverAttampter.ATTAMPTS && !isUpToDate);) {
					try {
						Log.i(this.getClass().getName(), attampts
								+ ". try starts");
						calendar.updateCalendar("s0534022".toCharArray(),
								"SessionName123".toCharArray());
						Log.i(this.getClass().getName(),
								"calendar data loading successfully");
						attampter.onSuccess();
						isUpToDate = true;
					} catch (LoginDataIncorrectException e) {
						Log.i(this.getClass().getName(), "login data incorrect");
						attampts = IDataReceiverAttampter.ATTAMPTS + 1;
						//TODO: implement behavior
					} catch (DataCannotReceivedException e) {
						attampts = attampts + 1;
						Log.i(this.getClass().getName(), attampts + ". try");
						attampter.onFailure(attampts);
					}
				}
			}
		};
		Thread thread = new Thread(calendarUpdater);
		thread.start();
	}

	public boolean isUpToDate() {
		return isUpToDate;
	}

	public void waitForUpdate(int howLongInSeconds) {
		while (!isUpToDate) {
			howLongInSeconds = howLongInSeconds - 100;
			if (howLongInSeconds <= 0) {
				break;
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
