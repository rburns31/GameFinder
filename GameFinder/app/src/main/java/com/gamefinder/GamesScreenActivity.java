package com.gamefinder;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 * Code for creating a drawer layout was adapted from this source: http://blog.teamtreehouse.com/add-navigation-drawer-android
 */
public class GamesScreenActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ActionBarDrawerToggle drawerToggle;

    private AppCompatActivity thisActivity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamescreen);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Set up the side drawer list
        ListView drawer = (ListView)findViewById(R.id.drawer);
        final String[] drawerItems
                = { "Update Interests", "Manage TVs", "Remote", "Account Management", "Sign Out" };
        drawer.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, drawerItems));

        drawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (drawerItems[position].equals("Remote")) {
                    startActivity(new Intent(thisActivity, RemoteActivity.class));

                } else if (drawerItems[position].equals("Update Interests")) {
                    Intent intent = new Intent(thisActivity, LeagueInterestActivity.class);
                    intent.putExtra("Update", true);
                    startActivity(intent);

                } else if (drawerItems[position].equals("Sign Out")) {
                    ApiUtils.accessToken = null;
                    ApiUtils.client = null;
                    ApiUtils.uid = null;

                    startActivity(new Intent(thisActivity, LoginActivity.class));

                }
            }
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this,
                drawerLayout, R.string.drawer_open, R.string.drawer_close) {

            // Called when a drawer has settled in a completely open state
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Options Menu");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            // Called when a drawer has settled in a completely closed state
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(R.string.title_activity_games_screen);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(drawerToggle);

        //
        try {
            // get a list of games from the service
            Call<List<GamesResponse>> call
                    = ApiUtils.service.getGames(ApiUtils.accessToken, ApiUtils.client, ApiUtils.uid);
            call.enqueue(new Callback<List<GamesResponse>>() {
                @Override
                public void onResponse(Call<List<GamesResponse>> call, Response<List<GamesResponse>> response) {
                    // retrieve response body on success and add them to the spinner
                    if (response.isSuccess()) {
                        final List<GamesResponse> responseBody = response.body();
                        final List<GamesResponse> gamesToDisplay = new ArrayList<>();

                        ArrayList<String> leaguesPresent = new ArrayList<>();
                        leaguesPresent.add("All");
                        for (GamesResponse game: responseBody) {
                            if (!leaguesPresent.contains(game.getLeague().getName())) {
                                leaguesPresent.add(game.getLeague().getName());
                            }
                        }

                        Spinner leaguesSpinner = (Spinner) findViewById(R.id.leaguesSpinner);
                        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                                thisActivity, android.R.layout.simple_spinner_item, leaguesPresent);
                        leaguesSpinner.setAdapter(spinnerAdapter);
                        leaguesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                TextView selected = (TextView) view;
                                if (!selected.getText().toString().equals("All")) {
                                    gamesToDisplay.clear();
                                    for (GamesResponse game: responseBody) {
                                        if (game.getLeague().getName().equals(selected.getText().toString())) {
                                            gamesToDisplay.add(game);
                                        }
                                    }
                                    adapter = new GamesRecyclerAdapter(gamesToDisplay);
                                } else {
                                    adapter = new GamesRecyclerAdapter(responseBody);
                                }
                                recyclerView.setAdapter(adapter);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parentView) { }
                        });

                        adapter = new GamesRecyclerAdapter(gamesToDisplay);
                        recyclerView.setAdapter(adapter);
                    }
                }

                @Override
                public void onFailure(Call<List<GamesResponse>> call, Throwable t) {
                    System.out.println(t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState){
            super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return drawerToggle.onOptionsItemSelected(item);
    }
}