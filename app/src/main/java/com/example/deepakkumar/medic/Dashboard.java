package com.example.deepakkumar.medic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Dashboard extends AppCompatActivity implements OnClickListener
{

	Button viewAllEntries;
	Button addNewEntry;
	String patient_id;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashboard);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		viewAllEntries = (Button) findViewById(R.id.viewAllEntriesButton);
		addNewEntry = (Button) findViewById(R.id.addNewEntryButton);

		viewAllEntries.setOnClickListener(this);
		addNewEntry.setOnClickListener(this);

		patient_id = getIntent().getExtras().getString("patient_id");

	}

	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		switch (v.getId())
		{
		case R.id.addNewEntryButton:
			//System.out.print(patient_id);
			Intent i = new Intent(this, AddStatistics
					.class);
			i.putExtra("patient_id", patient_id);
			startActivity(i);
			break;
		case R.id.viewAllEntriesButton:
			Intent u = new Intent(this, AllStatistics
					.class);
			u.putExtra("patient_id", patient_id);
			startActivity(u);
			break;

		default:
			break;
		}
	}

}
