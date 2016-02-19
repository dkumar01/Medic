package com.example.deepakkumar.medic;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;

public class DatabaseHelper extends SQLiteOpenHelper
{

	// Database Version
	private static final int DATABASE_VERSION = 1;
	// Database Name
	private static final String DATABASE_NAME = "medic";
	// Table names
	private static final String TABLE_PATIENTS = "patient";
	private static final String TABLE_PATIENT_STATISTICS = "patient_statistics";
	// PATIENT Table Column names
	private static final String PATIENT_ID = "id";
	private static final String FIRST_NAME = "first_name";
	private static final String LAST_NAME = "last_name";
	private static final String DATE_OF_BIRTH = "date_of_birth";
	private static final String PHONE_NO = "phone_No";
	private static final String EMAIL_ID = "email_id";
	private static final String PASSWORD = "password";
	private static final String DOCTOR_ID = "doctor_id";

	//PATIENT_STATISTICS Column names
	private static final String STATISTICS_ID = "statistics_id";

	String CREATE_PATIENTS_TABLE = "CREATE TABLE " + TABLE_PATIENTS + "("
			+ PATIENT_ID + " INTEGER PRIMARY KEY," + FIRST_NAME+ " TEXT,"
			+ LAST_NAME + " TEXT" + DATE_OF_BIRTH + " TEXT,"+  PHONE_NO
			+ "TEXT,"+ EMAIL_ID+ " TEXT," + PASSWORD + " TEXT," + DOCTOR_ID +
			" TEXT," + "FOREIGN KEY("+ DOCTOR_ID +") REFERENCES staff"
			+ "("+ DOCTOR_ID +"))";

	String CREATE_PATIENTS_STATISTICS_TABLE = "CREATE TABLE " + TABLE_PATIENT_STATISTICS + "("
			+ STATISTICS_ID + " INTEGER PRIMARY KEY," + PATIENT_ID+ " Integer,"
			+ LAST_NAME + " TEXT" + DATE_OF_BIRTH + " TEXT,"+  PHONE_NO
			+ "TEXT,"+ EMAIL_ID+ " TEXT," + PASSWORD + " TEXT," + DOCTOR_ID +
			" TEXT," + ")";


	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_PATIENTS_TABLE);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PATIENTS);
		// Creating tables again
		onCreate(db);
	}
}                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             