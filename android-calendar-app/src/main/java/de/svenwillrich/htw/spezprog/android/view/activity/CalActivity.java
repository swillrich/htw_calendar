/**
 * 
 */
package de.svenwillrich.htw.spezprog.android.view.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import de.svenwillrich.htw.spezprog.android.control.cal.CalendarAdapter;
import de.svenwillrich.htw.spezprog.android.view.ReceivedDataUI;
import de.svenwillrich.htw.spezprog.android.view.fragment.DayListFragment;
import de.svenwillrich.htw.spezprog.android.view.fragment.EventListFragment;
import de.svenwillrich.htw.spezprog.android.R;
import de.svenwillrich.htw.spezprog.exception.CalDataNotLoadedException;
import de.svenwillrich.htw.spezprog.logik.ICal;
import de.svenwillrich.htw.spezprog.model.Event;

/**
 * @author Sven Willrich 
 * Spezielle Programmierung: Android 
 * Datum: 20.10.2013
 */

public class CalActivity extends Activity implements
		DayListFragment.DayChangedListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.framelayout_main);

		refresh(false, true, null);
	}

	private void startCalendarUpdateUI() {
		ReceivedDataUI receivedDataUI = new ReceivedDataUI(this);
		receivedDataUI.startProcess();
	}

	public boolean isLandScape() {
		int orientation = getResources().getConfiguration().orientation;
		if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
			return true;
		} else {
			return false;
		}
	}

	public void change(Date date) {
		refresh(false, false, date);
	}

	public void refresh(boolean isOnBackPressed, boolean isActivityStart,
			Date date) {
		if (!CalendarAdapter.getInstance().isUpToDate()) {
			askForUpdate();
			return;
		}

		FragmentTransaction transaction = getFragmentManager()
				.beginTransaction();

		DayListFragment dayListFragment = new DayListFragment();
		dayListFragment.setDayChangedListener(this);

		EventListFragment eventListFragment = new EventListFragment();

		if (date == null) {
			date = EventListFragment.DEFAULT_DATE;
		}
		List<Event> events = null;
		try {
			CalendarAdapter calendarFactory = CalendarAdapter.getInstance();
			ICal calendar = calendarFactory.getCalendar();
			events = calendar.getEventsFromDate(date);
		} catch (CalDataNotLoadedException e) {
			events = new ArrayList<Event>();
			Toast.makeText(this, R.string.no_cal_data_there, Toast.LENGTH_SHORT)
					.show();
		}
		eventListFragment.setList(events);
		Log.i(this.getClass().getName(),
				"the event list contians " + events.size() + " elements");

		if (isLandScape()) {
			transaction.replace(R.id.fragment_daylist, dayListFragment);
			transaction.replace(R.id.fragment_eventlist, eventListFragment);
			dayListFragment.setWithInit(true);
		} else {
			if (isActivityStart || isOnBackPressed) {
				transaction.replace(R.id.fragment_container, dayListFragment);
			} else {
				Log.i(this.getClass().getName(),
						"load eventListFragment in fragment_container");
				transaction.replace(R.id.fragment_container, eventListFragment);
			}
		}

		transaction.commit();
	}

	@Override
	public void onBackPressed() {
		refresh(true, false, null);
	}

	private void askForUpdate() {
		if (!CalendarAdapter.getInstance().isUpToDate()) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setNegativeButton("nein",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();

						}
					});
			builder.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							startCalendarUpdateUI();
						}
					});
			builder.setTitle("holen?");
			builder.create().show();
		}
	}
}