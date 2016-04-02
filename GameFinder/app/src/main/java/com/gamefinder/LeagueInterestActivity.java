package com.gamefinder;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

/**
 *
 */
public class LeagueInterestActivity extends AppCompatActivity {
    /**
     * Holds the response from the getLeagues API hit
     */
    private List<LeaguesResponse> responseBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_league_interest);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Pull all relevant information out of the intent
        Intent intent = getIntent();
        final String accessToken = intent.getStringExtra("accessToken");
        final String client = intent.getStringExtra("client");
        final String uid = intent.getStringExtra("uid");

        // getLeagues API hit, sets the list's entries to the values in the response
        Call<List<LeaguesResponse>> getLeaguesCall = ApiUtils.service.getLeagues(accessToken, client, uid);
        getLeaguesApiHit(getLeaguesCall);

        // getLeaguesPrefs API hit, TODO: set the preferences from the server's current values
        Call<List<PreferencesResponse>> getLeaguesPrefsCall = ApiUtils.service.getLeaguesPrefs(accessToken, client, uid);
        getLeaguesPrefsApiHit(getLeaguesPrefsCall);

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

                // putPreferences API hit,
                Call<List<PreferencesResponse>> call = ApiUtils.service.putPreferences(accessToken, client, uid, preference);
                call.enqueue(new Callback<List<PreferencesResponse>>() {
                    @Override
                    public void onResponse(Call<List<PreferencesResponse>> call, retrofit2.Response<List<PreferencesResponse>> response) {
                        if (response.isSuccess()) {
                            List<List<CompetitorsResponse>> competitors = new ArrayList<>();

                            // If a non-zero preference then add those teams to the competitors list
                            for (int i = 0; i < numLeagues; i++) {
                                if (ratings[i] > 0) {
                                    Call<List<CompetitorsResponse>> getCompetitorsCall = ApiUtils.service.getCompetitors(String.valueOf(leagueIds[i]), accessToken, client, uid);
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
                            Bundle bundleObject = new Bundle();
                            bundleObject.putSerializable("competitorsList", (Serializable) competitors);
                            nextIntent.putExtras(bundleObject);
                            nextIntent.putExtra("accessToken", accessToken);
                            nextIntent.putExtra("client", client);
                            nextIntent.putExtra("uid", uid);
                            startActivity(nextIntent);
                        } else {
                            System.out.println("Response failure when putting league preferences");
                        }
                    }

                    @Override
                    public void onFailure(Call<List<PreferencesResponse>> call, Throwable t) {
                        System.out.println(t.getMessage());
                    }
                });
            }
        });
    }

    /**
     *
     */
    private void getLeaguesApiHit(Call<List<LeaguesResponse>> call) {
        final AppCompatActivity thisActivity = this;

        call.enqueue(new Callback<List<LeaguesResponse>>() {
            @Override
            public void onResponse(Call<List<LeaguesResponse>> call, retrofit2.Response<List<LeaguesResponse>> response) {
                if (response.isSuccess()) {
                    responseBody = response.body();

                    ArrayAdapter<LeaguesResponse> adapter
                            = new LeagueListViewAdapter(thisActivity, R.layout.item_listview, responseBody);

                    ListView listView = (ListView) findViewById(R.id.league_listview);
                    listView.setAdapter(adapter);
                } else {
                    System.out.println("Response failure when getting leagues");
                }
            }

            @Override
            public void onFailure(Call<List<LeaguesResponse>> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

    /**
     *
     */
    private void getLeaguesPrefsApiHit(Call<List<PreferencesResponse>> call) {
        //final AppCompatActivity thisActivity = this;

        call.enqueue(new Callback<List<PreferencesResponse>>() {
            @Override
            public void onResponse(Call<List<PreferencesResponse>> call, retrofit2.Response<List<PreferencesResponse>> response) {
                if (response.isSuccess()) {
                    //responseBody = response.body();
                    System.out.println(response.body().toString());

                    //ListView listView = (ListView) findViewById(R.id.league_listview);
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