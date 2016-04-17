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

import java.util.HashMap;
import java.util.List;

import jp.wasabeef.picasso.transformations.GrayscaleTransformation;

/**
 *
 * Created by Ryan on 4/2/2016.
 */
public class TeamInterestRecyclerAdapter extends RecyclerView.Adapter<TeamInterestRecyclerAdapter.ViewHolder> {
    private boolean[] selectedTeams;
    private List<CompetitorsResponse> teamsList;
    //private List<PreferencesResponse> prevPref;
    private Context parentContext;

    public TeamInterestRecyclerAdapter(List<CompetitorsResponse> teamsList, List<PreferencesResponse> prevPref) {
        this.teamsList = teamsList;
        //this.prevPref = prevPref;
        System.out.println(prevPref);
        selectedTeams = new boolean[teamsList.size()];

        // Populate any previously stored competitor preferences from the server
        HashMap<String, PreferencesResponse> idToPref = new HashMap<>();
        for (PreferencesResponse pref : prevPref) {
            idToPref.put(pref.getPreference_id(), pref);
        }

        for (int i = 0; i < teamsList.size(); i++) {
            CompetitorsResponse competitor = teamsList.get(i);
            if (idToPref.containsKey(competitor.getId())) {
                selectedTeams[i] = true;
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create a new view
        parentContext = parent.getContext();
        View view = LayoutInflater.from(parentContext).inflate(R.layout.team_card, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // Pull all of the useful information out of the CompetitorsResponse object
        CompetitorsResponse team = teamsList.get(position);
        String name = team.getName();
        String league = team.getLeagueName();

        Resources resources = parentContext.getResources();

        // Transform the team and league names to the logo file naming convention
        String teamLogoFile = name.replaceAll("[ .&()-/']", "_").toLowerCase();
        String leagueLogoFile = league.replaceAll("[ .&()-/']", "_").toLowerCase() + "_logo";

        // Get the id for the logo files (set to 0 if they are not present)
        int teamLogoId = resources.getIdentifier(teamLogoFile, "raw", parentContext.getPackageName());
        int leagueLogoId = resources.getIdentifier(leagueLogoFile, "raw", parentContext.getPackageName());
        int soccerLogoId = resources.getIdentifier("soccer", "raw", parentContext.getPackageName());
        int ncaaLogoId = resources.getIdentifier("ncaa_logo", "raw", parentContext.getPackageName());

        if (teamLogoId != 0) {
            setupTeamImage(position, holder.thumbnail, teamLogoId);

        } else if (leagueLogoId != 0) {
            setupTeamImage(position, holder.thumbnail, leagueLogoId);

        } else if (league.contains("Soccer")) {
            setupTeamImage(position, holder.thumbnail, soccerLogoId);

        } else if (league.contains("NCAA")) {
            setupTeamImage(position, holder.thumbnail, ncaaLogoId);

        } else {
            // If there is no picture (hopefully will never get to here)
            holder.thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedTeams[position] = !selectedTeams[position];
                }
            });
        }

        holder.teamName.setText(name);
    }

    /**
     *
     * @param position
     * @param image
     * @param imageId
     */
    private void setupTeamImage(final int position, final ImageView image, final int imageId) {
        // Set the image, gray or not depending on the current selected value
        if (selectedTeams[position]) {
            Picasso.with(parentContext).load(imageId).fit().centerCrop().into(image);
        } else {
            Picasso.with(parentContext).load(imageId).fit().centerCrop().transform(
                    new GrayscaleTransformation()).into(image);
        }

        // Handle the image being clicked on
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle the selected value
                selectedTeams[position] = !selectedTeams[position];

                // Update the image to either be gray or colored
                if (selectedTeams[position]) {
                    Picasso.with(parentContext).load(imageId).fit().centerCrop().into(image);
                } else {
                    Picasso.with(parentContext).load(imageId).fit().centerCrop().transform(
                            new GrayscaleTransformation()).into(image);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (teamsList == null) {
            return 0;
        }
        return teamsList.size();
    }

    public boolean[] getSelectedTeams() {
        return selectedTeams;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView teamName;
        protected ImageView thumbnail;

        public ViewHolder(View itemView) {
            super(itemView);
            teamName = (TextView) itemView.findViewById(R.id.teamName);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
        }
    }
}