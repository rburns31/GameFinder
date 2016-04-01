package com.gamefinder;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 *
 * Created by Paul on 3/31/2016.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private List<GamesResponse> gamesList;
    private Context parentContext;

    public RecyclerAdapter(List<GamesResponse> gamesList){
        this.gamesList = gamesList;
        System.out.println("From RecyclerAdapter, Game List Size: " + gamesList.size());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create a new view
        parentContext = parent.getContext();
        View view = LayoutInflater.from(parentContext)
                .inflate(R.layout.game_card, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        GamesResponse game = gamesList.get(position);
        String team1 = game.getCompetitor_1().getName();
        String team2 = game.getCompetitor_2().getName();
        String startTime = game.getStart_time();
        String network = game.getNetwork();
        String rating = game.getScore();

        Resources resources = parentContext.getResources();

        String logoFile1 = team1.replaceAll("[ .]", "_").toLowerCase();
        System.out.println(logoFile1);
        //String logoFile2 = team2.replaceAll(" ", "_").toLowerCase();

        int logoId1 = resources.getIdentifier(logoFile1, "raw", parentContext.getPackageName());
        Picasso.with(parentContext).load(logoId1).into(holder.thumbnail1);

        //int logoId2 = resources.getIdentifier(logoFile2, "raw", parentContext.getPackageName());
        //Picasso.with(parentContext).load(R.raw.anaheim_ducks).into(holder.thumbnail2);

        String text1 = team1 + " vs. " + team2;
        holder.gameName.setText(text1);
        if (gamesList.get(position).getNetwork() == null) {
            String text2 = "Start Time: " + startTime + ", Unavailable " + " Rating: " + rating;
            holder.gameDescription.setText(text2);
        } else {
            String text3 = "Start Time: " + startTime + ", on " + network + " Rating: " + rating;
            holder.gameDescription.setText(text3);
        }
    }

    @Override
    public int getItemCount() {
        if (gamesList == null) {
            return 0;
        }
        return gamesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView gameName;
        protected TextView gameDescription;
        protected ImageView thumbnail1;
        protected ImageView thumbnail2;

        public ViewHolder(View itemView) {
            super(itemView);
            gameName = (TextView) itemView.findViewById(R.id.gameName);
            gameDescription = (TextView) itemView.findViewById(R.id.gameDescription);
            thumbnail1 = (ImageView) itemView.findViewById(R.id.thumbnail1);
            thumbnail2 = (ImageView) itemView.findViewById(R.id.thumbnail2);
        }
    }
}