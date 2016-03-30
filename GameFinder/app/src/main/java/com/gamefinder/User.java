package com.gamefinder;

/**
 *
 * Created by Kevin on 2/26/2016.
 */
public class User {
    protected String email;
    protected String password;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
}