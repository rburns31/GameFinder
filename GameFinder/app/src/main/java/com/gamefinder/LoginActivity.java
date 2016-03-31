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
import java.util.List;

import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Header;

public class LoginActivity extends AppCompatActivity {

    public final String BASE_URL = "https://fathomless-woodland-78351.herokuapp.com/api/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        Picasso.with(LoginActivity.this).load(R.drawable.logo).fit().into(imageView);

        final Button loginButton = (Button) findViewById(R.id.loginButton);
        final TextView signUp = (TextView) findViewById(R.id.signUpButton);
        final EditText email = (EditText) findViewById(R.id.email);
        final EditText password = (EditText) findViewById(R.id.password);

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final APIService service = retrofit.create(APIService.class);
        final Intent intent = new Intent(this, SignUpActivity.class);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start the Sign Up Activity
                startActivity(intent);
            }
        });

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
                            AlertDialog alertDialog
                                    = new AlertDialog.Builder(LoginActivity.this).create();
                            Headers headers = response.headers();
                            openLeagues(headers);
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        System.out.println("failure");
                    }
                });
            }
        });

        System.out.println("========== Testing Rating Algorithm ==========");
        System.out.println("[sport, team1Fav, team2Fav, sportFav, inProgress, spread, margin, " +
                "combinedWinPct, Boolean.toString(playoffMatch), ranking1, ranking2] => score");
        printRating("NBA", 1, 1, 3, false, 0, 0, 0.5, false, 0, 0);
        printRating("NBA", 1, 0, 3, false, 0, 0, 0.5, false, 0, 0);
        printRating("NBA", 0, 1, 3, false, 0, 0, 0.5, false, 0, 0);
        printRating("NBA", 0, 0, 3, false, 0, 0, 0.5, false, 0, 0);
        printRating("NBA", 0, 0, 3, true, 0, 0, 0.5, false, 0, 0);
        printRating("PGA", 0, 0, 2, true, 0, 0, 0.5, false, 0, 0);
        printRating("NBA", 0, 0, 3, true, 0, 0, 0.5, false, 0, 0);
        printRating("NCAA Football", 4, 7, 3, true, 0, 0, 0.9, false, 3, 9);
        printRating("NBA", 0, 0, 3, true, 0, 0, 0.5, false, 0, 0);
        System.out.println("========== Testing Rating Algorithm ==========");
    }

    /**
     * Method for printing out rating based on parameters, no use in application
     */
    private void printRating(String sport, int team1Fav, int team2Fav, int sportFav,
                             boolean inProgress, double spread, double margin,
                             double combinedWinPct, boolean playoffMatch,
                             int ranking1, int ranking2) {

        System.out.printf("[%s, %d, %d, %d, %s, %f, %f, %f, %s, %d, %d] => %d %n",
                sport, team1Fav, team2Fav, sportFav, Boolean.toString(inProgress), spread,
                margin, combinedWinPct, Boolean.toString(playoffMatch), ranking1, ranking2,
                GameRatingAlgorithm.rateGame(sport, team1Fav, team2Fav, sportFav, inProgress, spread, margin, combinedWinPct, playoffMatch, ranking1, ranking2));
    }

    private void openLeagues(Headers headers) {
        Intent intent = new Intent(this, LeagueInterestActivity.class);
        String accessToken = headers.get("Access-Token");
        String client = headers.get("Client");
        String uid = headers.get("UID");
        intent.putExtra("accessToken", accessToken);
        intent.putExtra("client", client);
        intent.putExtra("uid", uid);
        startActivity(intent);
    }
}