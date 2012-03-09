package com.meuge.geolocalisation;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

//Obtenir les données
public class CoordonneesDataSource {

	// Database fields
		private SQLiteDatabase database;
		private SQLMeuge dbHelper;
		private String [] allColumns = { SQLMeuge.COLUMN_ID,
										 SQLMeuge.COLUMN_LATITUDE,
										 SQLMeuge.COLUMN_LONGITUDE,
										 SQLMeuge.COLUMN_ADRESSE };

		public CoordonneesDataSource(Context context) {
			dbHelper = new SQLMeuge(context);
		}

		public void open() throws SQLException {
			database = dbHelper.getWritableDatabase();
		}

		public void close() {
			dbHelper.close();
		}
		//Insertion en base
		public Coordonnees insert(Coordonnees comment) {
			ContentValues values = new ContentValues();
			values.put(SQLMeuge.COLUMN_ADRESSE, comment.getAdresse());
			values.put(SQLMeuge.COLUMN_LATITUDE, comment.getLatitude());
			values.put(SQLMeuge.COLUMN_LONGITUDE, comment.getLongitude());
			long insertId = database.insert(SQLMeuge.TABLE, null,
					values);
			// ON rapatrie pour voir si ca s 'est bien passe
			Cursor cursor = database.query(SQLMeuge.TABLE,
					allColumns, SQLMeuge.COLUMN_ID + " = " + insertId, null,
					null, null, null);
			cursor.moveToFirst();
			return cursorToComment(cursor);
		}
		//Suppression en base
		public void deleteComment(Coordonnees comment) {
			long id = comment.getId();
			database.delete(SQLMeuge.TABLE, SQLMeuge.COLUMN_ID
					+ " = " + id, null);
		}
		
		//Infos de la base
		public List<Coordonnees> getAllComments() {
			List<Coordonnees> comments = new ArrayList<Coordonnees>();
			Cursor cursor = database.query(SQLMeuge.TABLE,
					allColumns, null, null, null, null, null);
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				Coordonnees comment = cursorToComment(cursor);
				comments.add(comment);
				cursor.moveToNext();
			}
			// Fermeture du curseur
			cursor.close();
			return comments;
		}
		
		private Coordonnees cursorToComment(Cursor cursor) {
			Coordonnees comment = new Coordonnees();
			comment.setId(cursor.getLong(0));
			comment.setLatitude(cursor.getDouble(1));
			comment.setLongitude(cursor.getDouble(2));
			comment.setAdresse(cursor.getString(3));
			return comment;
		}
}
