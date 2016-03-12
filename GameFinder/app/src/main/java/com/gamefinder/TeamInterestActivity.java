package com.gamefinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paul on 3/6/2016.
 */
public class TeamInterestActivity extends AppCompatActivity {
    public final String BASE_URL = "https://fathomless-woodland-78351.herokuapp.com/api/";
    private ListView listView;
    ArrayAdapter<String> adapter;

    @Override
    @SuppressWarnings("unchecked")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_interest);

        Intent intent = getIntent();
        try {
            Bundle bundleObject = getIntent().getExtras();
            List<List<CompetitorsResponse>> competitors = (List<List<CompetitorsResponse>>) bundleObject.getSerializable("competitorsList");
            System.out.println(competitors.size());
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
