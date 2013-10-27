/**
 * 
 */
package de.svenwillrich.htw.spezprog.android.view.dialog;

import android.app.Activity;
import android.app.ProgressDialog;
import de.svenwillrich.htw.spezprog.android.R;

/**
 * @author Sven Willrich
 * Spezielle Programmierung: Android
 * Datum: 27.10.2013
 */

public class ReceivedDataProgressDialog {
	public ProgressDialog getDialog(Activity activity, int counter) {
		ProgressDialog dialog = new ProgressDialog(activity);
		dialog.setMessage(activity.getString(R.string.dialog_received_data,
				counter));
		return dialog;
	}
}
