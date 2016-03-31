package com.gamefinder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 *
 * Created by Paul on 3/6/2016.
 */
public class TeamInterestActivity extends AppCompatActivity {
    public final String BASE_URL = "https://fathomless-woodland-78351.herokuapp.com/api/";
    //private ListView listView;
    static List<List<CompetitorsResponse>> competitorsList;
    Button nextButton;
    List<CompetitorsResponse> teamList;
    MyCustomAdapter dataAdapter = null;
    static int currentLeagueLocation = 0;

    @Override
    @SuppressWarnings("unchecked")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_interest);
        System.out.println(currentLeagueLocation);

        checkButtonClick();

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

            /**listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // When clicked, show a toast with the TextView text
                    CompetitorsResponse team = (CompetitorsResponse) parent.getItemAtPosition(position);
                    Toast.makeText(getApplicationContext(),
                            "Clicked on Team: " + team.getName(),
                            Toast.LENGTH_LONG).show();
                }
            });*/

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

                    final Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    final APIService service = retrofit.create(APIService.class);

                    Call<List<PreferencesResponse>> call = service.putPreferences(accessToken,client,uid,preference);
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
                        /**Toast.makeText(getApplicationContext(),
                                "Clicked on Checkbox: " + cb.getText() +
                                        " is " + cb.isChecked(),
                                Toast.LENGTH_LONG).show();*/
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

    private void checkButtonClick() {
        nextButton = (Button) findViewById(R.id.teamNextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                StringBuffer responseText = new StringBuffer();
                responseText.append("The following were selected...\n");

                List<CompetitorsResponse> teamList = dataAdapter.teamList;
                for (int i = 0; i < teamList.size(); i++) {
                    CompetitorsResponse team = teamList.get(i);
                    if (team.getIsSelected()) {
                        responseText.append("\n" + team.getName()); //send to database here
                    }
                }
                //Toast.makeText(getApplicationContext(), responseText, Toast.LENGTH_LONG).show();
            }
        });
    }
}