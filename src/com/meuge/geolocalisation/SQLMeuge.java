package com.meuge.geolocalisation;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLMeuge extends SQLiteOpenHelper {

	public static final String TABLE = "coordonnees";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_ADRESSE = "adresse";
	public static final String COLUMN_LATITUDE = "latitude";
	public static final String COLUMN_LONGITUDE = "longitude";

	private static final String DATABASE_NAME = "dbmeuge.db";
	private static final int DATABASE_VERSION = 1;

	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table "
			+ TABLE 
			+ "( " + COLUMN_ID + " integer primary key autoincrement, " 
			+ COLUMN_LATITUDE  + " real not null," 
			+ COLUMN_LONGITUDE + " real not null," 
			+ COLUMN_ADRESSE   + " text	);";

	public SQLMeuge(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase arg0) {
		arg0.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(SQLMeuge.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE);
		onCreate(db);
	}

}
