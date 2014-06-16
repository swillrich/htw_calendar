/**
 * 
 */
package de.svenwillrich.htw.spezprog.logik;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import de.svenwillrich.htw.spezprog.exception.LoginDataIncorrectException;

/**
 * @author Sven Willrich Spezielle Programmierung: Android Datum: 18.10.2013
 * 
 */
/**
 * @author Sven Willrich
 * Spezielle Programmierung: Android
 * Datum: 29.10.2013
 */
public class WebAccess {

	private final static String URL_START_LSF = "https://lsf.htw-berlin.de/qisserver/rds?state=user&type=1";
	private final static String URL_CORRECT_VIEW = "https://lsf.htw-berlin.de/qisserver/rds?state=wplan&act=&pool=&show=plan&P.vx=kurz";

	/**
	 * Speichert das LSF-Cookie
	 */
	AbstractMap.SimpleEntry<String, String> cookieValue = new SimpleEntry<String, String>(
			"JSESSIONID", "");
	/**
	 * Speichert die Daten für den Login in einer Map
	 */
	private final static Map<String, String> loginMap;
	/**
	 * Speichert die Parameter für die Calendar-Ansicht in einer Map
	 */
	private final static Map<String, String> calendarViewMap;
	/**
	 * Timeout für den Lese-Vorgang
	 */
	private static final int TIMEOUT = 60 * 1000;

	// Maps werden initialisiert
	static {
		loginMap = new HashMap<String, String>();
		loginMap.put("username", "");
		loginMap.put("password", "");
		loginMap.put("submit", "Jetzt+einloggen");
		loginMap.put("state", "user");
		loginMap.put("type", "1");

		calendarViewMap = new HashMap<String, String>();
		calendarViewMap.put("state", "wplan");
		calendarViewMap.put("act", "");
		calendarViewMap.put("pool", "Jetzt+einloggen");
		calendarViewMap.put("show", "plan");
		calendarViewMap.put("P.vx", "kurz");
		calendarViewMap.put("week", "-2");
		calendarViewMap.put("P.vx", "kurz");
		calendarViewMap.put("work", "go");
	}

	public WebAccess() {

	}

	/**
	 * Alle Schritte für den Download des Calendars werden automatisch durchlaufen
	 * login -> Calendar-Ansicht laden -> Inhalt laden -> Inhalt zurückgegeben
	 */
	public String doAutomatically(String username, String pw)
			throws IOException {
		System.out.println("WebAccess: attampt to run through automatically");
		String content = getICalContent(getCalendarWebView(login(username, pw)));
		System.out
				.println("WebAccess: content successfully getted, it contains "
						+ content.length() + " chars");
		return content;
	}

	/**
	 * Login bei der HTW-Seite anhand Nutzername und PW
	 */
	public Document login(String username, String pw) throws IOException {
		loginMap.put("username", username);
		loginMap.put("password", pw);
		System.out
				.println("WebAccess: attampt to login with given username and password");

		Response response = Jsoup.connect(URL_START_LSF).timeout(TIMEOUT)
				.data(loginMap).method(Method.POST).execute();
		cookieValue.setValue(response.cookie(cookieValue.getKey()));
		return response.parse();
	}

	/**
	 * Gibt die Website als Document zurück, die den Calendar enthält
	 */
	public Document getCalendarWebView(Document doc) throws IOException {
		System.out.println("WebAccess: attampt to get view with calendar");
		Document receivedDoc = Jsoup.connect(URL_CORRECT_VIEW).timeout(TIMEOUT)
				.cookie(cookieValue.getKey(), cookieValue.getValue())
				.data(calendarViewMap).method(Method.POST).execute().parse();

		return receivedDoc;
	}

	/**
	 * Anhand der Calendar-Website wird der Link für den Download der iCal-Datei gesucht,
	 * der Download gestartet und die heruntergeladenen Daten zurückgegeben
	 */
	public String getICalContent(Document doc) throws IOException {
		System.out
				.println("WebAccess: attampt to get content of iCal document");
		Response calContent = null;

		String calendarLink = doc.getElementsByAttributeValueContaining("href",
				"moduleCall=iCalendarPlan").attr("href");
		calContent = null;
		try {
			calContent = Jsoup.connect(calendarLink).timeout(TIMEOUT)
					.cookie(cookieValue.getKey(), cookieValue.getValue())
					.method(Method.POST).execute();
		} catch (IllegalArgumentException e) {
			throw new LoginDataIncorrectException();
		}

		return calContent.body();
	}
}
