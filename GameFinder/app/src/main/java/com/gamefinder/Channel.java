package com.gamefinder;

/**
 *
 */
public class Channel {
    private String channelNumber;
    private String channelAcronym;
    private String televisionId;

    public Channel(String channelNumber, String channelAcronym, String televisionId) {
        this.channelNumber = channelNumber;
        this.channelAcronym = channelAcronym;
        this.televisionId = televisionId;
    }

    public String getChannelAcronym () {
        return channelAcronym;
    }

    public void setChannelAcronym (String channelAcronym) {
        this.channelAcronym = channelAcronym;
    }

    public String getChannelNumber () {
        return channelNumber;
    }

    public void setChannelNumber (String channelNumber) {
        this.channelNumber = channelNumber;
    }

    public String getTelevisionId () {
        return televisionId;
    }

    public void setTelevisionId (String televisionId) {
        this.televisionId = televisionId;
    }
}