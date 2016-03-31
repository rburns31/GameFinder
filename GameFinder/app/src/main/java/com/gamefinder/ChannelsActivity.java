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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 *
 */
public class ChannelsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channels);
        final AppCompatActivity thisActivity = this;

        // The layout element which will hold the list view
        final ListView listView = (ListView) findViewById(R.id.channel_listview);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Get the pertinent info passed into this activity from the last activity
        Intent intent = getIntent();
        final String accessToken = intent.getStringExtra("accessToken");
        final String client = intent.getStringExtra("client");
        final String uid = intent.getStringExtra("uid");

        // The service to hit the API to post channels
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://fathomless-woodland-78351.herokuapp.com/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        final APIService service = retrofit.create(APIService.class);

        // Populate the channels list
        List<String> defaultChannelNames = Arrays.asList(getResources().getStringArray(R.array.defaultChannels));
        System.out.println(defaultChannelNames.get(0));
        List<Channel> defaultChannels = new ArrayList<>();
        for (int i = 0; i < defaultChannelNames.size(); i++) {
            defaultChannels.add(new Channel("0", defaultChannelNames.get(i), "0"));
        }
        ArrayAdapter<Channel> adapter = new ChannelListViewAdapter(this, R.layout.channel_listview, defaultChannels);
        listView.setAdapter(adapter);

        // Set a listener on the next button to post any channels which were input before moving on
        Button nextButton = (Button) findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /**final Channel[] channels = new Channel[1];
                ChannelResponse channelResponse = new ChannelResponse();
                channelResponse.setChannels(channels);

                Call<List<ChannelResponse>> call = service.postChannels(accessToken, client, uid, channelResponse);
                call.enqueue(new Callback<List<ChannelResponse>>() {
                    @Override
                    public void onResponse(Call<List<ChannelResponse>> call, retrofit2.Response<List<ChannelResponse>> response) {
                        if (response.isSuccess()) {
                            List<ChannelResponse> responseBody = response.body();
                            for (int i = 0; i < responseBody.size(); i++) {
                                //channels.add(responseBody.get(i).getName());
                                //ids.add(Integer.parseInt(responseBody.get(i).getId())); //ids not used yet
                            }
                            final ArrayAdapter<ChannelResponse> adapter = new ChannelListViewAdapter(
                                    thisActivity, R.layout.channel_listview, responseBody);
                            listView.setAdapter(adapter);
                            for (int i = 0; i < responseBody.size(); i++) {
                                System.out.println("============> " + responseBody.get(i).getChannels().toString());
                            }
                        } else {
                            System.out.println("Response failure when trying to post channels");
                        }
                    }

                    @Override
                    public void onFailure(Call<List<ChannelResponse>> call, Throwable t) {
                        System.out.println(t.getMessage());
                    }
                });*/

                // Open the games screen and pass in the authentication headers
                Intent nextIntent = new Intent(thisActivity, GamesScreenActivity.class);
                nextIntent.putExtra("accessToken", accessToken);
                nextIntent.putExtra("client", client);
                nextIntent.putExtra("uid", uid);
                startActivity(nextIntent);
            }
        });
    }
}