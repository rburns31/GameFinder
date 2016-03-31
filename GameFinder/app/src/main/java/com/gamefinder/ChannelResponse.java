package com.gamefinder;

/**
 *
 * Created by Kevin on 3/30/2016.
 */
public class ChannelResponse {
    private Channel[] channels;

    public ChannelResponse(Channel[] channels) {
        this.channels = channels;
    }

    public Channel[] getChannels() {
        return channels;
    }

    public void setChannels(Channel[] channels) {
        this.channels = channels;
    }
}