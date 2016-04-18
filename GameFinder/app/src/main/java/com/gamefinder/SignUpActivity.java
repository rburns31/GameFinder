package com.gamefinder;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;

import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Verify that the phone's keyboard is closed
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        // Load the GameFinder logo
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        Picasso.with(SignUpActivity.this).load(R.drawable.logo).fit().into(imageView);

        // Pull all relevant elements from the layout
        final Button createAccountButton = (Button) findViewById(R.id.createAccount);
        final EditText email = (EditText) findViewById(R.id.email);
        final EditText password = (EditText) findViewById(R.id.input_password);
        final EditText confirmPassword = (EditText) findViewById(R.id.confirmPassword);
        final TextView linkLogin = (TextView) findViewById(R.id.link_login);

        final Intent intent = new Intent(this, LeagueInterestActivity.class);

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SignUpUser signUpUser = new SignUpUser(email.getText().toString(),
                        password.getText().toString(),
                        confirmPassword.getText().toString());

                Call<SignUpResponse> call = ApiUtils.service.signUp(signUpUser);
                call.enqueue(new Callback<SignUpResponse>() {
                    @Override
                    public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                        int responseCode = response.code();
                        //System.out.println(responseCode);
                        if (responseCode == 403) {
                            String errorMessage = "";
                            try {
                                SignUpErrorResponse errorResponse
                                        = (SignUpErrorResponse) ApiUtils.retrofit.responseBodyConverter(
                                                SignUpErrorResponse.class,
                                                SignUpErrorResponse.class.getAnnotations())
                                        .convert(response.errorBody());
                                errorMessage = errorResponse.getSignUpErrors().full_messages.get(0);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            AlertDialog alertDialog
                                    = new AlertDialog.Builder(SignUpActivity.this).create();
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
                            Headers headers = response.headers();
                            ApiUtils.accessToken = headers.get("Access-Token");
                            ApiUtils.client = headers.get("Client");
                            ApiUtils.uid = headers.get("UID");

                            System.out.println(headers.get("Access-Token"));
                            System.out.println(headers.get("Client"));
                            System.out.println(headers.get("UID"));

                            AlertDialog alertDialog
                                    = new AlertDialog.Builder(SignUpActivity.this).create();
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
                        System.out.println("Signup failure");
                    }
                });
            }
        });

        linkLogin.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View v) {
               // Finish the registration screen and return to the Login Activity
               finish();
           }
        });
    }
}