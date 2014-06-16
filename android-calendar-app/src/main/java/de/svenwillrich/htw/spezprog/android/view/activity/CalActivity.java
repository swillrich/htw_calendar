/**
 * 
 */
package de.svenwillrich.htw.spezprog.android.view.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import de.svenwillrich.htw.spezprog.android.R;
import de.svenwillrich.htw.spezprog.android.control.cal.CalendarAdapter;
import de.svenwillrich.htw.spezprog.android.model.logic.SettingHelper;
import de.svenwillrich.htw.spezprog.android.view.dialog.AskForUpdateDialog;
import de.svenwillrich.htw.spezprog.android.view.dialog.SettingsDialog;
import de.svenwillrich.htw.spezprog.android.view.fragment.DayListFragment;
import de.svenwillrich.htw.spezprog.android.view.fragment.EventListFragment;
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
		CalendarAdapter.getInstance().load(this);
		refresh(false, true, null);
	}

	public boolean isLandScape() {
		int orientation = getResources().getConfiguration().orientation;
		if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
			return true;
		} else {
			return false;
		}
	}

	public void onChange(Date date) {
		refresh(false, false, date);
	}

	public void refresh(boolean isOnBackPressed, boolean isActivityStart,
			Date date) {
		FragmentTransaction transaction = getFragmentManager()
				.beginTransaction();

		CalendarAdapter.getInstance().load(this);
		if (!CalendarAdapter.getInstance().isCalendarLoaded()) {
			Toast.makeText(this, R.string.no_cal_data_there, Toast.LENGTH_LONG)
					.show();

			int[] fragmentIds = new int[] { R.id.fragment_container,
					R.id.fragment_daylist, R.id.fragment_eventlist };
			for (int fragmentId : fragmentIds) {
				Fragment fragment = getFragmentManager().findFragmentById(
						fragmentId);
				if (fragment != null) {
					transaction.remove(fragment);
				}
			}
			transaction.commit();
			return;
		}

		DayListFragment dayListFragment = new DayListFragment();
		dayListFragment.setDayChangedListener(this);

		EventListFragment eventListFragment = new EventListFragment();

		if (date == null) {
			date = EventListFragment.DEFAULT_DATE;
		}
		List<Event> events = null;
		try {
			CalendarAdapter calendarAdapter = CalendarAdapter.getInstance();
			ICal calendar = calendarAdapter.getCalendar();
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
		Fragment fragment = getFragmentManager().findFragmentById(
				R.id.fragment_container);
		if (fragment instanceof EventListFragment) {
			refresh(true, false, null);
		} else {
			finish();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_item_settings:
			new SettingsDialog().getDialog(this).show();
			return true;
		case R.id.menu_item_update:
			new AskForUpdateDialog().askForUpdate(this);
			return true;
		case R.id.menu_item_remove:
			if (new SettingHelper(this).delete()) {
				Toast.makeText(this, R.string.deleting_successfully,
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(this, R.string.deleting_failed,
						Toast.LENGTH_LONG).show();
			}
			CalendarAdapter.getInstance().load(this);
			refresh(false, true, null);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
