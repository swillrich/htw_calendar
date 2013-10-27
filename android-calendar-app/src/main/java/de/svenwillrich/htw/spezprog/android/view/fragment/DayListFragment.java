package de.svenwillrich.htw.spezprog.android.view.fragment;

import java.util.Date;

import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import de.svenwillrich.htw.spezprog.android.control.DayListFragmentAdapter;
import de.svenwillrich.htw.spezprog.android.model.DayList;
import de.svenwillrich.htw.spezprog.logik.Utils;

public class DayListFragment extends ListFragment {

	private DayChangedListener dayChangedListener;
	private boolean isWithInit = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		DayList dayList = new DayList(Utils.subDays(new Date(), 3),
				Utils.addDays(new Date(), 10));
		DayListFragmentAdapter adapter = new DayListFragmentAdapter(
				getActivity(), dayList);
		setListAdapter(adapter);
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		DayListOnItemClickListener listener = new DayListOnItemClickListener();
		getListView().setOnItemClickListener(listener);
		getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	}

	public class DayListOnItemClickListener implements OnItemClickListener {

		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Date date = ((DayListFragmentAdapter) getListAdapter()).getList()
					.get(arg2).getDateOfDay();
			dayChangedListener.onChange(date);
			Log.i(this.getClass().getName(), "item " + arg2 + " checked");
			Log.i(this.getClass().getName(), Utils.getDateAsString(date)
					+ " was selected");
		}
	}

	public void setDayChangedListener(DayChangedListener dayChangedListener) {
		this.dayChangedListener = dayChangedListener;
	}

	public interface DayChangedListener {
		public void onChange(Date date);
	}

	public void setWithInit(boolean isWithInit) {
		this.isWithInit = isWithInit;
	}

}
