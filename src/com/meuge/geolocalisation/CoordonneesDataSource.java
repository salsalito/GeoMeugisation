package com.meuge.geolocalisation;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

//Obtenir les données
public class CoordonneesDataSource {

	// Database fields
		private SQLiteDatabase database;
		private SQLMeuge dbHelper;

		public CoordonneesDataSource(Context context) {
			dbHelper = new SQLMeuge(context);
		}

		public void open() throws SQLException {
			database = dbHelper.getWritableDatabase();
		}

		public void close() {
			dbHelper.close();
		}
		
		public long insert(Coordonnees comment) {
			ContentValues values = new ContentValues();
			values.put(SQLMeuge.COLUMN_ADRESSE, comment.getAdresse());
			values.put(SQLMeuge.COLUMN_LATITUDE, comment.getLatitude());
			values.put(SQLMeuge.COLUMN_LONGITUDE, comment.getLongitude());
			long insertId = database.insert(SQLMeuge.TABLE, null,
					values);
			return insertId;
		}
		
		public void deleteComment(Coordonnees comment) {
			long id = comment.getId();
			database.delete(SQLMeuge.TABLE, SQLMeuge.COLUMN_ID
					+ " = " + id, null);
		}
		
		
}
