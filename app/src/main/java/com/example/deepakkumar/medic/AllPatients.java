package com.example.deepakkumar.medic;

/**
 * Created by Deepak Kumar on 13/03/2016.
 */

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import org.apache.http.NameValuePair;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AllPatients extends ListActivity
{

	// Progress Dialog
	private ProgressDialog pDialog;

	// Creating JSON Parser object
	JSONParser jParser = new JSONParser();

	ArrayList<ArrayList<String>> patientList;

	// url to get all patients list
	private static String url_all_patients =
			"http://localhost/medic/get_all_patients.php";

	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_PATIENTS = "patients";
	private static final String TAG_PATIENT_ID = "patient_id";
	private static final String TAG_FIRST_NAME = "first_name";
	private static final String TAG_LAST_NAME = "last_name";
	private static final String TAG_DATE_OF_BIRTH = "date_of_birth";
	private static final String TAG_PHONE_NUMBER = "phone_number";
	private static final String TAG_EMAIL_ID = "email_id";
	private static final String TAG_PASSWORD = "password";
	private static final String TAG_DOCTOR_ID = "doctor_id";


	// patients JSONArray
	JSONArray patients = null;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.all_products);

		// Hashmap for ListView
		patientList = new ArrayList<ArrayList<String>>();

		// Loading patients in Background Thread
		new LoadAllPatients().execute();

		/*
		// Get listview
		ListView lv = getListView();

		// on seleting single product
		// launching Edit Product Screen
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				// getting values from selected ListItem
				String pid = ((TextView) view.findViewById(R.id.pid)).getText()
						.toString();

				// Starting new intent
				Intent in = new Intent(getApplicationContext(),
						EditProductActivity.class);
				// sending pid to next activity
				in.putExtra(TAG_PATIENT_ID, pid);

				// starting new activity and expecting some response back
				startActivityForResult(in, 100);
			}
		});*/

	}

	// Response from Edit Product Activity
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		// if result code 100
		if (resultCode == 100)
		{
			// if result code 100 is received
			// means user edited/deleted product
			// reload this screen again
			Intent intent = getIntent();
			finish();
			startActivity(intent);
		}

	}

	/**
	 * Background Async Task to Load all product by making HTTP Request
	 */
	class LoadAllPatients extends AsyncTask<String, String, String>
	{

		/**
		 * Before starting background thread Show Progress Dialog
		 */
		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			pDialog = new ProgressDialog(AllPatients.this);
			pDialog.setMessage("Loading patients. Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * getting All patients from url
		 */
		protected String doInBackground(String... args)
		{
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			// getting JSON string from URL
			JSONObject json = jParser
					.makeHttpRequest(url_all_patients, "GET", params);

			// Check your log cat for JSON reponse
			Log.d("All patients: ", json.toString());

			try
			{
				// Checking for SUCCESS TAG
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1)
				{
					// patients found
					// Getting Array of Patients
					patients = json.getJSONArray(TAG_PATIENTS);

					// looping through All Patients
					for (int i = 0; i < patients.length(); i++)
					{
						JSONObject c = patients.getJSONObject(i);

						// Storing each json item in variable
						String patient_id = c.getString(TAG_PATIENT_ID);
						String first_name = c.getString(TAG_FIRST_NAME);
						String last_name = c.getString(TAG_LAST_NAME);
						String date_of_birth = c.getString(TAG_DATE_OF_BIRTH);
						String phone_number = c.getString(TAG_PHONE_NUMBER);
						String email_id = c.getString(TAG_EMAIL_ID);
						String password = c.getString(TAG_PASSWORD);
						String doctor_id = c.getString(TAG_DOCTOR_ID);


						// creating new ArrayList
						ArrayList<String> list = new ArrayList<String>();

						// adding each child node to inner ArrayList
						list.add(patient_id);
						list.add(first_name);
						list.add(last_name);
						list.add(date_of_birth);
						list.add(phone_number);
						list.add(email_id);
						list.add(password);
						list.add(doctor_id);

						// adding HashList to ArrayList
						patientList.add(list);
					}
					insertPatients(patientList);
				}
				else
				{
					// no patients found
					/*
					// Launch Add New product Activity
					Intent i = new Intent(getApplicationContext(),
							NewProductActivity.class);
					// Closing all previous activities
					i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i);*/
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
			//TODO: Call InsertPatients and execute insertPatients


		}

		protected void insertPatients(ArrayList<ArrayList<String>> list)
		{

		}
	}
}