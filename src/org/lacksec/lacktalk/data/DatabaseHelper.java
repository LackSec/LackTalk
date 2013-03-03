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

package org.lacksec.lacktalk.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import org.lacksec.lacktalk.model.UserProfile;

/**
 * Creation info:
 * User: darndt
 * Date: 2013/03/02
 * Time: 17:21
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
	private static final String DATABASE_NAME = "lacktalk.db";
	private static final int DATABASE_VERSION = 1;

	private RuntimeExceptionDao<UserProfile, String> userProfileDao = null;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
		try {
			Log.i(DatabaseHelper.class.getName(), "onCreate");
			TableUtils.createTable(connectionSource, UserProfile.class);
		} catch (java.sql.SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Cannot create database", e);
			throw new RuntimeException(e);
		}
		UserProfile user = new UserProfile("systemuser");
		getUserProfileDao().create(user);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int initialVersion, int newVersion) {
		try {
			Log.i(DatabaseHelper.class.getName(), "onUpgrade");
			TableUtils.dropTable(connectionSource, UserProfile.class, true);
			onCreate(db, connectionSource);
		} catch (java.sql.SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Cannot drop database", e);
			throw new RuntimeException(e);
		}
	}

	public RuntimeExceptionDao<UserProfile, String> getUserProfileDao() {
		if (userProfileDao == null) {
			userProfileDao = getRuntimeExceptionDao(UserProfile.class);
		}
		return userProfileDao;
	}

	/**
	 * Close the database connections and clear any cached DAOs.
	 */
	@Override
	public void close() {
		super.close();
	}
}
