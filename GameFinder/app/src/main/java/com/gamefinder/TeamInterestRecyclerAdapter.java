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

import jp.wasabeef.picasso.transformations.GrayscaleTransformation;

/**
 *
 * Created by Ryan on 4/2/2016.
 */
public class TeamInterestRecyclerAdapter extends RecyclerView.Adapter<TeamInterestRecyclerAdapter.ViewHolder> {
    private boolean[] selectedTeams;
    private List<CompetitorsResponse> teamsList;
    private Context parentContext;

    public TeamInterestRecyclerAdapter(List<CompetitorsResponse> teamsList) {
        this.teamsList = teamsList;
        selectedTeams = new boolean[teamsList.size()];
        System.out.println(teamsList.get(0).getName());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create a new view
        parentContext = parent.getContext();
        View view = LayoutInflater.from(parentContext)
                .inflate(R.layout.team_card, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // Pull all of the useful information out of the CompetitorsResponse object
        CompetitorsResponse team = teamsList.get(position);
        String name = team.getName();
        String league = team.getLeagueName();

        // The set of leagues whose logos are supported to be displayed
        HashSet<String> supportedLeagues = new HashSet<>();
        supportedLeagues.addAll(Arrays.asList("NBA", "MLB", "NHL", "NFL", "MLS"));

        // Loads the logo into the image view on the team card
        Resources resources = parentContext.getResources();

        String logoFile = name.replaceAll("[ .]", "_").toLowerCase();
        //System.out.println(logoFile);
        String placeHolderLogoFile = league.replaceAll("[ .]", "_").toLowerCase() + "_logo";
        //System.out.println(placeHolderLogoFile);

        final int placeholderLogoId = resources.getIdentifier(placeHolderLogoFile, "raw", parentContext.getPackageName());
        final int logoId = resources.getIdentifier(logoFile, "raw", parentContext.getPackageName());
        if (logoId != 0) {
            Picasso.with(parentContext).load(logoId).fit().centerCrop().transform(new GrayscaleTransformation()).into(holder.thumbnail);
            holder.thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedTeams[position] = !selectedTeams[position];
                    if (selectedTeams[position]) {
                        Picasso.with(parentContext).load(logoId).fit().centerCrop().into(holder.thumbnail);
                    } else {
                        Picasso.with(parentContext).load(logoId).fit().centerCrop().transform(new GrayscaleTransformation()).into(holder.thumbnail);
                    }
                }
            });
        } else if (placeholderLogoId != 0) {
            Picasso.with(parentContext).load(placeholderLogoId).fit().centerCrop().transform(new GrayscaleTransformation()).into(holder.thumbnail);
            holder.thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("Clicked image");
                    selectedTeams[position] = !selectedTeams[position];
                    if (selectedTeams[position]) {
                        Picasso.with(parentContext).load(placeholderLogoId).fit().centerCrop().into(holder.thumbnail);
                    } else {
                        Picasso.with(parentContext).load(placeholderLogoId).fit().centerCrop().transform(new GrayscaleTransformation()).into(holder.thumbnail);
                    }
                }
            });
        } else {
            holder.thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedTeams[position] = !selectedTeams[position];
                }
            });
        }

        holder.teamName.setText(name);
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