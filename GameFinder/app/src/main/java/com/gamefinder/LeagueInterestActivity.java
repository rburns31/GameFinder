package com.gamefinder;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import retrofit2.Call;

/**
 *
 */
public class LeagueInterestActivity extends AppCompatActivity {
    /**
     * Holds the response from the getLeagues API hit
     */
    private List<LeaguesResponse> responseBody;
    private List<PreferencesResponse> prevPref;
    private static List<List<CompetitorsResponse>> competitors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_league_interest);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Verify that the phone's keyboard is closed
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        AlertDialog dialog = new AlertDialog.Builder(LeagueInterestActivity.this).create();
        dialog.setTitle("Tips");
        dialog.setMessage("Click on a league's stars to express your interest. To reset a rating to 0, long press that league's logo.");
        dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        dialog.show();

        // Set up the recycler view which holds the league cards
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // getLeaguesPrefs API hit, passes the stored preferences to be populated initially
        Call<List<PreferencesResponse>> getLeaguesPrefsCall
                = ApiUtils.service.getLeaguesPrefs(ApiUtils.accessToken, ApiUtils.client, ApiUtils.uid);
        try {
            prevPref = getLeaguesPrefsCall.execute().body();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        // getLeagues API hit, sets the list's entries to the values in the response
        Call<List<LeaguesResponse>> getLeaguesCall
                = ApiUtils.service.getLeagues(ApiUtils.accessToken, ApiUtils.client, ApiUtils.uid);
        try {
            responseBody = getLeaguesCall.execute().body();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        HashSet<String> supportedLeagues = new HashSet<>();
        supportedLeagues.addAll(Arrays.asList("MLB", "PGA", "NBA",
                "NCAA Men's Basketball", "NCAA Football", "NHL", "NFL",
                "North American Soccer", "European Soccer"));

        List<LeaguesResponse> supported = new ArrayList<>();
        for (LeaguesResponse league: responseBody) {
            if (supportedLeagues.contains(league.getName())) {
                supported.add(league);
            }
        }
        responseBody = supported;

        final RecyclerView.Adapter adapter = new LeagueInterestRecyclerAdapter(responseBody, prevPref);
        recyclerView.setAdapter(adapter);

        // Handle the next button being clicked
        Button nextButton = (Button) findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int numLeagues = responseBody.size();

                PreferenceAttributes[] allPrefs = new PreferenceAttributes[numLeagues];
                final int[] leagueIds = new int[numLeagues];
                final int[] ratings = new int[numLeagues];

                // For each league, create a preference attributes object
                for (int i = 0; i < numLeagues; i++) {
                    int id = Integer.parseInt(responseBody.get(i).getId());
                    leagueIds[i] = id;
                    Float ratingStar = responseBody.get(i).getRatingStar();
                    ratings[i] = ratingStar.intValue();

                    PreferenceAttributes thisPref = new PreferenceAttributes();
                    thisPref.setPreference_type("League");
                    thisPref.setPreference_id(id);
                    thisPref.setAmount(ratingStar.intValue());
                    thisPref.setScale(5);

                    allPrefs[i] = thisPref;
                }

                // Package these preferences into the desired format
                PreferenceUser user = new PreferenceUser();
                user.setPreferences_attributes(allPrefs);
                PreferenceBody preference = new PreferenceBody();
                preference.setUser(user);

                // putPreferences API hit
                Call<List<PreferencesResponse>> putPrefsCall
                        = ApiUtils.service.putPreferences(ApiUtils.accessToken, ApiUtils.client, ApiUtils.uid, preference);
                try {
                    putPrefsCall.execute();

                    competitors = new ArrayList<>();

                    // If a non-zero preference, (and not PGA), then add those teams to the competitors list
                    for (int i = 0; i < numLeagues; i++) {
                        if (ratings[i] > 0 && leagueIds[i] != 17) {
                            Call<List<CompetitorsResponse>> getCompetitorsCall
                                    = ApiUtils.service.getCompetitors(String.valueOf(leagueIds[i]), ApiUtils.accessToken, ApiUtils.client, ApiUtils.uid);
                            try {
                                List<CompetitorsResponse> responseBody = getCompetitorsCall.execute().body();
                                competitors.add(responseBody);
                            } catch (IOException e) {
                                System.out.println(e.getMessage());
                            }
                        }
                    }

                    // Start the next activity
                    Intent nextIntent = new Intent(LeagueInterestActivity.this, TeamInterestActivity.class);
                    if (competitors.size() == 0) {
                        nextIntent = new Intent(LeagueInterestActivity.this, TvSetupActivity.class);
                    }
                    if (getIntent().getBooleanExtra("Update", false)) {
                        nextIntent.putExtra("Update", true);
                    }
                    startActivity(nextIntent);

                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        });
    }

    /**
     *
     * @return
     */
    public static Bundle getCompetitorsBundle() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("competitorsList", (Serializable) competitors);
        return bundle;
    }
}