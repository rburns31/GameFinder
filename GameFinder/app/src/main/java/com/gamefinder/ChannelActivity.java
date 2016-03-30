package com.gamefinder;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 *
 */
public class ChannelActivity extends AppCompatActivity {
    /**
     * Maps from the channel's acronym to the user inputted channel number for that channel
     */
    public static final HashMap<String, String> channels = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channels);

        final ListView listView = (ListView) findViewById(R.id.channel_listview);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Intent intent = getIntent();
        final String accessToken = intent.getStringExtra("accessToken");
        final String client = intent.getStringExtra("client");
        final String uid = intent.getStringExtra("uid");

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://fathomless-woodland-78351.herokuapp.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final APIService service = retrofit.create(APIService.class);

        final ArrayList<String> channels = new ArrayList<>();
        final AppCompatActivity thisActivity = this;

       /** Call<List<ChannelResponse>> call = service.postChannels(accessToken, client, uid);
        call.enqueue(new Callback<List<ChannelResponse>>() {
            @Override
            public void onResponse(Call<List<ChannelResponse>> call, retrofit2.Response<List<ChannelResponse>> response) {
                if (response.isSuccess()) {
                    List<ChannelResponse> responseBody = response.body();
                    for (int i = 0; i < responseBody.size(); i++) {
                        channels.add(responseBody.get(i).getName());
                        //ids.add(Integer.parseInt(responseBody.get(i).getId())); //ids not used yet
                    }
                    final ArrayAdapter<ChannelResponse> adapter = new ChannelListViewAdapter(
                            thisActivity, R.layout.channel_listview, responseBody);
                    listView.setAdapter(adapter);
                    System.out.println(responseBody.toString());
                } else {
                    System.out.println("response failure");
                }
            }

            @Override
            public void onFailure(Call<List<ChannelResponse>> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });*/



        Button nextButton = (Button) findViewById(R.id.nextButton);
        final Intent nextIntent = new Intent(this, GamesScreenActivity.class);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextIntent.putExtra("accessToken", accessToken);
                nextIntent.putExtra("client", client);
                nextIntent.putExtra("uid", uid);
                startActivity(nextIntent);
            }
        });
    }
}