package de.svenwillrich.htw.spezprog.android.model.logic;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import android.content.Context;

import com.google.gson.Gson;

import de.svenwillrich.htw.spezprog.android.model.Setting;

public class SettingHelper {
	private Context context;

	public SettingHelper(Context context) {
		this.context = context;
	}

	public Setting read() {
		byte[] buffer = null;
		try {
			FileInputStream input = context.openFileInput(Setting.FILE_NAEME);
			buffer = new byte[input.available()];
			input.read(buffer);
			input.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		char[] bufferAsChar = convertToChar(buffer);
		Setting setting = new Gson().fromJson(new String(bufferAsChar),
				Setting.class);
		return setting;
	}

	public boolean save(String calAsString, char[] username, char[] password) {
		Setting setting = new Setting();
		setting.setLastUpdate(new Date());
		setting.setUsername(username);
		setting.setPassword(convertToByte(password));
		setting.setCalendar(calAsString.toCharArray());

		String json = new Gson().toJson(setting);
		byte[] jsonAsBytes = convertToByte(json.toCharArray());

		try {
			FileOutputStream outputStream = context.openFileOutput(
					Setting.FILE_NAEME, Context.MODE_PRIVATE);
			outputStream.write(jsonAsBytes);
			outputStream.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean commitCalendar(String calAsString) {
		Setting read = read();
		char[] username = read.getUsername();
		char[] password = convertToChar(read.getPassword());
		return save(calAsString, username, password);
	}

	public char[] getPasswordAsChar(byte[] bytes) {
		return convertToChar(bytes);
	}

	private byte[] convertToByte(char[] chars) {
		try {
			return new String(chars).getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	private char[] convertToChar(byte[] bytes) {
		try {
			return new String(bytes, "UTF-8").toCharArray();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
}
