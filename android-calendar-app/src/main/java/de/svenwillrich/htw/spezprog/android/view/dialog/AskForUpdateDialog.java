/**
 * 
 */
package de.svenwillrich.htw.spezprog.android.view.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;
import de.svenwillrich.htw.spezprog.android.R;
import de.svenwillrich.htw.spezprog.android.control.cal.CalendarAdapter;
import de.svenwillrich.htw.spezprog.android.view.ReceivedDataUI;

/**
 * @author Sven Willrich
 * Spezielle Programmierung: Android
 * Datum: 27.10.2013
 */

public class AskForUpdateDialog {
	public void askForUpdate(final Activity activity) {
		if (CalendarAdapter.getInstance().areUsernamePasswordGiven()) {
			AlertDialog.Builder builder = new AlertDialog.Builder(activity);
			builder.setNegativeButton(activity.getString(R.string.nein),
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();

						}
					});
			builder.setPositiveButton(activity.getString(R.string.ok),
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							ReceivedDataUI receivedDataUI = new ReceivedDataUI(
									activity);
							receivedDataUI.startProcess();
						}
					});
			builder.setTitle(activity
					.getString(R.string.should_runthrough_update));
			builder.create().show();
		} else {
			Toast.makeText(activity, R.string.no_settings, Toast.LENGTH_LONG)
					.show();
		}
	}
}
