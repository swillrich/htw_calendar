package de.svenwillrich.htw.spezprog.app.control;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import de.svenwillrich.htw.spezprog.app.R;
import de.svenwillrich.htw.spezprog.app.model.Day;
import de.svenwillrich.htw.spezprog.app.model.DayList;

public class DayListFragmentAdapter extends ArrayAdapter {

	private DayList list;
	private Context context;

	public DayListFragmentAdapter(Context context, DayList list) {
		super(context, R.layout.item_daylist, list);
		this.list = list;
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.item_daylist,
				(ViewGroup) parent.findViewById(R.id.fragment_daylist), false);
		TextView textView = (TextView) layout.findViewById(R.id.day);
		Day day = getList().get(position);
		String text = day.getDateFormatted(Day.WEEKDAY_SHORT) + ", "
				+ day.getDateFormatted(Day.DATE_SHORT);
		textView.setText(text);
		return layout;
	}

	public DayList getList() {
		return list;
	}
}
