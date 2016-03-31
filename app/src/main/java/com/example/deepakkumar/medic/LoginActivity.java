package com.example.deepakkumar.medic;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends Activity implements OnClickListener
{

	private Button mSubmit, mRegister;

	// Progress Dialog
	private ProgressDialog pDialog;

	// JSON parser class
	JSONParser jsonParser = new JSONParser();

	private EditText mEmailId;
	private EditText mPassword;

	String email_id = "";
	String password = "";
	int patient_id;

	// private static final String LOGIN_URL = "http://xxx.xxx.x.x:1234/webservice/login.php";

	//testing on Emulator:
	private static final String LOGIN_URL =
			"http://10.0.2.2:80/medic/get_patient_id.php";

	//JSON element ids from repsonse of php script:
	private final String TAG_SUCCESS = "success";
	private final String TAG_MESSAGE = "message";
	private final String TAG_PATIENT_ID = "patient_id";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		//setup buttons
		mSubmit = (Button) findViewById(R.id.signIn);
		mRegister = (Button) findViewById(R.id.register);
		//setup text fields
		mEmailId = (EditText) this.findViewById(R.id.email_id);
		mPassword = (EditText) this.findViewById(R.id.password);
		//register listeners
		mSubmit.setOnClickListener(this);
		mRegister.setOnClickListener(this);

	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.signIn:
			email_id = mEmailId.getText().toString();
			password = mPassword.getText().toString();
			attemptLogIn();
			//new AttemptLogin().execute();
			break;
		case R.id.register:
			Intent i = new Intent(this, Register.class);
			startActivity(i);
			break;

		default:
			break;
		}
	}

	private void attemptLogIn()
	{
		//ResetErrors

		mEmailId.setError(null);
		mPassword.setError(null);

		//Store Values at the time of sign up attempt
		email_id = mEmailId.getText().toString();
		password = mPassword.getText().toString();

		boolean cancel = false;
		View focusView = null;

		if (TextUtils.isEmpty(email_id))
		{
			mEmailId.setError("Enter Email Id");
			focusView = mEmailId;
			cancel = true;
		}
		if (TextUtils.isEmpty(password))
		{
			mPassword.setError("Enter Password");
			focusView = mPassword;
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
			new Login().execute();
		}

	}

	class Login extends AsyncTask<String, String, String>
	{

		/**
		 * Before starting background thread Show Progress Dialog
		 */
		boolean failure = false;

		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			pDialog = new ProgressDialog(LoginActivity.this);
			pDialog.setMessage("Attempting login...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... args)
		{
			// Check for success tag
			int success;

			try
			{
				// Building Parameters
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("email_id", email_id));
				params.add(new BasicNameValuePair("password", password));

				Log.d("request!", "starting");
				// getting patient details by making HTTP request
				JSONObject json = jsonParser.makeHttpRequest(LOGIN_URL,
						"POST", params);

				// check your log for json response
				Log.d("Login attempt", json.toString());

				// json success tag
				success = json.getInt(TAG_SUCCESS);
				if (success == 1)
				{
					Log.d("Login Successful!", json.toString());

					///////////////////////////////////////////////////////////
					patient_id = Integer.parseInt(json.getString
							(TAG_PATIENT_ID));
					///////////////////////////////////////////////////////////

					Intent i = new Intent(LoginActivity.this, Dashboard
							.class);

					/////////////////////////////////////////////////
					i.putExtra("patient_id", patient_id);
					/////////////////////////////////////////////////

					finish();
					startActivity(i);
					System.out.println(json.getString(TAG_MESSAGE));
					return json.getString(TAG_MESSAGE);
				}
				else
				{
					Log.d("Login Failure!", json.getString(TAG_MESSAGE));
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
			// dismiss the dialog once product deleted
			pDialog.dismiss();
			if (file_url != null)
			{
				Toast.makeText(LoginActivity.this, file_url, Toast.LENGTH_LONG)
						.show();
			}

		}

	}

}