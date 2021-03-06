package com.axelby.podax.ui;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceScreen;

import com.axelby.podax.R;
import com.axelby.podax.Storage;

import java.io.File;
import java.util.ArrayList;

public class PodaxPreferenceFragment extends PreferenceListFragment implements Preference.OnPreferenceChangeListener {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		PreferenceScreen screen = inflateFromResource(R.xml.preferences);

		// properly trim sdcard options
		ListPreference sdcard = (ListPreference) screen.findPreference("storageCard");
		if (sdcard != null) {
			CharSequence[] entries = sdcard.getEntries();
			CharSequence[] values = sdcard.getEntryValues();
			ArrayList<String> validEntries = new ArrayList<String>(entries.length);
			ArrayList<String> validValues = new ArrayList<String>(values.length);
			for (int i = 0; i < values.length; ++i) {
				if (new File(values[i].toString()).exists()) {
					validEntries.add(entries[i].toString());
					validValues.add(values[i].toString());
				}
			}

			if (validValues.size() == 1) {
				sdcard.setEntries(new CharSequence[]{entries[0]});
				sdcard.setEntryValues(new CharSequence[]{values[0]});
				sdcard.setEnabled(false);
			} else {
				sdcard.setEntries(validEntries.toArray(new String[validEntries.size()]));
				sdcard.setEntryValues(validValues.toArray(new String[validValues.size()]));
			}

			String title = getString(R.string.pref_sdcard_title) + ": " + getEntryText(sdcard, sdcard.getValue());
			sdcard.setTitle(title);

			sdcard.setOnPreferenceChangeListener(this);
		}

		setPreferenceScreen(screen);
	}

	private CharSequence getEntryText(ListPreference listPreference, String value) {
		if (listPreference.getEntries() == null)
			return "";
		int index = listPreference.findIndexOfValue(value);
		return listPreference.getEntries()[index];
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		if (preference == null || preference.getKey() == null)
			return false;

		if (preference.getKey().equals("storageCard")) {
			ListPreference sdcard = (ListPreference) preference;

			moveFilesToNewStorage(newValue.toString());

			String title = getString(R.string.pref_sdcard_title) + ": " + getEntryText(sdcard, newValue.toString());
			sdcard.setTitle(title);
			return true;
		}

		return false;
	}

	private boolean moveFilesToNewStorage(String newStorage) {
		File oldStorage = Storage.getExternalStorageDirectory(getActivity());
		if (oldStorage.getAbsolutePath().equals(newStorage))
			return true;

		String addition = "/Android/data/com.axelby.podax/files";
		File oldStorageDir = new File(oldStorage, addition);
		if (!oldStorageDir.exists())
			return true;

		File newStorageDir = new File(newStorage, addition);
		if (!newStorageDir.exists())
			if (!newStorageDir.mkdirs())
				return false;

		File[] filesToMove = oldStorageDir.listFiles();
		if (filesToMove == null)
			return false;

		boolean allgood = true;
		for (File from : filesToMove)
			allgood = allgood && from.renameTo(new File(newStorageDir, from.getName()));
		return allgood;
	}
}
