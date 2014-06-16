/**
 * 
 */
package de.svenwillrich.htw.spezprog.android.view.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import de.svenwillrich.htw.spezprog.android.R;
import de.svenwillrich.htw.spezprog.android.control.cal.CalendarAdapter;
import de.svenwillrich.htw.spezprog.android.model.Mock;
import de.svenwillrich.htw.spezprog.android.model.logic.SettingHelper;
import de.svenwillrich.htw.spezprog.logik.Utils;

/**
 * @author Sven Willrich
 * Spezielle Programmierung: Android
 * Datum: 27.10.2013
 */

public class SettingsDialog {

	public Dialog getDialog(final Activity activity) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		final View view = activity.getLayoutInflater().inflate(
				R.layout.dialog_settings, null);
		builder.setView(view);
		builder.setPositiveButton(R.string.save,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						char[] username = charsFromEditText((EditText) view
								.findViewById(R.id.edit_username));
						char[] password = charsFromEditText((EditText) view
								.findViewById(R.id.edit_password));
						boolean save = new SettingHelper(activity).save(null,
								username, password);
						dialog.dismiss();
						int resToMsg = 0;
						if (save) {
							resToMsg = R.string.settings_save_success_msg;
							CalendarAdapter.getInstance().load(activity);
							new AskForUpdateDialog().askForUpdate(activity);
						} else {
							resToMsg = R.string.settings_save_failure_msg;
						}
						Toast.makeText(activity, resToMsg, Toast.LENGTH_LONG)
								.show();
					}
				});

		builder.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		init(view);
		AlertDialog dialog = builder.create();
		return dialog;
	}

	private char[] charsFromEditText(EditText editText) {
		return editText.getText().toString().toCharArray();
	}

	private void init(View layout) {
		EditText username = (EditText) layout.findViewById(R.id.edit_username);
		username.setText(Utils.fromHexToString(Mock.USERNAME));
		EditText pw = (EditText) layout.findViewById(R.id.edit_password);
		pw.setText(Utils.fromHexToString(Mock.PASSWORD));
	}
}
