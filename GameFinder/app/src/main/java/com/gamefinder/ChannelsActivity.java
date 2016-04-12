package com.gamefinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 */
public class ChannelsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channels);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        final AppCompatActivity thisActivity = this;

        // The layout element which will hold the list view
        final ListView listView = (ListView) findViewById(R.id.channel_listview);

        // Populate the channels list
        List<String> defaultChannelNames = Arrays.asList(getResources().getStringArray(R.array.defaultChannels));

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

                // Open the games screen
                Intent nextIntent = new Intent(thisActivity, GamesScreenActivity.class);
                startActivity(nextIntent);
            }
        });
    }
}