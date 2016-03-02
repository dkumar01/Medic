package com.example.deepakkumar.medic;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Deepak Kumar on 17/02/2016.
 */
public class PatientStatistics
{
	private int id;
	private int patient_id;
	private Date dateOfSubmission;
	private int glucoseLevel;
	private int weight;
	private int cholesterol;
	private String comments;

	public PatientStatistics(int id, int patient_id, Date dateOfSubmission,
			int glucoseLevel, int weight, int cholesterol,
			String comments)
	{
		this.id = id;
		this.patient_id = patient_id;
		this.dateOfSubmission = dateOfSubmission;
		this.glucoseLevel = glucoseLevel;
		this.weight = weight;
		this.cholesterol = cholesterol;
		this.comments = comments;
	}

	public int getId()
	{
		return id;
	}

	public int getPatient_id()
	{
		return patient_id;
	}

	public String getDateOfSubmission()
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss", Locale.UK);
		return dateFormat.format(dateOfSubmission);
	}

	public int getGlucoseLevel()
	{
		return glucoseLevel;
	}

	public int getWeight()
	{
		return weight;
	}

	public int getCholesterol()
	{
		return cholesterol;
	}

	public String getComments()
	{
		return comments;
	}
}
