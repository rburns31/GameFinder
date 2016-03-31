package com.gamefinder;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

/**
 *
 */
public class ChannelListViewAdapter extends ArrayAdapter<ChannelsResponse> {

    private AppCompatActivity activity;
    private List<ChannelsResponse> channelList;

    public ChannelListViewAdapter(AppCompatActivity context, int resource, List<ChannelsResponse> objects) {
        super(context, resource, objects);
        this.activity = context;
        this.channelList = objects;
    }

    @Override
    public ChannelsResponse getItem(int position) {
        return channelList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
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

        holder.ratingBar.setOnRatingBarChangeListener(onRatingChangedListener(holder, position));

        holder.ratingBar.setTag(position);
        //holder.ratingBar.setRating(getItem(position).getRatingStar());
        holder.leagueName.setText(getItem(position).getName());

        return convertView;
    }

    private RatingBar.OnRatingBarChangeListener onRatingChangedListener(final ViewHolder holder, final int position) {
        return new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                ChannelsResponse item = getItem(position);
                //item.setRatingStar(v);
                Log.i("Adapter", "star: " + v);
            }
        };
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
