package com.gamefinder;

/**
 *
 * Created by Kevin on 3/30/2016.
 */
public class TelevisionResponse {
    private String id;
    private String name;
    private String cableCompany;
    private String brand;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCableCompany() {
        return cableCompany;
    }

    public void setCableCompany(String cableCompany) {
        this.cableCompany = cableCompany;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
}