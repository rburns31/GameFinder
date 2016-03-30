package com.gamefinder;

import java.util.List;

/**
 *
 * Created by Kevin on 3/3/2016.
 */
public class LoginErrorResponse {
    private List<String> errors;

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}