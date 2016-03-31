package com.gamefinder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;

import java.util.List;

/**
 * Created by Paul on 3/31/2016.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private List<GamesResponse> gamesList;
    public RecyclerAdapter(List<GamesResponse> gamesList){
        this.gamesList = gamesList;
        System.out.println("From Recylcer Adapater, Game List Size: " + gamesList.size());

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.game_card, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;


    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.gameName.setText(gamesList.get(position).getCompetitor_1().getName() + " vs " + gamesList.get(position).getCompetitor_2().getName());
        holder.gameDescription.setText("Start Time: " + gamesList.get(position).getStart_time() + " Score: " + gamesList.get(position).getScore());
    }

    @Override
    public int getItemCount() {
        if(gamesList == null) {
            return 0;
        }
        return gamesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        protected TextView gameName, gameDescription;
        public ViewHolder(View itemView) {
            super(itemView);
            gameName =  (TextView) itemView.findViewById(R.id.gameName);
            gameDescription = (TextView) itemView.findViewById(R.id.gameDescription);

        }


    }
}
