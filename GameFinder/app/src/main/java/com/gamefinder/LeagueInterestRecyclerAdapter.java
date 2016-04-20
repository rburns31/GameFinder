package com.gamefinder;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

/**
 * Controls the list of cards associated to leagues in the league interest activity
 * Created by Ryan on 4/2/2016.
 */
public class LeagueInterestRecyclerAdapter
        extends RecyclerView.Adapter<LeagueInterestRecyclerAdapter.ViewHolder> {
    /**
     * The list of the names of every supported league
     */
    private List<LeaguesResponse> leaguesList;
    /**
     * Any league preferences currently in the database for this user (blank if first time setup)
     */
    private List<PreferencesResponse> prevPref;
    /**
     * The parent context (used for getting resources and layouts from the league interest activity)
     */
    private Context parentContext;

    public LeagueInterestRecyclerAdapter(List<LeaguesResponse> leaguesList,
                                         List<PreferencesResponse> prevPref) {
        this.leaguesList = leaguesList;
        this.prevPref = prevPref;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create a new view
        parentContext = parent.getContext();
        View view = LayoutInflater.from(parentContext).inflate(R.layout.league_card, parent, false);

        // Populate any previously stored league preferences from the server
        HashMap<String, PreferencesResponse> idToPref = new HashMap<>();
        for (PreferencesResponse pref : prevPref) {
            idToPref.put(pref.getPreference_id(), pref);
        }

        for (LeaguesResponse league: leaguesList) {
            if (idToPref.containsKey(league.getId())) {
                PreferencesResponse thisPref = idToPref.get(league.getId());
                league.setRatingStar(Float.parseFloat(thisPref.getAmount()));
            }
        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // Pull all of the useful information out of the LeaguesResponse object
        final LeaguesResponse league = leaguesList.get(position);
        String name = league.getName();

        // Loads the logo into the image view on the team card
        Resources resources = parentContext.getResources();

        String logoFile = name.replaceAll(" ", "_").toLowerCase() + "_logo";

        int logoId = resources.getIdentifier(logoFile, "raw", parentContext.getPackageName());
        if (logoId != 0) {
            Picasso.with(parentContext).load(logoId).fit().centerCrop().into(holder.thumbnail);
        } else if (name.contains("Soccer")) {
            int soccerLogoId = resources.getIdentifier("soccer", "raw", parentContext.getPackageName());

            Picasso.with(parentContext).load(soccerLogoId).fit().centerCrop().into(holder.thumbnail);
        } else if (name.contains("NCAA")) {
            int ncaaLogoId = resources.getIdentifier("ncaa_logo", "raw", parentContext.getPackageName());

            Picasso.with(parentContext).load(ncaaLogoId).fit().centerCrop().into(holder.thumbnail);
        }

        // If a league icon is long clicked, reset its value to 0
        holder.thumbnail.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                league.setRatingStar(0);
                holder.ratingBar.setRating(0);
                return true;
            }
        });

        holder.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                league.setRatingStar(rating);
                //System.out.println("Star: " + rating);
            }
        });
        holder.ratingBar.setRating(leaguesList.get(position).getRatingStar());

        holder.leagueName.setText(name);
    }

    @Override
    public int getItemCount() {
        if (leaguesList == null) {
            return 0;
        }
        return leaguesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView leagueName;
        protected ImageView thumbnail;
        protected RatingBar ratingBar;

        public ViewHolder(View itemView) {
            super(itemView);
            leagueName = (TextView) itemView.findViewById(R.id.leagueName);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            ratingBar = (RatingBar) itemView.findViewById(R.id.rate_img);
        }
    }
}