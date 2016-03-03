package com.gamefinder;

import java.util.List;

/**
 * Created by Kevin on 3/3/2016.
 */
public class SignUpErrorResponse {
    private SignUpErrors errors;

    public SignUpErrors getSignUpErrors() {
        return errors;
    }

    public SignUpErrors setSignUpErrors(SignUpErrors errors) {
        return this.errors = errors;
    }
}
