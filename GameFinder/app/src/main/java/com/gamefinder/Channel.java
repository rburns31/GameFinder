package com.gamefinder;

/**
 *
 */
public class Channel {
    private int channel_number;
    private String channel_acronym;
    private int television_id;

    public Channel(int channel_number, String channel_acronym, int television_id) {
        this.channel_number = channel_number;
        this.channel_acronym = channel_acronym;
        this.television_id = television_id;
    }

    public String getChannel_acronym () {
        return channel_acronym;
    }

    public void setChannel_acronym (String channel_acronym) {
        this.channel_acronym = channel_acronym;
    }

    public int getChannel_number () {
        return channel_number;
    }

    public void setChannel_number (int channel_number) {
        this.channel_number = channel_number;
    }

    public int getTelevision_id () {
        return television_id;
    }

    public void setTelevision_id (int television_id) {
        this.television_id = television_id;
    }
}