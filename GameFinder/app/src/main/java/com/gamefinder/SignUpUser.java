package com.gamefinder;

/**
 *
 * Created by Kevin on 2/26/2016.
 */
public class SignUpUser {
    String email, password, password_confirmation;

    public SignUpUser(String email, String password, String password_confirmation) {
        this.email = email;
        this.password = password;
        this.password_confirmation = password_confirmation;
    }
}
