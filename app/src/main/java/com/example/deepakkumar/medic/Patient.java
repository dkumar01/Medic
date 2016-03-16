package com.example.deepakkumar.medic;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Deepak Kumar on 10/02/2016.
 */
public class Patient
{

	private int id;
	private String firstName, lastName;
	private Date dateOfBirth;
	private String phone_no;
	private String email_id;
	private String password;
	private int doctor_id;

	//For Object creation by client classes
	public Patient(int id, String firstName, String lastName, int day, int
			month, int year, String phone_no, String email_id, String password,
			int doctor_id)
	{
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		dateOfBirth = new Date(year, month, day);
		this.email_id = email_id;
		this.phone_no = phone_no;
		this.password = password;
		this.doctor_id = doctor_id;
	}

	//For object creation from DatabaseHandler (difference in DATE type)
	public Patient(int id, String firstName, String lastName, Date
			dateOfBirth, String phone_no, String email_id, String password,
			int doctor_id)
	{
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
		this.email_id = email_id;
		this.phone_no = phone_no;
		this.password = password;
		this.doctor_id = doctor_id;
	}

	public int getId()
	{
		return id;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public String getDateOfBirth()
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-mm-dd", Locale.UK);
		return dateFormat.format(dateOfBirth);
	}

	public String getEmail_id()
	{
		return email_id;
	}

	public String getPassword()
	{
		return password;
	}

	public String getPhone_no()
	{
		return phone_no;
	}

	public int getDoctor_id()
	{
		return doctor_id;
	}
}
