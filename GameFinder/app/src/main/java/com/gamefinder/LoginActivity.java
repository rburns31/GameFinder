package com.gamefinder;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private final AppCompatActivity thisActivity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Verify that the phone's keyboard is closed
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        // Load the GameFinder logo
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        Picasso.with(LoginActivity.this).load(R.drawable.logo).fit().into(imageView);

        // Pull all relevant elements from the layout
        final Button loginButton = (Button) findViewById(R.id.loginButton);
        final TextView signUp = (TextView) findViewById(R.id.signUpButton);
        final EditText email = (EditText) findViewById(R.id.email);
        final EditText password = (EditText) findViewById(R.id.password);

        // If selected, start the Sign Up Activity
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });

        // Handle the login button being selected
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User(email.getText().toString(), password.getText().toString());
                Call<LoginResponse> call = ApiUtils.service.signIn(user);
                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        int responseCode = response.code();
                        if (responseCode == 401) {
                            String errorMessage = "";
                            try {
                                LoginErrorResponse errorResponse
                                        = (LoginErrorResponse) ApiUtils.retrofit.responseBodyConverter(
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
                            Headers headers = response.headers();
                            ApiUtils.accessToken = headers.get("Access-Token");
                            ApiUtils.client = headers.get("Client");
                            ApiUtils.uid = headers.get("UID");

                            // Check if this user has any league preferences to determine where to direct them next
                            Call<List<PreferencesResponse>> getLeaguesPrefsCall
                                    = ApiUtils.service.getLeaguesPrefs(ApiUtils.accessToken, ApiUtils.client, ApiUtils.uid);
                            getLeaguesPrefsApiHit(getLeaguesPrefsCall);
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        System.out.println("failure");
                    }
                });
            }
        });
    }

    /**
     *
     */
    private void getLeaguesPrefsApiHit(Call<List<PreferencesResponse>> call) {
        final List<PreferencesResponse> prefs = new ArrayList<>();
        call.enqueue(new Callback<List<PreferencesResponse>>() {
            @Override
            public void onResponse(Call<List<PreferencesResponse>> call, retrofit2.Response<List<PreferencesResponse>> response) {
                if (response.isSuccess()) {
                    for (PreferencesResponse pref: response.body()) {
                        prefs.add(pref);
                    }
                    System.out.println("Prefs from LoginActivity: " + prefs);

                    if (prefs.size() > 0) {
                        startActivity(new Intent(thisActivity, GamesScreenActivity.class));
                    } else {
                        startActivity(new Intent(thisActivity, LeagueInterestActivity.class));
                    }
                } else {
                    System.out.println("Response failure when getting league preferences");
                }
            }

            @Override
            public void onFailure(Call<List<PreferencesResponse>> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }
}