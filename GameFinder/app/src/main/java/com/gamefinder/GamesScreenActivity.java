package com.gamefinder;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 * Code for creating a drawer layout was adapted from this source: http://blog.teamtreehouse.com/add-navigation-drawer-android
 */
public class GamesScreenActivity extends AppCompatActivity {
    /**
     *
     */
    private RecyclerView recyclerView;
    /**
     *
     */
    private RecyclerView.Adapter adapter;
    /**
     *
     */
    private ActionBarDrawerToggle drawerToggle;
    /**
     *
     */
    private AppCompatActivity thisActivity = this;

    /**
     *
     *
     */
    private ScheduleClient scheduleClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamescreen);

        // Create a new service client and bind our activity to this service
        scheduleClient = new ScheduleClient(this);
        scheduleClient.doBindService();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Set up the side drawer list
        ListView drawer = (ListView)findViewById(R.id.drawer);
        final String[] drawerItems
                = { "Update Interests", "Manage Tvs", "Add Tv", "Remote", "Account Management", "Sign Out" };
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

                } else if (drawerItems[position].equals("Manage Tvs")) {
                    startActivity(new Intent(thisActivity, ManageTvsActivity.class));

                } else if (drawerItems[position].equals("Add Tv")) {
                    startActivity(new Intent(thisActivity, TvSetupActivity.class));

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

                        //
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
                                    for (GamesResponse game : responseBody) {
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
                            public void onNothingSelected(AdapterView<?> parentView) {
                            }
                        });

                        adapter = new GamesRecyclerAdapter(gamesToDisplay);
                        recyclerView.setAdapter(adapter);

                        Calendar c = Calendar.getInstance();

                        String startTime = responseBody.get(0).getStart_time();
                        String hour = startTime.split("[T.]")[1];
                        String minute = hour.substring(0, hour.length() - 3);

                        //set notification for top most game
                        //c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
                        //c.set(Calendar.MINUTE, Integer.parseInt(minute)-5); //5 minutes before
                        //c.set(Calendar.SECOND, 0);

                        //ask service to set an alarm for that date
                        //scheduleClient.setAlarmForNotification(c);

                        //for testing and/or better way
                        PendingIntent contentIntent = PendingIntent.getActivity(thisActivity, 0, new Intent(thisActivity, GamesScreenActivity.class), 0);
                        int notif_id = 1;
                        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext())
                                .setContentIntent(contentIntent)
                                .setContentTitle(responseBody.get(0).getCompetitor_1().getName() + " vs. " + responseBody.get(0).getCompetitor_2().getName())
                                .setContentText("Game Starting Soon!")
                                .setWhen(System.currentTimeMillis() + (1000*30))
                                .setSmallIcon(R.drawable.ic_dialog_alert);
                        Notification notification = notificationBuilder.build();

                        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        mNotificationManager.notify(notif_id, notification);

                        System.out.println("Alarm set: " + (System.currentTimeMillis() + 1000*30));
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