/**
 * 
 */
package de.svenwillrich.htw.spezprog.android.control.cal;

import android.content.Context;
import android.util.Log;
import de.svenwillrich.htw.spezprog.android.model.Setting;
import de.svenwillrich.htw.spezprog.android.model.logic.SettingHelper;
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
	private boolean isCalendarLoaded = false;
	private IDataReceiverAttampter attampter;
	private Context context;
	private Setting setting = null;
	private String calendarAsString;

	public void setAttampter(IDataReceiverAttampter attampter) {
		this.attampter = attampter;
	}

	public String getCalendarAsString() {
		return calendarAsString;
	}

	public void setCalendarAsString(String calendarAsString) {
		this.calendarAsString = calendarAsString;
	}

	public CalendarAdapter load(Context context) {
		this.context = context;
		SettingHelper helper = new SettingHelper(context);
		setting = helper.read();
		calendar = new CalDAO();
		if (areUsernamePasswordGiven()) {
			if (setting.getCalendar() != null) {
				Log.i(this.getClass().getName(),
						"calendar adapter load: calendar data loaded");
				setCalendarAsString(new String(setting.getCalendar()));
				calendar.loadCalendar(getCalendarAsString());
				isCalendarLoaded = true;
			} else {
				Log.i(this.getClass().getName(),
						"calendar adapter load: calendar data NOT loaded");
				isCalendarLoaded = false;
			}
		}
		return this;
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
		if (context == null) {
			throw new NullPointerException("context is null");
		}
		Runnable calendarUpdater = new Runnable() {

			public void run() {
				Log.i(this.getClass().getName(), "try to load calendar data");
				isCalendarLoaded = false;
				for (int attampts = 1; (attampts <= IDataReceiverAttampter.ATTAMPTS && !isCalendarLoaded);) {
					try {
						Log.i(this.getClass().getName(), attampts
								+ ". try starts");
						SettingHelper helper = new SettingHelper(context);
						char[] username = setting.getUsername();
						char[] password = helper.getPasswordAsChar(setting
								.getPassword());
						setCalendarAsString(calendar.updateCalendar(username,
								password));
						Log.i(this.getClass().getName(),
								"calendar data loading successfully");
						attampter.onSuccess();
						isCalendarLoaded = true;
						helper.commitCalendar(getCalendarAsString());
					} catch (LoginDataIncorrectException e) {
						Log.i(this.getClass().getName(), "login data incorrect");
						attampts = IDataReceiverAttampter.ATTAMPTS + 1;
						attampter.onFailureBadPassword();
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

	public boolean isCalendarLoaded() {
		return isCalendarLoaded;
	}

	public boolean areUsernamePasswordGiven() {
		Setting setting = null;
		if (this.setting == null) {
			setting = new SettingHelper(context).read();
		} else {
			setting = this.setting;
		}
		if (setting == null) {
			return false;
		}
		if (setting.getPassword().length > 0
				&& setting.getUsername().length > 0) {
			return true;
		} else {
			return false;
		}
	}

	public void waitForUpdate(int howLongInSeconds) {
		while (!isCalendarLoaded) {
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