package com.example.deepakkumar.medic;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity
{

	private TextView mFirstName;
	private TextView mLastName;
	private TextView mDateofBirth;
	private TextView mEmailId;
	private TextView mPassword;
	private TextView mPhoneNumber;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		final Button signUpButton = (Button) this.findViewById(R.id
				.signUpButton);

		mFirstName = (EditText) this.findViewById(R.id.firstName);
		mLastName = (EditText) this.findViewById(R.id.lastName);
		mDateofBirth = (EditText) this.findViewById(R.id.date_of_birth);
		mPhoneNumber = (EditText) this.findViewById(R.id.phone_number);
		mEmailId = (EditText) this.findViewById(R.id.email_id);
		mPassword = (EditText) this.findViewById(R.id.password);

		signUpButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				attemptSignUp();
			}
		});

	}

	private void attemptSignUp()
	{
		//ResetErrors
		mFirstName.setError(null);
		mLastName.setError(null);
		mDateofBirth.setError(null);
		mPhoneNumber.setError(null);
		mEmailId.setError(null);
		mFirstName.setError(null);

		//Store Values at the time of sign up attempt
		String firstName = mFirstName.getText().toString();
		String lastName = mLastName.getText().toString();
		String dateOfBirth = mDateofBirth.getText().toString();
		String phoneNumber = mPhoneNumber.getText().toString();
		String emailId = mEmailId.getText().toString();
		String password = mPassword.getText().toString();

		boolean cancel = false;
		View focusView = null;

		//Checks for valid first name, if user entered one
		if (TextUtils.isEmpty(firstName) || !isNameValid(firstName))
		{
			mFirstName.setError("Name can only contain letters");
			focusView = mFirstName;
			cancel = true;
		}
		//Checks for valid last name, if user entered one
		if (TextUtils.isEmpty(lastName) || !isNameValid(lastName))
		{
			mLastName.setError("Name can only contain letters");
			focusView = mLastName;
			cancel = true;
		}
		//Checks for valid last name, if user entered one
		if (TextUtils.isEmpty(dateOfBirth) || !isDateValid(dateOfBirth))
		{
			mLastName.setError("Name can only contain letters");
			focusView = mLastName;
			cancel = true;
		}

		if (cancel)
		{
			// There was an error; don't attempt sign up and focus the first
			// form field with an error.
			focusView.requestFocus();
		}
		else
		{
			System.out.println("This be passin mon");
		}

	}

	private boolean isNameValid(String name)
	{
		return Pattern.matches("[a-zA-Z]+", name) && name.length()
				> 2;
	}

	private boolean isDateValid(String date)
	{
		try
		{
			DateFormat df = new SimpleDateFormat("dd/mm/yyyy");
			df.setLenient(false);
			df.parse(date);
			return true;
		} catch (ParseException e)
		{
			return false;
		}
	}

	private boolean isPhoneNoValid(String phone_no)
	{
		//11 digits. Extension not needed because NHS is UK based
		return phone_no.matches("[0-9]+") && phone_no.length() == 11;
	}

	private boolean isEmailValid(String email)
	{
		return email.contains("@");
	}

	private boolean isPasswordValid(String password)
	{
		return password.length() >= 4;
	}

}
