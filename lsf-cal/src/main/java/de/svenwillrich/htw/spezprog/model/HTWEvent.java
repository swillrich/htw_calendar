/**
 * 
 */
package de.svenwillrich.htw.spezprog.model;

import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.VEvent;
import de.svenwillrich.htw.spezprog.logik.Utils;

/**
 * @author Sven Willrich
 * Spezielle Programmierung: Android
 * Datum: 19.10.2013
 */

public class HTWEvent extends Event {
	private Categorie categorie;
	private VEvent vEvent;

	public HTWEvent acceptVEvent(VEvent vEvent) {
		this.vEvent = vEvent;
		String categorieValue = getVEvent().getProperty(Property.CATEGORIES)
				.getValue();
		this.categorie = HTWEvent.Categorie
				.getCategorieByDescription(categorieValue);
		super.setCategorie(categorie.getDescription());
		setTitle(getVEvent().getSummary().getValue());
		setFrom(Utils.getDateFromCal4JDateString(getVEvent().getProperty(
				Property.DTSTART).getValue()));
		setTo(Utils.getDateFromCal4JDateString(getVEvent().getProperty(
				Property.DTEND).getValue()));
		setLocation(getVEvent().getLocation().getValue());
		super.setuID(vEvent.getProperty(Property.UID).getValue());
		return this;
	}

	public VEvent getVEvent() {
		return vEvent;
	}

	static enum Categorie {
		Uebung("Übung"), SUUE("seminaristischer Unterricht/Übung"), SePr(
				"Seminar/Projektseminar"), SU("seminaristischer Unterricht");
		private String name;

		private Categorie(String string) {
			name = string;
		}

		public String getDescription() {
			return name;
		}

		static public Categorie getCategorieByDescription(String description) {
			if (description.equals(Categorie.Uebung.getDescription())) {
				return Uebung;
			} else if (description.equals(Categorie.SUUE.getDescription())) {
				return SUUE;
			} else if (description.equals(Categorie.SePr.getDescription())) {
				return SePr;
			} else if (description.equals(Categorie.SU.getDescription())) {
				return SU;
			} else {
				return null;
			}
		}
	}
}
