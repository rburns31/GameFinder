package com.gamefinder;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.TextKeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
        final ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.channel_listview, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.channelName.setText(getItem(position).getChannelAcronym());

        return convertView;
    }

    private static class ViewHolder {
        private EditText channelNumberField;
        private TextView channelName;

        public ViewHolder(View view) {
            channelNumberField = (EditText) view.findViewById(R.id.channelNumberField);
            channelName = (TextView) view.findViewById(R.id.channelName);
        }
    }
}