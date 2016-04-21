package com.gamefinder;

/**
 *
 * Created by Kevin on 3/30/2016.
 */
public class TelevisionResponse {
    private int id;
    private String name;
    private String cable_company;
    private String brand;
    private boolean selected;

    public int getId ()
    {
        return id;
    }

    public void setId (int id)
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

    public String getCable_company ()
    {
        return cable_company;
    }

    public void setCable_company (String cable_company)
    {
        this.cable_company = cable_company;
    }

    public String getBrand ()
    {
        return brand;
    }

    public void setBrand (String brand)
    {
        this.brand = brand;
    }

    public boolean getSelected ()
    {
        return selected;
    }

    public void setSelected (boolean selected)
    {
        this.selected = selected;
    }

}