package com.gamefinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Paul on 3/6/2016.
 */
public class TeamInterestActivity extends AppCompatActivity {
    public final String BASE_URL = "https://fathomless-woodland-78351.herokuapp.com/api/";
    private ListView listView;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_interest);

        Intent intent = getIntent();

        ArrayList<Integer> ids = intent.getIntegerArrayListExtra("ids");
        ArrayList<Integer> league_ids = intent.getIntegerArrayListExtra("league_ids");
        ArrayList<String> names = intent.getStringArrayListExtra("names");

        System.out.println(ids.size());

    }
}
