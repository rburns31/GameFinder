package com.example.ryan.myapplication;

import android.content.Context;
import android.hardware.ConsumerIrManager;
import android.hardware.ConsumerIrManager.CarrierFrequencyRange;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends ActionBarActivity {
    public static final String SAMSUNG_POWER_HEX = "0000 006d 0022 0003 00a9 00a8 0015 003f 0015 " +
            "003f 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 003f 0015 " +
            "003f 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 " +
            "003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0040 0015 " +
            "0015 0015 003f 0015 003f 0015 003f 0015 003f 0015 003f 0015 003f 0015 0702 00a9 " +
            "00a8 0015 0015 0015 0e6e";
    public static final String SAMSUNG_CHANNEL_UP_HEX = "0000 006d 0022 0003 00a9 00a8 0015 003f " +
            "0015 003f 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 003f " +
            "0015 003f 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 " +
            "0015 003f 0015 0015 0015 0015 0015 003f 0015 0015 0015 0015 0015 0015 0015 003f " +
            "0015 0015 0015 003f 0015 003f 0015 0015 0015 0040 0015 003f 0015 003f 0015 0702 " +
            "00a9 00a8 0015 0015 0015 0e6e";
    public static final String TEST_HEX = "0000 006D 0000 0022 00AC 00AC 0015 0040 0015 0040 0015 0040 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0040 0015 0040 0015 0040 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0040 0015 0040 0015 0015 0015 0015 0015 0040 0015 0040 0015 0040 0015 0040 0015 0015 0015 0015 0015 0040 0015 0040 0015 0015 0015 0689";

    private int frequency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context mContext = MainActivity.this;
                ConsumerIrManager irManager =
                        (ConsumerIrManager) mContext.getSystemService(CONSUMER_IR_SERVICE);
                CarrierFrequencyRange freqRange = irManager.getCarrierFrequencies()[0];

                String[] temp = count2duration(hex2dec(TEST_HEX)).split(",");
                int[] durations = new int[temp.length - 1];
                for (int i = 1; i < temp.length; i++) {
                    durations[i - 1] = Integer.parseInt(temp[i]);
                    //System.out.println(durations[i - 1]);
                }

                irManager.transmit(frequency, durations);

                TextView textView = (TextView) findViewById(R.id.textView);
                textView.setText("Did it work?");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
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
        List<String> list = new ArrayList<String>(Arrays.asList(irData.split(" ")));
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
        List<String> list = new ArrayList<String>(Arrays.asList(countPattern.split(",")));
        frequency = Integer.parseInt(list.get(0));
        int pulses = 1000000 / frequency;
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

        System.out.println("Frequency: " + frequency);
        System.out.println("Duration Pattern: " + durationPattern);

        return durationPattern;
    }
}