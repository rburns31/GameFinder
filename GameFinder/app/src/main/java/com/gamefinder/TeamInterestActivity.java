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
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paul on 3/6/2016.
 */
public class TeamInterestActivity extends AppCompatActivity {
    public final String BASE_URL = "https://fathomless-woodland-78351.herokuapp.com/api/";
    private ListView listView;
    Button nextButton;
    List<List<CompetitorsResponse>> competitors;
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
            Bundle bundleObject = getIntent().getExtras();
            competitors = (List<List<CompetitorsResponse>>) bundleObject.getSerializable("competitorsList");
            System.out.println(competitors.size());
        } catch (Exception e) {
            e.printStackTrace();
        }

        teamList = competitors.get(currentLeagueLocation);

        //create an ArrayAdapter
        dataAdapter = new MyCustomAdapter(this, R.layout.team_listview, teamList);
        ListView listView = (ListView) findViewById(R.id.listviewteam);
        // Assign adapter to ListView

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // When clicked, show a toast with the TextView text
                CompetitorsResponse team = (CompetitorsResponse) parent.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(),
                        "Clicked on Team: " + team.getName(),
                        Toast.LENGTH_LONG).show();
            }
        });

        final Intent nextIntent;
        if(currentLeagueLocation > competitors.size()) {
            nextIntent = new Intent(this, TvSetupActivity.class);
        } else {
            currentLeagueLocation++;
            nextIntent = new Intent(this,TeamInterestActivity.class);
        }

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //nextIntent.putExtras();
                nextIntent.putExtra("accessToken", accessToken);
                nextIntent.putExtra("client", client);
                nextIntent.putExtra("uid", uid);
                startActivity(nextIntent);
            }
        });
    }

    private class MyCustomAdapter extends ArrayAdapter<CompetitorsResponse> {

        private List<CompetitorsResponse> teamList;

        public MyCustomAdapter(Context context, int textViewResourceId,
                               List<CompetitorsResponse> teamList) {
            super(context, textViewResourceId, teamList);
            this.teamList = new ArrayList<CompetitorsResponse>();
            this.teamList.addAll(teamList);
        }

        private class ViewHolder {
            TextView teamName;
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
                holder.teamName = (TextView) convertView.findViewById(R.id.team_name);
                holder.checkbox = (CheckBox) convertView.findViewById(R.id.checkbox_interest);
                convertView.setTag(holder);

                holder.checkbox.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;
                        CompetitorsResponse team = (CompetitorsResponse) cb.getTag();
                        Toast.makeText(getApplicationContext(),
                                "Clicked on Checkbox: " + cb.getText() +
                                        " is " + cb.isChecked(),
                                Toast.LENGTH_LONG).show();
                        team.setIsSelected(cb.isChecked());
                    }
                });
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            CompetitorsResponse team = teamList.get(position);
            holder.teamName.setText(" (" +  team.getName() + ")");
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
                for(int i = 0; i < teamList.size(); i++){
                    CompetitorsResponse team = teamList.get(i);
                    if(team.getIsSelected()){
                        responseText.append("\n" + team.getName()); //send to database here
                    }
                }

                Toast.makeText(getApplicationContext(),
                        responseText, Toast.LENGTH_LONG).show();

            }
        });

    }
}