/**
 * 
 */
package de.svenwillrich.htw.spezprog.app.view;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import de.svenwillrich.htw.spezprog.app.R;
import de.svenwillrich.htw.spezprog.app.logik.CalFactory;
import de.svenwillrich.htw.spezprog.app.logik.IDataReceiverAttampter;

/**
 * @author Sven Willrich
 * Spezielle Programmierung: Android
 * Datum: 25.10.2013
 */

public class ReceivedDataUI {
	private Context c;
	private ProgressDialog dialog;

	public ReceivedDataUI(Context c) {
		this.c = c;
	}

	public void startProcess() {
		updateDialog(1);
		CalFactory instance = CalFactory.getInstance();
		instance.setAttampter(new Attampter());
		instance.update();
	}

	private void updateDialog(int counter) {
		finish();
		dialog = new ProgressDialog(c);
		dialog.setMessage(c.getString(R.string.dialog_received_data, counter));
		dialog.show();
	}

	private void finish() {
		if (dialog != null) {
			dialog.dismiss();
		}
	}

	private void showFailureDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(c);
		builder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
					}
				});
		builder.setTitle(c.getString(R.string.dialog_data_download_failure));
		builder.create().show();
	}

	private class Attampter implements IDataReceiverAttampter {

		public void onFailure(int counter) {
			if (counter > IDataReceiverAttampter.ATTAMPTS) {
				Log.i(this.getClass().getName(), "failed to load data");
				showFailureDialog();
			} else {
				Log.i(this.getClass().getName(), counter
						+ " fails, next attampt and update dialog");
				updateDialog(counter);
			}
		}

		public void onSuccess() {
			finish();
		}
	}
}
