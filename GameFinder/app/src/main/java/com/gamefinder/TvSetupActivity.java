package com.gamefinder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import java.io.IOException;

import okhttp3.Headers;
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

        Intent intent = getIntent();
        final String accessToken = intent.getStringExtra("accessToken");
        final String client = intent.getStringExtra("client");
        final String uid = intent.getStringExtra("uid");

        final Button nextButton = (Button) findViewById(R.id.nextButton);
        final EditText tvConfigName = (EditText) findViewById(R.id.tvConfigName);
        final Spinner tvBrandSpinner = (Spinner) findViewById(R.id.tvBrandSpinner);
        Spinner cableSpinner = (Spinner) findViewById(R.id.cableSpinner);

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
                /**if (nextButton.getText().toString().equals("Next")) {
                    final Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("https://fathomless-woodland-78351.herokuapp.com/api/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    final APIService service = retrofit.create(APIService.class);

                    loginButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            User user = new User(email.getText().toString(), password.getText().toString());
                            Call<LoginResponse> call = service.signIn(user);
                            call.enqueue(new Callback<LoginResponse>() {
                                @Override
                                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                                    int responseCode = response.code();
                                    if (responseCode == 401) {
                                        String errorMessage = "";
                                        try {
                                            LoginErrorResponse errorResponse
                                                    = (LoginErrorResponse) retrofit.responseBodyConverter(
                                                    LoginErrorResponse.class,
                                                    LoginErrorResponse.class.getAnnotations())
                                                    .convert(response.errorBody());
                                            errorMessage = errorResponse.getErrors().get(0);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        AlertDialog alertDialog
                                                = new AlertDialog.Builder(LoginActivity.this).create();
                                        alertDialog.setTitle("Alert");
                                        alertDialog.setMessage(errorMessage);
                                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                });
                                        alertDialog.show();
                                    } else if (responseCode == 200) {
                                        System.out.println("Success");
                                    }
                                }

                                @Override
                                public void onFailure(Call<LoginResponse> call, Throwable t) {
                                    System.out.println("Failure");
                                }
                            });
                        }
                    });
                }*/
                nextIntent.putExtra("accessToken", accessToken);
                nextIntent.putExtra("client", client);
                nextIntent.putExtra("uid", uid);
                startActivity(nextIntent);
            }
        });
    }
}