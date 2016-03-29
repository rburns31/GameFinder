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
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LeagueInterestActivity extends AppCompatActivity {

    public final String BASE_URL = "https://fathomless-woodland-78351.herokuapp.com/api/";
    private ListView listView;
    ArrayAdapter<String> adapter;
    AppCompatActivity thisActivity = this;
    ArrayList<String> leagues;
    ArrayList<Integer> ids;
    List<LeaguesResponse> responseBody;
    int[] leagueIDs, ratings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_league_interest);

        listView = (ListView) findViewById(R.id.league_listview);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Intent intent = getIntent();
        final String accessToken = intent.getStringExtra("accessToken");
        final String client = intent.getStringExtra("client");
        final String uid = intent.getStringExtra("uid");
        final List<List<CompetitorsResponse>> competitors = new ArrayList<>();

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
                    responseBody = response.body();
                    for (int i = 0; i < responseBody.size(); i++) {
                        leagues.add(responseBody.get(i).getName());
                        ids.add(Integer.parseInt(responseBody.get(i).getId())); //ids not used yet
                    }
                    final ArrayAdapter<LeaguesResponse> adapter = new LeagueListViewAdapter(thisActivity,
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
                PreferenceAttributes[] attributes = new PreferenceAttributes[responseBody.size()];
                leagueIDs = new int[responseBody.size()];
                ratings = new int[leagueIDs.length];
                for (int i = 0; i < responseBody.size(); i++) {
                    int id = Integer.parseInt(responseBody.get(i).getId());
                    leagueIDs[i] = id;
                    Float ratingStar = responseBody.get(i).getRatingStar();
                    ratings[i] = ratingStar.intValue();
                    PreferenceAttributes preferenceAttribute = new PreferenceAttributes();
                    preferenceAttribute.setPreference_type("League");
                    preferenceAttribute.setPreference_id(id);
                    preferenceAttribute.setAmount(ratingStar.intValue());
                    preferenceAttribute.setScale(5);
                    attributes[i] = preferenceAttribute;
                }
                PreferenceUser user = new PreferenceUser();
                user.setPreferences_attributes(attributes);
                PreferenceBody preference = new PreferenceBody();
                preference.setUser(user);

                Call<List<PreferencesResponse>> call = service.putPreferences(accessToken,client,uid,preference);
                call.enqueue(new Callback<List<PreferencesResponse>>() {
                    @Override
                    public void onResponse(Call<List<PreferencesResponse>> call, retrofit2.Response<List<PreferencesResponse>> response) {
                        if (response.isSuccess()) {

                            for (int i = 0; i < leagueIDs.length; i++) {
                                int id = leagueIDs[i];
                                int rating = ratings[i];
                                if (rating > 0) {
                                    Call<List<CompetitorsResponse>> getCompetitors = service.getCompetitors(String.valueOf(id), accessToken, client, uid);
                                    try {
                                        List<CompetitorsResponse> responseBody = getCompetitors.execute().body();
                                        competitors.add(responseBody);
                                    } catch (IOException e) {
                                        System.out.println(e.getMessage());
                                    }
/*                                    getCompetitors.enqueue(new Callback<List<CompetitorsResponse>>() {
                                        @Override
                                        public void onResponse(Call<List<CompetitorsResponse>> call, retrofit2.Response<List<CompetitorsResponse>> response) {
                                            if (response.isSuccess()) {
                                                List<CompetitorsResponse> responseBody = response.body();
                                                System.out.println(responseBody.size());
                                                competitors.addAll(responseBody);

                                            } else {
                                                System.out.println("response failure");
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<List<CompetitorsResponse>> call, Throwable t) {
                                            System.out.println(t.getMessage());
                                        }
                                    });*/
                                }
                            }
                            Bundle bundleObject = new Bundle();
                            bundleObject.putSerializable("competitorsList", (Serializable) competitors);
                            nextIntent.putExtras(bundleObject);
                            nextIntent.putExtra("accessToken", accessToken);
                            nextIntent.putExtra("client", client);
                            nextIntent.putExtra("uid", uid);
                            startActivity(nextIntent);
                        } else {
                            System.out.println("response failure");
                        }
                    }

                    @Override
                    public void onFailure(Call<List<PreferencesResponse>> call, Throwable t) {
                        System.out.println(t.getMessage());
                    }
                });



                startActivity(nextIntent);
                // Temporary (for initial code demo)
                //openRemote();
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
