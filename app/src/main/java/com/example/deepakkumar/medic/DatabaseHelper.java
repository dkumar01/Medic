package com.example.deepakkumar.medic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
	private static final String DATE_OF_SUBMISSION = "date_of_submission";
	private static final String GLUCOSE_LEVEL = "glucose_level";
	private static final String WEIGHT = "weight";
	private static final String CHOLESTEROL = "cholesterol";
	private static final String COMMENTS = "comments";

	String CREATE_PATIENTS_TABLE = "CREATE TABLE " + TABLE_PATIENTS + "("
			+ PATIENT_ID + " INTEGER PRIMARY KEY, " + FIRST_NAME + " TEXT,"
			+ LAST_NAME + " TEXT, " + DATE_OF_BIRTH + " DATE," + PHONE_NO
			+ "TEXT, " + EMAIL_ID + " TEXT, " + PASSWORD + " TEXT, " + DOCTOR_ID
			+ " TEXT, " + "FOREIGN KEY(" + DOCTOR_ID + ") REFERENCES staff"
			+ "(" + DOCTOR_ID + "))";

	String CREATE_PATIENTS_STATISTICS_TABLE =
			"CREATE TABLE " + TABLE_PATIENT_STATISTICS + "("
					+ STATISTICS_ID + " INTEGER PRIMARY KEY, " + PATIENT_ID
					+ " INTEGER," + DATE_OF_SUBMISSION + " TEXT," +
					GLUCOSE_LEVEL + " INTEGER," + WEIGHT + "INTEGER, " +
					CHOLESTEROL + " INTEGER, " + COMMENTS + "TEXT,"
					+ "FOREIGN KEY(" + PATIENT_ID + ") REFERENCES patient"
					+ "(" + PATIENT_ID + "))";

	public DatabaseHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL(CREATE_PATIENTS_TABLE);
		db.execSQL(CREATE_PATIENTS_STATISTICS_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PATIENTS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PATIENT_STATISTICS);
		// Creating tables again
		onCreate(db);
	}

	//TODO: Insert using PHP scripts

	//Insert new row into Patients table
	public void insertRows(Patient p)
	{
		//Opening database connection
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(PATIENT_ID, p.getId());    //ID to be inserted
		// automatically
		values.put(FIRST_NAME, p.getFirstName());
		values.put(LAST_NAME, p.getLastName());
		values.put(DATE_OF_BIRTH, p.getDateOfBirth());
		values.put(EMAIL_ID, p.getEmail_id());
		values.put(PASSWORD, p.getPassword());
		values.put(DOCTOR_ID, p.getDoctor_id());

		//Inserting the row
		db.insert(TABLE_PATIENTS, null, values);

		//Closing the database connection
		db.close();
	}

	//Insert new row into PatientStatistics table
	public void insertRows(PatientStatistics p)
	{
		//Opening database connection
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		//values.put(STATISTICS_ID, p.getId());  //Id to be inserted
		// automatically
		values.put(PATIENT_ID, p.getPatient_id());
		values.put(DATE_OF_SUBMISSION, p.getDateOfSubmission());
		values.put(GLUCOSE_LEVEL, p.getGlucoseLevel());
		values.put(WEIGHT, p.getWeight());
		values.put(CHOLESTEROL, p.getCholesterol());
		values.put(COMMENTS, p.getComments());

		//Inserting the row
		db.insert(TABLE_PATIENT_STATISTICS, null, values);

		//Closing db connection
		db.close();
	}

	public Patient getPatient(int patient_id)
	{
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "Select * from " + TABLE_PATIENTS + " where " +
				PATIENT_ID + " = " + patient_id;

		Cursor c = db.rawQuery(selectQuery, null);

		if (c != null)
			c.moveToFirst();
		DateFormat format;
		Date dateOfBirth = null;
		try
		{
			format = new SimpleDateFormat("MMMM d, yyyy", Locale.UK);
			dateOfBirth = format.parse(c.getString(4));
		} catch (java.text.ParseException e)
		{
			e.printStackTrace();
		}

		Patient p = new Patient(c.getInt(1), c.getString(2), c.getString(3),
				dateOfBirth, c.getString(5), c.getString(6), c.getString(7),
				c.getInt(8));

		return p;
	}

	public List<Patient> getAllPatients()
	{
		SQLiteDatabase db = this.getReadableDatabase();
		Patient p;
		DateFormat format;
		Date dateOfBirth = null;
		List<Patient> allPatients = new ArrayList<Patient>();

		String selectQuery = "Select * from " + TABLE_PATIENTS;

		Cursor c = db.rawQuery(selectQuery, null);

		if (c != null)
			c.moveToFirst();


		do
		{
			try
			{
				format = new SimpleDateFormat("MMMM d, yyyy", Locale.UK);
				dateOfBirth = format.parse(c.getString(4));
			} catch (java.text.ParseException e)
			{
				e.printStackTrace();
			}

			 p = new Patient(c.getInt(1), c.getString(2), c.getString(3),
					dateOfBirth, c.getString(5), c.getString(6), c.getString(7),
					c.getInt(8));

			allPatients.add(p);


		}while(c.moveToNext());

		return allPatients;
	}

	/**
	 * Returns all statistics for a specific patient
	 * @param patient_id
	 * @return
	 */
	public List<PatientStatistics> getAllStatistics(int patient_id)
	{
		SQLiteDatabase db = this.getReadableDatabase();
		PatientStatistics p;
		DateFormat format;
		Date dateOfSubmission = null;
		List<PatientStatistics> allStatistics = new ArrayList<PatientStatistics>();

		String selectQuery = "Select * from " + TABLE_PATIENT_STATISTICS + " where " +
				PATIENT_ID + " = " + patient_id;

		Cursor c = db.rawQuery(selectQuery, null);

		if (c != null)
			c.moveToFirst();

		do
		{
			try
			{
				format = new SimpleDateFormat("MMMM d, yyyy", Locale.UK);
				dateOfSubmission = format.parse(c.getString(3));
			} catch (java.text.ParseException e)
			{
				e.printStackTrace();
			}

			p = new PatientStatistics(c.getInt(1), c.getInt(2), dateOfSubmission, c
					.getInt(4),	c.getInt(5), c.getInt(6), c.getString(7));

			allStatistics.add(p);


		}while(c.moveToNext());

		return allStatistics;
	}

	public int updateRow(Patient p)
	{
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(FIRST_NAME, p.getFirstName());
		values.put(LAST_NAME, p.getLastName());
		values.put(LAST_NAME, p.getPhone_no());
		values.put(DATE_OF_BIRTH, p.getDateOfBirth());
		values.put(EMAIL_ID, p.getEmail_id());
		values.put(PASSWORD, p.getPassword());
		values.put(DOCTOR_ID, p.getDoctor_id());

		// updating row
		return db.update(TABLE_PATIENTS, values, PATIENT_ID + " = ?",
				new String[] { String.valueOf(p.getId()) });
	}

	public int updateRow(PatientStatistics p)
	{
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(PATIENT_ID, p.getPatient_id());
		values.put(DATE_OF_SUBMISSION, p.getDateOfSubmission());
		values.put(GLUCOSE_LEVEL, p.getGlucoseLevel());
		values.put(WEIGHT, p.getWeight());
		values.put(CHOLESTEROL, p.getCholesterol());
		values.put(COMMENTS, p.getComments());

		// updating row
		return db.update(TABLE_PATIENTS, values, STATISTICS_ID + " = ?",
				new String[] { String.valueOf(p.getId()) });
	}

	/**
	 * deleting a row from Patients table
	 * @param id
	 */
	public void deleteRow(int id)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_PATIENTS, PATIENT_ID + " = ?",
				new String[] { String.valueOf(id) });
	}
}