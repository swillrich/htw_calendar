/**
 * 
 */
package de.svenwillrich.htw.spezprog.android.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import de.svenwillrich.htw.spezprog.android.R;
import de.svenwillrich.htw.spezprog.android.control.cal.CalendarAdapter;
import de.svenwillrich.htw.spezprog.android.control.cal.IDataReceiverAttampter;
import de.svenwillrich.htw.spezprog.android.model.logic.SettingHelper;
import de.svenwillrich.htw.spezprog.android.view.activity.CalActivity;
import de.svenwillrich.htw.spezprog.android.view.dialog.ReceivedDataProgressDialog;

/**
 * @author Sven Willrich
 * Spezielle Programmierung: Android
 * Datum: 25.10.2013
 */

public class ReceivedDataUI {
	private Activity activity;
	private ProgressDialog dialog;
	private final static int MSG_UPDATE = 1;
	private final static int MSG_SUCCESS = 2;
	private final static int MSG_FAILURE = 3;
	private final static int MSG_BADPASSWORD = 4;
	private final static String MSG_KEY = "msg";
	private final static String MSG_KEY_DATA_SUCCESS = "counter";

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			Log.i(this.getClass().getName(), "msg handler called with key "
					+ msg.getData().getInt(MSG_KEY));
			switch (msg.getData().getInt(MSG_KEY)) {
			case MSG_UPDATE:
				updateDialog(msg.getData().getInt(MSG_KEY_DATA_SUCCESS));
				break;
			case MSG_SUCCESS:
				finish();
				Toast.makeText(activity, R.string.data_loading_successfully,
						Toast.LENGTH_LONG).show();
				new SettingHelper(activity).commitCalendar(CalendarAdapter
						.getInstance().getCalendarAsString());
				CalendarAdapter.getInstance().load(activity);
				((CalActivity) activity).refresh(false, true, null);
				break;
			case MSG_FAILURE:
				showFailureDialog();
				break;
			case MSG_BADPASSWORD:
				badPasswordToast();
				break;
			}
		}

	};

	public void badPasswordToast() {
		finish();
		Toast.makeText(activity, R.string.bad_password, Toast.LENGTH_LONG)
				.show();
	}

	public ReceivedDataUI(Activity activity) {
		this.activity = activity;
	}

	public void startProcess() {
		updateDialog(1);
		CalendarAdapter instance = CalendarAdapter.getInstance();
		instance.setAttampter(new Attampter());
		instance.update();
	}

	private void updateDialog(int counter) {
		finish();
		dialog = new ReceivedDataProgressDialog().getDialog(activity, counter);
		dialog.show();
	}

	private void finish() {
		if (dialog != null) {
			dialog.dismiss();
		}
	}

	private void showFailureDialog() {
		finish();
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
					}
				});
		builder.setTitle(activity
				.getString(R.string.dialog_data_download_failure));
		builder.create().show();
	}

	private class Attampter implements IDataReceiverAttampter {

		private Message getMessage(int variante) {
			Message msg = new Message();
			Bundle bundle = new Bundle();
			bundle.putInt(MSG_KEY, variante);
			msg.setData(bundle);
			return msg;
		}

		public void onFailure(int counter) {
			if (counter > IDataReceiverAttampter.ATTAMPTS) {
				Log.i(this.getClass().getName(), "failed to load data");
				handler.sendMessage(getMessage(MSG_FAILURE));
			} else {
				Log.i(this.getClass().getName(), counter
						+ " fails, next attampt and update dialog");
				Message message = getMessage(MSG_UPDATE);
				message.getData().putInt(MSG_KEY_DATA_SUCCESS, counter);
				handler.sendMessage(message);
			}
		}

		public void onSuccess() {
			handler.sendMessage(getMessage(MSG_SUCCESS));
		}

		public void onFailureBadPassword() {
			handler.sendMessage(getMessage(MSG_BADPASSWORD));
		}
	}

}
