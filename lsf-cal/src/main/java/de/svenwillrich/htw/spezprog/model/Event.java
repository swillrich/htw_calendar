/**
 * 
 */
package de.svenwillrich.htw.spezprog.model;

import java.util.Date;

import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.property.RRule;
import de.svenwillrich.htw.spezprog.logik.Utils;

/**
 * @author Sven Willrich
 * Spezielle Programmierung: Android
 * Datum: 20.10.2013
 * Beschreibung: Ein Event, das alle seine Eigenschaften speichert
 */

public class Event implements Comparable<Event> {

	private Date from;
	private Date to;
	private String location;
	private String title;
	private String categorie;
	private String description;
	private String uID;

	public void setuID(String uID) {
		this.uID = uID;
	}

	public String getuID() {
		return uID;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setCategorie(String categorie) {
		this.categorie = categorie;
	}

	public Date getFrom() {
		return from;
	}

	public Date getTo() {
		return to;
	}

	public String getLocation() {
		return location;
	}

	public String getTitle() {
		return title;
	}

	public void setFrom(Date from) {
		this.from = from;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setTo(Date to) {
		this.to = to;
	}

	public String getFromFormatted() {
		return Utils.getDateAsString(from);
	}

	public String getToFromatted() {
		return Utils.getDateAsString(to);
	}

	public String getCategorie() {
		return categorie;
	}

	/**
	 * Das absolute Ende eins Events (Periode)
	 */
	public Date getEndOfPeriod() {
		try {
			return (((RRule) ((HTWEvent) this).getVEvent().getProperty(
					Property.RRULE)).getRecur()).getUntil();
		} catch (NullPointerException e) {
			return null;
		}
	}

	@Override
	public boolean equals(Object obj) {
		return ((Event) obj).getuID().equals(this.getuID());
	}

	/**
	 * @author Sven Willrich
	 * Spezielle Programmierung: Android
	 * Datum: 29.10.2013
	 * Beschreibung: Ein SingleEvent, das alle Einzelevents einer Periode speichert
	 */
	public class SingleEvent implements Comparable<SingleEvent> {
		private Event parent = Event.this;
		private Date start;
		private Date end;

		@Override
		public String toString() {
			return parent.getTitle() + "\t\t FROM: "
					+ Utils.getDateAsString(from) + "\t\t TO: "
					+ Utils.getDateAsString(to);
		}

		public Date getEnd() {
			return end;
		}

		public Date getStart() {
			return start;
		}

		public void setEnd(Date end) {
			this.end = end;
		}

		public void setStart(Date start) {
			this.start = start;
		}

		public Event getParent() {
			return parent;
		}

		public int compareTo(SingleEvent o) {
			if (o.getStart().getTime() < this.getStart().getTime()) {
				return -1;
			} else if (o.getEnd().getTime() > this.getEnd().getTime()) {
				return 1;
			} else {
				return 0;
			}
		}
	}

	public int compare(Event o1, Event o2) {
		if (o1.getFrom().getTime() < o2.getFrom().getTime()) {
			return -1;
		} else if (o1.getFrom().getTime() > o2.getFrom().getTime()) {
			return 1;
		} else {
			return 0;
		}
	}

	static public enum Prop {
		START, END;
	}

	public int compareTo(Event o) {
		if (o.getFrom().getTime() < this.getFrom().getTime()) {
			return -1;
		} else if (o.getTo().getTime() > this.getTo().getTime()) {
			return 1;
		} else {
			return 0;
		}
	}

}
