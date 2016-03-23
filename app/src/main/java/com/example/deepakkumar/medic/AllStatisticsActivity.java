package com.example.deepakkumar.medic;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Deepak Kumar on 10/03/2016.
 */

public class AllStatisticsActivity extends ListActivity
{

	// Progress Dialog
	private ProgressDialog pDialog;

	// Creating JSON Parser object
	JSONParser jParser = new JSONParser();

	ArrayList<HashMap<String, String>> productsList;

	// url to get all statistics list
	private static String url_all_products =
			"http:///10.0.2.2:80/android_connect/get_all_patients.php";

	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_PATIENTS = "patient";
	private static final String TAG_PATIENT_ID = "patient_id";
	private static final String TAG_FIRST_NAME = "first_name";

	// patients JSONArray
	JSONArray products = null;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_statistics);

		// Hashmap for ListView
		productsList = new ArrayList<HashMap<String, String>>();

		// Loading patients in Background Thread
		new LoadAllStatistics().execute();

		// Get listview
		ListView lv = getListView();

		// on seleting single product
		// launching Edit Product Screen
		lv.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				// getting values from selected ListItem
				String patient_id = ((TextView) view.findViewById(R.id.patient_id))
						.getText()
						.toString();

				// Starting new intent
				/*Intent in = new Intent(getApplicationContext(),
						EditProductActivity.class);*/
				// sending pid to next activity
				//in.putExtra(TAG_PID, pid);

				// starting new activity and expecting some response back
				//startActivityForResult(in, 100);
			}
		});

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
	class LoadAllStatistics extends AsyncTask<String, String, String>
	{

		/**
		 * Before starting background thread Show Progress Dialog
		 */
		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			pDialog = new ProgressDialog(AllStatisticsActivity.this);
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
					.makeHttpRequest(url_all_products, "GET", params);

			// Check your log cat for JSON reponse
			Log.d("All Products: ", json.toString());

			try
			{
				// Checking for SUCCESS TAG
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1)
				{
					// patients found
					// Getting Array of Products
					products = json.getJSONArray(TAG_PATIENTS);

					// looping through All Products
					for (int i = 0; i < products.length(); i++)
					{
						JSONObject c = products.getJSONObject(i);

						// Storing each json item in variable
						String id = c.getString(TAG_PATIENT_ID);
						String name = c.getString(TAG_FIRST_NAME);

						// creating new HashMap
						HashMap<String, String> map = new HashMap<String, String>();

						// adding each child node to HashMap key => value
						map.put(TAG_PATIENT_ID, id);
						map.put(TAG_FIRST_NAME, name);

						// adding HashList to ArrayList
						productsList.add(map);
					}
				}
				/*else
				{
					// no patients found
					// Launch Add New product Activity
					Intent i = new Intent(getApplicationContext(),
							NewProductActivity.class);
					// Closing all previous activities
					i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i);
				}*/
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
			// dismiss the dialog after getting all patients
			pDialog.dismiss();
			// updating UI from Background Thread
			runOnUiThread(new Runnable()
			{
				public void run()
				{
					/**
					 * Updating parsed JSON data into ListView
					 * */
					ListAdapter adapter = new SimpleAdapter(
							AllStatisticsActivity.this, productsList,
							R.layout.content_list_item, new String[] {
							TAG_PATIENT_ID,
							TAG_FIRST_NAME },
							new int[] { R.id.patient_id, R.id.first_name });
					// updating listview
					setListAdapter(adapter);
				}
			});

		}

	}
}
