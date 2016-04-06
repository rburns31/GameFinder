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

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 *
 * Created by Paul on 3/31/2016.
 */
public class GamesRecyclerAdapter extends RecyclerView.Adapter<GamesRecyclerAdapter.ViewHolder> {
    private List<GamesResponse> gamesList;
    private Context parentContext;

    public GamesRecyclerAdapter(List<GamesResponse> gamesList){
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
        // Pull all of the useful information out of the GamesResponse object
        GamesResponse game = gamesList.get(position);
        String team1 = game.getCompetitor_1().getName();
        String team2 = game.getCompetitor_2().getName();
        String league = game.getLeague().getName();
        String startTime = game.getStart_time();
        String network = game.getNetwork();
        String rating = game.getScore();

        // The set of leagues whose logos are supported to be displayed
        HashSet<String> supportedLeagues = new HashSet<>();
        supportedLeagues.addAll(Arrays.asList("NBA", "MLB", "NHL", "NFL", "MLS"));
        if (!supportedLeagues.contains(league)) {
            return;
        }

        // Loads the logos into the image views on the game card
        Resources resources = parentContext.getResources();

        String logoFile1 = team1.replaceAll("[ .&]", "_").toLowerCase();
        //System.out.println(logoFile1);
        String logoFile2 = team2.replaceAll("[ .&]", "_").toLowerCase();
        //System.out.println(logoFile2);
        String placeHolderLogoFile = league.replaceAll("[ .]", "_").toLowerCase() + "_logo";
        //System.out.println(placeHolderLogoFile);

        int placeholderLogo = resources.getIdentifier(placeHolderLogoFile, "raw", parentContext.getPackageName());

        int logoId1 = resources.getIdentifier(logoFile1, "raw", parentContext.getPackageName());
        if (logoId1 != 0) {
            Picasso.with(parentContext).load(logoId1).fit().into(holder.thumbnail1);
        } else {
            Picasso.with(parentContext).load(placeholderLogo).fit().into(holder.thumbnail1);
        }

        int logoId2 = resources.getIdentifier(logoFile2, "raw", parentContext.getPackageName());
        if (logoId2 != 0) {
            Picasso.with(parentContext).load(logoId2).error(placeholderLogo).fit().into(holder.thumbnail2);
        } else {
            Picasso.with(parentContext).load(placeholderLogo).fit().into(holder.thumbnail2);
        }

        // Parse the start time into a read-able format
        String gameStartTimeText = "Start time: ";

        String temp = startTime.split("[T.]")[1];
        String temp2 = temp.substring(0, temp.length() - 3);
        int hour = Integer.parseInt(temp2.substring(0, 2)) - 4;
        if (hour == 0) {
            gameStartTimeText += "12" + temp2.substring(2) + "am ET";
        } else if (hour >= 12) {
            gameStartTimeText += Integer.toString(hour - 12) + temp2.substring(2) + "pm ET";
        } else if (hour < 0) {
            gameStartTimeText += Integer.toString(hour + 12) + temp2.substring(2) + "pm ET";
        } else {
            gameStartTimeText += Integer.toString(hour) + temp2.substring(2) + "am ET";
        }

        // Sets the game data text in the game card's body
        String gameNameText = team1 + " vs. " + team2;
        String gameLeagueText = "League: " + league;
        String gameRatingText = "Rating: " + rating;
        String gameNetworkText = "Network: " + network;
        if (network == null) {
            gameNetworkText = "Network: Unavailable";
        }

        holder.gameName.setText(gameNameText);
        holder.gameLeague.setText(gameLeagueText);
        holder.gameRating.setText(gameRatingText);
        holder.gameStartTime.setText(gameStartTimeText);
        holder.gameNetwork.setText(gameNetworkText);
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
        protected TextView gameLeague;
        protected TextView gameRating;
        protected TextView gameStartTime;
        protected TextView gameNetwork;
        protected ImageView thumbnail1;
        protected ImageView thumbnail2;

        public ViewHolder(View itemView) {
            super(itemView);
            gameName = (TextView) itemView.findViewById(R.id.gameName);
            gameLeague = (TextView) itemView.findViewById(R.id.gameLeague);
            gameRating = (TextView) itemView.findViewById(R.id.gameRating);
            gameStartTime = (TextView) itemView.findViewById(R.id.gameStartTime);
            gameNetwork = (TextView) itemView.findViewById(R.id.gameNetwork);
            thumbnail1 = (ImageView) itemView.findViewById(R.id.thumbnail1);
            thumbnail2 = (ImageView) itemView.findViewById(R.id.thumbnail2);
        }
    }
}