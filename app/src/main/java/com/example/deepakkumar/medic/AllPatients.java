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
import org.apache.http.NameValuePair;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.*;

public class AllPatients extends ListActivity
{

	// Progress Dialog
	private ProgressDialog pDialog;

	// Creating JSON Parser object
	JSONParser jParser = new JSONParser();

	ArrayList<ArrayList<String>> patientList;

	// url to get all patients list
	private static String url_patient_login =
			"http://localhost/medic/get_patient_login.php";

	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_LOGIN = "login";
	private static final String TAG_EMAIL_ID = "email_id";
	private static final String TAG_PASSWORD = "password";

	//Database Helper
	DatabaseHelper db = new DatabaseHelper(this);

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
					.makeHttpRequest(url_patient_login, "GET", params);

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
					patients = json.getJSONArray(TAG_LOGIN);

					// looping through All Patients

						JSONObject c = patients.getJSONObject(0);

						// Storing each json item in variable
						String email_id = c.getString(TAG_EMAIL_ID);
						String password = c.getString(TAG_PASSWORD);

						//creating new array
						String details[] = {email_id, password};



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
			}
			catch (JSONException e)
			{
				e.printStackTrace();
			}
			catch (java.text.ParseException e)
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
			pDialog.dismiss();

		}

		protected void insertPatients(ArrayList<ArrayList<String>> list)
				throws java.text.ParseException
		{
			Patient p;
			int i, j;
			int size;
			ArrayList<String> cur;
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"yyyy-MM-dd", Locale.UK);
			Date date;



			for(i = 0; i < list.size(); i++)
			{
				cur = list.get(i);
				size = cur.size();

				for(j = 0; j < size; j++)
				{
					date = dateFormat.parse(cur.get(3));
					p = new Patient(Integer.parseInt(cur.get(0)), cur.get(1),
							cur.get(2), date, cur.get(4), cur.get(5),
							cur.get(6), Integer.parseInt(cur.get(7)));
					db.insertRows(p);
				}
			}
		}
	}
}