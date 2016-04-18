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
import android.widget.Button;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import retrofit2.Call;

/**
 *
 */
public class TeamInterestActivity extends AppCompatActivity {
    private List<PreferencesResponse> prevPref;
    private static List<List<CompetitorsResponse>> competitorsList;
    private static int currentLeagueLocation = 0;

    @Override
    @SuppressWarnings("unchecked")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_interest);

        // Set up the recycler view which holds the team cards
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // getCompetitorsPrefs API hit, passes the stored preferences to be populated initially
        Call<List<PreferencesResponse>> getCompetitorsPrefsCall
                = ApiUtils.service.getCompetitorsPrefs(ApiUtils.accessToken, ApiUtils.client, ApiUtils.uid);
        try {
            prevPref = getCompetitorsPrefsCall.execute().body();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        // If this is the first team interest screen then populate the list of all competitors and show the help dialog
        if (competitorsList == null) {
            AlertDialog dialog = new AlertDialog.Builder(TeamInterestActivity.this).create();
            dialog.setTitle("Tips");
            dialog.setMessage("Click on a team's logo to favorite or unfavorite them. Teams with colored logos are currently favorited.");
            dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            dialog.show();

            Bundle bundle = LeagueInterestActivity.getCompetitorsBundle();
            competitorsList = (List<List<CompetitorsResponse>>) bundle.getSerializable("competitorsList");
        }
        assert competitorsList != null;

        // Pull this league's teams out of the overall list
        List<CompetitorsResponse> teamsList = competitorsList.get(currentLeagueLocation);
        Collections.sort(teamsList);

        // Set the title of the page to the current league's name
        setTitle(competitorsList.get(currentLeagueLocation).get(0).getLeagueName());

        // Decide where to go after this page
        final Intent nextIntent;
        if (currentLeagueLocation < competitorsList.size() - 1) {
            currentLeagueLocation++;
            nextIntent = new Intent(this, TeamInterestActivity.class);
            nextIntent.putExtra("Update", true);
        } else if (getIntent().getBooleanExtra("Update", false)) {
            nextIntent = new Intent(this, GamesScreenActivity.class);

            // Clear out the current static variables
            currentLeagueLocation = 0;
            competitorsList = null;
        } else {
            nextIntent = new Intent(this, TvSetupActivity.class);
            nextIntent.putExtra("Update", true);

            // Clear out the current static variables
            currentLeagueLocation = 0;
            competitorsList = null;
        }

        // Filter out all of the duplicates, TODO: determine how this affects games list
        final List<CompetitorsResponse> filteredTeamsList = new ArrayList<>();
        HashSet<String> namesUsed = new HashSet<>();
        for (CompetitorsResponse team: teamsList) {
            String teamName = team.getName();
            if (!namesUsed.contains(teamName)) {
                filteredTeamsList.add(team);
                namesUsed.add(teamName);
            }
        }

        // Set the adapter to display the current league's team cards
        final RecyclerView.Adapter adapter = new TeamInterestRecyclerAdapter(filteredTeamsList, prevPref);
        recyclerView.setAdapter(adapter);

        // Handle the next button being clicked
        Button nextButton = (Button) findViewById(R.id.teamNextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Store all of the team id's were favorited by the user for this league
                boolean[] selectedPositions = ((TeamInterestRecyclerAdapter) adapter).getSelectedTeams();
                ArrayList<Integer> selectedTeamIds = new ArrayList<>();
                for (int i = 0; i < filteredTeamsList.size(); i++) {
                    CompetitorsResponse team = filteredTeamsList.get(i);
                    if (selectedPositions[i]) {
                        System.out.println(team.getName());
                        selectedTeamIds.add(Integer.parseInt(team.getId()));
                    }
                }

                if (selectedTeamIds.size() != 0) {
                    // For each selected team, create a preference attributes object
                    PreferenceAttributes[] attributes = new PreferenceAttributes[selectedTeamIds.size()];
                    for (int i = 0; i < attributes.length; i++) {
                        PreferenceAttributes preferenceAttribute = new PreferenceAttributes();
                        preferenceAttribute.setPreference_type("Competitor");
                        preferenceAttribute.setPreference_id(selectedTeamIds.get(i));
                        preferenceAttribute.setAmount(1);
                        preferenceAttribute.setScale(2);

                        attributes[i] = preferenceAttribute;
                    }

                    // Package these preferences into the desired format
                    PreferenceUser user = new PreferenceUser();
                    user.setPreferences_attributes(attributes);
                    PreferenceBody preference = new PreferenceBody();
                    preference.setUser(user);

                    // putPreferences API hit
                    Call<List<PreferencesResponse>> putPrefsCall
                            = ApiUtils.service.putPreferences(ApiUtils.accessToken, ApiUtils.client, ApiUtils.uid, preference);
                    try {
                        putPrefsCall.execute();
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }

                // Start the next activity
                startActivity(nextIntent);
            }
        });
    }
}