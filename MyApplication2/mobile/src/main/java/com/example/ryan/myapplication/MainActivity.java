package com.example.ryan.myapplication;

import android.content.Context;
import android.hardware.ConsumerIrManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends ActionBarActivity {
    public static final String SAMSUNG_CHANNEL_UP_HEX = "0000 006d 0022 0003 00a9 00a8 0015 003f " +
            "0015 003f 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 003f " +
            "0015 003f 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 " +
            "0015 003f 0015 0015 0015 0015 0015 003f 0015 0015 0015 0015 0015 0015 0015 003f " +
            "0015 0015 0015 003f 0015 003f 0015 0015 0015 0040 0015 003f 0015 003f 0015 0702 " +
            "00a9 00a8 0015 0015 0015 0e6e";

    public static final int[] SAMSUNG_POWER =
            {4394,4368,546,1638,546,1638,546,1638,546,546,546,546,546,546,546,546,546,546,546,1638,
                    546,1638,546,1638,546,546,546,546,546,546,546,546,546,546,546,546,546,1638,546,
                    546,546,546,546,546,546,546,546,546,546,546,546,1664,546,546,546,1638,546,1638,
                    546,1638,546,1638,546,1638,546,1638,546,46644,4394,4368,546,546,546,96044};
    public static final int[] SAMSUNG_CHANNEL_UP =
            {4394,4368,546,1638,546,1638,546,1638,546,546,546,546,546,546,546,546,546,546,546,1638,
                    546,1638,546,1638,546,546,546,546,546,546,546,546,546,546,546,546,546,1638,546,
                    546,546,546,546,1638,546,546,546,546,546,546,546,1638,546,546,546,1638,546,1638,
                    546,546,546,1664,546,1638,546,1638,546,46644,4394,4368,546,546,546,96044,};

    public static final int[] TOSHIBA_POWER =
            {8550,4300,550,525,550,525,550,525,550,525,550,525,550,525,550,1600,550,525,550,1600,
                    550,1600,550,1600,550,1600,550,1600,550,1600,550,525,550,1600,550,525,550,1600,
                    550,525,550,525,550,1600,550,525,550,525,550,525,550,1600,550,525,550,1600,550,
                    1600,550,525,550,1600,550,1600,550,1600,550,38275,8550,2150,550,91825};

    public static final HashMap<String, int[]> controls = new HashMap<>();
    static {
        controls.put("SAMSUNG_POWER", SAMSUNG_POWER);
        controls.put("SAMSUNG_CHANNEL_UP", SAMSUNG_CHANNEL_UP);

        controls.put("TOSHIBA_POWER", TOSHIBA_POWER);
    }

    private static int FREQUENCY;
    private ConsumerIrManager irManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context mContext = MainActivity.this;
        irManager = (ConsumerIrManager) mContext.getSystemService(CONSUMER_IR_SERVICE);

        count2duration(hex2dec(SAMSUNG_CHANNEL_UP_HEX));
    }

    /**
     * Button handler for any control clicked
     * @param v The button which was pushed
     */
    public void onControlClicked(View v) {
        Spinner tvBrands = (Spinner) findViewById(R.id.spinner);
        Button thisControl = (Button) v;
        int[] payload = controls.get(tvBrands.getSelectedItem().toString().toUpperCase() + "_"
                + thisControl.getText().toString().toUpperCase().replace(" ","_"));
        if (payload != null) {
            irManager.transmit(FREQUENCY, payload);
        } else {
            System.out.println("That control is not currently supported for that brand. Sorry!");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu, this adds items to the action bar if it is present
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Handle action bar item clicks here
     * The action bar will automatically handle clicks on the Home/Up button, as long as you
     *   specify a parent activity in AndroidManifest.xml
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Taken from https://github.com/rngtng/IrDude/blob/master/src/com/rngtng/irdude/MainActivity.java
     * @param irData
     * @return
     */
    private String hex2dec(String irData) {
        List<String> list = new ArrayList<>(Arrays.asList(irData.split(" ")));
        list.remove(0); // dummy
        int frequency = Integer.parseInt(list.remove(0), 16); // frequency
        list.remove(0); // seq1
        list.remove(0); // seq2

        for (int i = 0; i < list.size(); i++) {
            list.set(i, Integer.toString(Integer.parseInt(list.get(i), 16)));
        }

        frequency = (int) (1000000 / (frequency * 0.241246));
        list.add(0, Integer.toString(frequency));

        irData = "";
        for (String s : list) {
            irData += s + ",";
        }
        return irData;
    }

    /**
     * Taken from http://stackoverflow.com/questions/20244337/consumerirmanager-api-19
     * @param countPattern
     * @return
     */
    private String count2duration(String countPattern) {
        List<String> list = new ArrayList<>(Arrays.asList(countPattern.split(",")));
        FREQUENCY = Integer.parseInt(list.get(0));
        int pulses = 1000000 / FREQUENCY;
        int count;
        int duration;

        list.remove(0);

        for (int i = 0; i < list.size(); i++) {
            count = Integer.parseInt(list.get(i));
            duration = count * pulses;
            list.set(i, Integer.toString(duration));
        }

        String durationPattern = "";
        for (String s : list) {
            durationPattern += s + ",";
        }

        System.out.println("Frequency: " + FREQUENCY);
        System.out.println("Duration Pattern: " + durationPattern);

        return durationPattern;
    }
}