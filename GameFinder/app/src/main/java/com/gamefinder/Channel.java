package com.gamefinder;

/**
 * Created by Kevin on 3/30/2016.
 */
public class Channel {
    private String channel_acronym;

    private String channel_number;

    private String television_id;

    public String getChannel_acronym ()
    {
        return channel_acronym;
    }

    public void setChannel_acronym (String channel_acronym)
    {
        this.channel_acronym = channel_acronym;
    }

    public String getChannel_number ()
    {
        return channel_number;
    }

    public void setChannel_number (String channel_number)
    {
        this.channel_number = channel_number;
    }

    public String getTelevision_id ()
    {
        return television_id;
    }

    public void setTelevision_id (String television_id)
    {
        this.television_id = television_id;
    }
}
