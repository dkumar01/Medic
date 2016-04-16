package com.example.deepakkumar.medic;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Deepak Kumar on 10/03/2016.
 */

public class AllStatistics extends ListActivity
{

	// Progress Dialog
	private ProgressDialog pDialog;

	// Creating JSON Parser object
	JSONParser jsonParser = new JSONParser();

	String patient_id = "";

	ArrayList<HashMap<String, String>> statisticsList;

	// url to get all statistics list
	private static String url_all_statistics =
			"http://10.0.2.2:80/medic/get_patient_statistics.php";

	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_STATISTICS = "patient_statistics";
	private static final String TAG_STATISTICS_ID = "statistics_id";
	private static final String TAG_DATE_OF_SUBMISSION = "date_of_submission";
	private static final String TAG_GLUCOSE_LEVEL = "glucose_level";
	private static final String TAG_CHOLESTEROL = "cholesterol";
	private static final String TAG_WEIGHT = "weight";
	private static final String TAG_COMMENTS = "comments";

	// patients JSONArray
	JSONArray statistics = null;

	ListView listView;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_statistics);

		// Hashmap for ListView
		statisticsList = new ArrayList<HashMap<String, String>>();

		//Getting Patient id from previous activity
		patient_id = getIntent().getExtras().getString("patient_id");

		//listView = (ListView) findViewById(R.id.list);

		// Loading patients in Background Thread
		new LoadAllStatistics().execute();

		// Get listview
		ListView lv = getListView();



		// on seleting single product
		// launching Edit Product Screen
		/*lv.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				// getting values from selected ListItem
				String patient_id = ((TextView) view
						.findViewById(R.id.patient_id))
						.getText()
						.toString();

				// Starting new intent
				Intent in = new Intent(getApplicationContext(),
						EditProductActivity.class);
				// sending pid to next activity
				//in.putExtra(TAG_PID, pid);

				// starting new activity and expecting some response back
				//startActivityForResult(in, 100);
			}
		});*/

	}

	// Response from Edit Product Activity
	//@Override
	/*protected void onActivityResult(int requestCode, int resultCode,
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

	}*/

	/**
	 * Background Async Task to Load all statistics by making HTTP Request
	 */
	class LoadAllStatistics extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(AllStatistics.this);
			pDialog.setMessage("Loading products. Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * getting All statistics from url
		 * */
		protected String doInBackground(String... args) {
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("patient_id", patient_id));
			// getting JSON string from URL
			JSONObject json = jsonParser.makeHttpRequest(url_all_statistics,
					"GET", params);

			// Check log for JSON reponse
			Log.d("All Statistics ", json.toString());

			try {
				// Checking for SUCCESS TAG
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					
					// Getting Array of Statistics
					statistics = json.getJSONArray(TAG_STATISTICS);

					// looping through All Statistics
					for (int i = 0; i < statistics.length(); i++) {
						JSONObject c = statistics.getJSONObject(i);

						// Storing each json item in variable
						String id = c.getString(TAG_STATISTICS_ID);
						String date_of_submission = c.getString
								(TAG_DATE_OF_SUBMISSION);
						String glucose_level = c.getString(TAG_GLUCOSE_LEVEL);
						String cholesterol = c.getString(TAG_CHOLESTEROL);
						String weight = c.getString(TAG_WEIGHT);
						String comments = c.getString(TAG_COMMENTS);

						// creating new HashMap
						HashMap<String, String> map = new HashMap<String, String>();

						// adding each child node to HashMap key => value
						map.put(TAG_STATISTICS_ID, id);
						map.put(TAG_DATE_OF_SUBMISSION, "Date and Time of "
										+ "Submission: " + date_of_submission);
						map.put(TAG_GLUCOSE_LEVEL, "Glucose "
								+ "Level: " + glucose_level);
						map.put(TAG_CHOLESTEROL, "Cholesterol Level: "
								+ cholesterol);
						map.put(TAG_WEIGHT, "Weight: " + weight);
						map.put(TAG_COMMENTS, "Comments: " + comments);


						// adding HashMap to ArrayList
						statisticsList.add(map);
					}
				} /*else {
					// no products found
					// Launch Add New product Activity
					Intent i = new Intent(getApplicationContext(),
							NewProductActivity.class);
					// Closing all previous activities
					i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i);
				}*/
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog after getting all products
			pDialog.dismiss();
			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					/**
					 * Updating parsed JSON data into ListView
					 * */
					ListAdapter adapter = new SimpleAdapter(
							AllStatistics.this, statisticsList,
							R.layout.activity_list_item, new String[] {
							TAG_STATISTICS_ID, TAG_DATE_OF_SUBMISSION,
							TAG_GLUCOSE_LEVEL, TAG_CHOLESTEROL, TAG_WEIGHT, TAG_COMMENTS},
							new int[] { R.id.statistics_id, R.id
									.date_of_submission, R.id.glucose_level, R.id
									.cholesterol, R.id.weight, R.id.comments });
					// updating listview
					setListAdapter(adapter);
				}
			});
			if (file_url != null)
			{
				Toast.makeText(AllStatistics.this, file_url, Toast.LENGTH_LONG)
						.show();
			}

		}

	}
}
