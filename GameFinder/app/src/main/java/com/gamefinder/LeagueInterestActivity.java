package com.gamefinder;

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
import android.widget.ListView;
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
    ArrayList<String> leagues;
    ArrayList<Integer> ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_league_interest);

        listView = (ListView) findViewById(R.id.listView);
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
                    final StableArrayAdapter adapter = new StableArrayAdapter(getApplicationContext(),
                            android.R.layout.simple_list_item_1, leagues) {
                        @Override
                        public View getView(int position, View convertView,
                                            ViewGroup parent) {
                            View view =super.getView(position, convertView, parent);

                            TextView textView=(TextView) view.findViewById(android.R.id.text1);

                            /*YOUR CHOICE OF COLOR*/
                            textView.setTextColor(Color.BLACK);
                            return view;
                        }
                    };
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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                view.animate().setDuration(2000).alpha(0)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                leagues.remove(item);
                                adapter.notifyDataSetChanged();
                                view.setAlpha(1);
                            }
                        });
            }

        });
    }

    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }
    }
}
