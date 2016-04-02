package com.gamefinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GamesScreenActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private AppCompatActivity thisActivity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamescreen);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        try {
            Intent intent = getIntent();
            final String accessToken = intent.getStringExtra("accessToken");
            final String client = intent.getStringExtra("client");
            final String uid = intent.getStringExtra("uid");

            Call<List<GamesResponse>> call = ApiUtils.service.getGames(accessToken, client, uid);
            call.enqueue(new Callback<List<GamesResponse>>() {
                @Override
                public void onResponse(Call<List<GamesResponse>> call, Response<List<GamesResponse>> response) {
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
                        System.out.println("GamesResponse Size: " + responseBody.size());
                        System.out.println("Number of leagues present: " + Integer.toString(leaguesPresent.size() - 1));

                        Spinner leaguesSpinner = (Spinner) findViewById(R.id.leaguesSpinner);
                        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                                thisActivity, android.R.layout.simple_spinner_item, leaguesPresent);
                        leaguesSpinner.setAdapter(spinnerAdapter);
                        leaguesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                TextView selected = (TextView) view;
                                System.out.println(selected.getText().toString());
                                if (!selected.getText().toString().equals("All")) {
                                    gamesToDisplay.clear();
                                    for (GamesResponse game: responseBody) {
                                        if (game.getLeague().getName().equals(selected.getText().toString())) {
                                            gamesToDisplay.add(game);
                                        }
                                    }
                                    adapter = new RecyclerAdapter(gamesToDisplay);
                                } else {
                                    adapter = new RecyclerAdapter(responseBody);
                                }
                                recyclerView.setAdapter(adapter);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parentView) { }
                        });

                        adapter = new RecyclerAdapter(gamesToDisplay);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.games_screen_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(new Intent(this, RemoteActivity.class));
        return true;
    }
}