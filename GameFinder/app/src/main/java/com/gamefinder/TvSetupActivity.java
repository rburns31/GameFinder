package com.gamefinder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TvSetupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_setup);

        final Context thisContext = this;

        final Button nextButton = (Button) findViewById(R.id.nextButton);
        final EditText tvConfigName = (EditText) findViewById(R.id.tvConfigName);
        final Spinner tvBrandSpinner = (Spinner) findViewById(R.id.tvBrandSpinner);
        final Spinner cableSpinner = (Spinner) findViewById(R.id.cableSpinner);

        tvBrandSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (!tvBrandSpinner.getSelectedItem().equals("None/Other") && !tvConfigName.getText().toString().equals("")) {
                    nextButton.setText("Next");
                } else {
                    nextButton.setText("Skip");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

        tvConfigName.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (!tvBrandSpinner.getSelectedItem().equals("None/Other") && !tvConfigName.getText().toString().equals("")) {
                    nextButton.setText("Next");
                } else {
                    nextButton.setText("Skip");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        final Intent nextIntent = new Intent(this, ChannelsActivity.class);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If a tv config name and brand are input then save them on the server
                if (nextButton.getText().toString().equals("Next")) {
                    TelevisionBody televisionBody = new TelevisionBody();
                    Television tv = new Television();
                    tv.setName(tvConfigName.getText().toString());
                    tv.setBrand(tvBrandSpinner.getSelectedItem().toString());
                    tv.setCable_company(cableSpinner.getSelectedItem().toString());
                    televisionBody.setTelevision(tv);

                    Call<List<TelevisionResponse>> call
                            = ApiUtils.service.postTelevisions(ApiUtils.accessToken, ApiUtils.client, ApiUtils.uid, televisionBody);
                    call.enqueue(new Callback<List<TelevisionResponse>>() {
                        @Override
                        public void onResponse(Call<List<TelevisionResponse>> call, Response<List<TelevisionResponse>> response) {

                        }

                        @Override
                        public void onFailure(Call<List<TelevisionResponse>> call, Throwable t) {
                            System.out.println("television response failure");
                        }
                    });
                }
                startActivity(nextIntent);
            }
        });
    }
}