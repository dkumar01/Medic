package com.example.deepakkumar.medic;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class Dashboard extends AppCompatActivity
{

	Button viewAllEntries;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashboard);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		viewAllEntries = (Button)findViewById(R.id.viewAllEntriesButton);


	}


//	public void onClick(View v) {
//		// TODO Auto-generated method stub
//		switch (v.getId()) {
//		case R.id.viewAllEntriesButton:
//			Intent i = new Intent(Dashboard.this, AllStatisticsActivity
//					.class);
//			startActivity(i);
//			break;
//		case R.id.register:
//			Intent u = new Intent(this, Register.class);
//			startActivity(u);
//			break;
//
//		default:
//			break;
//		}
//	}




}
