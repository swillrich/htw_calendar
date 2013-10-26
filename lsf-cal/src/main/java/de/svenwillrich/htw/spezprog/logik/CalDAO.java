package de.svenwillrich.htw.spezprog.logik;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.filter.Filter;
import net.fortuna.ical4j.filter.PeriodRule;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.Period;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.VEvent;
import de.svenwillrich.htw.spezprog.exception.CalDataNotLoadedException;
import de.svenwillrich.htw.spezprog.exception.DataCannotReceivedException;
import de.svenwillrich.htw.spezprog.exception.LoginDataIncorrectException;
import de.svenwillrich.htw.spezprog.model.Event;
import de.svenwillrich.htw.spezprog.model.HTWEvent;

public class CalDAO implements ICal {
	Calendar cal;

	public void loadCalendar(String content) {
		CalendarBuilder builder = new CalendarBuilder();
		try {
			InputStream stream = new ByteArrayInputStream(
					content.getBytes("UTF-8"));
			cal = builder.build(stream);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserException e) {
			e.printStackTrace();
		}
	}

	public Calendar getCal() {
		if (cal == null) {
			throw new CalDataNotLoadedException();
		}
		return cal;
	}

	public void setCal(Calendar cal) {
		this.cal = cal;
	}

	public List<Event> convertVEventListToEventList(List<VEvent> vEvents) {
		List<Event> events = new ArrayList<Event>();
		Iterator iterator = vEvents.iterator();
		while (iterator.hasNext()) {
			Component component = (Component) iterator.next();
			if (component.getName().equals("VEVENT")) {
				events.add(new HTWEvent().acceptVEvent((VEvent) component));
			}
		}
		return events;
	}

	public List<Event> getEvents() {
		return convertVEventListToEventList(getCal().getComponents());
	}

	public List<Property> getPropertiesFromEvent(VEvent event) {
		List<Property> properties = new ArrayList<Property>();
		Iterator iterator = event.getProperties().iterator();
		while (iterator.hasNext()) {
			properties.add((Property) iterator.next());
		}
		return properties;
	}

	public List<Event> getEventsFromDate(Date date)
			throws CalDataNotLoadedException {
		return getEventsBetweenDates(date, Utils.addDays(date, 1));
	}

	public List<Event> getEventsBetweenDates(Date from, Date to)
			throws CalDataNotLoadedException {
		if (cal == null) {
			throw new CalDataNotLoadedException();
		}
		List<Event> eventList = new ArrayList<Event>();
		if (from == null) {
			from = new Date();
		}
		if (to == null) {
			to = new Date();
		}
		to = Utils.getDateWithoutTime(to);
		from = Utils.getDateWithoutTime(from);

		Period period = new Period(new DateTime(from), new DateTime(to));
		Filter filter = new Filter(new PeriodRule(period));
		List<VEvent> events = (List<VEvent>) filter.filter(getCal()
				.getComponents(Component.VEVENT));

		return convertVEventListToEventList(events);
	}

	public Event getFullEvent(Event event) {
		for (Event e : getEvents()) {
			if (event.equals(e)) {
				return e;
			}
		}
		return null;
	}

	public Map<Event.Prop, Date> getDatesFromEvent(Event e) {
		HashMap<Event.Prop, Date> map = new HashMap<Event.Prop, Date>();
		VEvent vEvent = ((HTWEvent) e).getVEvent();
		Date start = null;
		Date end = null;
		try {
			start = new DateTime(vEvent.getProperty(Property.DTSTART)
					.getValue());
			end = new DateTime(vEvent.getProperty(Property.DTEND).getValue());
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		map.put(Event.Prop.START, start);
		map.put(Event.Prop.END, end);
		return map;
	}

	public void printDate(Event e) {
		Map<Event.Prop, Date> dates = getDatesFromEvent(e);
		System.out.println("START "
				+ Utils.getDateAsString(dates.get(Event.Prop.START)));
		System.out.println("Ende "
				+ Utils.getDateAsString(dates.get(Event.Prop.END)));
	}

	public void updateCalendar(char[] username, char[] password)
			throws LoginDataIncorrectException, DataCannotReceivedException {
		System.out.println("CalDAO: attampt to update the calendar");
		try {
			WebAccess webA = new WebAccess();
			String content = webA.doAutomatically(new String(username),
					new String(password));
			System.out.println("CalDAO: content string was loaded successfully");
			loadCalendar(content);
			System.out.println("CalDAO: calendar was setted");
		} catch (SocketTimeoutException e) {
			throw new DataCannotReceivedException();
		} catch (IllegalArgumentException e) {
			throw new LoginDataIncorrectException();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
