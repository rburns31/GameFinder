package com.gamefinder;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

/**
 *
 * Created by Paul on 3/6/2016.
 */
public class LeagueListViewAdapter extends ArrayAdapter<LeaguesResponse> {
    private AppCompatActivity activity;
    private List<LeaguesResponse> leagueList;

    public LeagueListViewAdapter(AppCompatActivity context, int resource, List<LeaguesResponse> objects) {
        super(context, resource, objects);
        this.activity = context;
        this.leagueList = objects;
    }

    @Override
    public LeaguesResponse getItem(int position) {
        return leagueList.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_listview, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            //holder.ratingBar.getTag(position);
        }

        holder.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                LeaguesResponse item = getItem(position);
                item.setRatingStar(rating);
                System.out.println("Star: " + rating);
            }
        });

        holder.ratingBar.setTag(position);
        holder.ratingBar.setRating(getItem(position).getRatingStar());
        holder.leagueName.setText(getItem(position).getName());

        return convertView;
    }

    private static class ViewHolder {
        private RatingBar ratingBar;
        private TextView leagueName;

        public ViewHolder(View view) {
            ratingBar = (RatingBar) view.findViewById(R.id.rate_img);
            leagueName = (TextView) view.findViewById(R.id.league_name);
        }
    }
}