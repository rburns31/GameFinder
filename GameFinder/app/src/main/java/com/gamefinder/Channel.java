package com.gamefinder;

/**
 *
 */
public class Channel {
    private String channelNumber;
    private String channelAcronym;
    private int televisionId;

    public Channel(String channelNumber, String channelAcronym, int televisionId) {
        this.channelNumber = channelNumber;
        this.channelAcronym = channelAcronym;
        this.televisionId = televisionId;
    }
}