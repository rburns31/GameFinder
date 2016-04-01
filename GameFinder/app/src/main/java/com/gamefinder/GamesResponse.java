package com.gamefinder;

/**
 *
 * Created by Kevin on 3/30/2016.
 */
public class GamesResponse {
    private String score;
    private String start_time;
    private League league;
    private String network;
    private Competitor competitor_1;
    private Competitor competitor_2;

    public String getScore ()
    {
        return score;
    }

    public void setScore (String score)
    {
        this.score = score;
    }

    public String getStart_time ()
    {
        return start_time;
    }

    public void setStart_time (String start_time)
    {
        this.start_time = start_time;
    }

    public League getLeague ()
    {
        return league;
    }

    public void setLeague (League league)
    {
        this.league = league;
    }

    public String getNetwork ()
    {
        return network;
    }

    public void setNetwork (String network)
    {
        this.network = network;
    }

    public Competitor getCompetitor_1 ()
    {
        return competitor_1;
    }

    public void setCompetitor_1 (Competitor competitor_1)
    {
        this.competitor_1 = competitor_1;
    }

    public Competitor getCompetitor_2 ()
    {
        return competitor_2;
    }

    public void setCompetitor_2 (Competitor competitor_2)
    {
        this.competitor_2 = competitor_2;
    }
}