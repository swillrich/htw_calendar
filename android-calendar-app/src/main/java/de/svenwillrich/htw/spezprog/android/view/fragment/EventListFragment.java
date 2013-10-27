package de.svenwillrich.htw.spezprog.android.view.fragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.ListFragment;
import android.os.Bundle;
import de.svenwillrich.htw.spezprog.android.control.EventListFragmentAdapter;
import de.svenwillrich.htw.spezprog.model.Event;

public class EventListFragment extends ListFragment {

	private List<Event> list;
	public static Date DEFAULT_DATE;
	
	{
		DEFAULT_DATE = new Date();
	}

	public void setList(List<Event> list) {
		this.list = list;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		if (list == null) {
			list = new ArrayList<Event>();
		}
		EventListFragmentAdapter adapter = new EventListFragmentAdapter(
				getActivity(), list);
		setListAdapter(adapter);
		super.onCreate(savedInstanceState);
	}

}
