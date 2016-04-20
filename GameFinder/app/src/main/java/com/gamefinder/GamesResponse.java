package com.gamefinder;

/**
 *
 * Created by Kevin on 3/30/2016.
 */
public class GamesResponse {
    private String score;
    private String start_time;
    private LeaguesResponse league;
    private String network;
    private CompetitorsResponse competitor_1;
    private CompetitorsResponse competitor_2;

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

    public LeaguesResponse getLeague ()
    {
        return league;
    }

    public void setLeague (LeaguesResponse league)
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

    public CompetitorsResponse getCompetitor_1 ()
    {
        return competitor_1;
    }

    public void setCompetitor_1 (CompetitorsResponse competitor_1)
    {
        this.competitor_1 = competitor_1;
    }

    public CompetitorsResponse getCompetitor_2 ()
    {
        return competitor_2;
    }

    public void setCompetitor_2 (CompetitorsResponse competitor_2)
    {
        this.competitor_2 = competitor_2;
    }
}