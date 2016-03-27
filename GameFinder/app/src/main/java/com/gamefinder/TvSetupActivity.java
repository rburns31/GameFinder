package com.gamefinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class TvSetupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_setup);

        Button nextButton = (Button) findViewById(R.id.nextButton);
        EditText tvConfigName = (EditText) findViewById(R.id.tvConfigName);
        Spinner tvBrandSpinner = (Spinner) findViewById(R.id.tvBrandSpinner);
        Spinner cableSpinner = (Spinner) findViewById(R.id.cableSpinner);

        final Intent nextIntent = new Intent(this, ChannelsActivity.class);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //nextIntent.putExtras(bundleObject);
                startActivity(nextIntent);
            }
        });
    }
}