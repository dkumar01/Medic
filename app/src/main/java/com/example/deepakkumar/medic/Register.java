package com.example.deepakkumar.medic;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity
{

	private TextView mFirstName;
	private TextView mLastName;
	private TextView mDateofBirth;
	private TextView mEmailId;
	private TextView mPassword;
	private TextView mPhoneNumber;
	private TextView mDoctorId;

	String firstName;
	String lastName;
	String dateOfBirth;
	String phoneNumber;
	String emailId;
	String password;
	String doctorId;

	JSONParser jsonParser = new JSONParser();

	private ProgressDialog pDialog;

	//testing on Emulator:
	private static final String url_create_patient =
			"http://10.0.2.2:80/medic/create_patient.php";

	//JSON element ids from repsonse of php script:
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_MESSAGE = "message";

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
		mDoctorId = (EditText) this.findViewById(R.id.doctor_id);

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
		mPassword.setError(null);
		mDateofBirth.setError(null);

		//Store Values at the time of sign up attempt
		firstName = mFirstName.getText().toString();
		lastName = mLastName.getText().toString();
		dateOfBirth = mDateofBirth.getText().toString();
		phoneNumber = mPhoneNumber.getText().toString();
		emailId = mEmailId.getText().toString();
		password = mPassword.getText().toString();
		doctorId = mDoctorId.getText().toString();

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
			mDateofBirth.setError("Invalid Entry");
			focusView = mDateofBirth;
			cancel = true;
		}
		if (TextUtils.isEmpty(phoneNumber) || !isPhoneNoValid(phoneNumber))
		{
			mPhoneNumber.setError("Invalid Entry");
			focusView = mPhoneNumber;
			cancel = true;
		}
		if (TextUtils.isEmpty(emailId) || !isEmailValid(emailId))
		{
			mEmailId.setError("Invalid Email");
			focusView = mEmailId;
			cancel = true;
		}
		if (TextUtils.isEmpty(password) || !isPasswordValid(password))
		{
			mPassword.setError("Invalid Password");
			focusView = mPassword;
			cancel = true;
		}
		if (TextUtils.isEmpty(doctorId) || !isDoctorIdValid(doctorId))
		{
			mDoctorId.setError("Invalid Id");
			focusView = mDoctorId;
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
			new CreateNewPatient().execute();
		}

	}

	private boolean isNameValid(String name)
	{
		return Pattern.matches("[a-zA-Z]+", name) && name.length()
				>= 2;
	}

	private boolean isDateValid(String date)
	{
		try
		{
			DateFormat df = new SimpleDateFormat("yyyy/mm/dd");
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

	private boolean isDoctorIdValid(String doctorId)
	{
		return doctorId.matches("[0-9]+");
	}

	/**
	 * Background Async Task to Create new patient
	 */
	class CreateNewPatient extends AsyncTask<String, String, String>
	{

		/**
		 * Before starting background thread Show Progress Dialog
		 */
		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			pDialog = new ProgressDialog(Register.this);
			pDialog.setMessage("Creating Patient..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Creating product
		 */
		protected String doInBackground(String... args)
		{
			int success;

			try
			{
				// Building Parameters
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("first_name", firstName));
				params.add(new BasicNameValuePair("last_name", lastName));
				params.add(
						new BasicNameValuePair("date_of_birth", dateOfBirth));
				params.add(new BasicNameValuePair("phone_no", phoneNumber));
				params.add(new BasicNameValuePair("email_id", emailId));
				params.add(new BasicNameValuePair("password", password));
				params.add(new BasicNameValuePair("doctor_id", doctorId));

				// getting JSON Object
				// create patient by making http request
				JSONObject json = jsonParser.makeHttpRequest(url_create_patient,
						"POST", params);

				// check log for json response
				Log.d("Create Response", json.toString());

				success = json.getInt(TAG_SUCCESS);
				// check for success tag

				if (success == 1)
				{
					// successfully created product
					Intent i = new Intent(getApplicationContext(),
							LoginActivity.class);

					startActivity(i);
					// closing this screen
					finish();
					System.out.println(json.getString(TAG_MESSAGE));
					return json.getString(TAG_MESSAGE);
				}
				else
				{
					Log.d("Register Failure!", json.getString(TAG_MESSAGE));
					return json.getString(TAG_MESSAGE);
				}
			} catch (JSONException e)
			{
				e.printStackTrace();
			}

			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 **/
		protected void onPostExecute(String file_url)
		{
			// dismiss the dialog once done
			pDialog.dismiss();

			if (file_url != null)
			{
				Toast.makeText(Register.this, file_url, Toast.LENGTH_LONG)
						.show();
			}
		}

	}
}
