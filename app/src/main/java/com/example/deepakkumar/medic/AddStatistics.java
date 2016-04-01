package com.example.deepakkumar.medic;

import android.app.ActionBar;
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
import android.widget.Toast;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddStatistics extends AppCompatActivity
{
	ActionBar actionBar;

	EditText mGlucoseLevel;
	EditText mCholesterol;
	EditText mWeight;
	EditText mComments;

	String glucoseLevel = "";
	String cholesterol = "";
	String weight = "";
	String comments = "";
	String patient_id = "";
	String dateOfSubmission = "";

	private ProgressDialog pDialog;
	private JSONParser jsonParser = new JSONParser();

	//testing on Emulator:
	private static final String url_create_patient_statistics =
			"http://10.0.2.2:80/medic/create_patient_statistics.php";

	//JSON element ids from repsonse of php script:
	private final String TAG_SUCCESS = "success";
	private final String TAG_MESSAGE = "message";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_statistics);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		final Button submitButton = (Button) this.findViewById(R.id
				.submitButton);

		//setup text fields
		mGlucoseLevel = (EditText) findViewById(R.id.GlucoseLevel);
		mCholesterol = (EditText) findViewById(R.id.Cholesterol);
		mWeight = (EditText) findViewById(R.id.Weight);
		mComments = (EditText) findViewById(R.id.Comments);

		//Getting Patient id from previous activity
		patient_id = getIntent().getExtras().getString("patient_id");

		int year, month, day;
		int hour, minute;
		//Setting Submission Date and Time
		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH) + 1;
		day = c.get(Calendar.DAY_OF_MONTH);

		hour = c.get(Calendar.HOUR_OF_DAY);
		minute = c.get(Calendar.MINUTE);

		dateOfSubmission += year + "/" + month + "/" + day + " " + hour + ":" +
				minute;

		System.out.println(url_create_patient_statistics);
//		actionBar = getActionBar();
//		actionBar.setTitle("");

		submitButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				attemptInsert();
			}
		});
	}

	private void attemptInsert()
	{
		//ResetErrors
		mGlucoseLevel.setError(null);
		mCholesterol.setError(null);
		mWeight.setError(null);

		//Store Values at the time of sign up attempt
		glucoseLevel = mGlucoseLevel.getText().toString();
		cholesterol = mCholesterol.getText().toString();
		weight = mWeight.getText().toString();
		comments = mComments.getText().toString();


		boolean cancel = false;
		View focusView = null;

		//Checks for valid first name, if user entered one
		if (TextUtils.isEmpty(glucoseLevel) || !isNumberValid(glucoseLevel))
		{
			mGlucoseLevel.setError("Invalid Glucose Level");
			focusView = mGlucoseLevel;
			cancel = true;
		}
		//Checks for valid last name, if user entered one
		if (TextUtils.isEmpty(cholesterol) || !isNumberValid(cholesterol))
		{
			mCholesterol.setError("Invalid Cholesterol Level");
			focusView = mCholesterol;
			cancel = true;
		}
		//Checks for valid last name, if user entered one
		if (TextUtils.isEmpty(weight) || !isNumberValid(weight))
		{
			mWeight.setError("Invalid Entry");
			focusView = mWeight;
			cancel = true;
		}
		if (TextUtils.isEmpty(comments))
		{
			comments = " ";
		}


		if (cancel)
		{
			// There was an error; don't attempt sign up and focus the first
			// form field with an error.
			focusView.requestFocus();
		}
		else
		{
			new CreateNewEntry().execute();
		}

	}

	public boolean isNumberValid(String value)
	{
		return value.matches("[0-9.]*") || value.matches("[0-9]+");
	}

	/**
	 * Background Async Task to Create new patient
	 */
	class CreateNewEntry extends AsyncTask<String, String, String>
	{

		/**
		 * Before starting background thread Show Progress Dialog
		 */
		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			pDialog = new ProgressDialog(AddStatistics.this);
			pDialog.setMessage("Creating Entry..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Creating entry
		 */
		protected String doInBackground(String... args)
		{
			int success;

			try
			{
				// Building Parameters
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("patient_id", patient_id));
				params.add(new BasicNameValuePair("date_of_submission",
						dateOfSubmission));
				params.add(
						new BasicNameValuePair("glucose_level", glucoseLevel));
				params.add(new BasicNameValuePair("cholesterol", cholesterol));
				params.add(new BasicNameValuePair("weight", weight));
				params.add(new BasicNameValuePair("comments", comments));

				// getting JSON Object
				// create entry by making http request
				JSONObject json = jsonParser.makeHttpRequest
						(url_create_patient_statistics, "POST", params);

				// check log for json response
				Log.d("Create Response", json.toString());

				success = json.getInt(TAG_SUCCESS);
				// check for success tag

				if (success == 1)
				{
					// successfully created product
					Intent i = new Intent(getApplicationContext(),
							Dashboard.class);
					i.putExtra("patient_id", patient_id);
					startActivity(i);
					// closing this screen
					finish();
					System.out.println(json.getString(TAG_MESSAGE));
					return json.getString(TAG_MESSAGE);
				}
				else
				{
					Log.d("Entry Failure!", json.getString(TAG_MESSAGE));
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
				Toast.makeText(AddStatistics.this, file_url, Toast.LENGTH_LONG)
						.show();
			}
		}

	}

}
