package com.gamefinder;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LeagueInterestActivity extends AppCompatActivity {

    public final String BASE_URL = "https://fathomless-woodland-78351.herokuapp.com/api/";
    private ListView listView;
    ArrayAdapter<String> adapter;
    AppCompatActivity thisActivity = this;
    ArrayList<String> leagues;
    ArrayList<Integer> ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_league_interest);

        listView = (ListView) findViewById(R.id.league_listview);

        Intent intent = getIntent();
        String accessToken = intent.getStringExtra("accessToken");
        String client = intent.getStringExtra("client");
        String uid = intent.getStringExtra("uid");

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final APIService service = retrofit.create(APIService.class);

        leagues = new ArrayList<String>();
        ids = new ArrayList<Integer>();

        Call<List<LeaguesResponse>> call = service.getLeagues(accessToken, client, uid);
        call.enqueue(new Callback<List<LeaguesResponse>>() {
            @Override
            public void onResponse(Call<List<LeaguesResponse>> call, retrofit2.Response<List<LeaguesResponse>> response) {
                if (response.isSuccess()) {
                    List<LeaguesResponse> responseBody = response.body();
                    for (int i = 0; i < responseBody.size(); i++) {
                        leagues.add(responseBody.get(i).getName());
                        ids.add(Integer.parseInt(responseBody.get(i).getId())); //ids not used yet
                    }
                    final ArrayAdapter<LeaguesResponse> adapter = new ListViewAdapter(thisActivity,
                            R.layout.item_listview, responseBody);
                    listView.setAdapter(adapter);
                    System.out.println(responseBody.toString());
                } else {
                    System.out.println("response failure");
                }
            }

            @Override
            public void onFailure(Call<List<LeaguesResponse>> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });

        final Button nextButton = (Button) findViewById(R.id.nextButton);
        final Intent nextIntent = new Intent(this, TeamInterestActivity.class);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start the Team Interest Activity
                //startActivity(nextIntent);
                // Temporary (for initial code demo)
                openRemote();
            }
        });
    }

    /*private AdapterView.OnItemClickListener onItemClickListener() { //Might come back to it
        return new AdapterView.OnItemClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Dialog dialog = new Dialog(getApplicationContext());
                dialog.setContentView(R.layout.layout_dialog);
                dialog.setTitle("Leagues");

                TextView name = (TextView) dialog.findViewById(R.id.league_name);
                TextView starRate = (TextView) dialog.findViewById(R.id.rate);

                LeaguesResponse leagues = (LeaguesResponse) parent.getAdapter().getItem(position);
                name.setText("League name: " + leagues.getName());
                starRate.setText("Your rating: " + leagues.getRatingStar());

                dialog.show();
            }
        };
    }*/

    private void openRemote() {
        Intent intent = new Intent(this, RemoteActivity.class);
        startActivity(intent);
    }
}
