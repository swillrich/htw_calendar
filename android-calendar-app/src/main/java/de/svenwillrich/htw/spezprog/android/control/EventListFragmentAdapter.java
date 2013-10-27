package de.svenwillrich.htw.spezprog.android.control;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import de.svenwillrich.htw.spezprog.android.R;
import de.svenwillrich.htw.spezprog.model.Event;

public class EventListFragmentAdapter extends ArrayAdapter {

	private List<Event> list;
	private Context context;

	public EventListFragmentAdapter(Context context, List<Event> list) {
		super(context, R.layout.item_eventlist, list);
		this.list = list;
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater
				.inflate(R.layout.item_eventlist, (ViewGroup) parent
						.findViewById(R.id.fragment_eventlist), false);
		TextView textView = (TextView) layout.findViewById(R.id.event_title);
		Event event = list.get(position);
		if (event != null) {
			textView.setText(event.getTitle());
		}
		return layout;
	}

	public List<Event> getList() {
		return list;
	}
}
