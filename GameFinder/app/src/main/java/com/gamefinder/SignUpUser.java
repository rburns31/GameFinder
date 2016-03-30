package com.gamefinder;

/**
 *
 * Created by Kevin on 2/26/2016.
 */
public class SignUpUser {
    protected String email;
    protected String password;
    protected String passwordConfirmation;

    public SignUpUser(String email, String password, String passwordConfirmation) {
        this.email = email;
        this.password = password;
        this.passwordConfirmation = passwordConfirmation;
    }
}