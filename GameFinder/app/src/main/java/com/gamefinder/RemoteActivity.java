package com.gamefinder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.ConsumerIrManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * This activity is the remote control layout
 * It also creates the controls codes from hex values and reads in the controls from file
 * Hex codes taken from: http://www.remotecentral.com/
 */
public class RemoteActivity extends AppCompatActivity {
    /**
     * Maps from the control's name to the code to be transmitted to accomplish it
     */
    public static final HashMap<String, int[]> controls = new HashMap<>();
    /**
     * Maps from the control's name to the frequency with which to transmit its code
     */
    public static final HashMap<String, Integer> frequencies = new HashMap<>();

    /**
     * Holds the instance of the device's IR hardware so codes can be transmitted via it
     */
    private ConsumerIrManager irManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote);

        // Gain access to the device's IR hardware
        Context mContext = RemoteActivity.this;
        irManager = (ConsumerIrManager) mContext.getSystemService(CONSUMER_IR_SERVICE);

        // Read in the hex.csv file and write the outputted codes to the console
        BufferedReader br;
        try {
            br = new BufferedReader(new InputStreamReader(
                    mContext.getResources().openRawResource(R.raw.hex)));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                sb.append(parts[0]).append(",");
                sb.append(count2duration(hex2dec(parts[1]))).append("\n");
            }
            System.out.println(sb);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Read in the codes.csv file and populate the controls map
        BufferedReader br2;
        try {
            br2 = new BufferedReader(new InputStreamReader(
                    mContext.getResources().openRawResource(R.raw.codes)));
            String line;
            while ((line = br2.readLine()) != null) {
                String[] parts = line.split(",");
                int[] payload = new int[parts.length - 2];
                for (int i = 2; i < parts.length; i++) {
                    payload[i - 2] = Integer.parseInt(parts[i]);
                }
                frequencies.put(parts[0], Integer.parseInt(parts[1]));
                controls.put(parts[0], payload);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Button handler for any control clicked
     * @param v The button which was pushed
     */
    public void onControlClicked(View v) {
        Spinner tvBrands = (Spinner) findViewById(R.id.spinner);
        Button thisControl = (Button) v;
        String id = tvBrands.getSelectedItem().toString().toLowerCase() + "_"
                + thisControl.getText().toString().toLowerCase().replace(" ","_");
        int[] payload = controls.get(id);
        int frequency = frequencies.get(id);
        if (payload != null && frequency != 0) {
            irManager.transmit(frequency, payload);
        } else {
            AlertDialog alertDialog
                    = new AlertDialog.Builder(RemoteActivity.this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("That control is not currently supported for that brand. Sorry!");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
            System.out.println("That control is not currently supported for that brand. Sorry!");
        }
    }

    /**
     * Taken from
     *   https://github.com/rngtng/IrDude/blob/master/src/com/rngtng/irdude/MainActivity.java
     * Converts a string of hex values separated by spaces into a string of decimal values
     *   separated by commas where the first element is the frequency
     * @param irData A string of hex values separated by spaces
     * @return A string of decimal values separated by commas, the first element is the frequency
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
     * Converts a string of decimal values separated by commas where the first element is the
     *   frequency into a string of decimal values representing the duration of bursts to be
     *   transmitted, separated by commas where the first element is the frequency
     * @param countPattern A string of decimal values separated by commas,
     *                       the first element is the frequency
     * @return A string of decimal values representing the duration of bursts to be transmitted,
     *           separated by commas where the first element is the frequency
     */
    private String count2duration(String countPattern) {
        List<String> list = new ArrayList<>(Arrays.asList(countPattern.split(",")));
        int thisFreq = Integer.parseInt(list.get(0));
        int pulses = 1000000 / thisFreq;
        int count;
        int duration;

        list.remove(0);

        for (int i = 0; i < list.size(); i++) {
            count = Integer.parseInt(list.get(i));
            duration = count * pulses;
            list.set(i, Integer.toString(duration));
        }

        String durationPattern = thisFreq + ",";
        for (String s : list) {
            durationPattern += s + ",";
        }
        // Cut off the trailing comma
        durationPattern = durationPattern.substring(0, durationPattern.length() - 1);

        //System.out.println("Frequency: " + thisFreq);
        //System.out.println("Duration Pattern: " + durationPattern);

        return durationPattern;
    }
}