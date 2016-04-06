package com.gamefinder;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
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
    private static List<List<CompetitorsResponse>> competitors;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_league_interest);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Set up the recycler view which holds the league cards
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // getLeagues API hit, sets the list's entries to the values in the response
        Call<List<LeaguesResponse>> getLeaguesCall
                = ApiUtils.service.getLeagues(ApiUtils.accessToken, ApiUtils.client, ApiUtils.uid);
        getLeaguesApiHit(getLeaguesCall);

        // getLeaguesPrefs API hit, TODO: set the preferences from the server's current values
        Call<List<PreferencesResponse>> getLeaguesPrefsCall
                = ApiUtils.service.getLeaguesPrefs(ApiUtils.accessToken, ApiUtils.client, ApiUtils.uid);
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
                Call<List<PreferencesResponse>> call
                        = ApiUtils.service.putPreferences(ApiUtils.accessToken, ApiUtils.client, ApiUtils.uid, preference);
                call.enqueue(new Callback<List<PreferencesResponse>>() {
                    @Override
                    public void onResponse(Call<List<PreferencesResponse>> call, retrofit2.Response<List<PreferencesResponse>> response) {
                        if (response.isSuccess()) {
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

                    // Set the adapter to display the current league's team cards
                    final RecyclerView.Adapter adapter = new LeagueInterestRecyclerAdapter(responseBody);
                    recyclerView.setAdapter(adapter);
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

    public static Bundle getCompetitorsBundle() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("competitorsList", (Serializable) competitors);
        return bundle;
    }
}