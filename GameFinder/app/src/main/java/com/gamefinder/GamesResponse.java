package com.gamefinder;

/**
 *
 * Created by Kevin on 3/30/2016.
 */
public class GamesResponse {
    private String score;
    private String startTime;
    private League league;
    private String network;
    private Competitor_1 competitor_1;
    private Competitor_2 competitor_2;

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public League getLeague() {
        return league;
    }

    public void setLeague(League league) {
        this.league = league;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public Competitor_1 getCompetitor_1() {
        return competitor_1;
    }

    public void setCompetitor_1(Competitor_1 competitor_1) {
        this.competitor_1 = competitor_1;
    }

    public Competitor_2 getCompetitor_2() {
        return competitor_2;
    }

    public void setCompetitor_2(Competitor_2 competitor_2) {
        this.competitor_2 = competitor_2;
    }
}