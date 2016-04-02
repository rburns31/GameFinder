package com.gamefinder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

/**
 *
 * Created by Paul on 3/6/2016.
 */
public class TeamInterestActivity extends AppCompatActivity {
    static List<List<CompetitorsResponse>> competitorsList;
    List<CompetitorsResponse> teamList;
    MyCustomAdapter dataAdapter = null;
    static int currentLeagueLocation = 0;

    @Override
    @SuppressWarnings("unchecked")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_interest);

        System.out.println(currentLeagueLocation);

        Intent intent = getIntent();
        final String accessToken = intent.getStringExtra("accessToken");
        final String client = intent.getStringExtra("client");
        final String uid = intent.getStringExtra("uid");

        try {
            if (competitorsList == null) {
                Bundle bundleObject = getIntent().getExtras();
                List<List<CompetitorsResponse>> competitors = (List<List<CompetitorsResponse>>) bundleObject.getSerializable("competitorsList");
                competitorsList = competitors;
                System.out.println("Competitors Size: " + competitors.size());
            }

            teamList = competitorsList.get(currentLeagueLocation);
            Collections.sort(teamList);

            // Create an ArrayAdapter
            dataAdapter = new MyCustomAdapter(this, R.layout.team_listview, teamList);
            ListView listView = (ListView) findViewById(R.id.listviewteam);

            // Assign adapter to ListView
            listView.setAdapter(dataAdapter);

            setTitle(competitorsList.get(currentLeagueLocation).get(0).getLeagueName());

            final Intent nextIntent;
            System.out.println("Current League Location: " + currentLeagueLocation);
            System.out.println("Competitors List Size: " + competitorsList.size());
            if(currentLeagueLocation < competitorsList.size()-1) {
                currentLeagueLocation++;
                nextIntent = new Intent(this,TeamInterestActivity.class);
            } else {
                nextIntent = new Intent(this, TvSetupActivity.class);
            }

            // Handle the next button being clicked
            Button nextButton = (Button) findViewById(R.id.nextButton);
            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<CompetitorsResponse> teamList = dataAdapter.teamList;
                    ArrayList<Integer> competitorIDs = new ArrayList<Integer>();
                    for(int i = 0; i < teamList.size(); i++){
                        CompetitorsResponse team = teamList.get(i);
                        if(team.getIsSelected()){
                            competitorIDs.add(Integer.parseInt(team.getId()));
                        }
                    }
                    PreferenceAttributes[] attributes = new PreferenceAttributes[competitorIDs.size()];
                    for (int i = 0; i < attributes.length; i++) {
                        PreferenceAttributes preferenceAttribute = new PreferenceAttributes();
                        preferenceAttribute.setPreference_type("Competitor");
                        preferenceAttribute.setPreference_id(competitorIDs.get(i));
                        preferenceAttribute.setAmount(1);
                        preferenceAttribute.setScale(2);
                        attributes[i] = preferenceAttribute;
                    }
                    System.out.println("ATTRIBUTES LENGTH: " + attributes.length);
                    PreferenceUser user = new PreferenceUser();
                    user.setPreferences_attributes(attributes);
                    PreferenceBody preference = new PreferenceBody();
                    preference.setUser(user);



                    Call<List<PreferencesResponse>> call = ApiUtils.service.putPreferences(accessToken,client,uid,preference);
                    call.enqueue(new Callback<List<PreferencesResponse>>() {
                        @Override
                        public void onResponse(Call<List<PreferencesResponse>> call, retrofit2.Response<List<PreferencesResponse>> response) {
                            if (response.isSuccess()) {
                                System.out.println("successful team interest preferences call");
                            }
                        }

                        @Override
                        public void onFailure(Call<List<PreferencesResponse>> call, Throwable t) {
                            System.out.println(t.getMessage());
                        }
                    });

                    nextIntent.putExtra("accessToken", accessToken);
                    nextIntent.putExtra("client", client);
                    nextIntent.putExtra("uid", uid);
                    startActivity(nextIntent);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class MyCustomAdapter extends ArrayAdapter<CompetitorsResponse> {
        private List<CompetitorsResponse> teamList;

        public MyCustomAdapter(Context context, int textViewResourceId,
                               List<CompetitorsResponse> teamList) {
            super(context, textViewResourceId, teamList);
            this.teamList = new ArrayList<>();
            this.teamList.addAll(teamList);
        }

        private class ViewHolder {
            CheckBox checkbox;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.team_listview, null);

                holder = new ViewHolder();
                holder.checkbox = (CheckBox) convertView.findViewById(R.id.checkbox_interest);
                convertView.setTag(holder);

                holder.checkbox.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;
                        CompetitorsResponse team = (CompetitorsResponse) cb.getTag();
                        team.setIsSelected(cb.isChecked());
                    }
                });
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            CompetitorsResponse team = teamList.get(position);
            holder.checkbox.setText(team.getName());
            holder.checkbox.setChecked(team.getIsSelected());
            holder.checkbox.setTag(team);

            return convertView;
        }
    }
}