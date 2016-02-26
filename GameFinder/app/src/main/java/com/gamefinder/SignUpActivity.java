package com.gamefinder;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUpActivity extends AppCompatActivity {

    public final String BASE_URL = "https://fathomless-woodland-78351.herokuapp.com/api/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Button createAccountButton = (Button) findViewById(R.id.createAccount);
        final EditText username = (EditText) findViewById(R.id.username);
        final EditText password = (EditText) findViewById(R.id.password);
        final EditText confirmPassword = (EditText) findViewById(R.id.confirmPassword);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final APIService service2 = retrofit.create(APIService.class);
        final Intent intent = new Intent(this, LoginActivity.class);

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SignUpUser signUpUser = new SignUpUser(username.getText().toString(),
                        password.getText().toString(),
                        confirmPassword.getText().toString());

                Call<SignUpResponse> call = service2.signUp(signUpUser);
                call.enqueue(new Callback<SignUpResponse>() {
                    @Override
                    public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                        int responseCode = response.code();
                        System.out.println(responseCode);
                        if (responseCode == 403) {
                            AlertDialog alertDialog = new AlertDialog.Builder(SignUpActivity.this).create();
                            alertDialog.setTitle("Alert");
                            alertDialog.setMessage("Password too short/password confirmation not same/email address already in use");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
                        } else if (responseCode == 200) {
                            AlertDialog alertDialog = new AlertDialog.Builder(SignUpActivity.this).create();
                            alertDialog.setTitle("Success");
                            alertDialog.setMessage("Account creation successful!");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            startActivity(intent);
                                        }
                                    });
                            alertDialog.show();
                        }
                    }

                    @Override
                    public void onFailure(Call<SignUpResponse> call, Throwable t) {
                        System.out.println("signup failure");
                    }
                });
            }
        });

    }

}
