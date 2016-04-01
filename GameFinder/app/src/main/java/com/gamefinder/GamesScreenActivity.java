package com.gamefinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GamesScreenActivity extends AppCompatActivity {

    public final String BASE_URL = "https://fathomless-woodland-78351.herokuapp.com/api/";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamescreen);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        try {
            Intent intent = getIntent();
            final String accessToken = intent.getStringExtra("accessToken");
            final String client = intent.getStringExtra("client");
            final String uid = intent.getStringExtra("uid");

            final Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            final APIService service = retrofit.create(APIService.class);

            Call<List<GamesResponse>> call = service.getGames(accessToken, client, uid);
            call.enqueue(new Callback<List<GamesResponse>>() {
                @Override
                public void onResponse(Call<List<GamesResponse>> call, Response<List<GamesResponse>> response) {
                    if (response.isSuccess()) {
                        List<GamesResponse> responseBody = response.body();
                        System.out.println("GamesResponse Size: " + responseBody.size());

                        adapter = new RecyclerAdapter(responseBody);
                        recyclerView.setAdapter(adapter);
                    }
                }

                @Override
                public void onFailure(Call<List<GamesResponse>> call, Throwable t) {
                    System.out.println(t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.games_screen_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(new Intent(this, RemoteActivity.class));
        return true;
    }
}