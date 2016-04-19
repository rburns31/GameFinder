package com.gamefinder;

/**
 *
 * Created by Kevin on 3/7/2016.
 */
public class PreferenceAttributes {
    private int amount;
    private int scale;
    private String preference_type;
    private int preference_id;

    public int getAmount ()
    {
        return amount;
    }

    public void setAmount (int amount)
    {
        this.amount = amount;
    }

    public int getScale ()
    {
        return scale;
    }

    public void setScale (int scale)
    {
        this.scale = scale;
    }

    public String getPreference_type ()
    {
        return preference_type;
    }

    public void setPreference_type (String preference_type)
    {
        this.preference_type = preference_type;
    }

    public int getPreference_id ()
    {
        return preference_id;
    }

    public void setPreference_id (int preference_id)
    {
        this.preference_id = preference_id;
    }
}