/**
 * 
 */
package de.svenwillrich.htw.spezprog.logik;

import java.io.IOException;

import junit.framework.Assert;

import org.junit.Test;

import de.svenwillrich.htw.spezprog.exception.LoginDataIncorrectException;

/**
 * @author Sven Willrich Spezielle Programmierung: Android Datum: 18.10.2013
 * 
 */
public class WebAccessTest {

	private final static String USERNAME = "7330353334303232";
	private final static String PASSWORD = "53657373696f6e4e616d65313233";

	@Test
	public void testReadOut() {
		WebAccess webA = new WebAccess();
		String str = null;
		try {
			str = webA.doAutomatically(Utils.fromHexToString(USERNAME),
					Utils.fromHexToString(PASSWORD));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Assert.assertTrue(str.length() > 0);
	}

	@Test(expected = LoginDataIncorrectException.class)
	public void testIncorrectLoginData() {
		WebAccess webA = new WebAccess();
		try {
			webA.doAutomatically("asd", "ad2");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
