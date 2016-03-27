package com.gamefinder;

import java.io.Serializable;

/**
 * Created by Kevin on 3/7/2016.
 */
public class CompetitorsResponse implements Serializable{
    private String id, league_id, name;
    private Boolean isSelected = false;

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

    public String getLeague_id() {
        return league_id;
    }

    public void setLeague_id(String league_id) {
        this.league_id = league_id;
    }

    public Boolean getIsSelected() { return isSelected;}

    public void setIsSelected(Boolean isSelected) {
        this.isSelected = isSelected;
    }
}
