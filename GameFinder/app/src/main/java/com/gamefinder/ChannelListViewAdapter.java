package com.gamefinder;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

/**
 *
 */
public class ChannelListViewAdapter extends ArrayAdapter<Channel> {
    private AppCompatActivity activity;
    private List<Channel> channelList;

    public ChannelListViewAdapter(AppCompatActivity activity, int resource, List<Channel> channelList) {
        super(activity, resource, channelList);
        this.activity = activity;
        this.channelList = channelList;
    }

    @Override
    public Channel getItem(int position) {
        return channelList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.channel_listview, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
            //holder.ratingBar.getTag(position);
        }

        //holder.ratingBar.setOnRatingBarChangeListener(onRatingChangedListener(holder, position));

        //holder.ratingBar.setTag(position);
        //holder.channelNumberField.setRating(getItem(position).getRatingStar());
        holder.channelName.setText(getItem(position).getChannelAcronym());

        return convertView;
    }

    /**private RatingBar.OnRatingBarChangeListener onRatingChangedListener(final ViewHolder holder, final int position) {
        return new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                Channel item = getItem(position);
                //item.setRatingStar(v);
                Log.i("Adapter", "star: " + v);
            }
        };
    }*/

    private static class ViewHolder {
        private EditText channelNumberField;
        private TextView channelName;

        public ViewHolder(View view) {
            channelNumberField = (EditText) view.findViewById(R.id.channelNumberField);
            channelName = (TextView) view.findViewById(R.id.channelName);
        }
    }
}