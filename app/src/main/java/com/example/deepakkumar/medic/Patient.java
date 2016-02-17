package com.example.deepakkumar.medic;

import java.util.Date;

/**
 * Created by Deepak Kumar on 10/02/2016.
 */
public class Patient
{

	private static int id = 0;
	private String firstName, lastName;
	private Date dateOfBirth;
	private String email_id;
	private String password;
	private String phone_no;
	private int doctor_id;

	public Patient(String firstName, String lastName, int day, int month,
			int year, String email_id, String phone_no, String password,
			int doctor_id)
	{
		this.id++;
		this.firstName = firstName;
		this.lastName = lastName;
		dateOfBirth = new Date(year, month, day);
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

	public Date getDateOfBirth()
	{
		return dateOfBirth;
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
