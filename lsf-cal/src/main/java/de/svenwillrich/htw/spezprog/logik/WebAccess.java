/**
 * 
 */
package de.svenwillrich.htw.spezprog.logik;

import java.io.IOException;
import java.net.SocketTimeoutException;
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
public class WebAccess {

	private final static String URL_START_LSF = "https://lsf.htw-berlin.de/qisserver/rds?state=user&type=1";
	private final static String URL_CORRECT_VIEW = "https://lsf.htw-berlin.de/qisserver/rds?state=wplan&act=&pool=&show=plan&P.vx=kurz";

	AbstractMap.SimpleEntry<String, String> cookieValue = new SimpleEntry<String, String>(
			"JSESSIONID", "");
	private final static Map<String, String> loginMap;
	private final static Map<String, String> calendarViewMap;

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

	public String doAutomatically(String username, String pw)
			throws IOException {
		return getICalContent(getCalendarWebView(login(username,
				pw)));
	}

	public Document login(String username, String pw) throws IOException {
		loginMap.put("username", username);
		loginMap.put("password", pw);

		Response response = Jsoup.connect(URL_START_LSF).data(loginMap)
				.method(Method.POST).execute();
		cookieValue.setValue(response.cookie(cookieValue.getKey()));
		return response.parse();
	}

	public Document getCalendarWebView(Document doc) throws IOException {
		Document receivedDoc = Jsoup.connect(URL_CORRECT_VIEW)
				.cookie(cookieValue.getKey(), cookieValue.getValue())
				.data(calendarViewMap).method(Method.POST).execute().parse();

		return receivedDoc;
	}

	public String getICalContent(Document doc) throws IOException {
		Response calContent = null;

		String calendarLink = doc.getElementsByAttributeValueContaining("href",
				"moduleCall=iCalendarPlan").attr("href");
		calContent = null;
		try {
			calContent = Jsoup.connect(calendarLink)
					.cookie(cookieValue.getKey(), cookieValue.getValue())
					.method(Method.POST).execute();
		} catch (IllegalArgumentException e) {
			throw new LoginDataIncorrectException();
		}

		return calContent.body();
	}
}
