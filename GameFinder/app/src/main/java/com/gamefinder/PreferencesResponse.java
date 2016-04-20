package com.gamefinder;

/**
 *
 * Created by Kevin on 3/7/2016.
 */
public class PreferencesResponse {
    private String amount;
    private String scale;
    private String preference_type;
    private String preference_id;

    public String getAmount ()
    {
        return amount;
    }

    public void setAmount (String amount)
    {
        this.amount = amount;
    }

    public String getScale ()
    {
        return scale;
    }

    public void setScale (String scale)
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

    public String getPreference_id ()
    {
        return preference_id;
    }

    public void setPreference_id (String preference_id)
    {
        this.preference_id = preference_id;
    }

}