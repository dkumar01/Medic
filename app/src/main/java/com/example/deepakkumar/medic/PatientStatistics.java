package com.example.deepakkumar.medic;

import java.util.Date;

/**
 * Created by Deepak Kumar on 17/02/2016.
 */
public class PatientStatistics
{
	private static int id = 0;
	private String patient_id;
	private Date dateOfSubmission;
	private int glucoseLevel;
	private int weight;
	private int cholesterol;
	private String comments;


	public PatientStatistics(String patient_id, Date dateOfSubmission,
			int glucoseLevel, int weight, int cholesterol,
			String comments)
	{
		id++;
		this.patient_id = patient_id;
		this.dateOfSubmission = dateOfSubmission;
		this.glucoseLevel = glucoseLevel;
		this.weight = weight;
		this.cholesterol = cholesterol;
		this.comments = comments;
	}

	public static int getId()
	{
		return id;
	}

	public String getPatient_id()
	{
		return patient_id;
	}

	public Date getDateOfSubmission()
	{
		return dateOfSubmission;
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
