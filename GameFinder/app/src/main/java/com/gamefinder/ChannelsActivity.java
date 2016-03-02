package com.gamefinder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.HashMap;

/**
 *
 */
public class ChannelsActivity extends AppCompatActivity {
    /**
     * Maps from the channel's acronym to the user inputted channel number for that channel
     */
    public static final HashMap<String, String> channels = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channels);
    }
}