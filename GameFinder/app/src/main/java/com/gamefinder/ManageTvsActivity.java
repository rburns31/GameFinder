package com.gamefinder;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;

public class ManageTvsActivity extends AppCompatActivity {
    private AppCompatActivity thisActivity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_tvs);

        // Keeps the application from throwing an error when doing a synchronous API call
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Display a diagnostic tip dialog to the user about how to function this screen
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setTitle("Tips");
        dialog.setMessage("To select a tv as your current tv simply check the box next to it's name.");
        dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        dialog.show();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final RecyclerView.Adapter adapter = new ManageTvsRecyclerAdapter();
        recyclerView.setAdapter(adapter);

        Button doneButton = (Button) findViewById(R.id.doneButton);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set the new selected value
                int selectedTvId = ((ManageTvsRecyclerAdapter) adapter).getSelectedTvId();
                Television tvSelect = new Television();
                tvSelect.setSelected(true);
                Call<List<TelevisionResponse>> putTelevisionSelectedCall
                        = ApiUtils.service.putTelevisionSelected(ApiUtils.accessToken, ApiUtils.client, ApiUtils.uid, tvSelect, selectedTvId);
                System.out.println(putTelevisionSelectedCall.request().toString());
                try {
                    putTelevisionSelectedCall.execute();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }

                startActivity(new Intent(thisActivity, GamesScreenActivity.class));
            }
        });
    }
}