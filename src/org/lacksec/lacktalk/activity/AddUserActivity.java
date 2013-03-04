/*
 * Copyright (c) 2013.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the Don't be a Duck Public License v1.0.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * You should have received a copy of the Don't be a Duck Public License
 * along with this program. If not, just don't be a duck.
 */

package org.lacksec.lacktalk.activity;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import org.lacksec.lacktalk.Constants;
import org.lacksec.lacktalk.R;
import org.lacksec.lacktalk.RosterActivity;
import org.lacksec.lacktalk.data.DatabaseHelper;
import org.lacksec.lacktalk.model.UserProfile;
import org.lacksec.lacktalk.util.NfcUtils;

/**
 * Creation info:
 * User: darndt
 * Date: 2013/02/23
 * Time: 23:31
 */
public class AddUserActivity extends OrmLiteBaseActivity<DatabaseHelper> {
	private static String LOG_TAG = RosterActivity.class.getName();

	NfcAdapter mNfcAdapter;
	PendingIntent mNfcPendingIntent;
	IntentFilter[] mNdefExchangeFilters;
	private RuntimeExceptionDao<UserProfile, String> mUserProfileDao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.v(LOG_TAG, "onCreate - begin");
		super.onCreate(savedInstanceState);

		setContentView(R.layout.add_user);
		setUpNfc();
		setUpDaos();
	}

	@Override
	protected void onResume() {
		Log.v(LOG_TAG, "onResume - begin");
		super.onResume();

		UserProfile userProfile = mUserProfileDao.queryForId(Constants.SYSTEM_USER_ID);

		mNfcAdapter.enableForegroundNdefPush(AddUserActivity.this,
				NfcUtils.getNdefMessage(userProfile.getUserId(), userProfile.getUserId()));
		mNfcAdapter.enableForegroundDispatch(this, mNfcPendingIntent, mNdefExchangeFilters, null);
	}

	@Override
	protected void onPause() {
		Log.v(LOG_TAG, "onPause - begin");
		super.onPause();

		mNfcAdapter.disableForegroundNdefPush(this);
		mNfcAdapter.disableForegroundDispatch(this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		Log.v(LOG_TAG, "onNewIntent - begin");
		super.onNewIntent(intent);

		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
			NdefMessage[] msgs = NfcUtils.getNdefMessages(intent);
			NdefRecord ndefRecord = msgs[0].getRecords()[0];
			String msg = new String(ndefRecord.getPayload());
			// TODO Have this do something
			Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
		}
	}

	private void setUpNfc() {
		Log.v(LOG_TAG, "setUpNfc - begin");

		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
		mNfcPendingIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
		try {
			ndefDetected.addDataType(NfcUtils.NFC_DATA_TYPE);
		} catch (IntentFilter.MalformedMimeTypeException e) {
			throw new RuntimeException(e);
		}
		mNdefExchangeFilters = new IntentFilter[]{ndefDetected};
	}

	private void setUpDaos() {
		Log.v(LOG_TAG, "setUpDaos - begin");

		mUserProfileDao = getHelper().getUserProfileDao();
	}
}
