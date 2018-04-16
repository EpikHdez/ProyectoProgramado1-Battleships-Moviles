package com.boom.battleships.views;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.boom.battleships.R;

public class HelpActivity extends AppCompatActivity {

    public void openReportActivity(View view) {
        Intent intent = new Intent(this, ReportActivity.class);
        startActivity(intent);

    }

    public void openConfigurationActivity(View view) {
        Intent intent = new Intent(this,ConfigurationActivity.class);
        startActivity(intent);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
    }
}
