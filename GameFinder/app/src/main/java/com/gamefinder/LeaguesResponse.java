package com.gamefinder;

/**
 * Holds the attributes of a league when returned from the getLeagues API endpoint
 * Created by Kevin on 3/5/2016.
 */
public class LeaguesResponse {
    private String id;
    private String name;
    private float rating;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public float getRatingStar() { return rating; }

    public void setRatingStar(float rating) { this.rating = rating; }
}